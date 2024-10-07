package com.mowitnow.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class TestFileUtils {
    public static String getFileAbsolutePath(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return resource.getFile().getAbsolutePath();
    }
}
