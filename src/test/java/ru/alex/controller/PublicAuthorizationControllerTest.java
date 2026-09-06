package ru.alex.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.alex.controller.common.PublicAuthorizationController;
import ru.alex.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicAuthorizationController.class)
@ActiveProfiles("test")
class PublicAuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean  // ✅ Новый способ
    private UserService userService;

    @MockitoBean  // ✅ Новый способ
    private PasswordEncoder passwordEncoder;

    @MockitoBean  // ✅ Новый способ
    private SecurityContextRepository securityContextRepository;
    @Test
    void getRegistrationPage_ShouldReturnRegistrationPage() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/authorization/registration-page"));
    }

    @Test
    void getLoginPage_ShouldReturnLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/authorization/login-page"));
    }

    @Test
    void getLoginPage_WithError_ShouldAddErrorAttribute() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/authorization/login-page"))
                .andExpect(model().attributeExists("isAutentificationFailed"));
    }
}