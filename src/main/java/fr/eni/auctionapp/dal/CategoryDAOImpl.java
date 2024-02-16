package fr.eni.auctionapp.dal;

import fr.eni.auctionapp.bo.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CategoryDAOImpl extends DAOImpl implements CategoryDAO {
    @Override
    public List<Category> findAll() {
        String sql = "SELECT id, label FROM category";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }

    @Override
    public Optional<Category> findById(int id) {
        Category category;
        String sql = "SELECT id, label FROM category WHERE id=?";

        try {
            category = jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) -> new Category(
                    rs.getInt(1), rs.getString(2)), id);
        } catch (DataAccessException dae) {
            return Optional.empty();
        }

        return Optional.ofNullable(category);
    }

    @Override
    public int insert(Category category) {
        String sql = "INSERT INTO category (label) VALUES (?)";

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, category.getLabel());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();

            category.setId(generatedKeys.getInt(1));

            return category.getId();

        } catch (Exception ex) {
            throw new RuntimeException("failed to store data");
        }
    }

    @Override
    public void remove(Category category) {

    }

	@Override
	public void deleteCategoryById(int catId) {
		String sql = "DELETE FROM category WHERE id = ?";
		try(Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()){
			PreparedStatement preparedSrarement = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			preparedSrarement.setInt(1,catId);
			preparedSrarement.executeUpdate();
			}
		catch(Exception ex) {
			throw new RuntimeException("failed to delete category");
		}
		
	}

	@Override
	public void addCategoryByName(String catName) {
		String sql ="INSERT INTO category (label) VALUES (?)";
		try(Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()){
			PreparedStatement preparedStatement = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			StringBuilder cat = new StringBuilder("category.");
			cat.append(catName);
			preparedStatement.setString(1, cat.toString());
			preparedStatement.executeUpdate();
		}catch(Exception ex) {
			throw new RuntimeException("failed to create new category");
		}
		
	}
}
