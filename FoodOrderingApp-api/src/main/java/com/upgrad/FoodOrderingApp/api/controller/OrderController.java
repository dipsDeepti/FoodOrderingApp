package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    CustomerService customerService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/coupon/{coupon_name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCoupon(@PathVariable("coupon_name") final String couponName,
                                                           @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, CouponNotFoundException {
        //Access the accessToken from the request Header
        String accessToken = authorization.split("Bearer ")[1];

        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        CouponEntity couponDetailsEntity = orderService.getCouponByCouponName(couponName);
        CouponDetailsResponse couponDetails = couponDetails = new CouponDetailsResponse()
                .id(UUID.fromString(couponDetailsEntity.getUuid()))
                .couponName(couponDetailsEntity.getCouponName())
                .percent(couponDetailsEntity.getPercent());

        return new ResponseEntity<CouponDetailsResponse>(couponDetails, HttpStatus.OK);
    }

}
