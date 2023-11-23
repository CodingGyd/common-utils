package com.codinggyd.diff;


import java.util.List;


public class DiffResult {

    private Boolean result;

    private List<ResultRow> diffResultRowList;

    private List<String> ignoreFieldList;

    private List<IgnoreArrayOrder> ignoreArrayOrderList;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<ResultRow> getDiffResultRowList() {
        return diffResultRowList;
    }

    public void setDiffResultRowList(List<ResultRow> diffResultRowList) {
        this.diffResultRowList = diffResultRowList;
    }

    public List<String> getIgnoreFieldList() {
        return ignoreFieldList;
    }

    public void setIgnoreFieldList(List<String> ignoreFieldList) {
        this.ignoreFieldList = ignoreFieldList;
    }

    public List<IgnoreArrayOrder> getIgnoreArrayOrderList() {
        return ignoreArrayOrderList;
    }

    public void setIgnoreArrayOrderList(List<IgnoreArrayOrder> ignoreArrayOrderList) {
        this.ignoreArrayOrderList = ignoreArrayOrderList;
    }
}
