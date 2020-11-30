package com.shalini.dbms.events;


import com.shalini.dbms.models.ProductsBoughtByCustomer;
import com.shalini.dbms.models.ProductsInCart;
import com.shalini.dbms.models.User;
import com.shalini.dbms.repositories.ProductsBoughtByCustomerRepository;
import com.shalini.dbms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RegistrationEmailListener implements ApplicationListener<OnRegistrationSuccessEvent> {
    private MailSender mailSender;
    private UserService userService;

    @Autowired
    public RegistrationEmailListener(MailSender mailSender, UserService userService) {
        this.mailSender = mailSender;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(OnRegistrationSuccessEvent onRegistrationSuccessEvent) {
        this.confirmRegistration(onRegistrationSuccessEvent);
    }

    private void confirmRegistration(OnRegistrationSuccessEvent onRegistrationSuccessEvent) {
        User user = onRegistrationSuccessEvent.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        String recipient = user.getEmail();
        String subject = "Registration Confirmation";
        String url = onRegistrationSuccessEvent.getAppUrl() + "/confirmRegistration?token=" + token;
        String message = "Thank you for registering. Please click on the below link to activate your account.";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipient);
        email.setSubject(subject);
        email.setText(message + "http://shalini-dbms.herokuapp.com/" + url);
        mailSender.send(email);
    }
}
