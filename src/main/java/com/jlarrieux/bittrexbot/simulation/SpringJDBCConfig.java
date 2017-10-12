package com.jlarrieux.bittrexbot.simulation;

import com.jlarrieux.bittrexbot.simulation.DAO.SpringDBExchangeDAOImpl;
import com.jlarrieux.bittrexbot.simulation.DAO.IDBExchangeDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class SpringJDBCConfig {

    @Bean
    public  DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        //MySQL database we are using
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/bittrex");//change url
        dataSource.setUsername("root");//change userid
        dataSource.setPassword("");//change pwd
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

   /* @Bean
    public IDBExchangeDAO IDbexchangeDAO(){
        SpringDBExchangeDAOImpl dbExchangeDao = new SpringDBExchangeDAOImpl();
        dbExchangeDao.setJdbcTemplate(jdbcTemplate());
        return dbExchangeDao;
    }*/
}
