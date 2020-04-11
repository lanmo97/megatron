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
@RequestMapping("/api/v1.0")
public class FileRestController {
    @Value(value = "${megatron.filePath}")
    private String resourcePath;
    @Autowired
    private FileService fileService;
    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    public FileRestController(NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler) {
        this.nonStaticResourceHttpRequestHandler = nonStaticResourceHttpRequestHandler;
    }

    @GetMapping("/files")
    public List<FileModel> listAllFilesFromDir(HttpSession session) {
        List<FileModel> fileList = new ArrayList<>();
        fileService.listFiles(resourcePath, fileList, new String[]{"pdf", "mp4", "jpg", "jpeg"});
        session.setAttribute("FILE_LIST", fileList);
        return fileList;
    }

    /**
     * 获取文件流（播放视频文件等）
     */
    @GetMapping("/file/{fingerprint}.{type}")
    public void videoPreview(@PathVariable String fingerprint, @PathVariable String type, HttpSession session,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (session.getAttribute("FILE_LIST") == null) {
            log.info("no File_LIST in session");
            return;
        }
        List<FileModel> fileList = (List<FileModel>) session.getAttribute("FILE_LIST");
        Iterator<FileModel> fit = fileList.iterator();

        while (fit.hasNext()) {
            FileModel fileModel = fit.next();
            if (fingerprint.equals(fileModel.getFingerprint())) {
                log.info("match file name: " + fileModel.getName());
                Path filePath = Paths.get(fileModel.getPath());
                if (Files.exists(filePath)) {
                    String mimeType = Files.probeContentType(filePath);
                    if (!StringUtils.isEmpty(mimeType)) {
                        response.setContentType(mimeType);
                    }
                    request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
                    nonStaticResourceHttpRequestHandler.handleRequest(request, response);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
                }
                break;
            }
        }
    }
}