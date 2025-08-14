package com.loopers.interfaces.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping
    public String hello() {
        return "Hello, World!";
    }

}
