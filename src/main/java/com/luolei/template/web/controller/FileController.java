package com.luolei.template.web.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.luolei.template.error.BaseException;
import com.luolei.template.service.FileService;
import com.luolei.template.support.R;
import com.luolei.template.support.annotation.RequestURI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.Part;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @author luolei
 * @createTime 2018-03-31 11:02
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 单文件上传
     * @param part
     * @return
     */
    @PostMapping("/upload")
    public R uploadSingle(@RequestParam(name = "file") Part part) {
        return R.ok(fileService.save(part));
    }

    /**
     * 批量文件上传
     * @param parts
     * @return
     */
    @PostMapping("/upload/multi")
    public R uploadMulti(@RequestParam(name = "files") List<Part> parts) {
        return R.ok(fileService.save(parts));
    }

    /**
     * 文件下载
     * @param requestURI
     * @return
     */
    @GetMapping("/download/**")
    public StreamingResponseBody getFile(@RequestURI String requestURI) {
        final String prefix = "/file/download/";
        String filePath = requestURI.substring(prefix.length());
        File file = fileService.getFile(filePath);
        if (Objects.nonNull(file)) {
            return outputStream -> IoUtil.copy(FileUtil.getInputStream(file), outputStream);
        } else {
            throw new BaseException("待下载的文件不存在:" + requestURI);
        }
    }
}
