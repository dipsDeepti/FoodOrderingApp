package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class OrderService {

    @Autowired
    CouponDao couponDao;

    @Autowired
    CustomerAuthDao customerAuthDao;

    public CouponEntity getCouponByCouponName(String couponName) throws AuthorizationFailedException, CouponNotFoundException {

        CouponEntity couponEntity = new CouponEntity();
        if(!couponName.isEmpty()){
            couponEntity = couponDao.getCouponDetails(couponName);
            if(couponEntity == null){
                throw new AuthorizationFailedException("CPF-001", "No coupon by this name");
            }
        } else{
           throw new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
        }

        return couponEntity;
    }
}
