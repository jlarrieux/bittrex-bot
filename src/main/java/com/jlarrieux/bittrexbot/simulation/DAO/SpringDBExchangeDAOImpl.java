package com.jlarrieux.bittrexbot.simulation.DAO;

import com.jlarrieux.bittrexbot.simulation.TO.MarketSummariesTO;
import org.springframework.jdbc.core.JdbcTemplate;

public class SpringDBExchangeDAOImpl  {

    private JdbcTemplate jdbcTemplate;


    public MarketSummariesTO getMarketSummaries() {
        return null;
    }


    public MarketSummariesTO getMarketSummaryFor1(int id){
        String sql = "select market_currency from market where id = ?";
        String name = jdbcTemplate.queryForObject(sql,new Object[]{id},String.class);
        return new MarketSummariesTO();
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
}
