package com.uspacex.megatron.controller;

import com.uspacex.megatron.model.MessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.util.*;

@Slf4j
@Controller
public class ViewController {
    private List<MessageModel> messageList;

    public ViewController(ServletContext servletContext) {
        messageList = (List<MessageModel>) servletContext.getAttribute("MESSAGE_LIST");
        if (messageList == null) {
            messageList = new ArrayList<>();
            servletContext.setAttribute("MESSAGE_LIST", messageList);
        }
        MessageModel messageModel = new MessageModel();
        messageModel.setContent("学习服务计算技术是有用的，你同意么？");
        messageModel.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        messageModel.setTimeCreated(new Date());
        messageList.add(messageModel);
    }

    @GetMapping(value = {"/", "/index"})
    public String getIndex() {
        return "index";
    }

    @GetMapping(value = {"/messages"})
    public String getMessages(Model model) {
        if (messageList.size() >= 10) {
            model.addAttribute("messageList", messageList.subList(messageList.size() - 10, messageList.size()));
        } else {
            model.addAttribute("messageList", messageList);
        }
        return "messages";
    }

    @GetMapping(value = {"/error"})
    public String getError() {
        return "error";
    }

    @PostMapping(value = {"/messages"})
    public ModelAndView postMessages(@RequestParam(value = "message", required = false) String message) {
        if (message.trim().equals("")) {
            ModelAndView mvError = new ModelAndView();
            mvError.setViewName("redirect:/error");
            return mvError;
        }
        MessageModel messageModel = new MessageModel();
        messageModel.setContent(message);
        messageModel.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        messageModel.setTimeCreated(new Date());
        messageList.add(messageModel);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/messages/" + messageModel.getId());
        mv.setStatus(HttpStatus.SEE_OTHER);
        return mv;
    }

    @GetMapping(value = {"/messages/{id}"})
    public String getMessageById(@PathVariable("id") String id, Model model) {
        for (MessageModel message : messageList) {
            if (id.equals(message.getId())) {
                model.addAttribute("message", message);
                break;
            }
        }
        if (model.getAttribute("message") == null) {
            return "error";
        }
        return "message";
    }
}
