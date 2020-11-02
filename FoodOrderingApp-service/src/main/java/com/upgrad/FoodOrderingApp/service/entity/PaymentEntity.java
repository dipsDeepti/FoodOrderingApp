package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "payment",uniqueConstraints = {@UniqueConstraint(columnNames = {"uuid","id"})})
@NamedQueries(
        {
                @NamedQuery(name = "getPaymentByUUID",query = "SELECT p FROM PaymentEntity p WHERE p.uuid = :uuid"),
                @NamedQuery(name = "paymentById", query = "select p from PaymentEntity p where p.id=:id"),
                @NamedQuery(name = "getAllPaymentMethods", query = "SELECT c from PaymentEntity c")
        }
)
public class PaymentEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "payment_name")
    @Size(max = 255)
    @NotNull
    private String paymentName;

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentname) {
        this.paymentName = paymentname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
