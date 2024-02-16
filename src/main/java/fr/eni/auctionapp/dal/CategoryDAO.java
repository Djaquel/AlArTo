package fr.eni.auctionapp.dal;

import fr.eni.auctionapp.bo.Category;

public interface CategoryDAO extends DAO<Category> {

	void deleteCategoryById(int catId);

	void addCategoryByName(String catName);

}
