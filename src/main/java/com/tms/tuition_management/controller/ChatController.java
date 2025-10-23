package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.User;
import com.tms.tuition_management.service.ChatService;
import com.tms.tuition_management.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @GetMapping("/messages")
    public String showMessages(Model model, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        model.addAttribute("conversations", chatService.getConversationsForUser(currentUser));
        return "messages";
    }

    @GetMapping("/chat/{recipientId}")
    public String getChat(@PathVariable Long recipientId, Model model, Authentication authentication) {
        User sender = userService.findByEmail(authentication.getName());
        User recipient = userService.findById(recipientId);

        model.addAttribute("recipient", recipient);
        model.addAttribute("messages", chatService.getConversation(sender, recipient));
        model.addAttribute("currentUser", sender);

        return "chat";
    }

    @PostMapping("/chat/send")
    public String sendMessage(@RequestParam Long recipientId, @RequestParam String content, Authentication authentication) {
        User sender = userService.findByEmail(authentication.getName());
        User recipient = userService.findById(recipientId);
        chatService.sendMessage(sender, recipient, content);
        return "redirect:/chat/" + recipientId;
    }
}