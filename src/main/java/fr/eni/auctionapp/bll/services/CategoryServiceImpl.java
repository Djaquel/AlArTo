package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Category;
import fr.eni.auctionapp.dal.CategoryDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    CategoryDAO categoryDAO;

    public CategoryServiceImpl(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(int categoryId) {
        return categoryDAO.findById(categoryId);
    }

	@Override
	public void deleteCategoryById(int catId) {
		categoryDAO.deleteCategoryById(catId);
		
	}

	@Override
	public void createCategory(String catName) {
		categoryDAO.addCategoryByName(catName);
		
	}
}
