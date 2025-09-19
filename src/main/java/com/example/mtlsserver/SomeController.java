package com.example.mtlsserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SomeController {

    @GetMapping("hello")
    public String hello() {
        return "hi";
    }
}
