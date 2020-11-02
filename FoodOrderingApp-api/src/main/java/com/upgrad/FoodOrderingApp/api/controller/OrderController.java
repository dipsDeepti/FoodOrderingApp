package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    CustomerService customerService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CustomerOrderResponse> getAllOrders(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException{

        String accessToken = authorization.split("Bearer ")[1];

        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        List<OrderEntity> queryResponse = orderService.getOrdersByCustomers(customerEntity.getUuid());
        CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();


        for(OrderEntity singleEntity : queryResponse){

            OrderList orderList = new OrderList();
            orderList.setId(UUID.fromString(singleEntity.getUuid()));
            orderList.setBill(BigDecimal.valueOf(singleEntity.getBill()));

            //Adding Address to the Order List
            AddressEntity addressEntity = singleEntity.getAddress();
            OrderListAddressState orderListAddressState = new OrderListAddressState();
            StateEntity stateEntity = singleEntity.getAddress().getState();
            orderListAddressState.setId(UUID.fromString(stateEntity.getUuid()));
            OrderListAddress orderListAddress = new OrderListAddress();
            orderListAddress.setCity(addressEntity.getCity());
            orderListAddress.setFlatBuildingName(addressEntity.getFlatBuilNo());
            orderListAddress.setId(UUID.fromString(addressEntity.getUuid()));
            orderListAddress.setPincode(addressEntity.getPincode());
            orderListAddress.setState(orderListAddressState);
            orderList.setAddress(orderListAddress);

            // Adding coupon to the orderList

            OrderListCoupon orderListCoupon = new OrderListCoupon();
            CouponEntity couponEntity = singleEntity.getCoupon();
            orderListCoupon.setCouponName(couponEntity.getCouponName());
            orderListCoupon.setId(UUID.fromString(couponEntity.getUuid()));
            orderListCoupon.setPercent(couponEntity.getPercent());

            OrderListCustomer orderListCustomer = new OrderListCustomer();
            orderListCustomer.setId(UUID.fromString(customerEntity.getUuid()));
            orderListCustomer.setContactNumber(customerEntity.getContactNumber());
            orderList.setCustomer(orderListCustomer);




            customerOrderResponse.addOrdersItem(orderList);
        }

        return new ResponseEntity<CustomerOrderResponse>(customerOrderResponse, HttpStatus.OK);

    }

}
