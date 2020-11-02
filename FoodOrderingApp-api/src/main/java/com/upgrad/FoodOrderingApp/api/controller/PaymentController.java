package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("")
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/payment",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PaymentListResponse> GetAllPaymentMethods()
    {
        List<PaymentEntity> response = paymentService.getAllPaymentMethods();
        PaymentListResponse paymentListResponse = new PaymentListResponse();
        PaymentResponse paymentResponse = new PaymentResponse();
        List<PaymentResponse> paymentResponseList = new ArrayList<>();
        for(PaymentEntity paymentEntity:response)
        {
            paymentResponse = new PaymentResponse();
            paymentResponse.setId(UUID.fromString(paymentEntity.getUuid()));
            paymentResponse.setPaymentName(paymentEntity.getPaymentName());
            paymentResponseList.add(paymentResponse);
        }
        paymentListResponse.setPaymentMethods(paymentResponseList);
        return new ResponseEntity<PaymentListResponse>(paymentListResponse, HttpStatus.OK);
    }
}
