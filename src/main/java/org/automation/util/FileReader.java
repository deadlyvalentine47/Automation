//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.automation.util;

import java.io.File;

public interface FileReader {
    <T> T read(File data, Class<?> responseClass);

    <T> T read(String data, Class<?> responseClass);

    void write(Object data, File filePath);

    String write(Object data);

    <T> T convertTo(Object obj, Class type);

    <T> T readList(File data, Class<?> responseClass);

    <T> T readList(String data, Class<?> responseClass);
}