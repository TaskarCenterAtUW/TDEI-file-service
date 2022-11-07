package com.tdei.filesvc.service.contract;

import java.io.InputStream;
import java.net.URI;

public interface IStorageService {
    URI uploadBlob(InputStream sourceStream);
}
