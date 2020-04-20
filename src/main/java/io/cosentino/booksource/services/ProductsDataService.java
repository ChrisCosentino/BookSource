package io.cosentino.booksource.services;

import io.cosentino.booksource.models.Product;
import io.cosentino.booksource.models.Review;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProductsDataService {
    private static String PRODUCT_DATA_URL = "https://us-central1-cosentino-ecom-proj.cloudfunctions.net/api/products";
    private static String PRODUCT_REVIEW_URL = PRODUCT_DATA_URL + "/reviews/";
    private List<Product> productList = new ArrayList();
    private Product product;
    private List<Product> cart = new ArrayList<>();
    private String productId;


    public void fetchProducts() throws Exception {

        try{

        String result = readUrl(PRODUCT_DATA_URL);

        JSONArray json = new JSONArray(result);

        for(int i = 0; i < json.length(); i++){

            Product p = new Product();
            JSONObject e = json.getJSONObject(i);

            p.setTitle(e.getString("title"));
            p.setAuthor(e.getString("author"));
            p.setAvailable(e.getBoolean("available"));
            p.setCategory(e.getString("category"));
            p.setDescription(e.getString("description"));
            p.setPrice(e.getDouble("price"));
            p.setProductId(e.getString("productId"));
//            URL u = new URL(e.getString("photo"));
//            p.setPhotoUrl(u);
            p.setPhotoUrl(e.getString("photo"));
            productList.add(p);
        }
        }catch(Exception e ){
            e.printStackTrace();
        }
    }


    //fetch individual product
    private Product fetchProduct(String id) throws Exception{
        Product p = new Product();

        try{

            String result = readUrl(PRODUCT_DATA_URL + "/" + id);
            String reviews = readUrl(PRODUCT_REVIEW_URL + id);

            JSONArray jsonProducts = new JSONArray(result);
            JSONArray jsonReviews = new JSONArray(reviews);

            for(int i = 0; i < jsonProducts.length(); i++){

                JSONObject e = jsonProducts.getJSONObject(i);


                p.setTitle(e.getString("title"));
                p.setAuthor(e.getString("author"));
                p.setAvailable(e.getBoolean("available"));
                p.setCategory(e.getString("category"));
                p.setDescription(e.getString("description"));
                p.setPrice(e.getDouble("price"));
                p.setProductId(e.getString("productId"));
//                URL u = new URL(e.getString("photo"));
//                p.setPhotoUrl(u);
                p.setPhotoUrl(e.getString("photo"));
            }

            List<Review> reviews1 = new ArrayList<>();

            for(int i = 0; i < jsonReviews.length(); i++){
                JSONObject e = jsonReviews.getJSONObject(i);

                Review r = new Review();
                r.setTitle(e.getString("title"));
                r.setMessage(e.getString("message"));
                r.setUser(e.getString("user"));
                reviews1.add(r);
            }
            p.setReviews(reviews1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return p;
    }

    public boolean addReview(Review r, String id) throws IOException, InterruptedException {
        String z = "https://us-central1-cosentino-ecom-proj.cloudfunctions.net/api/products/reviews/" + id;

        String jsonInputString = new JSONObject()
                .put("user", r.getUser())
                .put("title", r.getTitle())
                .put("message", r.getMessage())
                .toString();
        
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(z))
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        JSONObject res = new JSONObject(response.body());

        if(res.get("message").equals("document created successfully")){
            return true;
        }else{
            return false;
        }
    }

    public Product getProduct(String id) throws Exception {
            this.product = this.fetchProduct(id);
            return this.product;
    }

    public List<Product> getAllProducts() throws Exception {
        this.productList.clear();
        fetchProducts();
        return this.productList;
    }

    public void addToCart(Product p){
        cart.add(p);
    }

    public List<Product> getCart(){
        return this.cart;
    }





    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            System.out.println(buffer.toString());
            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
