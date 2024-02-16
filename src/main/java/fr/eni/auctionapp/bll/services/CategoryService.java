package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();

    Optional<Category> getCategoryById(int categoryId);

	void deleteCategoryById(int catId);

	void createCategory(String catName);
}
