package com.example.user_app.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_images", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "image_link")
    private List<String> imageLinks = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_delete_hashes", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "delete_hash")
    private List<String> deleteHashes = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserImage> images = new ArrayList<>();

    public List<UserImage> getImages() {
        return images;
    }

    public void addImage(UserImage image) {
        images.add(image);
        image.setUser(this);
    }

    public void removeImage(UserImage image) {
        images.remove(image);
        image.setUser(null);
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(List<String> imageLinks) {
        this.imageLinks = imageLinks;
    }

    public List<String> getDeleteHashes() {
        return deleteHashes;
    }

    public void setDeleteHashes(List<String> deleteHashes) {
        this.deleteHashes = deleteHashes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}