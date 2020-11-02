package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    CouponDao couponDao;

    @Autowired
    CustomerAuthDao customerAuthDao;

    @Autowired
    OrderDao orderDao;

    public CouponEntity getCouponByCouponName(String couponName) throws AuthorizationFailedException, CouponNotFoundException {

        CouponEntity couponEntity = new CouponEntity();
        if (!couponName.isEmpty()) {
            couponEntity = couponDao.getCouponDetails(couponName);
            if (couponEntity == null) {
                throw new AuthorizationFailedException("CPF-001", "No coupon by this name");
            }
        } else {
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
        }

        return couponEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrderEntity> getOrdersByCustomers(String customerId){
        List<OrderEntity> listOfOrders = orderDao.getAllPastOrders(customerId);
        return listOfOrders;
    }
}

