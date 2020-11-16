package com.shalini.dbms.controllers;

import com.shalini.dbms.models.Inventory;
import com.shalini.dbms.models.Product;
import com.shalini.dbms.models.ProductsInCart;
import com.shalini.dbms.models.User;
import com.shalini.dbms.repositories.*;
import com.shalini.dbms.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

public class ProductController {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    CartRepository cartRepository;
    InventoryRepository inventoryRepository;
    SecurityService securityService;
    UserRepository userRepository;
    ProductsInCartRepository productsInCartRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository, InventoryRepository inventoryRepository, CartRepository cartRepository, SecurityService securityService, UserRepository userRepository, ProductsInCartRepository productsInCartRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.cartRepository = cartRepository;
        this.inventoryRepository = inventoryRepository;
        this.securityService = securityService;
        this.productsInCartRepository = productsInCartRepository;
    }

@RequestMapping({"/product/{id}"})
    public String productDetails(@PathVariable("id") int id, Model model)
{
    model.addAttribute("qty","unknown");
    model.addAttribute("productsInCart",new ProductsInCart());
    model.addAttribute("product",productRepository.findById(id));
    model.addAttribute("inventory", inventoryRepository.findByProductId(id));
    if(securityService.findLoggedInUsername()!=null)
        model.addAttribute("cart",cartRepository.findByCustomerId(userRepository.findByEmail(securityService.findLoggedInUsername()).getId()));
    return "productDetails";
}

@RequestMapping({"/category/{id}"})
    public String categoryProducts(@PathVariable("id") int id, Model model)
{
    List<Product> categoryProducts = productRepository.findByCategoryId(id);
    model.addAttribute("categoryProducts",categoryProducts);
    return "categoryProducts";
}

@PostMapping({"/product/{id}"})
    public String addToCart(@PathVariable("id") int id, Model model, @ModelAttribute("productsInCart") ProductsInCart productsInCart)
{
    if(securityService.findLoggedInUsername()==null){
        model.addAttribute("user",new User());
        model.addAttribute("verification","unknown");
        return "redirect:/login";}
    productsInCart.setCart(cartRepository.findByCustomerId(userRepository.findByEmail(securityService.findLoggedInUsername()).getId()));
    productsInCart.setProduct(productRepository.findById(id));

    ProductsInCart productsInCart1 = productsInCartRepository.findBySizeCartIdProductId(productsInCart.getSize(),productsInCart.getCart().getId(),id);
    if(productsInCart.getQty()<=inventoryRepository.findQuantity(productsInCart.getProduct().getId(),productsInCart.getSize())){
    if(productsInCart1 == null) {
        productsInCartRepository.addToCart(productsInCart);

    }
    else
    {
        productsInCartRepository.updateQty(productsInCart.getQty(),productsInCartRepository.findBySizeCartIdProductId(productsInCart.getSize(),productsInCart.getCart().getId(),productsInCart.getProduct().getId()).getId() );

    }
    cartRepository.updateTotalPrice(productsInCart.getCart().getId(), productsInCartRepository.getTotalPrice(productsInCart.getCart().getId()));
    model.addAttribute("cartProducts", productsInCartRepository.findProductsByCartId(productsInCart.getCart().getId()));
    model.addAttribute("cart",cartRepository.findById(productsInCart.getCart().getId()));
    return "redirect:/cart";}
    model.addAttribute("product",productRepository.findById(id));
    model.addAttribute("productsInCart",new ProductsInCart());
    model.addAttribute("inventory", inventoryRepository.findByProductId(id));
    model.addAttribute("cart",cartRepository.findByCustomerId(userRepository.findByEmail(securityService.findLoggedInUsername()).getId()));
    model.addAttribute("qty","no");
    return "productDetails";
}
}
