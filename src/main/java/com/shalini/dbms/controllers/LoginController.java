package com.shalini.dbms.controllers;

import com.shalini.dbms.models.*;
import com.shalini.dbms.repositories.*;
import com.shalini.dbms.services.SecurityService;
import com.shalini.dbms.services.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.security.Principal;

@Controller
public class LoginController {
    FAQRepository faqRepository;
    UserService userService;
    SecurityService securityService;
    CartRepository cartRepository;
    OrderRepository orderRepository;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    ProductsBoughtByCustomerRepository productsBoughtByCustomerRepository;
    InventoryRepository inventoryRepository;
    FeedbackRepository feedbackRepository;
    VendorProposalRepository vendorProposalRepository;
    VendorShopAddressRepository vendorShopAddressRepository;
    @Autowired
    public LoginController(FAQRepository faqRepository,UserService userService,OrderRepository orderRepository,VendorShopAddressRepository vendorShopAddressRepository,VendorProposalRepository vendorProposalRepository,InventoryRepository inventoryRepository, SecurityService securityService, CartRepository cartRepository, CategoryRepository categoryRepository, ProductRepository productRepository, ProductsBoughtByCustomerRepository productsBoughtByCustomerRepository,FeedbackRepository feedbackRepository) {
        this.userService = userService;
        this.faqRepository = faqRepository;
        this.securityService = securityService;
        this.cartRepository = cartRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.feedbackRepository = feedbackRepository ;
        this.productsBoughtByCustomerRepository = productsBoughtByCustomerRepository;
        this.inventoryRepository= inventoryRepository;
        this.vendorProposalRepository = vendorProposalRepository;
        this.vendorShopAddressRepository = vendorShopAddressRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/login")
    public String login(Model model){
        if(securityService.findLoggedInUsername()==null){
        model.addAttribute("user",new User());
        model.addAttribute("verification","unknown");
        return "login";}
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
    @RequestMapping("/viewProfile")
    public String viewProfile(Model model)
    {
        model.addAttribute("user",userService.findByUsername(securityService.findLoggedInUsername()));
        if(userService.findByUsername(securityService.findLoggedInUsername()).getRole()=="Vendor")
            model.addAttribute("vendorShopAddress",new VendorShopAddress());
        return "viewProfile";
    }

    @PostMapping("/viewProfile")
    public String editProfile(Model model,@ModelAttribute("user") User user)
    {
        user.setId(userService.findByUsername(securityService.findLoggedInUsername()).getId());
        userService.updateInfo(user);
        model.addAttribute("user",userService.findByUsername(securityService.findLoggedInUsername()));
        return "viewProfile";
    }

    @RequestMapping("/shopAddress")
    public String shopAddress(Model model)
    {

        model.addAttribute("address",new VendorShopAddress());
        return "shopAddress";
    }

    @PostMapping("/shopAddress")
    public String editAddress(Model model, @ModelAttribute("address") VendorShopAddress vendorShopAddress)
    {
        vendorShopAddress.setVendor(userService.findByUsername(securityService.findLoggedInUsername()));
        vendorShopAddressRepository.add(vendorShopAddress);
        model.addAttribute("shopaddress",vendorShopAddressRepository.findByVendorId(userService.findByUsername(securityService.findLoggedInUsername()).getId()));
        return "editaddress";
    }
    @RequestMapping("/proposals")
    public String proposals(Model model)
    {
        model.addAttribute("proposals",vendorProposalRepository.findByVendorId(userService.findByUsername(securityService.findLoggedInUsername()).getId()));
        return "proposals";
    }

    @RequestMapping("/addProposal")
    public String addProposal(Model model)
    {
        model.addAttribute("proposal",new VendorProposal());
        return "addProposal";
    }

    @PostMapping("/addProposal")
    public String newProposal(Model model,@ModelAttribute("proposal") VendorProposal vendorProposal)
    {
        vendorProposal.setVendor(userService.findByUsername(securityService.findLoggedInUsername()));
        vendorProposalRepository.add(vendorProposal);
        model.addAttribute("proposals",vendorProposalRepository.findByVendorId(userService.findByUsername(securityService.findLoggedInUsername()).getId()));

        return "proposals";
    }

    @RequestMapping("/welcome")
    public String welcome(Principal principal, Model model){
        User user = userService.findByUsername(principal.getName());
        if(user.getStatus().equals("not-verified")) {
            model.addAttribute("verification","false");
            model.addAttribute("user",new User());
            securityService.notVerified();
            return "login";
        }
        model.addAttribute("categories",categoryRepository.findAll());
        model.addAttribute("products", productRepository.findDiscounted());
        model.addAttribute("user",userService.findByUsername(securityService.findLoggedInUsername()));
        model.addAttribute("cart",cartRepository.findByCustomerId(userService.findByUsername(securityService.findLoggedInUsername()).getId()));
        model.addAttribute("feedback",new Feedback());
        if(vendorShopAddressRepository.findByVendorId(userService.findByUsername(securityService.findLoggedInUsername()).getId())==null)
            model.addAttribute("isAddressAdded","no");
        else
            model.addAttribute("isAddressAdded","yes");
        return "welcome";
    }

    @RequestMapping("/editAddress")
    public String editAddress(Model model)
    {
        model.addAttribute("shopaddress",vendorShopAddressRepository.findByVendorId(userService.findByUsername(securityService.findLoggedInUsername()).getId()));
        return "editAddress";
    }
    @PostMapping("/editAddress")
    public String addressedited(Model model, @ModelAttribute("shopaddress") VendorShopAddress vendorShopAddress)
    {
        vendorShopAddress.setVendor(userService.findByUsername(securityService.findLoggedInUsername()));
        vendorShopAddressRepository.update(vendorShopAddress);
        return "editAddress";
    }

    @PostMapping("welcome")
    public  String feedback(Model model,@ModelAttribute("feedback") Feedback feedback)
    {
        feedback.setCustomer(userService.findByUsername(securityService.findLoggedInUsername()));
        feedbackRepository.add(feedback);
        return "feedback";
    }

    @RequestMapping("/orders")
    public String orders(Model model)
    {
        model.addAttribute("allOrders",orderRepository.findAll(userService.findByUsername(securityService.findLoggedInUsername()).getId()));
        return "orders";
    }

    @RequestMapping("/admin")
    public String admin(Model model)
    {
        model.addAttribute("categories",categoryRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        return "admin";
    }

    @RequestMapping("/admin/addProduct/{id}")
    public String addProduct(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("category",categoryRepository.findById(id));
        model.addAttribute("product",new Product());
        return "addProduct";
    }

    @RequestMapping("/admin/addCategory")
    public String addCategory(Model model)
    {
        model.addAttribute("category",new Category());
        return "addCategory";
    }

    @PostMapping("/admin/addCategory")
    public String addNewCategory(Model model,@ModelAttribute("category") Category category)
    {
        categoryRepository.addCategory(category);
        model.addAttribute("categories",categoryRepository.findAll());
        model.addAttribute("products",productRepository.findAll());
        model.addAttribute("productsnotrecieved",orderRepository.notRecievedOrder());
        model.addAttribute("recieved", new ProductsBoughtByCustomer());
        model.addAttribute("feedbacks",feedbackRepository.findAll());
        return "redirect:/admin";
    }

    @PostMapping("/admin/addProduct/{id}")
    public String addProduct(@PathVariable("id") int id, Model model, @ModelAttribute("product")Product product)
    {
        product.setCategory(categoryRepository.findById(id));
        productRepository.addProduct(product);
        model.addAttribute("categories",categoryRepository.findAll());
        model.addAttribute("products",productRepository.findAll());
        model.addAttribute("productsnotrecieved",orderRepository.notRecievedOrder());
        model.addAttribute("recieved", new ProductsBoughtByCustomer());
        model.addAttribute("feedbacks",feedbackRepository.findAll());
        return "redirect:/admin";
    }


@RequestMapping("/admin/updateProductInfo/{id}")
    public String updateProductInfo(@PathVariable("id") int id, Model model)
{
    model.addAttribute("product",productRepository.findById(id));
    return "updateProductInfo";
}

@PostMapping("/admin/updateProductInfo/{id}")
public String updateProductInfo(@PathVariable("id") int id, Model model, @ModelAttribute("product")Product product)
{
    productRepository.updateProductInfo(product);
    model.addAttribute("categories",categoryRepository.findAll());
    model.addAttribute("products",productRepository.findAll());
    model.addAttribute("productsnotrecieved",orderRepository.notRecievedOrder());
    model.addAttribute("recieved", new ProductsBoughtByCustomer());
    model.addAttribute("feedbacks",feedbackRepository.findAll());
    return "redirect:/admin";
}
    @RequestMapping("/admin/updateProductInventory/{id}")
    public String updateProductInventory(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("inventories",inventoryRepository.findByProductId(id));
        model.addAttribute("inventoryToUpdate",new Inventory());
        model.addAttribute("product",productRepository.findById(id));
        model.addAttribute("inventoryToAdd",new Inventory());
        return "updateProductInventory";
    }

    @PostMapping ("/admin/updateProductInventory/{id}")
    public String UpdateInventory(@PathVariable("id") int id,@ModelAttribute("inventoryToUpdate") Inventory inventoryToUpdate, Model model, @ModelAttribute("inventoryToAdd") Inventory inventoryToAdd,@RequestParam(value="action", required=true) String action)
    {
        if(action.equals("Update Inventory"))
        {
            inventoryToUpdate.setProduct(productRepository.findById(id));
            inventoryRepository.updateQtyOfProduct(inventoryToUpdate);
        }
        else if(action.equals("Add Inventory"))
        {
            Inventory inventory = inventoryRepository.findByProductAndSize(id,inventoryToAdd.getSize());
            inventoryToAdd.setProduct(productRepository.findById(id));
            if(inventory==null)
            inventoryRepository.add(inventoryToAdd);
            else
                inventoryRepository.updateQtyOfProduct(inventoryToAdd);
        }
        model.addAttribute("inventories",inventoryRepository.findByProductId(id));
        model.addAttribute("inventoryToUpdate",new Inventory());
        model.addAttribute("product",productRepository.findById(id));
        model.addAttribute("inventoryToAdd",new Inventory());
        return "updateProductInventory";
    }

    @RequestMapping("/admin/ordersNotRecieved")
    public String ordersNotRecieved(Model model)
    {
        model.addAttribute("productsnotrecieved",orderRepository.notRecievedOrder());
        return "ordersNotRecieved";
    }

    @RequestMapping("/viewOrderDetails/{id}")
    public String ViewOrderDetails(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("allproducts",productsBoughtByCustomerRepository.findByOrderId(id));
        return "viewOrderDetails";
    }

    @RequestMapping("/admin/viewOrderDetails/{id}")
    public String ViewOrderDetailss(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("allproducts",productsBoughtByCustomerRepository.findByOrderId(id));
        return "viewOrderDetails";
    }

    @RequestMapping("/admin/feedbacksFromUsers")
    public String feedbacksFromUsers(Model model)
    {
        model.addAttribute("feedbacks",feedbackRepository.findAll());
        return "feedbacksFromUsers";
    }

    @PostMapping("/admin/markRecievedOrder/{id}")
    public String adminpost(Model model,@PathVariable("id") int id)
    {
        orderRepository.markRecieved(id);
        model.addAttribute("productsnotrecieved",orderRepository.notRecievedOrder());
        return "ordersNotRecieved";
    }

    @RequestMapping("/admin/vendorProposals")
    public String vendorProposals(Model model)
    {
        model.addAttribute("proposals",vendorProposalRepository.findAll());
        return "vendorProposals";
    }

    @RequestMapping("/admin/addFaq")
    public String addFaq(Model model)
    {
        model.addAttribute("faq",new FAQ());
        return "addFaq";
    }

    @PostMapping("/admin/addFaq")
    public String addedFaq(Model model,@ModelAttribute("faq") FAQ faq)
    {
        faqRepository.addFAQ(faq);
        return "redirect:/admin";
    }
}
