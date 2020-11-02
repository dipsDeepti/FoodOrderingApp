package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

public class OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    //To get all past orders of a customer
    public List<OrderEntity> getAllPastOrders (String customerId){
        try{
            List<OrderEntity> paymentMethods = entityManager.createNamedQuery("getOrdersByCustomers",OrderEntity.class).setParameter("customer",customerId).getResultList();
            return paymentMethods;
        }catch (NoResultException nre){
            return null;
        }
    }
}
