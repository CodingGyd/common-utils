package com.codinggyd.utils;

import com.codinggyd.diff.*;
import com.codinggyd.entity.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JsonDiffUtil {

    private static final String NBSP = " ";

    private static final String FIELD_NAME = "FIELD_NAME";

    private List<ResultRow> startList = new ArrayList<>();

    private Stack<ResultRow> endStack = new Stack<>();

    //忽略字段
    private List<String> ignoreFieldList = new ArrayList<>();

    //排序字段
    private List<String> ignoreArrayOrderList = new ArrayList<>();

    private Map<String, String> ignoreArrayOrder2sortFieldMap = Maps.newHashMap();

    private Boolean diffResult = true;

    private Map<String, Rule> field2DiffRuleMap = Maps.newHashMap();

    public static JsonDiffUtil getInstance() {
        return new JsonDiffUtil();
    }

    private static void handleAddStartList(ResultRow resultRow, List<ResultRow> startList, Stack<ResultRow> endStack) {
        if (CollectionUtils.isEmpty(startList)) {
            startList.add(resultRow);
            return;
        }
        ResultRow preResultRow = startList.get(startList.size() - 1);
        Integer preDepth = preResultRow.getDepth();
        Integer depth = resultRow.getDepth();
        while (preDepth - depth > 0 && !endStack.empty()) {
            startList.add(endStack.pop());
            preDepth = preDepth - 1;
        }
        startList.add(resultRow);
    }

    public void setIgnoreArrayOrder2sortFieldMap(Map<String, String> ignoreArrayOrder2sortFieldMap) {
        this.ignoreArrayOrder2sortFieldMap = ignoreArrayOrder2sortFieldMap;
    }

    public static void main(String[] args) {
        Product a = new Product();
        a.setName("张三");
        a.setCount("10");
        a.setPrice("12.1");

        Product b = new Product();
        b.setName("李四");
        b.setCount("10");
        b.setPrice("12.1");
        JsonDiffUtil jsonDiffUtil = new JsonDiffUtil();

        DiffResult diffResult1 = jsonDiffUtil.diffJson(a,b,null,null,null);
        System.out.println(diffResult1);

    }
    public DiffResult diffJson(Object referenceJO,
                               Object testJO,
                               List<String> ignoreFieldList,
                               List<IgnoreArrayOrder> ignoreArrayOrderList,
                               List<DiffRule> diffRules) {
        if (CollectionUtils.isNotEmpty(ignoreFieldList)) {
            this.ignoreFieldList = ignoreFieldList;
        }
        if (CollectionUtils.isNotEmpty(ignoreArrayOrderList)) {
            ignoreArrayOrderList.forEach(ignoreArrayOrder -> {
                this.ignoreArrayOrderList.add(ignoreArrayOrder.getJsonPath());
                this.ignoreArrayOrder2sortFieldMap.put(ignoreArrayOrder.getJsonPath(), ignoreArrayOrder.getSortField() == null ? "" : ignoreArrayOrder.getSortField());
            });
        }
        if (CollectionUtils.isNotEmpty(diffRules)) {
            diffRules.forEach(diffRule -> {
                this.field2DiffRuleMap.put(diffRule.getField(), RuleFactory.getRule(diffRule.getDiffRuleType(), diffRule.getRule(), diffRule.getFieldDataType(), diffRule.getExtra()));
            });
        }
        try {
            diffJson("", referenceJO, testJO);
            while (!endStack.empty()) {
                startList.add(endStack.pop());
            }
        } finally {
        }

        DiffResult diffResult1 = new DiffResult();
        diffResult1.setDiffResultRowList(startList);
        diffResult1.setResult(diffResult);
        diffResult1.setIgnoreArrayOrderList(ignoreArrayOrderList);
        diffResult1.setIgnoreFieldList(ignoreFieldList);
        return diffResult1;
    }

    private boolean needIgnore(String path) {
        String newPath = removePathNumber(path);
        if (CollectionUtils.isNotEmpty(ignoreFieldList) && StringUtils.isNotBlank(newPath)) {
            for (String ignoreField : ignoreFieldList) {
                boolean endsWith = newPath.endsWith(ignoreField);

                if (endsWith) {
                    return true;
                }
            }
        }
        return false;
    }

    private String removeArrayIndexNumber(String path) {
        return path.replaceAll("\\[\\d+\\]", "[]");
    }

    /**
     * 将全路径中的数字路径去除
     *
     * @param path /1323/stockPrices
     * @return /stockPrices
     */
    private String removePathNumber(String path) {
        return path.replaceAll("\\/\\d+\\/", "/");
    }

    private void diffJson(String path, Object referenceJO, Object testObject) {
        try {
            String tempPath = removeArrayIndexNumber(path);
            if (needIgnore(tempPath)) {
                renderIgnoreResultRow(referenceJO, testObject, path);
                return;
            }

            if (referenceJO != null && testObject == null) {
                renderDeleteResultRow(referenceJO, path);
                return;
            }
            if (referenceJO == null && testObject != null) {
                renderAddResultRow(testObject, path);
                return;
            }
            if (referenceJO == null) {
                renderSameResultRow(path, null);
                return;
            }

            if (!isSameInstance(referenceJO, testObject)) {
                renderDiffResultRow(referenceJO, testObject, path);
                return;
            }

            if (referenceJO instanceof JsonNode && ((JsonNode) referenceJO).isObject()) {
                try {
                    JsonNode expectedJsonObject = (JsonNode) referenceJO;
                    JsonNode actualJsonObject = (JsonNode) testObject;

                    List<String> expectedKeyList = getSortedKeyList(expectedJsonObject);
                    List<String> actualKeyList = getSortedKeyList(actualJsonObject);
                    if (expectedKeyList.isEmpty() && actualKeyList.isEmpty()) {
                        renderEmptyJson(path, "{}");
                    } else {
                        renderJson(path, "{", "}");
                    }
                    if (expectedKeyList.isEmpty() && !actualKeyList.isEmpty()) {
                        for (String actualKey : actualKeyList) {
                            renderAddResultRow(actualJsonObject.get(actualKey), path + "/" + actualKey);
                        }
                    } else if (!expectedKeyList.isEmpty() && actualKeyList.isEmpty()) {
                        for (String expectedKey : expectedKeyList) {
                            renderDeleteResultRow(expectedJsonObject.get(expectedKey), path + "/" + expectedKey);
                        }
                    } else if (!expectedKeyList.isEmpty() && !actualKeyList.isEmpty()) {
                        for (String expectedKey : expectedKeyList) {
                            String keypath = path + "/" + expectedKey;
                            diffJson(keypath, expectedJsonObject.get(expectedKey), actualJsonObject.get(expectedKey));
                        }
                        for (String actualKey : actualKeyList) {
                            if (!expectedKeyList.contains(actualKey)) {
                                renderAddResultRow(actualJsonObject.get(actualKey), path + "/" + actualKey);
                            }
                        }
                    }
                } finally {
                }
            } else if (referenceJO instanceof JsonNode && ((JsonNode) referenceJO).isArray()) {
                try {
                    JsonNode referenceJsonArray = (JsonNode) referenceJO;
                    JsonNode actualJsonArray = (JsonNode) testObject;

                    boolean needOrder = false;
                    if (referenceJsonArray.size() != 0 && actualJsonArray.size() != 0) {
                        for (String orderArray : ignoreArrayOrderList) {
                            if (isMatch(tempPath, orderArray)) {
                                needOrder = true;
                            }
                        }

                    }
                    if (needOrder) {
                        //todo:可能是bug，待修复
                        List<String> ignoreJsonKeyList = Lists.newArrayList();
                        Optional<String> specificFieldOptional = getBestMatchSortField(tempPath);
                        if (specificFieldOptional.isPresent()) {
                            List<String> sortJsonKeyList = Arrays.asList(specificFieldOptional.get().split("\\|"));
                            diffJsonArraySortedByMultiFields(tempPath, referenceJsonArray, actualJsonArray, sortJsonKeyList, ignoreJsonKeyList);
                        }
                    } else {
                        jsonArraySortedDiff(path, referenceJsonArray, actualJsonArray);
                    }
                } finally {
                }
            } else if (referenceJO instanceof JsonNode && isBasicObject(referenceJO)) {
                // 将全路径中的数字路径去除，比如/1323/stockPrices处理成/stockPrices
                String allButNoNumericPatch = removePathNumber(tempPath);
                if (this.field2DiffRuleMap.containsKey(allButNoNumericPatch)) {
                    try {
                        Rule rule = field2DiffRuleMap.get(allButNoNumericPatch);
                        if (rule.satisfy((JsonNode) referenceJO, (JsonNode) testObject)) {
                            renderSameResultRow(path, referenceJO);
                        } else {
                            renderDiffResultRow(referenceJO, testObject, path);
                        }
                    } finally {
                    }
                } else {
                    try {
                        if (!referenceJO.equals(testObject)) {
                            renderDiffResultRow(referenceJO, testObject, path);
                        } else {
                            renderSameResultRow(path, referenceJO);
                        }
                    } finally {
                    }
                }
            } else if (referenceJO instanceof String) {
                try {
                    String expectedString = (String) referenceJO;
                    String actualString = (String) testObject;
                    if (!expectedString.equals(actualString)) {
                        renderDiffResultRow(referenceJO, testObject, path);
                    } else {
                        renderSameResultRow(path, referenceJO);
                    }
                } finally {
                }
            } else if (referenceJO instanceof Number) {
                try {
                    Number expectedNumber = (Number) referenceJO;
                    Number actualNumber = (Number) testObject;
                    if (!expectedNumber.equals(actualNumber)) {
                        renderDiffResultRow(referenceJO, testObject, path);
                    } else {
                        renderSameResultRow(path, referenceJO);
                    }
                } finally {
                }
            } else if (referenceJO instanceof Boolean) {
                try {
                    Boolean expectedBoolean = (Boolean) referenceJO;
                    Boolean actualBoolean = (Boolean) testObject;
                    if (!expectedBoolean.equals(actualBoolean)) {
                        renderDiffResultRow(referenceJO, testObject, path);
                    } else {
                        renderSameResultRow(path, referenceJO);
                    }
                } finally {
                }
            } else {
                renderDiffResultRow(referenceJO, testObject, path);
            }
        } finally {
        }
    }

    public Optional<String> getBestMatchSortField(String paths) {
        try {
            return ignoreArrayOrder2sortFieldMap.entrySet().stream().filter(entry -> isMatch(paths, entry.getKey())).map(Map.Entry::getValue).findFirst();
        } finally {
        }
    }

    private boolean isMatch(String paths, String key) {
        return paths.endsWith(key) || Pattern.matches(key, paths);
    }

    private void jsonArraySortedDiff(String path, JsonNode referenceJsonArray, JsonNode actualJsonObArray) {
        try {
            Integer expectedSize = referenceJsonArray.size();
            Integer actualSize = actualJsonObArray.size();
            if (expectedSize == 0 && actualSize == 0) {
                renderEmptyJson(path, "[]");
            } else {
                renderJson(path, "[", "]");
            }

            if (expectedSize == 0 && actualSize == 0) {
            } else {
                Integer minSize = expectedSize <= actualSize ? expectedSize : actualSize;
                for (int i = 0; i < minSize; i++) {
                    Object expectedArrayObject = referenceJsonArray.get(i);
                    Object actualArrayObject = actualJsonObArray.get(i);
                    diffJson(path + "[" + i + "]", expectedArrayObject, actualArrayObject);
                }
                if (expectedSize >= actualSize) {
                    for (int i = minSize; i < expectedSize; i++) {
                        String newPath = path + "[" + i + "]";
                        renderDeleteResultRow(referenceJsonArray.get(i), newPath);
                    }
                } else {
                    for (int i = minSize; i < actualSize; i++) {
                        String newPath = path + "[" + i + "]";
                        renderAddResultRow(actualJsonObArray.get(i), newPath);
                    }
                }
            }
        } finally {
        }
    }

    /**
     * 比对json array,
     */
    private void diffJsonArraySortedByMultiFields(String path, JsonNode referenceJsonArray, JsonNode actualJsonArray, List<String> sortJsonKeyList, List<String> ignoreJsonKeyList) {
        TreeMap<String, JsonNode> expectedKeyElementMap = convertJsonArray2TreeMap(referenceJsonArray, ignoreJsonKeyList, sortJsonKeyList);
        TreeMap<String, JsonNode> actualKeyElementMap = convertJsonArray2TreeMap(actualJsonArray, ignoreJsonKeyList, sortJsonKeyList);
        diffJsonArray(path, expectedKeyElementMap, actualKeyElementMap);
    }

    /**
     * 比对json array
     */
    private void diffJsonArray(String path, TreeMap<String, JsonNode> expectedKeyElementMap, TreeMap<String, JsonNode> actualKeyElementMap) {
        try {
            renderJson(path, "[", "]");
            int position = 0;
            Set<String> expectKeySet = expectedKeyElementMap.keySet();
            for (String expectKey : expectKeySet) {
                diffJson(path + "[" + position + "]", expectedKeyElementMap.get(expectKey), actualKeyElementMap.get(expectKey));
                position++;
            }
            for (String actualKey : actualKeyElementMap.keySet()) {
                if (expectedKeyElementMap.get(actualKey) == null) {
                    diffJson(path + "[" + position + "]", null, actualKeyElementMap.get(actualKey));
                    position++;
                }
            }
        } finally {
        }
    }

    private TreeMap<String, JsonNode> convertJsonArray2TreeMap(JsonNode jsonArray, List<String> ignoreJsonKeyList, List<String> sortJsonKeyList) {
        try {
            TreeMap<String, JsonNode> keyElementMap = Maps.newTreeMap();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonNode jsonNode = jsonArray.get(i);
                String jsonNodeString = getJsonNodeString(jsonNode, ignoreJsonKeyList, sortJsonKeyList);

                if (StringUtils.isEmpty(jsonNodeString)) {
                    keyElementMap.put(MD5Utils.encode("{}" + i), jsonNode);
                } else {
                    keyElementMap.put(MD5Utils.encode(jsonNodeString), jsonNode);
                }
            }
            return keyElementMap;
        } finally {
        }
    }

    /**
     * 获取jsonNode所对应的string
     *
     * @param jsonNode           jsonNode
     * @param ignoreJsonKeyList  序列化时忽略掉指定的字段列表
     * @param specifiedFieldList 只生成指定字段的序列化列表。如果specifiedFieldList为空，则生成全部字段的序列化列表。
     * @return
     */
    public String getJsonNodeString(JsonNode jsonNode, List<String> ignoreJsonKeyList, List<String> specifiedFieldList) {
        try {
            if (isBasicObject(jsonNode)) {
                return jsonNode.asText();
            }

            StringBuilder result = new StringBuilder();
            if (jsonNode.isObject()) {
                Iterator<String> keys = jsonNode.fieldNames();
                Set<String> keySet = Sets.newHashSet(keys);
                TreeSet<String> keyTreeSet = Sets.newTreeSet(keySet);
                for (String key : keyTreeSet) {
                    //1. 忽略字段不作为排序的字段
                    if (CollectionUtils.isNotEmpty(ignoreJsonKeyList) && ignoreJsonKeyList.contains(key)) {
                        continue;
                    }
                    JsonNode subJsonNode = jsonNode.get(key);

                    //2. 如果设置了指定字段，那么只按照指定字段来排序
                    if (CollectionUtils.isNotEmpty(specifiedFieldList)) {
                        if (specifiedFieldList.contains(key)) {
                            result.append(key).append(getJsonNodeString(subJsonNode, ignoreJsonKeyList, specifiedFieldList));
                        }
                        continue;
                    }
                    result.append(key).append(getJsonNodeString(subJsonNode, ignoreJsonKeyList, specifiedFieldList));
                }
            }

            if (jsonNode.isArray() && jsonNode.size() != 0) {
                TreeMap<String, JsonNode> treeMap = convertJsonArray2TreeMap(jsonNode, ignoreJsonKeyList, specifiedFieldList);
                for (JsonNode subJsonNode : treeMap.values()) {
                    result.append(getJsonNodeString(subJsonNode, ignoreJsonKeyList, specifiedFieldList));
                }
            }

            return result.toString();
        } finally {
        }
    }

    private boolean isSameInstance(Object object1, Object object2) {
        if (object1 instanceof JsonNode && object2 instanceof JsonNode) {
            return true;
        }

        if (object1 instanceof String && object2 instanceof String) {
            return true;
        }

        if (object1 instanceof Number && object2 instanceof Number) {
            return true;
        }

        return object1 instanceof Boolean && object2 instanceof Boolean;
    }

    private void renderDiffResultRow(Object oldObject, Object newObject, String path) {
        try {
            diffResult = false;
            int depth = getDepthByPath(path);
            String oldContent = getContent(oldObject, path, depth);
            String newContent = getContent(newObject, path, depth);
            ResultRow resultRow = new ResultRow(oldContent, newContent, JsonRowResultEnum.DIFF.getName(), depth, path);
            handleAddStartList(resultRow, startList, endStack);
        } finally {
        }
    }

    private void renderSameResultRow(String path, Object object) {
        try {
            int depth = getDepthByPath(path);
            String content = getContent(object, path, depth);
            ResultRow resultRow = new ResultRow(content, content, JsonRowResultEnum.SAME.getName(), depth, path);
            handleAddStartList(resultRow, startList, endStack);
        } finally {
        }
    }

    private String getContent(Object obj, String path, int depth) {
        String content = getPrefix(depth) + getKeyByPath(path);
        if (obj instanceof String) {
            content += "\"" + obj + "\"";
        } else {
            content += obj;
        }
        return content;
    }

    private void renderJson(String path, String startSymbol, String endSymbol) {
        renderEmptyJson(path, startSymbol);
        int depth = getDepthByPath(path);
        String content = getPrefix(depth) + endSymbol;
        ResultRow endResultRow = new ResultRow(content, content, JsonRowResultEnum.SAME.getName(), depth, path);
        endStack.add(endResultRow);
    }

    private void renderEmptyJson(String path, String symbol) {
        try {
            int depth = getDepthByPath(path);
            String key = getKeyByPath(path);
            String content = getPrefix(depth) + key + symbol;
            ResultRow resultRow = new ResultRow(content, content, JsonRowResultEnum.SAME.getName(), depth, path);
            handleAddStartList(resultRow, startList, endStack);
        } finally {
        }
    }

    private void renderAddResultRow(Object object, String path) {
        try {
            if (addIgnoreField(object, path)) {
                return;
            }
            diffResult = false;
            int depth = getDepthByPath(path);
            if (isBasicObject(object)) {
                String newContent = getPrefix(depth) + getKeyByPath(path) + object;
                ResultRow resultRow = new ResultRow("", newContent, JsonRowResultEnum.ADD.getName(), depth, path);
                handleAddStartList(resultRow, startList, endStack);
                return;
            }

            if (object instanceof JsonNode && ((JsonNode) object).isObject()) {
                JsonNode jsonObject = (JsonNode) object;
                if (jsonObject.size() == 0) {
                    String newContent = getPrefix(depth) + getKeyByPath(path) + "{}";
                    ResultRow newResultRow = new ResultRow("", newContent, JsonRowResultEnum.ADD.getName(), depth, path);
                    handleAddStartList(newResultRow, startList, endStack);
                } else {
                    String newContent = getPrefix(depth) + getKeyByPath(path) + "{";
                    ResultRow newResultRow = new ResultRow("", newContent, JsonRowResultEnum.ADD.getName(), depth, path);
                    handleAddStartList(newResultRow, startList, endStack);

                    ResultRow endResultRow = new ResultRow("", getPrefix(depth) + "}", JsonRowResultEnum.ADD.getName(), depth, path);
                    endStack.add(endResultRow);
                    List<String> keyList = getSortedKeyList(jsonObject);
                    for (String key : keyList) {
                        String newPath = path + "/" + key;
                        renderAddResultRow(jsonObject.get(key), newPath);
                    }
                }
            }

            if (object instanceof JsonNode && ((JsonNode) object).isArray()) {
                JsonNode jsonArray = (JsonNode) object;
                if (jsonArray.size() == 0) {
                    String newContent = getPrefix(depth) + getKeyByPath(path) + "[]";
                    ResultRow newResultRow = new ResultRow("", newContent, JsonRowResultEnum.ADD.getName(), depth, path);
                    handleAddStartList(newResultRow, startList, endStack);
                } else {
                    String newContent = getPrefix(depth) + getKeyByPath(path) + "[";
                    ResultRow newResultRow = new ResultRow("", newContent, JsonRowResultEnum.ADD.getName(), depth, path);
                    handleAddStartList(newResultRow, startList, endStack);
                    ResultRow endResultRow = new ResultRow("", getPrefix(depth) + "]", JsonRowResultEnum.ADD.getName(), depth, path);
                    endStack.add(endResultRow);

                    if (jsonArray.size() > 0) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            String newPath = path + "[" + i + "]";
                            renderAddResultRow(jsonArray.get(i), newPath);
                        }
                    }
                }
            }
        } finally {
        }
    }

    private boolean addIgnoreField(Object object, String path) {
        if (needIgnore(removeArrayIndexNumber(path))) {
            renderIgnoreResultRow(null, object, path);
            return true;
        }
        return false;
    }

    private boolean deleteIgnoreField(Object object, String path) {
        if (needIgnore(removeArrayIndexNumber(path))) {
            renderIgnoreResultRow(object, null, path);
            return true;
        }
        return false;
    }

    private void renderDeleteResultRow(Object object, String path) {
        try {
            if (deleteIgnoreField(object, path)) {
                return;
            }
            diffResult = false;
            int depth = getDepthByPath(path);
            if (isBasicObject(object)) {
                String oldContent = getPrefix(depth) + getKeyByPath(path) + object;
                ResultRow oldResultRow = new ResultRow(oldContent, "", JsonRowResultEnum.DELETE.getName(), depth, path);
                handleAddStartList(oldResultRow, startList, endStack);
                return;
            }

            if (object instanceof JsonNode && ((JsonNode) object).isObject()) {
                JsonNode jsonObject = (JsonNode) object;
                if (jsonObject.size() == 0) {
                    String oldContent = getPrefix(depth) + getKeyByPath(path) + "{}";
                    ResultRow startResultRow = new ResultRow(oldContent, "", JsonRowResultEnum.DELETE.getName(), depth, path);
                    handleAddStartList(startResultRow, startList, endStack);
                } else {
                    String oldContent = getPrefix(depth) + getKeyByPath(path) + "{";
                    ResultRow startResultRow = new ResultRow(oldContent, "", JsonRowResultEnum.DELETE.getName(), depth, path);
                    handleAddStartList(startResultRow, startList, endStack);

                    ResultRow endResultRow = new ResultRow(getPrefix(depth) + "}", "", JsonRowResultEnum.DELETE.getName(), getDepthByPath(path), path);
                    endStack.add(endResultRow);

                    List<String> keyList = getSortedKeyList(jsonObject);
                    for (String key : keyList) {
                        String newPath = path + "/" + key;
                        renderDeleteResultRow(jsonObject.get(key), newPath);
                    }
                }
            }

            if (object instanceof JsonNode && ((JsonNode) object).isArray()) {
                JsonNode jsonArray = (JsonNode) object;
                if (jsonArray.size() == 0) {
                    String oldContent = getPrefix(depth) + getKeyByPath(path) + "[]";
                    ResultRow startResultRow = new ResultRow(oldContent, "", JsonRowResultEnum.DELETE.getName(), depth, path);
                    handleAddStartList(startResultRow, startList, endStack);
                } else {
                    String oldContent = getPrefix(depth) + getKeyByPath(path) + "[";
                    ResultRow startResultRow = new ResultRow(oldContent, "", JsonRowResultEnum.DELETE.getName(), depth, path);
                    handleAddStartList(startResultRow, startList, endStack);

                    ResultRow endResultRow = new ResultRow(getPrefix(depth) + "]", "", JsonRowResultEnum.DELETE.getName(), getDepthByPath(path), path);
                    endStack.add(endResultRow);

                    if (jsonArray.size() > 0) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            String newPath = path + "[" + i + "]";
                            renderDeleteResultRow(jsonArray.get(i), newPath);
                        }
                    }
                }
            }
        } finally {
        }
    }

    private void renderIgnoreResultRow(Object oldObject, Object newObject, String path) {
        try {
            int depth = getDepthByPath(path);
            String oldContent = JsonUtils.object2JsonString(oldObject);
            String newContent = JsonUtils.object2JsonString(newObject);
            if (StringUtils.isNotEmpty(oldContent)) {
                oldContent = getPrefix(depth) + getKeyByPath(path) + oldContent;
            }
            if (StringUtils.isNotEmpty(newContent)) {
                newContent = getPrefix(depth) + getKeyByPath(path) + newContent;
            }
            ResultRow startResultRow = new ResultRow(oldContent, newContent, JsonRowResultEnum.IGNORE.getName(), depth, path);
            handleAddStartList(startResultRow, startList, endStack);
        } finally {
        }
    }

    private boolean isBasicObject(Object object) {
        if (object == null || object instanceof Boolean || object instanceof Number || object instanceof String) {
            return true;
        }
        if (object instanceof JsonNode) {
            JsonNode jsonNode = (JsonNode) object;
            return jsonNode.isBoolean() || jsonNode.isNumber() || jsonNode.isTextual() || jsonNode.isNull();
        }
        return false;
    }

    private String getKeyByPath(String path) {
        String[] pathArr = path.split("/");
        if (pathArr.length == 1) {
            return "";
        }
        if (pathArr[pathArr.length - 1].contains("[")) {
            return "";
        }
        return pathArr[pathArr.length - 1] + ":";
    }

    private String getPrefix(Integer depth) {
        String prefix = "";
        for (int i = 0; i < depth; i++) {
            prefix = prefix + NBSP;
        }
        return prefix;
    }

    private int getDepthByPath(String path) {
        return path.split("/").length - 1 + path.split("\\[").length - 1;
    }

    private List<String> getSortedKeyList(JsonNode jsonObject) {
        try {
            if (jsonObject == null) {
                return Lists.newArrayList();
            }
            Iterator<String> keys = jsonObject.fieldNames();
            if (keys == null) {
                return Lists.newArrayList();
            }
            Set<String> keySet = Sets.newHashSet(keys);
            return keySet.stream().sorted().collect(Collectors.toList());
        } finally {
        }
    }
}
