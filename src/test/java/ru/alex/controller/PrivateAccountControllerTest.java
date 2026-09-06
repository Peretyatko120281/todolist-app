package ru.alex.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.alex.controller.secured.PrivateAccountController;
import ru.alex.entity.RecordStatus;
import ru.alex.entity.dto.RecordsConteinerDto;
import ru.alex.service.RecordService;
import ru.alex.service.UserService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrivateAccountController.class)
@ActiveProfiles("test")
class PrivateAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private RecordService recordService;

    @Test
    @WithMockUser(username = "test@example.com")
    void getMainPage_ShouldReturnAccountPage() throws Exception {
        // given
        RecordsConteinerDto dto = new RecordsConteinerDto();
        dto.setUserName("Test User");
        dto.setRecords(Collections.emptyList());
        dto.setNumberOfActiveRecords(0L);  // ✅ Используем 0L для long
        dto.setNumberOfDoneRecords(0L);    // ✅ Используем 0L для long
        when(recordService.findAllService(anyString())).thenReturn(dto);

        // when & then
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(view().name("private/account-page"))
                .andExpect(model().attributeExists("userName"))
                .andExpect(model().attributeExists("records"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void addRecord_ShouldAddRecordAndRedirect() throws Exception {
        mockMvc.perform(post("/account/add-record")
                        .param("title", "New Task"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void makeRecordDone_ShouldUpdateRecordAndRedirect() throws Exception {
        mockMvc.perform(post("/account/make-record-done")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account"));

        verify(recordService).updateRecordStatus(1, RecordStatus.DONE);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void deleteRecord_ShouldDeleteRecordAndRedirect() throws Exception {
        mockMvc.perform(post("/account/delete-record")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account"));

        verify(recordService).deleteRecord(1);
    }
}