package com.tdei.filesvc.core.config.exception.handler.exceptions;

import com.tdei.filesvc.common.model.MetaValidationError;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MetadataValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private List<MetaValidationError> errorList;

    public MetadataValidationException(String message, List<MetaValidationError> errorList) {
        super(message);
        this.errorList = errorList;
    }
}
