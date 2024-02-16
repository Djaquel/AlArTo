package fr.eni.auctionapp.dal;

import fr.eni.auctionapp.bo.Article;
import fr.eni.auctionapp.bo.Auction;
import fr.eni.auctionapp.bo.Member;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class MemberDAOImpl extends DAOImpl implements MemberDAO {

    @Override
    public List<Member> findAll() {
        String sql = "SELECT id, pseudo, lastname, firstname, email, phone, street, zipCode, city, password, credits, isAdmin, isEnabled FROM member";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    @Override
    public Optional<Member> findById(int id) {
        Member user;
        String sql = "SELECT id, pseudo, lastname, firstname, email, phone, street, zipCode, city, password, credits, isAdmin, isEnabled FROM member WHERE id=?";

        try {
            user = jdbcTemplate.queryForObject(sql, new MemberRowMapper(), id);
        } catch (DataAccessException dae) {
            return Optional.empty();
        }

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<Member> findByPseudo(String pseudo) {
        Member user;
        String sql = "SELECT id, pseudo, lastname, firstname, email, phone, street, zipCode, city, password, credits, isAdmin, isEnabled FROM member WHERE pseudo=?";

        try {
            user = jdbcTemplate.queryForObject(sql, new MemberRowMapper(), pseudo);
        } catch (DataAccessException dae) {
            return Optional.empty();
        }

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        Member user;
        String sql = "SELECT id, pseudo, lastname, firstname, email, phone, street, zipCode, city, password, credits, isAdmin, isEnabled FROM member WHERE email=?";

        try {
            user = jdbcTemplate.queryForObject(sql, new MemberRowMapper(), email);
        } catch (DataAccessException dae) {
            return Optional.empty();
        }

        return Optional.ofNullable(user);
    }

    @Override

    public int insert(Member member) {
        String sql = "INSERT INTO member (pseudo, lastname, firstname, email, phone, street, zipCode, city, password, credits, isAdmin, isEnabled) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, member.getPseudo());
            preparedStatement.setString(2, member.getLastname());
            preparedStatement.setString(3, member.getFirstname());
            preparedStatement.setString(4, member.getEmail());
            preparedStatement.setString(5, member.getPhone());
            preparedStatement.setString(6, member.getStreet());
            preparedStatement.setString(7, member.getZipCode());
            preparedStatement.setString(8, member.getCity());
            preparedStatement.setString(9, member.getPassword());
            preparedStatement.setInt(10, member.getCredits());
            preparedStatement.setBoolean(11, member.isAdmin());
            preparedStatement.setBoolean(12, member.isEnabled());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();

            member.setId(generatedKeys.getInt(1));

            return member.getId();

        } catch (Exception ex) {
            throw new RuntimeException("Data not implement correctly");
        }
    }

    @Override
    public void removeById(int memberId) {
        String sql ="DELETE FROM member WHERE id = ?";
        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, memberId);
            preparedStatement.executeUpdate();

        }catch (Exception ex) {
            throw new RuntimeException("failed to delete Member");
        }
    }

    @Override
    public void remove(Member member) {
        removeById(member.getId());
    }

    @Override
    public void update(Member member, Authentication authentication) {
        String sql = "UPDATE member SET pseudo = ?, lastname = ?, firstname = ?, email = ?, phone = ?, street = ?, zipCode = ?, city = ?, password = ? WHERE pseudo = ?";

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource())
                .getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, member.getPseudo());
            preparedStatement.setString(2, member.getLastname());
            preparedStatement.setString(3, member.getFirstname());
            preparedStatement.setString(4, member.getEmail());
            preparedStatement.setString(5, member.getPhone());
            preparedStatement.setString(6, member.getStreet());
            preparedStatement.setString(7, member.getZipCode());
            preparedStatement.setString(8, member.getCity());
            preparedStatement.setString(9, member.getPassword());
            preparedStatement.setString(10, authentication.getName());
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException("failed to update data");
        }

    }

    @Override
    public void updatePasswordByMemberId(int memberId, String encodedPassword) {
        String sql = "UPDATE member SET password = ? WHERE id = ?";

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource())
                .getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, encodedPassword);
            preparedStatement.setInt(2, memberId);
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException("failed to update member password");
        }
    }


    @Override
    public void updateCredits(Member member, int credits) {
        String sql = "UPDATE member SET credits = ? WHERE id = ?";



        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, credits);
            preparedStatement.setInt(2, member.getId());
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException("failed to update credits");
        }
    }

    @Override
    public void supprMemberLogout(Authentication authentication) {
        String sql = "DELETE FROM member WHERE pseudo = ?";

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource())
                .getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setString(1, authentication.getName());
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("failed to delete member");
        }
    }

    @Override
    public void deactivateMemberDAO(int memberId) {
        String sql = "UPDATE member SET isEnabled = 0 WHERE id = ?";
        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource())
                .getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, memberId);
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("failed to desactivate member");
        }

    }
    
    @Override
    public void nullableSellerId(int memberId) {
    	String sql = "UPDATE article SET seller_id = null WHERE seller_id = ?";
    	try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource())
                .getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, memberId);
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("failed to remove seller");
        }
    }

    @Override
    public void nullableBuyerId(int memberId) {
        String sql = "UPDATE article SET buyer_id = null WHERE buyer_id = ?";
        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource())
                .getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, memberId);
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("failed to remove buyer");
        }
    }

    @Override
    public void reactivateMemberDAO(int memberId) {
        String sql = "UPDATE member SET isEnabled = 1 WHERE id = ?";
        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource())
                .getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, memberId);
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("failed to reactivate member");
        }

    }

    private class MemberRowMapper implements RowMapper<Member> {

        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(
                    rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
                    rs.getString(9), rs.getString(10), rs.getInt(11), rs.getBoolean(12), rs.getBoolean(13));
        }
    }
}
