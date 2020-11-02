package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CouponDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CouponEntity getCouponDetails(final String couponName) {
        try {
            CouponEntity couponDetails = entityManager.createNamedQuery("couponByName", CouponEntity.class).setParameter("cpn_name", couponName).getSingleResult();
            return couponDetails;
        } catch (NoResultException nre) {
            return null;
        }

    }

}
