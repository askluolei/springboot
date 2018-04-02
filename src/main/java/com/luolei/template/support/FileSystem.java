package com.luolei.template.support;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.InputStream;

/**
 * 一个文件系统的接口
 *
 * @author luolei
 * @createTime 2018-03-31 10:48
 */
public interface FileSystem {

    String save(InputStream inputStream, String filePath);

    String save(byte[] bytes, String filePath);

    void remove(String filePath);

    String[] list(String path);

    String[] list(String path, FilenameFilter filter);

    File[] listFiles(String path);

    File[] listFiles(String path, FileFilter filter);

    File getFile(String filePath);

    boolean exist(String filePath);

    boolean move(String srcFilePath, String destFilePath);

    boolean copy(String srcFilePath, String destFilePath);

    boolean mkdir(String dirPath);

    boolean mkdirs(String dirPath);
}
