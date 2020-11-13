package com.shalini.dbms.events;

import com.shalini.dbms.models.ProductsBoughtByCustomer;
import com.shalini.dbms.models.ProductsInCart;
import com.shalini.dbms.repositories.ProductsBoughtByCustomerRepository;
import com.shalini.dbms.repositories.ProductsInCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentEmailSender implements ApplicationListener<OnPaymentSuccessEvent> {

    private ProductsInCartRepository productsInCartRepository;
    private MailSender mailSender;

    @Autowired
    public PaymentEmailSender(MailSender mailSender, ProductsInCartRepository productsInCartRepository) {
        this.mailSender = mailSender;
        this.productsInCartRepository = productsInCartRepository;
    }

    @Override
    public void onApplicationEvent(OnPaymentSuccessEvent onPaymentSuccessEvent) {
        this.sendReciept(onPaymentSuccessEvent);
    }



    private void sendReciept(OnPaymentSuccessEvent onPaymentSuccessEvent)
    {
        List<ProductsInCart> products = onPaymentSuccessEvent.getProducts();
        int i,amount=0;
        String subject = "Reciept";
        String recipient = products.get(0).getCart().getCustomer().getEmail();
        String message="Thank you for Shopping. Your Payment was successful for INR ";
        for(i=0;i<products.size();i++)
        {
            amount+=products.get(i).getProduct().getPrice()*(100-products.get(i).getProduct().getDiscount())/100*products.get(i).getQty();
        }
        message+=amount+"\n";
        message+="Your bought products are:\n";
        for(i=0;i<products.size();i++)
        {
            message+="Product Name="+products.get(i).getProduct().getName()+"\nProduct Brand="+products.get(i).getProduct().getBrand();
            message+="\nSize="+products.get(i).getSize()+"\nQuantity="+products.get(i).getQty();
            message+="\nPrice="+ products.get(i).getProduct().getPrice();
            if(products.get(i).getProduct().getDiscount()>0)
                message+="\nDiscount="+products.get(i).getProduct().getDiscount()+"%";
            message+="\n";
        }
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipient);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
