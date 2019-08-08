package com.agateram.emptyvideo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Controller
public class OAuthController {
    @RequestMapping("/")
    public String loginPage(final Model model, @RequestParam String clientId, @RequestParam String secret, @RequestParam String error) {
        model.addAttribute("clientId", clientId);
        model.addAttribute("secret", secret);
        model.addAttribute("error", error);
        return "login";
    }
}

