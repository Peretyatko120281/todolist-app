package ru.alex.controller.secured;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alex.entity.User;
import ru.alex.entity.dto.UserRole;
import ru.alex.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/super-admin")
public class PrivateSuperAdminController {
    private UserService userService;

    public PrivateSuperAdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/make-user-admin")
   public String makeUserAdmin(@RequestParam int id){
        Optional<User> userToBeUogradedOptional = userService.findById(id);
        User userToBeUpgraded = userToBeUogradedOptional.get();

        if(userToBeUpgraded.isSuperAdmin()) return "redirect:/admin";

        userService.updateRole(id, UserRole.ADMIN);


        return "redirect:/admin";

   }

}
