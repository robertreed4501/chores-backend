package com.robertreed4501.choresbackend.mainpagetest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")


public class mainPageTestController {
    @GetMapping
    public String homePage(){
        return "<h1>Hello, World</h1>";
    }
}
