package io.cosentino.booksource.services;

import io.cosentino.booksource.models.User;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class UserDataService {
    private static String SIGNUP_URL = "https://us-central1-cosentino-ecom-proj.cloudfunctions.net/api/signup";
    private static String LOGIN_URL = "https://us-central1-cosentino-ecom-proj.cloudfunctions.net/api/login";
    private static String LOGOUT_URL = "https://us-central1-cosentino-ecom-proj.cloudfunctions.net/api/logout";

    public JSONObject signUp(String email, String password, String username) throws IOException, InterruptedException {

        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("username", username);
        json.put("password", password);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SIGNUP_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(json)))
                .build();
        HttpClient client = HttpClient.newHttpClient();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());

        JSONObject j = new JSONObject(response.body());
        return j;
    }

    public JSONObject login(String email, String password) throws IOException, InterruptedException {
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LOGIN_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(json)))
                .build();
        HttpClient client = HttpClient.newHttpClient();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());

        JSONObject j = new JSONObject(response.body());
        return j;

    }

    public String logout() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LOGOUT_URL))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        HttpClient client = HttpClient.newHttpClient();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject j = new JSONObject(response.body());
        System.out.println(j.getString("message"));
        return j.getString("message");
    }

}
