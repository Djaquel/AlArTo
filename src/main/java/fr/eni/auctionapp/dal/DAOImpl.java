package fr.eni.auctionapp.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DAOImpl {
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate template) {
        this.jdbcTemplate = template.getJdbcTemplate();
    }
}
