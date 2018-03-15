package com.mtl.zuul.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZuulController {
    @RequestMapping("/hellozuul")
    public String helloZuul(){
        return "hello zuul";
    }

}
