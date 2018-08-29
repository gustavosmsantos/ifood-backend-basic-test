package com.ifood.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class About {

    @GetMapping("/about")
    public String healthCheck() {
        return "Service is running.";
    }

}
