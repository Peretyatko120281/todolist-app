package ru.alex.controller.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@ControllerAdvice
public class GlobalExceptionHandler implements ErrorController {

    @RequestMapping("/error")
    public String redirectToSpecificErrorPage(HttpServletResponse httpServletResponse){

        switch (HttpStatus.valueOf(httpServletResponse.getStatus())){
            case FORBIDDEN -> {
                return "forward:/error/403";
            }
            case NOT_FOUND -> {
                return "forward:/error/404";
            }
            default -> {
                return "forward:/error/500";
            }
        }
    }


    @GetMapping("/error/500")
    public String getCommonErrorPage(){
        return "public/error/common-error-page";
    }

    @GetMapping("/error/404")
    public String getNotFoundPage(){
        return "public/error/not-found-error-page";
    }

    @GetMapping("/error/403")
    public String getForbiddenErrorPage(){
        return "public/error/forbidden-error-page";
    }


    @ExceptionHandler(Throwable.class)
    public String handleThrowable(){
        return "redirect:=/error/500";
    }
}
