package com.shalini.dbms.controllers;


import com.shalini.dbms.repositories.CategoryRepository;
import com.shalini.dbms.repositories.FAQRepository;
import com.shalini.dbms.repositories.ProductRepository;
import com.shalini.dbms.services.SecurityService;
import com.shalini.dbms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomepageController {

    UserService userService;
    SecurityService securityService;
    FAQRepository faqRepository;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    @Autowired
    public HomepageController(UserService userService, SecurityService securityService,FAQRepository faqRepository,CategoryRepository categoryRepository, ProductRepository productRepository)
    {
        this.productRepository = productRepository;
        this.categoryRepository =categoryRepository;
        this.securityService = securityService;
        this.userService = userService;
        this.faqRepository = faqRepository;
    }

//@GetMapping
//    public String home()
//    {
//        return "home";
//    }

    @RequestMapping({ "/","", "/homepage"})
    public String homepage(Model model) {
        if(securityService.findLoggedInUsername()!=null){
            model.addAttribute("user",userService.findByUsername(securityService.findLoggedInUsername()));
            model.addAttribute("isLoggedIn","yes");}
        else
            model.addAttribute("isLoggedIn","no");
        return "homepage";
    }

    @RequestMapping({"/feedback"})
    public String feedback(Model model)
    {
        return "feedback";
    }

    @RequestMapping("/faqs")
    public String faq(Model model)
    {
        model.addAttribute("faqs",faqRepository.findAll());
        return "faqs";
    }

    @RequestMapping("/categories")
    public String categories(Model model)
    {
        model.addAttribute("categories",categoryRepository.findAll());
        return "categories";
    }

    @RequestMapping("bestOffers")
    public String bestOffers(Model model)
    {
        model.addAttribute("products", productRepository.findDiscounted());
        return "bestOffers";
    }


}