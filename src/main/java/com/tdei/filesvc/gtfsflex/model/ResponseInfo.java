package com.tdei.filesvc.gtfsflex.model;

public class ResponseInfo {
    private boolean success;
    private String message;

    public ResponseInfo(boolean success, String message) {
        this.message = message;
        this.success = success;
    }
}
