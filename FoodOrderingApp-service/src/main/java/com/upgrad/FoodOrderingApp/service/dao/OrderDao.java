package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

//This Class is created to access DB with respect to Order entity

@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    //To save Order in the db
    public OrderEntity saveOrder(OrderEntity ordersEntity) {
        entityManager.persist(ordersEntity);
        return ordersEntity;
    }

    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity) {
        entityManager.persist(orderItemEntity);
        return orderItemEntity;
    }

    public List<OrderEntity> getAllPastOrders (String customerId){
        try{
            List<OrderEntity> paymentMethods = entityManager.createNamedQuery("getOrdersByCustomers",OrderEntity.class).setParameter("customer",customerId).getResultList();
            return paymentMethods;
        }catch (NoResultException nre){
            return null;
        }
    }

    //To get list of OrdersEntity by the restaurant if no result then null is returned
    public List<OrderEntity> getOrdersByRestaurant(RestaurantEntity restaurantEntity) {
        try {
            List<OrderEntity> ordersEntities = new ArrayList<>();
            //= entityManager.createNamedQuery("getOrdersByRestaurant", OrderEntity.class).setParameter("restaurant",restaurantEntity).getResultList();
            return ordersEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }

    //To get all the order corresponding to the address
    public List<OrderEntity> getOrdersByAddress(AddressEntity addressEntity) {
        try {
            List<OrderEntity> ordersEntities = entityManager.createNamedQuery("getOrdersByAddress", OrderEntity.class).setParameter("address", addressEntity).getResultList();
            return ordersEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CouponEntity getCouponByName(String couponName) {
        try {
            return entityManager.createNamedQuery("couponByName", CouponEntity.class).setParameter("couponName", couponName)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CouponEntity getCouponByCouponId(String couponId) {
        try {
            return entityManager.createNamedQuery("couponByUuid", CouponEntity.class).setParameter("couponName", couponId)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<OrderEntity> getCustomerOrders(CustomerEntity customerEntity) {
        try {
            return entityManager.createNamedQuery("ordersByCustomer", OrderEntity.class).setParameter("customer", customerEntity)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
