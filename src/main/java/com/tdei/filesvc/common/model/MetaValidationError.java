package com.tdei.filesvc.common.model;

/**
 * Validation error generated when there is a metadata validation error.
 *
 */
public class MetaValidationError extends  Error {
    String errorName;
    String errorDescription;

    MetaErrorCodes code;

    public MetaValidationError(String errorName, String errorDescription) {
        this.errorName = errorName;
        this.errorDescription = errorDescription;
    }

    public MetaValidationError(MetaErrorCodes code, String errorDescription) {
        this.code = code;
        this.errorDescription = errorDescription;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public MetaErrorCodes getCode() {
        return code;
    }

    public void setCode(MetaErrorCodes code) {
        this.code = code;
    }
}
