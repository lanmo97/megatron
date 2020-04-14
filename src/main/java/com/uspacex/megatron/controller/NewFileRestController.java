package com.uspacex.megatron.controller;

import com.uspacex.megatron.model.FileModel;
import com.uspacex.megatron.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v2.0")
public class NewFileRestController {
    @Value(value = "${megatron.filePath}")
    private String resourcePath;
    @Autowired
    private FileService fileService;

    @GetMapping("/files/tree")
    public FileModel fileTree(HttpSession session) {
        FileModel fileModel = new FileModel();
        fileService.fileTree(resourcePath, fileModel, new String[]{"pdf", "mp4", "jpg", "mp3", "txt", "md"}, 5);
        return fileModel;
    }
}