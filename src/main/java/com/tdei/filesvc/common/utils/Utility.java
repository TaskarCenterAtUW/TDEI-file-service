package com.tdei.filesvc.common.utils;

import java.util.Optional;

public class Utility {
    public static Optional<String> getExtensionByStringHandling(String filename) {
        if (!filename.contains(".")) return Optional.of(filename);
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
