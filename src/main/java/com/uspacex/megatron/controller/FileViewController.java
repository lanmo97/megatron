package com.uspacex.megatron.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class FileViewController {
    @GetMapping(value = {"/files"})
    public String getFiles() {
        return "files";
    }

    @GetMapping(value = {"/resources"})
    public String getResources() {
        return "resources";
    }
}
