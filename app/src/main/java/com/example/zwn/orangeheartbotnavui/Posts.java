package com.example.zwn.orangeheartbotnavui;

public class Posts {
    String artist,title, link, imageUrl;

    public Posts(String artist, String title, String link, String imageUrl) {
        this.artist = artist;
        this.title = title;
        this.link = link;
        this.imageUrl = imageUrl;
    }

    public Posts() {
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
