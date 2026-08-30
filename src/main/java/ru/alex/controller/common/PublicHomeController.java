package ru.alex.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PublicHomeController {

    @GetMapping("/")
    public String getHomePage(){
        return "public/home-page";
    }
}
