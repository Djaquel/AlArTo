package fr.eni.auctionapp.dal;

import fr.eni.auctionapp.bo.Member;
import fr.eni.auctionapp.bo.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ResetTokenDAOImpl extends DAOImpl implements ResetTokenDAO {

    @Autowired
    MemberDAO memberDAO;

    @Override
    public List<PasswordResetToken> findAll() {
        return null;
    }

    @Override
    public Optional<PasswordResetToken> findById(int id) {
        String sql = "SELECT * FROM reset_token WHERE id=?";
        PasswordResetToken resetToken = jdbcTemplate.queryForObject(sql, new ResetTokenRowMapper(), id);
        return Optional.ofNullable(resetToken);
    }

    @Override
    public int insert(PasswordResetToken resetToken) {
        String sql = "INSERT INTO reset_token (token, member_id, expiry) VALUES (?, ?, ?)";

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, resetToken.getToken());
            preparedStatement.setInt(2, resetToken.getMember().getId());
            preparedStatement.setTimestamp(3, resetToken.getExpiry());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();

            resetToken.setId(generatedKeys.getInt(1));

            return resetToken.getId();

        } catch (Exception ex) {
            throw new RuntimeException("failed to create new token");
        }
    }

    @Override
    public void remove(PasswordResetToken resetToken) {
        String sql = "DELETE FROM reset_token WHERE id = ?";
        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, resetToken.getId());
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException("failed to delete token");
        }
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        try {
            String sql = "SELECT * FROM reset_token WHERE token=?";
            PasswordResetToken resetToken = jdbcTemplate.queryForObject(sql, new ResetTokenRowMapper(), token);
            return Optional.ofNullable(resetToken);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    private class ResetTokenRowMapper implements RowMapper<PasswordResetToken> {

        @Override
        public PasswordResetToken mapRow(ResultSet rs, int rowNum) {
            try {
                Member member = memberDAO.findById(rs.getInt("member_id")).orElseThrow(RuntimeException::new);

                return new PasswordResetToken(rs.getInt("id"), rs.getString("token"),
                        member, rs.getTimestamp("expiry"));
            } catch (Exception ex) {
                throw new RuntimeException("Data not implement correctly");
            }
        }
    }
}
