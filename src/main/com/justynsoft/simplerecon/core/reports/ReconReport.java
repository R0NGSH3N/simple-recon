package com.justynsoft.simplerecon.core.reports;

import com.justynsoft.simplerecon.core.object.ReconObject;

import java.util.List;

public class ReconReport<T extends ReconObject>{

    private Boolean isMatch = false;
    private String errorMessage;
    private List<T> primaryList;
    private List<T> secondaryList;

    public Boolean getMatch() {
        return isMatch;
    }

    public void setMatch(Boolean match) {
        isMatch = match;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<T> getPrimaryList() {
        return primaryList;
    }

    public void setPrimaryList(List<T> primaryList) {
        this.primaryList = primaryList;
    }

    public List<T> getSecondaryList() {
        return secondaryList;
    }

    public void setSecondaryList(List<T> secondaryList) {
        this.secondaryList = secondaryList;
    }
}
