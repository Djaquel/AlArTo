package fr.eni.auctionapp.dal;


import fr.eni.auctionapp.bo.Auction;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


import org.springframework.jdbc.core.RowMapper;
import fr.eni.auctionapp.bo.Article;
import fr.eni.auctionapp.bo.AuctionState;
import fr.eni.auctionapp.bo.Category;
import fr.eni.auctionapp.bo.Member;



@Repository
public class AuctionDAOImpl extends DAOImpl implements AuctionDAO {

	private MemberDAO memberDAO;
	private ArticleDAO articleDAO;
	private CategoryDAO categoryDAO;
	
	public AuctionDAOImpl(MemberDAO memberDAO,ArticleDAO articleDAO,CategoryDAO categoryDAO) {
		this.memberDAO = memberDAO;
		this.articleDAO = articleDAO;
		this.categoryDAO = categoryDAO;
	}
	
	@Override
	public List<Auction> findAll() {
		// TODO Auto-generated method stub
		return null;
	}


    @Override
    public Optional<Auction> findById(int id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public int insert(Auction entity) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void remove(Auction auction) {
		String sql ="DELETE FROM auction WHERE id = ?";
		try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, auction.getId());
			preparedStatement.executeUpdate();

		}catch (Exception ex) {
			throw new RuntimeException("failed to delete Auction");
		}
    }

	@Override
	public void deleteAuction(int memberId) {
		String sql ="DELETE FROM auction WHERE member_id = ?";
		 try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
	            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            preparedStatement.setInt(1, memberId );
	            preparedStatement.executeUpdate();

	        }catch (Exception ex) {
	            throw new RuntimeException("failed to delete Auction");
	        }
	}

	@Override
	public Optional<Auction> getHigherAuction(int articleId) {
		String sql = "SELECT * FROM auction WHERE article_id = ? ORDER BY amount DESC LIMIT 1";
		List<Auction> auctions = jdbcTemplate.query(sql, new AuctionRowMapper(), articleId);
		return !auctions.isEmpty() ? Optional.of(auctions.get(0)) : Optional.empty();
	}

	@Override
	public void doAuction(Authentication authentication, int idArticle, int price) {

		Timestamp dateNow = Timestamp.valueOf(LocalDateTime.now());
		String sql = "INSERT INTO auction (date , amount, member_id, article_id) VALUES(?, ? ,? ,? )";
		try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setTimestamp(1, dateNow);
			preparedStatement.setInt(2, price);
			preparedStatement.setInt(3, memberDAO.findByPseudo(authentication.getName()).get().getId());
			preparedStatement.setInt(4, idArticle);

			preparedStatement.executeUpdate();

		} catch (Exception ex) {
			throw new RuntimeException("Failed to do Auction");
		}
	}

	@Override
	public List<Auction> findAllByArticleId(int articleId) {
		String sql = "SELECT * FROM auction WHERE article_id = ? ORDER BY amount DESC";
        return jdbcTemplate.query(sql, new AuctionRowMapper(),articleId);
	}

	@Override
	public List<Auction> getHigherCurrentAuctionsBySellerId(int memberId) {
		String sql = "SELECT * FROM auction WHERE id IN(WITH ordered AS " +
				"(SELECT a.*, ROW_NUMBER() OVER (PARTITION BY article_id ORDER BY amount DESC) rn " +
				"FROM auction a INNER JOIN article ON a.article_id = article.id WHERE seller_id=? AND state='IN_PROGRESS') " +
				"SELECT id FROM ordered WHERE rn = 1)";
		return jdbcTemplate.query(sql, new AuctionRowMapper(), memberId);
	}

	@Override
	public List<Auction> getWinningAuctionsByMemberId(int memberId) {
		String sql = "SELECT * FROM auction WHERE id IN(WITH ordered AS " +
				"(SELECT a.*, ROW_NUMBER() OVER (PARTITION BY article_id ORDER BY amount DESC) rn " +
				"FROM auction a INNER JOIN article ON a.article_id = article.id WHERE member_id=? AND state='IN_PROGRESS') " +
				"SELECT id FROM ordered WHERE rn = 1)";
		return jdbcTemplate.query(sql, new AuctionRowMapper(), memberId);
	}

	@Override
	public Optional<Auction> getHigherValidAuctionByArticleId(int articleId) {
		String sql = "SELECT a.* FROM auction a INNER JOIN article ON a.article_id = article.id " +
				"INNER JOIN member ON a.member_id = member.id WHERE isEnabled=1 AND member.credits>=a.amount " +
				"AND state='IN_PROGRESS' AND article_id=? ORDER BY amount DESC LIMIT 1";
		return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new AuctionRowMapper(), articleId));
	}

	@Override
	public void removeAuctionsAboveAmountByArticleId(int articleId, int amount) {
		String sql = "DELETE auction FROM auction INNER JOIN article ON auction.article_id = article.id " +
				"WHERE auction.amount>? AND state='IN_PROGRESS' AND article_id=?";
		try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, amount);
			preparedStatement.setInt(2, articleId);
			preparedStatement.executeUpdate();

		} catch (Exception ex) {
			throw new RuntimeException("Failed to remove unvalid auctions");
		}
	}

	@Override
	public void removeAuctionFromCancelledArticleBySellerId(int memberId) {
		String sql = "DELETE auction FROM auction INNER JOIN article ON auction.article_id = article.id WHERE seller_id=? AND state=?";
		try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, memberId);
			preparedStatement.setString(2, AuctionState.CANCELED.getValue());
			preparedStatement.executeUpdate();

		} catch (Exception ex) {
			throw new RuntimeException("Failed to remove auctions from cancelled article");
		}
	}

	private class AuctionRowMapper implements RowMapper<Auction> {

		@Override
		public Auction mapRow(ResultSet rs, int rowNum) {
			try {
				Member seller = memberDAO.findById(rs.getInt("member_id")).orElseThrow(RuntimeException::new);
				Article article = articleDAO.findById(rs.getInt("article_id")).orElseThrow(RuntimeException::new);

				return new Auction(rs.getInt("id"), rs.getDate("date"),
						rs.getInt("amount"), seller, article);
			} catch (Exception ex) {
				throw new RuntimeException("Data not implement correctly");
			}
		}
	}
}

