package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "coupon",uniqueConstraints = {@UniqueConstraint(columnNames = {"uuid"})})
@NamedQueries(
        {
                @NamedQuery(name = "couponByName", query = "SELECT cpn from CouponEntity cpn where cpn.couponName = :cpn_name")
        }
)
public class CouponEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "coupon_name")
    @Size(max = 255)
    @NotNull
    private String couponName;

    @Column(name = "percent")
    private Integer percent;

    public CouponEntity(){

    }

    public CouponEntity(String couponId, String myCoupon, int percent) {
        this.uuid = couponId;
        this.couponName = myCoupon;
        this.percent = percent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

}
