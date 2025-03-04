package com.feeeeel.couponcore.repository.mysql;

import com.feeeeel.couponcore.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {
}
