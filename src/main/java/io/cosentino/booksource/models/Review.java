package io.cosentino.booksource.models;

public class Review {
    private String user;
    private String title;
    private String message;
//    private String id;


    public Review(String user, String title, String message) {
        this.user = user;
        this.title = title;
        this.message = message;
//        this.id = id;
    }

    public Review(){
        this.user = "";
        this.title = "";
        this.message = "";
//        this.id = "";
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
}
