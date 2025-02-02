package com.feeeeel.couponcore.model;

import com.feeeeel.couponcore.exception.CouponIssueException;
import com.feeeeel.couponcore.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("발급 수량이 남아있다면 true 반환")
    void availableIssueQuantity_1() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .build();

        boolean result = coupon.availableIssuedQuantity();

        assertTrue(result);
    }

    @Test
    @DisplayName("발급 수량이 없다면 false 반환")
    void availableIssueQuantity_2() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(100)
                .build();

        boolean result = coupon.availableIssuedQuantity();

        assertFalse(result);
    }

    @Test
    @DisplayName("최대 발급 수량이 없다면 true 반환")
    void availableIssueQuantity_3() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(null)
                .issuedQuantity(100)
                .build();

        boolean result = coupon.availableIssuedQuantity();

        assertTrue(result);
    }

    @Test
    @DisplayName("발급 기간이 시작되지 않았다면 false 반환")
    void availableIssueDate_1() {
        Coupon coupon = Coupon.builder()
                .dateIssueStart(LocalDateTime.now().plusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();

        boolean result = coupon.availableIssuedDate();

        assertFalse(result);
    }

    @Test
    @DisplayName("발급 기간에 해당된다면 true 반환")
    void availableIssueDate_2() {
        Coupon coupon = Coupon.builder()
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();

        boolean result = coupon.availableIssuedDate();

        assertTrue(result);
    }

    @Test
    @DisplayName("발급 기간이 종료되었다면 false 반환")
    void availableIssueDate_3() {
        Coupon coupon = Coupon.builder()
                .dateIssueStart(LocalDateTime.now().minusDays(2))
                .dateIssueEnd(LocalDateTime.now().minusDays(1))
                .build();

        boolean result = coupon.availableIssuedDate();

        assertFalse(result);
    }

    @Test
    @DisplayName("발급 수량과 발급 기간이 유효하다면 쿠폰을 발급")
    void issue_1() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .dateIssueStart(LocalDateTime.now().minusDays(2))
                .dateIssueEnd(LocalDateTime.now().plusDays(1))
                .build();

        coupon.issue();

        assertEquals(100, coupon.getIssuedQuantity());
    }

    @Test
    @DisplayName("발급 수량을 초과하면 예외를 반환")
    void issue_2() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(100)
                .dateIssueStart(LocalDateTime.now().minusDays(2))
                .dateIssueEnd(LocalDateTime.now().plusDays(1))
                .build();

        CouponIssueException exception = assertThrowsExactly(CouponIssueException.class, coupon::issue);
        assertEquals(ErrorCode.INVALID_COUPON_ISSUE_QUANTITY, exception.getErrorCode());
    }

    @Test
    @DisplayName("발급 기간이 아니면 예외를 반환")
    void issue_3() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .dateIssueStart(LocalDateTime.now().minusDays(2))
                .dateIssueEnd(LocalDateTime.now().minusDays(1))
                .build();

        CouponIssueException exception = assertThrowsExactly(CouponIssueException.class, coupon::issue);
        assertEquals(ErrorCode.INVALID_COUPON_ISSUE_DATE, exception.getErrorCode());
    }
}