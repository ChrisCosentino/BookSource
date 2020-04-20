package io.cosentino.booksource.controller;

import io.cosentino.booksource.models.*;
import io.cosentino.booksource.services.ProductsDataService;
import io.cosentino.booksource.services.UserDataService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    ProductsDataService productsDataService;

    @Autowired
    UserDataService userDataService;

    private String loginToken = "";

    private User user;

    private String error = "";

    private List<Product> cart = new ArrayList<Product>();

    private List<Review> reviews = new ArrayList<>();

    private double subtotal = 0.0;

    private int purchaseRequests = 0;


    @GetMapping("/")
    public String goHome(){
        return "redirect:home";
    }

    @GetMapping("/home")
    public String home(Model model) throws IOException, InterruptedException {
        model.addAttribute("cart", cart);

        this.error = "";
        model.addAttribute("loginToken", loginToken);
        model.addAttribute("user", this.user);

        return "home"; //Points to templates/home.html
    }

    @GetMapping("/products")
    public String products(Model model) throws Exception {
        model.addAttribute("products", productsDataService.getAllProducts());
        model.addAttribute("cart", cart);
        model.addAttribute("loginToken", loginToken);
        return "products"; //Points to templates/products.html
    }

    @GetMapping("/cart")
    public String cart(Model model){
        model.addAttribute("cart", cart);
        model.addAttribute("loginToken", loginToken);

        updateTotal();
        model.addAttribute("subtotal", subtotal);

        return "cart";
    }

    private void updateTotal(){
        this.subtotal = 0.0;
        for(Product item : this.cart){
            this.subtotal += item.getPrice();
        }
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable String id, Model model) throws Exception {
        model.addAttribute("product", productsDataService.getProduct(id));
        model.addAttribute("cart", cart);

        model.addAttribute("reviews", reviews);
        model.addAttribute("r", new Review());
        model.addAttribute("id", id);
        model.addAttribute("user", this.user);

        model.addAttribute("loginToken", loginToken);

        return "product";
    }

    @GetMapping(value = "/product/{id}", params = {"user", "title", "message"})
    public String addReview(@RequestParam String user, @RequestParam String title, @RequestParam String message, @PathVariable String id) throws IOException, InterruptedException {
        System.out.println(title);

        Review r = new Review(user, title, message);


        if(productsDataService.addReview(r, id) == true){
            System.out.println("Review added");
        }else{
            System.out.println("Review not added");
        }

        return "redirect:/product/{id}";
    }

    @PostMapping(value = "/product")
    public String reviewSubmit(@ModelAttribute Review review){
        reviews.add(review);
        return "redirect:/product";
    }


    @GetMapping("/add-to-cart")
    public String addCart(@RequestParam String author, @RequestParam String category, @RequestParam String title, @RequestParam String description, @RequestParam String price, @RequestParam boolean available, @RequestParam String productId, @RequestParam String photoUrl, @RequestParam String token) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
        String s = photoUrl + "&token=" + token;
        s = s.replace("books/", "books%2F");
        Product p = new Product(author, category, title, description, Double.parseDouble(price), available, productId, s);

        cart.add(p);

        System.out.println(cart);
        return "redirect:products";
    }

    @GetMapping("/login")
    public String login(Model model, @RequestParam(required = false) String error) throws IOException, InterruptedException {
        model.addAttribute("cart", cart);
        model.addAttribute("user", new User());
        model.addAttribute("loginToken", loginToken);

        if(error != ""){
            model.addAttribute("error", this.error);
            System.out.println(model.getAttribute("error"));
        }

        return "login";
    }

    @PostMapping(value = "/login")
    public String loginPressed(@ModelAttribute User user) throws IOException, InterruptedException {
        this.user = user;
        this.error = "";
        JSONObject loginMessage = userDataService.login(user.getEmail(), user.getPassword());

        if(loginMessage.has("general")){
            this.error = loginMessage.getString("general");
            return "redirect:login";
        }else if(loginMessage.has("error")){
            this.error = loginMessage.getString("error");
            return "redirect:login";
        }else{
            this.error = "";
            this.loginToken = loginMessage.getString("token");
            return "redirect:home";
        }
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("cart", cart);
        model.addAttribute("user", new User());
        model.addAttribute("error", this.error);
        model.addAttribute("loginToken", this.loginToken);
        if(this.loginToken != ""){
            return "redirect:home";
        }

        System.out.println(this.loginToken);
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpBtn(@ModelAttribute User user) throws IOException, InterruptedException {
        this.user = user;
        if(this.loginToken != ""){
            return "redirect:home";
        }
        JSONObject signUpMessage = userDataService.signUp(user.getEmail(), user.getPassword(), user.getUsername());

        if(signUpMessage.has("error")){
            this.error = signUpMessage.getString("error");
            return "redirect:signup";
        }
        if(signUpMessage.has("general")){
            this.error = signUpMessage.getString("general");
            return "redirect:signup";
        }
        this.error = "";
        return "redirect:login";
    }

    @GetMapping("/confirm")
    public String confirmPurchase(Model model){
//        model.addAttribute("purchaseRequests", this.purchaseRequests);
        return "confirm-purchase";
    }

    @PostMapping("/confirm")
    public String confirmPurchasePost(){
        this.purchaseRequests++;
        if(this.purchaseRequests >= 3){
            this.purchaseRequests = 0;
            return "redirect:failed";
        }

        return "redirect:success";
    }

    @GetMapping("/success")
    public String success(Model model){
        model.addAttribute("purchaseRequests", this.purchaseRequests);

        //Clear cart, reset everything to zero
        this.cart.clear();

        return "success";
    }

    @GetMapping("/failed")
    public String failed(Model model){
        return "failed";
    }


    @GetMapping("/logout")
    public String logout(Model model) throws IOException, InterruptedException {

        String logoutMessage = userDataService.logout();
        if(logoutMessage.equals("successfully logged out")){
            this.loginToken = "";
            System.out.println(logoutMessage);
        }else{
            System.out.println("Error logging out");
        }

        return "redirect:home";
    }

    @GetMapping("/clearCart")
    public String clearCart(){
        this.cart.clear();
        return "redirect:cart";
    }

    @GetMapping("/contact")
    public String contact(Model model){
        model.addAttribute("cart", this.cart);
        model.addAttribute("loginToken", this.loginToken);
        model.addAttribute("user", this.user);
        return "contact";
    }
}

