package com.shalini.dbms.controllers;

import com.shalini.dbms.events.OnRegistrationSuccessEvent;
import com.shalini.dbms.models.Feedback;
import com.shalini.dbms.models.User;
import com.shalini.dbms.models.VerificationToken;
import com.shalini.dbms.repositories.CartRepository;
import com.shalini.dbms.repositories.CategoryRepository;
import com.shalini.dbms.repositories.ProductRepository;
import com.shalini.dbms.services.SecurityService;
import com.shalini.dbms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;

@Controller
public class RegistrationController {

    private UserService userService;
    private SecurityService securityService;
    private ApplicationEventPublisher eventPublisher;
    private CartRepository cartRepository;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    @Autowired
    public RegistrationController(UserService userService, CategoryRepository categoryRepository,ProductRepository productRepository,SecurityService securityService, ApplicationEventPublisher eventPublisher, CartRepository cartRepository) {
        this.userService = userService;
        this.securityService = securityService;
        this.eventPublisher = eventPublisher;
        this.cartRepository = cartRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/register")
    public String register(Model model){
        if(securityService.findLoggedInUsername()==null){
            model.addAttribute("user",new User());
            return "register";}
        else
        {
            model.addAttribute("categories",categoryRepository.findAll());
            model.addAttribute("products", productRepository.findDiscounted());
            model.addAttribute("user",userService.findByUsername(securityService.findLoggedInUsername()));
            model.addAttribute("cart",cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()));
            model.addAttribute("feedback",new Feedback());
            return "welcome";
        }
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, BindingResult result, WebRequest request, Model model, @RequestParam(value="action", required=true) String action){

        if(userService.findByUsername(user.getEmail())!=null){
            model.addAttribute("emailError","email already exists");
            model.addAttribute("user",new User());
            return "register";
        }
        if (action.equals("Register as Customer")) {
            user.setRole("Customer");
        }

        if (action.equals("Register as Vendor")) {
           user.setRole("Vendor");
        }

        userService.save(user);
        User user1 = userService.findByUsername(user.getEmail());
        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationSuccessEvent(appUrl,user1));
        }catch (Exception re) {
            re.printStackTrace();
        }
        return "mail";
    }

    @GetMapping("/confirmRegistration")
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token){
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if(verificationToken == null) {
            return "redirect:/access-denied";
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if(verificationToken.getExpiryDate().getTime()-calendar.getTime().getTime()<=0){
            return "redirect:/access-denied";
        }
        userService.enableRegisteredUser(user);
        cartRepository.addCart(user);
        return "redirect:/login";
    }

}