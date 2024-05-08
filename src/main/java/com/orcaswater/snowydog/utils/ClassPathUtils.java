package com.orcaswater.snowydog.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.utils
 * @className: ClassPathUtils
 * @author: Orca121
 * @description:
 * @createTime: 2024-05-07 23:02
 * @version: 1.0
 */

public class ClassPathUtils {

    static final Logger logger = LoggerFactory.getLogger(ClassPathUtils.class);

    public static byte[] readBytes(String path) {
        try (InputStream input = ClassPathUtils.class.getResourceAsStream(path)) {
            if (input == null) {
                throw new FileNotFoundException("File not found in classpath: " + path);
            }
            return input.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String readString(String path) {
        return new String(readBytes(path), StandardCharsets.UTF_8);
    }
}
