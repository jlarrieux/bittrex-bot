package com.jlarrieux.bittrexbot.simulation.DAO;

import com.jlarrieux.bittrexbot.simulation.TO.ResponseTO;
import org.springframework.jdbc.core.JdbcTemplate;

public class DBExchangeDAOImpl implements IDBExchangeDAO {

    private JdbcTemplate jdbcTemplate;

    @Override
    public ResponseTO getMarketSummaries() {
        return null;
    }

    @Override
    public String getMarketSummaryFor1(int id){
        String sql = "select market_currency from market where id = ?";
        String name = jdbcTemplate.queryForObject(sql,new Object[]{id},String.class);
        return name;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
}
