package com.tdei.filesvc.osw.model;

import lombok.Data;

@Data
public class OswUploadMessage extends OswUpload {
    private String fileUploadPath;
}
