package com.example.user_app.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.user_app.model.User;
import com.example.user_app.model.UserImage;
import com.example.user_app.service.ImgurService;
import com.example.user_app.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImgurService imgurService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/upload")
    public ResponseEntity<UserImage> upload(@RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        // Upload image to Imgur and get response with link and deleteHash
        Map<String, String> imgurResponse = imgurService.uploadImageWithDeleteHash(file);

        UserImage userImage = new UserImage();
        userImage.setImageUrl(imgurResponse.get("link"));
        userImage.setDeleteHash(imgurResponse.get("deleteHash"));

        // Associate new image with user
        user.addImage(userImage);

        userService.register(user);  // save user and cascade save images

        return ResponseEntity.ok(userImage);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserByUsername(authentication.getName()));
    }

    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        UserImage imageToDelete = user.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Delete from Imgur via deleteHash
        imgurService.deleteImage(imageToDelete.getDeleteHash());

        // Remove image from user and save
        user.removeImage(imageToDelete);
        userService.register(user);

        return ResponseEntity.ok().build();
    }
}