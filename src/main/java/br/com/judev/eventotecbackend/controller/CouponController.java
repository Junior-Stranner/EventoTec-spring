package br.com.judev.eventotecbackend.controller;


import br.com.judev.eventotecbackend.domain.coupon.Coupon;
import br.com.judev.eventotecbackend.domain.coupon.CouponRequestDto;
import br.com.judev.eventotecbackend.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/coupon")
public class CouponController {


    @Autowired
    private CouponService couponService;

    @PostMapping("/event/{eventId")
    public ResponseEntity<Coupon> addCouponsToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDto data) {
        Coupon coupons = couponService.addCouponToEvent(eventId,data);
        return ResponseEntity.ok(coupons);
    }
}
