package com.shalini.dbms.controllers;

import com.shalini.dbms.models.Category;
import com.shalini.dbms.models.Product;
import com.shalini.dbms.models.User;
import com.shalini.dbms.repositories.CategoryRepository;
import com.shalini.dbms.repositories.FAQRepository;
import com.shalini.dbms.repositories.ProductRepository;
import com.shalini.dbms.services.SecurityService;
import com.shalini.dbms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomepageController {

    UserService userService;
    SecurityService securityService;
    FAQRepository faqRepository;
    @Autowired
    public HomepageController(UserService userService, SecurityService securityService,FAQRepository faqRepository)
    {
        this.securityService = securityService;
        this.userService = userService;
        this.faqRepository = faqRepository;
    }




//    @RequestMapping({"/", "", "/homepage"})
//    public String homepage(Model model) {
//        model.addAttribute("faqs",faqRepository.findAll());
//        if(securityService.findLoggedInUsername()!=null){
//            model.addAttribute("user",userService.findByUsername(securityService.findLoggedInUsername()));
//        model.addAttribute("isLoggedIn","yes");}
//        else
//        model.addAttribute("isLoggedIn","no");
//        return "homepage";
//    }

    @RequestMapping({"/feedback"})
    public String feedback(Model model)
    {
        return "feedback";
    }


}