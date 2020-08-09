package com.forum.dao.impl;

import com.forum.dao.AlphaDao;
import org.springframework.stereotype.Repository;

@Repository
public class AlphaHibernateDaoImpl implements AlphaDao {
    @Override
    public void select() {
        System.out.println("hibernate");
    }
}
