package ru.alex.controller.common;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alex.entity.User;
import ru.alex.entity.dto.UserRole;
import ru.alex.service.UserService;

import java.util.Collections;
import java.util.Set;

@Controller
public class PublicAuthorizationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextRepository securityContextRepository;

    public PublicAuthorizationController(UserService userService, PasswordEncoder passwordEncoder, SecurityContextRepository securityContextRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.securityContextRepository = securityContextRepository;
    }


    @GetMapping("/registration")
    public String getRegistrationPage(){

        return "public/authorization/registration-page";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, @RequestParam(required = false) String error){
        if(error != null) {
            model.addAttribute("isAutentificationFailed", true);
        }

        return "public/authorization/login-page";
    }

    @PostMapping("/registration")
    public String createUserAccount(@RequestParam String name,
                                    @RequestParam String email,
                                    @RequestParam String password, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){

        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(name,email,encodedPassword, UserRole.USER);
        userService.save(user);
        forceAutoLogin(email,encodedPassword,httpServletRequest,httpServletResponse);
        return "redirect:/account";

    }
    private void forceAutoLogin(String email, String password, HttpServletRequest request, HttpServletResponse response){
        Set<SimpleGrantedAuthority> roles = Collections.singleton(UserRole.USER.toAuthority());
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password, roles);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);


    }
}
