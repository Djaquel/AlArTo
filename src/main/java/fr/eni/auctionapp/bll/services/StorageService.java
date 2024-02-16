package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.ResourceType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void init();

    void save(MultipartFile file, String filename, ResourceType type);

    Resource load(String filename, ResourceType type);
}