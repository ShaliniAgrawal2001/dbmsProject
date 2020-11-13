package com.shalini.dbms.controllers;

import com.shalini.dbms.events.OnPaymentSuccessEvent;
import com.shalini.dbms.models.Inventory;
import com.shalini.dbms.models.Order;
import com.shalini.dbms.models.ProductsInCart;
import com.shalini.dbms.repositories.*;
import com.shalini.dbms.services.SecurityService;
import com.shalini.dbms.services.UserService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class CartController {
    InventoryRepository inventoryRepository;
    CartRepository cartRepository;
    ProductsInCartRepository productsInCartRepository;
    ProductsBoughtByCustomerRepository productsBoughtByCustomerRepository;
    UserService userService;
    SecurityService securityService;
    private ApplicationEventPublisher eventPublisher;
    OrderRepository orderRepository;
    @Autowired
    public CartController(CartRepository cartRepository, OrderRepository orderRepository,SecurityService securityService, UserService userService, ApplicationEventPublisher eventPublisher, InventoryRepository inventoryRepository, ProductsInCartRepository productsInCartRepository, ProductsBoughtByCustomerRepository productsBoughtByCustomerRepository) {
        this.cartRepository = cartRepository;
        this.productsInCartRepository = productsInCartRepository;
        this.productsBoughtByCustomerRepository = productsBoughtByCustomerRepository;
        this.inventoryRepository = inventoryRepository;
        this.eventPublisher= eventPublisher;
        this.securityService = securityService;
        this.userService = userService;
        this.orderRepository = orderRepository;
    }


    @RequestMapping({"/cart"})
    public String cart(Model model)
    {
        model.addAttribute("cart",cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()));
        cartRepository.updateTotalPrice(cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()).getId(),productsInCartRepository.getTotalPrice(cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()).getId()));
        if(cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()).getTotalPrice()>0)
        model.addAttribute("cartProducts",productsInCartRepository.findProductsByCartId(cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()).getId()));
model.addAttribute("message","none");
        return "cart";
    }

    @PostMapping({"/cart"})
    public String payment( Model model, @RequestParam(value="action", required=true) String action)
    {
        int i,c=0;
        if(action.equals("Proceed to Pay")){
            List<ProductsInCart> productsInCart = productsInCartRepository.findProductsByCartId(cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()).getId());
            for(i=0;i<productsInCart.size();i++)
            {
                Inventory inventory = inventoryRepository.findByProductAndSize(productsInCart.get(i).getProduct().getId(),productsInCart.get(i).getSize());
                if(inventory==null||inventory.getInhouseqty()<productsInCart.get(i).getQty()){
                   productsInCartRepository.removeFromCart(productsInCart.get(i).getId());
                c++;}
            }
            if(c>0){
                int cartId = cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()).getId();
                cartRepository.updateTotalPrice(cartRepository.findById(cartId).getId(), productsInCartRepository.getTotalPrice(cartRepository.findById(cartId).getId()));
                model.addAttribute("cart",cartRepository.findById(cartId));
                model.addAttribute("cartProducts",productsInCartRepository.findProductsByCartId(cartId));
                model.addAttribute("message","Some items were removed from cart because desired quantity is not available now!");
                return "cart";}

            Order order = new Order();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            order.setTotalAmount(cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()).getTotalPrice());
            order.setCustomer(userService.findByUsername(securityService.findLoggedInUsername()));
            orderRepository.addRow(order);
            Order order1 = orderRepository.getId(dtf.format(now));
            productsBoughtByCustomerRepository.addRow(productsInCartRepository.findProductsByCartId(cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()).getId()),order1.getId());
            inventoryRepository.updateQty(productsInCartRepository.findProductsByCartId(cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()).getId()));
            try {
                eventPublisher.publishEvent(new OnPaymentSuccessEvent(productsInCartRepository.findProductsByCartId(cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()).getId())));
            }
            catch (Exception re) {
                re.printStackTrace();
            }

            productsInCartRepository.emptyCart(cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()).getId());
            cartRepository.updateTotalPrice(cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()).getId(),0);

            return "payment";}
model.addAttribute("message","none");
        return "cart";
    }
    @RequestMapping({"removeFromCart/{id}"})
    public String remove(@PathVariable("id") int id, Model model)
    {
        int cartId = productsInCartRepository.findById(id).getCart().getId();
        productsInCartRepository.removeFromCart(id);
        cartRepository.updateTotalPrice(cartRepository.findById(cartId).getId(), productsInCartRepository.getTotalPrice(cartRepository.findById(cartId).getId()));
        model.addAttribute("cart",cartRepository.findById(cartId));
        model.addAttribute("cartProducts",productsInCartRepository.findProductsByCartId(cartId));
        model.addAttribute("message","none");
        return "cart";
    }
}
