package fr.eni.auctionapp.config;

import fr.eni.auctionapp.bll.services.CategoryService;
import fr.eni.auctionapp.bo.Category;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {
    @Bean
    public List<Category> categories(CategoryService categoryService) {
        return categoryService.getAllCategories();
    }
}
