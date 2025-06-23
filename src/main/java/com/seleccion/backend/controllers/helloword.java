package com.seleccion.backend.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class helloword {
   @GetMapping("/api/hello")
   
    public String hello(){
        return "Hello World";
    }
}
