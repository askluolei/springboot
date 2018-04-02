package com.luolei.template.service;

import cn.hutool.core.util.StrUtil;
import com.luolei.template.support.FileSystem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传下载的功能
 *
 * @author luolei
 * @createTime 2018-03-31 11:01
 */
@Slf4j
@Service
public class FileService {
    /**
     * 上传文件保存的根路径
     */
    private static final String uploadRoot = "/upload";
    /**
     * 未知文件类型的上传文件保存目录
     */
    private static final String unknownTypeDir = "unknown";
    /**
     * 文件下载的路径前缀
     */
    private static final String downloadApiPre = "/file/download";

    private final FileSystem fileSystem;

    public FileService(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    private String getDownloadUrl(String path) {
        if (path.startsWith("/")) {
            return downloadApiPre + path;
        } else {
            return downloadApiPre + "/" + path;
        }
    }

    private String getParentDir(Part part) {
        String filePath = null;
        String contentType = part.getContentType();
        if (StrUtil.isNotBlank(contentType)) {
            int index = contentType.indexOf("/");
            if (index != -1) {
                filePath = uploadRoot + "/" + contentType.substring(0, index) + "/";
            }
        }
        if (StrUtil.isBlank(filePath)) {
            filePath = uploadRoot + "/" + unknownTypeDir + "/";
        }
        fileSystem.mkdirs(filePath);
        return filePath;
    }

    /**
     * 保存上传文件
     * @param part
     * @return 可用的链接
     */
    public String save(Part part) {
        String result = null;
        try {
            String parentDir = getParentDir(part);
            result = fileSystem.save(part.getInputStream(), parentDir + part.getSubmittedFileName());
        } catch (IOException e) {
            log.error("存文件异常", e);
        }
        return getDownloadUrl(result);
    }

    /**
     * 保存批量上传的文件
     * @param parts
     * @return 可用的链接
     */
    public List<String> save(List<Part> parts) {
        List<String> result = new ArrayList<>(parts.size());
        for (Part part : parts) {
            result.add(save(part));
        }
        return result;
    }

    /**
     * 根据请求路径，返回对应文件
     * @param filePath
     * @return
     */
    public File getFile(String filePath) {
        File file = fileSystem.getFile(filePath);
        if (file.exists() && file.isFile()) {
            return file;
        }
        return null;
    }
}
