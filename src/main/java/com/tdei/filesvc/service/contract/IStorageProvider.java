package com.tdei.filesvc.service.contract;

import java.io.InputStream;
import java.net.URI;

public interface IStorageProvider {
    URI uploadBlob(InputStream sourceStream);
}
