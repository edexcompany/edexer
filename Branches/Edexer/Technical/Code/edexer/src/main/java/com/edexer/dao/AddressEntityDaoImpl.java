package com.edexer.dao;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.ActAs;
import com.edexer.model.Address;

@Repository("addressEntityDao")
public class AddressEntityDaoImpl extends GenericEntityDaoImpl<Address> {

}
