package ru.alex.controller.secured;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.alex.entity.RecordStatus;
import ru.alex.entity.dto.RecordsConteinerDto;
import ru.alex.service.RecordService;
import ru.alex.service.UserService;

@Controller
@RequestMapping("/account")
public class PrivateAccountController {
    private final UserService userService;
    private final RecordService recordService;

@Autowired
    public PrivateAccountController(UserService userService, RecordService recordService) {
        this.userService = userService;
        this.recordService = recordService;
    }

    @GetMapping
    public ModelAndView getMainPage(HttpServletRequest httpServletRequest, @RequestParam(name = "filter",required = false) String filterMode, Model model){
    HttpSession session = httpServletRequest.getSession();
    Object counter = session.getAttribute("visitsCounter");
    if(counter !=null){
        model.addAttribute("visitsCounter",(Integer)counter);
        session.setAttribute("visitsCounter",((Integer)counter)+1);
        }else{
        model.addAttribute("visitsCounter",0);
        session.setAttribute("visitsCounter",1);
    }
        ModelAndView modelAndView= new ModelAndView();
        modelAndView.setViewName("private/account-page");
        RecordsConteinerDto conteiner = recordService.findAllService(filterMode);

        modelAndView.getModelMap().addAttribute("userName",conteiner.getUserName());
        modelAndView.getModelMap().addAttribute("records",conteiner.getRecords());
        modelAndView.getModelMap().addAttribute("numberOfDoneRecords",conteiner.getNumberOfDoneRecords());
        modelAndView.getModelMap().addAttribute("numberOfActiveRecords",conteiner.getNumberOfActiveRecords());
        return modelAndView;
    }

    @PostMapping( "/add-record")
    public String addRecord(@RequestParam(name="title") String title) {

        recordService.saveRecord(title);

        return "redirect:/account";
    }
    @PostMapping( "/make-record-done")
    public String makeRecordDone(@RequestParam(name="id") int id,
                                 @RequestParam(name="filter",required = false) String filterMode) {

        recordService.updateRecordStatus(id,RecordStatus.DONE);

        return "redirect:/account" + ((filterMode!=null && !filterMode.isBlank()) ? "filter=" + filterMode : "");
    }
    @PostMapping("/delete-record")
    public String deleteRecord(@RequestParam (name="id") int id,
                                @RequestParam(name="filter",required = false) String filterMode) {

        recordService.deleteRecord(id);

        return "redirect:/account" + ((filterMode!=null && !filterMode.isBlank()) ? "?filter=" + filterMode : "");
    }
}
