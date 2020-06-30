package com.uspacex.megatron.controller;

import com.uspacex.megatron.entity.ClientEntity;
import com.uspacex.megatron.model.FileModel;
import com.uspacex.megatron.repository.ClientRepository;
import com.uspacex.megatron.service.ClientService;
import com.uspacex.megatron.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v2.0")
public class NewFileRestController {
    @Value(value = "${megatron.filePath}")
    private String resourcePath;
    @Autowired
    private FileService fileService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/files/tree")
    public FileModel fileTree(@RequestParam("cip") String clientIp, @RequestParam("city") String clientCity,
                              HttpServletRequest request, HttpSession session) {
        String userAgent = request.getHeader("User-Agent");
        log.info("Agent: {} access from IP: {} : CITY: {}", userAgent, clientIp, clientCity);
        clientService.saveClientAccessLog(clientIp, clientCity, userAgent, "GET /files/tree");
        FileModel fileModel = new FileModel();
        fileService.fileTree(resourcePath, fileModel, new String[]{"pdf", "mp4", "jpg", "mp3", "txt", "md"}, 5);

        /**
         * 为了调用/api/v1.0/file/{fingerprint}.{type}，将所有文件提取到文件列表，目前文件提出只支持两层目录结构
         */
        List<FileModel> fileList = new ArrayList<>();
        for (FileModel iFileModel : fileModel.getFileList()) {
            if (iFileModel.isDir()) {
                for (FileModel jFileModel : iFileModel.getFileList()) {
                    if (!jFileModel.isDir()) {
                        fileList.add(jFileModel);
                    }
                }
            }
        }
        session.setAttribute("FILE_LIST", fileList);
        return fileModel;
    }
}