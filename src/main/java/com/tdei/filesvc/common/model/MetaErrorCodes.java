package com.tdei.filesvc.common.model;

public enum MetaErrorCodes {

    INVALID_COLLECTEDBY_LENGTH(4000),
    NO_COLLECTION_DATE(4001),
    COLLECTION_DATE_FUTURE(4002),
    NO_COLLECTION_METHOD(4003),
    INVALID_COLLECTION_METHOD(4004),
    NO_DATA_SOURCE(4005),
    INVALID_DATA_SOURCE(4006);


    private final int status;
    MetaErrorCodes(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
