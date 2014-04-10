package es.appmaster.restapp.model;


import com.google.gson.annotations.SerializedName;

public class Student {

    private int id;
    private String name;
    private String city;

    @SerializedName("photo")
    private String urlPhoto;

    public Student(int id, String name, String city, String urlPhoto) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.urlPhoto = urlPhoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}
