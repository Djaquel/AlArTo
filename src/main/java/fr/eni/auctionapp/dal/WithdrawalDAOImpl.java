package fr.eni.auctionapp.dal;

import fr.eni.auctionapp.bo.Withdrawal;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class WithdrawalDAOImpl extends DAOImpl implements WithdrawalDAO {

    @Override
    public List<Withdrawal> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Withdrawal> findById(int id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public void remove(Withdrawal withdrawal) {
        // TODO Auto-generated method stub

    }

    @Override
    public int insert(Withdrawal withdrawal) {
        String sql = "INSERT INTO withdrawal (article_id, street, zipCode, city) VALUES (?, ?, ?, ?)";

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, withdrawal.getArticle().getId());
            preparedStatement.setString(2, withdrawal.getStreet());
            preparedStatement.setString(3, withdrawal.getZipCode());
            preparedStatement.setString(4, withdrawal.getCity());
            preparedStatement.executeUpdate();

            return withdrawal.getArticle().getId();

        } catch (Exception ex) {
            throw new RuntimeException("Data not implement correctly");
        }
    }

    @Override
    public void update(Withdrawal withdrawal) {
        String sql = "UPDATE withdrawal SET street = ?, zipCode = ?, city = ? WHERE article_id = ?";
        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, withdrawal.getStreet());
            preparedStatement.setString(2, withdrawal.getZipCode());
            preparedStatement.setString(3, withdrawal.getCity());
            preparedStatement.setLong(4, withdrawal.getArticle().getId());
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("failed to update data");
        }
    }

}
