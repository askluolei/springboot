package com.luolei.template.support;

import cn.hutool.core.io.FileUtil;
import com.luolei.template.config.ApplicationProperties;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.nio.file.Paths;

/**
 * 本地文件系统
 * 本地存储上传的文件，提供下载服务
 *
 * @author luolei
 * @createTime 2018-03-31 10:59
 */
public class LocalFileSystem implements FileSystem {

    private final ApplicationProperties.FileSystem fileSystem;

    public LocalFileSystem(ApplicationProperties applicationProperties) {
        this.fileSystem = applicationProperties.getFileSystem();
    }

    private String getPath(String filePath) {
        return Paths.get(fileSystem.getRootDir(), filePath).toString();
    }

    @Override
    public String save(InputStream inputStream, String filePath) {
        String path = getPath(filePath);
        FileUtil.writeFromStream(inputStream, path);
        return filePath;
    }

    @Override
    public String save(byte[] bytes, String filePath) {
        String path = getPath(filePath);
        FileUtil.writeBytes(bytes, path);
        return filePath;
    }

    @Override
    public void remove(String filePath) {
        String path = getPath(filePath);
        File file = new File(path);
        file.deleteOnExit();
    }

    @Override
    public String[] list(String path) {
        String realPath = getPath(path);
        File file = new File(realPath);
        if (file.exists() && file.isDirectory()) {
            return file.list();
        }
        return new String[0];
    }

    @Override
    public String[] list(String path, FilenameFilter filter) {
        String realPath = getPath(path);
        File file = new File(realPath);
        if (file.exists() && file.isDirectory()) {
            return file.list(filter);
        }
        return new String[0];
    }

    @Override
    public File[] listFiles(String path) {
        String realPath = getPath(path);
        File file = new File(realPath);
        if (file.exists() && file.isDirectory()) {
            return file.listFiles();
        }
        return new File[0];
    }

    @Override
    public File[] listFiles(String path, FileFilter filter) {
        String realPath = getPath(path);
        File file = new File(realPath);
        if (file.exists() && file.isDirectory()) {
            return file.listFiles(filter);
        }
        return new File[0];
    }

    @Override
    public File getFile(String filePath) {
        String realPath = getPath(filePath);
        File file = new File(realPath);
        return file;
    }

    @Override
    public boolean exist(String filePath) {
        String realPath = getPath(filePath);
        File file = new File(realPath);
        return file.exists();
    }

    @Override
    public boolean move(String srcFilePath, String destFilePath) {
        FileUtil.move(new File(srcFilePath), new File(destFilePath), true);
        return true;
    }

    @Override
    public boolean copy(String srcFilePath, String destFilePath) {
        FileUtil.copy(srcFilePath, destFilePath, true);
        return true;
    }

    @Override
    public boolean mkdir(String dirPath) {
        String realPath = getPath(dirPath);
        File file = new File(realPath);
        if (!file.exists()) {
            return file.mkdir();
        }
        return true;
    }

    @Override
    public boolean mkdirs(String dirPath) {
        String realPath = getPath(dirPath);
        File file = new File(realPath);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }
}
