package com.codinggyd.diff;

public class ResultRow {

    private String oldContent;

    /**
     * 如果为schema约束模式 此处内容为匹配失败的规则
     */
    private String newContent;

    /**
     * add 新增 delete 删除 diff 不同
     */
    private String diffType;

    /**
     * 字段在json串里面的深度
     */
    private Integer depth;

    /**
     * json key
     */
    private String jsonPath;

    public ResultRow(String oldContent, String newContent, String diffType, Integer depth, String jsonPath) {
        this.oldContent = oldContent;
        this.newContent = newContent;
        this.diffType = diffType;
        this.depth = depth;
        this.jsonPath = jsonPath;
    }

    public String getOldContent() {
        return oldContent;
    }

    public void setOldContent(String oldContent) {
        this.oldContent = oldContent;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public String getDiffType() {
        return diffType;
    }

    public void setDiffType(String diffType) {
        this.diffType = diffType;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }
}
