package ru.alex.controller.secured;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.alex.entity.User;
import ru.alex.entity.dto.UserRole;
import ru.alex.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class PrivateAdminController {
    private final UserService userService;

    @Autowired
    public PrivateAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ModelAndView getManagementPage(@RequestParam(name = "filter",required = false) String filterMode) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getCurrentUser();

        if (user.isSuperAdmin()) {
            List<User> candidatesToDelete = userService
                    .findAllByRoleIn(Arrays.asList(UserRole.USER, UserRole.ADMIN));

            List<User> candidatesToUpgrade = candidatesToDelete
                    .stream().filter(User::isSimpleUser).collect(Collectors.toList());

            modelAndView.getModelMap().addAttribute("candidatesToUpgrade", candidatesToUpgrade);
            modelAndView.getModelMap().addAttribute("candidatesToDelete", candidatesToDelete);


        } else {
            List<User> candidatesToDelete = userService
                    .findAllByRoleIn(Collections.singleton(UserRole.USER));
            modelAndView.getModelMap().addAttribute("candidatesToDelete", candidatesToDelete);
        }
         modelAndView.setViewName("private/admin/management-page");
        return modelAndView;
    }

    @PostMapping("/delete-user")
        public String deleteUser(@RequestParam int id){
        Optional<User> userToBeDeletedOptional = userService.findById(id);

        if(userToBeDeletedOptional.isEmpty()){
            return "redirect:/admin";
        }

        User userToBeDeleted = userToBeDeletedOptional.get();
        User currentUser = userService.getCurrentUser();
        if(userToBeDeleted.isSuperAdmin()){
            return "redirect:/admin";
        }
        if(userToBeDeleted.isSuperAdmin() && !currentUser.isSuperAdmin()){
            return "redirect:/admin";
        }
        userService.deleteById(id);

        return "redirect:/admin";
    }
}
