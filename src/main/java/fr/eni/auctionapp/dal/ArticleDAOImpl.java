package fr.eni.auctionapp.dal;

import fr.eni.auctionapp.bo.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ArticleDAOImpl extends DAOImpl implements ArticleDAO {

    MemberDAO memberDAO;
    CategoryDAO categoryDAO;

    public ArticleDAOImpl(MemberDAO memberDAO, CategoryDAO categoryDAO) {
        this.memberDAO = memberDAO;
        this.categoryDAO = categoryDAO;
    }

    @Override
    public List<Article> findAll() {
        String sql = "SELECT * FROM article";
        return jdbcTemplate.query(sql, new ArticleRowMapper());
    }

    @Override
    public void callStatusUpdateProcedure() {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("UpdateAuctionStatus");
        simpleJdbcCall.execute();
    }

    @Override
    public Pageable<Article> findAllPaginated(int pageIndex, int itemQuantity) {
        String pageCountSql = "SELECT COUNT(*) FROM article";
        String articlesSql = "SELECT * FROM article";
        return queryPageable(pageCountSql, articlesSql, pageIndex, itemQuantity, new ArrayList<>());
    }

    @Override
    public Optional<Article> findById(int id) {
        String sql = "SELECT * FROM article WHERE id=?";
        Article article = jdbcTemplate.queryForObject(sql, new ArticleRowMapper(), id);
        return Optional.ofNullable(article);
    }

    @Override
    public int insert(Article article) {
        String sql = "INSERT INTO article (name, description, auction_start_date, auction_end_date, initial_price, sell_price, seller_id, state, buyer_id, category_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, article.getName());
            preparedStatement.setString(2, article.getDescription());
            preparedStatement.setDate(3, article.getAuctionStartDate());
            preparedStatement.setDate(4, article.getAuctionEndDate());
            preparedStatement.setInt(5, article.getInitialPrice());
            preparedStatement.setInt(6, article.getSellPrice());
            preparedStatement.setLong(7, article.getSeller().getId());
            preparedStatement.setString(8, article.getState().getValue());
            preparedStatement.setLong(9, article.getBuyer().getId());
            preparedStatement.setLong(10, article.getCategory().getId());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();

            article.setId(generatedKeys.getInt(1));

            return article.getId();

        } catch (Exception ex) {
            throw new RuntimeException("failed to create new article");
        }

    }

    @Override
    public void remove(Article article) {

    }

    @Override
    public Pageable<Article> findArticlesByCategoryAndNamePaginated(int categoryId, String search, int pageIndex, int itemQuantity) {
        String sqlCondition = " WHERE category_id=? AND name LIKE ? AND state=?";
        String pageCountSql = "SELECT COUNT(*) FROM article" + sqlCondition;
        String articlesSql = "SELECT * FROM article" + sqlCondition;

        List<Object> args = getSearchRequestArgs(categoryId, search);
        args.add(AuctionState.IN_PROGRESS.getValue());

        return queryPageable(pageCountSql, articlesSql, pageIndex, itemQuantity, args);
    }

    @Override
    public Pageable<Article> findMyArticlesBySearchPaginated(Search search, int pageIndex, int itemQuantity) {
        StringBuilder sqlCondition = new StringBuilder(" WHERE category_id=? AND name LIKE ? AND seller_id=? AND NOT state=?");
        StringBuilder pageCountSql = new StringBuilder("SELECT COUNT(*) FROM article");
        StringBuilder articlesSql = new StringBuilder("SELECT * FROM article");

        List<Object> args = getSearchRequestArgs(search.getCategoryId(), search.getSearchText());
        args.add(search.getConnectedMember().getId());
        args.add(AuctionState.CANCELED.getValue());

        if (search.hasFilters()) {
            int statesCount = 0;
            if (search.isCurrentSales()) {
                args.add(AuctionState.IN_PROGRESS.getValue());
                statesCount += 1;
            }
            if (search.isUnstartedSales()) {
                args.add(AuctionState.UNSTARTED.getValue());
                statesCount += 1;
            }
            if (search.isFinishedSales()) {
                args.add(AuctionState.EXPIRED.getValue());
                args.add(AuctionState.FINISHED.getValue());
                statesCount += 2;
            }
            sqlCondition.append(" AND state IN (")
                    .append(getParameterListPlaceholder(statesCount))
                    .append(")");
        }

        pageCountSql.append(sqlCondition);
        articlesSql.append(sqlCondition);

        return queryPageable(pageCountSql.toString(), articlesSql.toString(), pageIndex, itemQuantity, args);
    }

    @Override
    public Pageable<Article> findOthersArticlesBySearchPaginated(Search search, int pageIndex, int itemQuantity) {
        StringBuilder sqlCondition = new StringBuilder(" WHERE article.category_id=? AND article.name LIKE ? AND (NOT article.seller_id=? OR article.seller_id IS NULL) AND NOT article.state=? AND NOT article.state=?");
        StringBuilder pageCountSql = new StringBuilder("SELECT COUNT(DISTINCT article.id) FROM article LEFT JOIN auction ON article.id=auction.article_id");
        StringBuilder articlesSql = new StringBuilder("SELECT UNIQUE article.id, name, description, auction_start_date, auction_end_date, initial_price, sell_price, seller_id, state, buyer_id, category_id FROM article LEFT JOIN auction ON article.id=auction.article_id");

        List<Object> args = getSearchRequestArgs(search.getCategoryId(), search.getSearchText());
        int memberId = search.getConnectedMember().getId();
        args.add(memberId);
        args.add(AuctionState.UNSTARTED.getValue());
        args.add(AuctionState.CANCELED.getValue());

        if (search.hasFilters()) {
            boolean filterAdded = false;
            sqlCondition.append(" AND (");
            if (search.isOpenedAuctions()) {
                filterAdded = true;
                sqlCondition.append(" (article.state=? AND article.id NOT IN (SELECT article_id FROM auction WHERE member_id=?))");
                args.add(AuctionState.IN_PROGRESS.getValue());
                args.add(memberId);
            }
            if (search.isCurrentAuctions()) {
                if (filterAdded) {
                    sqlCondition.append(" OR");
                } else {
                    filterAdded = true;
                }
                sqlCondition.append(" (article.state=? AND auction.member_id=?)");
                args.add(AuctionState.IN_PROGRESS.getValue());
                args.add(memberId);
            }
            if (search.isWonAuctions()) {
                if (filterAdded) {
                    sqlCondition.append(" OR");
                }
                sqlCondition.append(" (article.state=? AND article.buyer_id=?)");
                args.add(AuctionState.FINISHED.getValue());
                args.add(memberId);
            }
            sqlCondition.append(")");
        }

        pageCountSql.append(sqlCondition);
        articlesSql.append(sqlCondition);

        return queryPageable(pageCountSql.toString(), articlesSql.toString(), pageIndex, itemQuantity, args);
    }

    private Pageable<Article> queryPageable(String pageCountSql, String articlesSql, int pageIndex, int itemQuantity, List<Object> args) {
        Integer totalCount = jdbcTemplate.queryForObject(pageCountSql, (rs, rowNum) -> rs.getInt(1), args.toArray());
        assert totalCount != null;

        if (totalCount > 0) {
            args.add(pageIndex * itemQuantity);
            args.add(itemQuantity);
            int pageCount = totalCount / itemQuantity + (totalCount % itemQuantity > 0 ? 1 : 0);
            List<Article> articles = jdbcTemplate.query(articlesSql + "ORDER BY auction_start_date DESC LIMIT ?,?", new ArticleRowMapper(), args.toArray());

            return new Pageable<>(articles, pageIndex, pageCount, false);
        } else {
            return new Pageable<>();
        }
    }

    private List<Object> getSearchRequestArgs(int categoryId, String search) {
        return new ArrayList<>(List.of(categoryId, "%" + search + "%"));
    }

    private String getParameterListPlaceholder(int count) {
        StringBuilder builder = new StringBuilder();
        builder.append("?,".repeat(Math.max(0, count)));
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    private class ArticleRowMapper implements RowMapper<Article> {
        @Override
        public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
            Article article;
            try {
                Member seller = memberDAO.findById(rs.getInt("seller_id")).orElse(null);
                Member buyer = memberDAO.findById(rs.getInt("buyer_id")).orElse(null);
                Category category = categoryDAO.findById(rs.getInt("category_id")).orElseThrow(RuntimeException::new);

                article = new Article(rs.getInt("id"), rs.getString("name"), rs.getString("description"),
                        rs.getDate("auction_start_date"), rs.getDate("auction_end_date"), rs.getInt("initial_price"),
                        rs.getInt("sell_price"), AuctionState.getFromString(rs.getString("state")), seller, buyer, category);

                return article;
            } catch (Exception ex) {
                throw new RuntimeException("Data not implement correctly");
            }
        }
    }
   
    

    @Override
    public void doAuction(int price, int idArticle, Authentication authentication) {
        String sql = "UPDATE article SET sell_price = ?, buyer_id = ? WHERE id = ?";
        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, price);
            preparedStatement.setInt(2, memberDAO.findByPseudo(authentication.getName()).get().getId());
            preparedStatement.setInt(3, idArticle);

            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException("failed to update the article after doing auction");
        }

    }

    @Override
    public List<Article> findAllBySellerId(int id) {
        String sql = "SELECT * FROM article WHERE seller_id = ?";
        return jdbcTemplate.query(sql, new ArticleRowMapper(), id);
    }

    @Override
    public List<Article> findAllByBuyerId(int id) {
        String sql = "SELECT * FROM article WHERE buyer_id = ?";
        return jdbcTemplate.query(sql, new ArticleRowMapper(), id);
    }

    @Override
    public List<Article> findUnstartedBySellerId(int id) {
        String state = AuctionState.UNSTARTED.getValue();
        String sql = "SELECT * FROM article WHERE seller_id = ? AND state = ?";
        return jdbcTemplate.query(sql, new ArticleRowMapper(), id, state);
    }

    @Override
    public void update(Article article) {
        String sql = "UPDATE article SET name = ?, description = ?,auction_start_date = ?, auction_end_date = ?, initial_price = ?, sell_price = ?, seller_id = ?, state = ?, buyer_id = ?, category_id = ? WHERE id = ?";

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource())
                .getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, article.getName());
            preparedStatement.setString(2, article.getDescription());
            preparedStatement.setDate(3, article.getAuctionStartDate());
            preparedStatement.setDate(4, article.getAuctionEndDate());
            preparedStatement.setInt(5, article.getInitialPrice());
            preparedStatement.setInt(6, article.getSellPrice());
            preparedStatement.setLong(7, article.getSeller().getId());
            preparedStatement.setString(8, article.getState().getValue());
            preparedStatement.setLong(9, article.getBuyer().getId());
            preparedStatement.setLong(10, article.getCategory().getId());
            preparedStatement.setInt(11, article.getId());
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("failed to update Article");
        }

    }

    @Override
    public void deleteArticle(int id) {
        String sql = "UPDATE article SET state = ? WHERE id = ?";
        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource())
                .getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, AuctionState.CANCELED.getValue());
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("failed to cancel Article");
        }
    }
   
    @Override
    public void cancelAllSellBySellerId(int memberId) {
        String sql = "UPDATE article SET state = ? WHERE seller_id = ?";

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource())
                .getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, AuctionState.CANCELED.getValue());
            preparedStatement.setInt(2, memberId);
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException("failed to cancel the Article");
        }
    }

    @Override
    public List<Article> findWinningArticlesByMemberId(int memberId) {
        String sql = "SELECT * FROM article WHERE id IN(WITH ordered AS " +
                "(SELECT a.*, ROW_NUMBER() OVER (PARTITION BY article_id ORDER BY amount DESC) rn " +
                "FROM auction a INNER JOIN article ON a.article_id = article.id WHERE member_id=22 AND state='IN_PROGRESS') " +
                "SELECT article_id FROM ordered WHERE rn = 1)";
        return jdbcTemplate.query(sql, new ArticleRowMapper(), memberId);
    }
}
