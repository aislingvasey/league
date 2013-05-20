package com.africaapps.league.dao.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class BaseHibernateDao {

  @Autowired
  @Qualifier("sessionFactory")
  protected SessionFactory sessionFactory;
   
}
