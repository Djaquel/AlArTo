package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.ResourceType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageServiceImpl implements StorageService {
    private final Path articleRoot = Paths.get("./uploads/articles");
    private final Path profileRoot = Paths.get("./uploads/profiles");

    @Override
    public void init() {
        try {
            Files.createDirectories(articleRoot);
            Files.createDirectories(profileRoot);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file, String filename, ResourceType type) {
        try {
            file.transferTo((switch (type) {
                case ARTICLE_IMAGE -> articleRoot;
                case PROFILE_IMAGE -> profileRoot;
            }).resolve(filename));
        } catch (Exception e) {
            throw new RuntimeException("failed to save the image");
        }
    }

    @Override
    public Resource load(String filename, ResourceType type) {
        try {
            Path file = (switch (type) {
                case ARTICLE_IMAGE -> articleRoot;
                case PROFILE_IMAGE -> profileRoot;
            }).resolve(filename);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("failed to load the image");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}