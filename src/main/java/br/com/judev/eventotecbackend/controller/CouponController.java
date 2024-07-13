package br.com.judev.eventotecbackend.controller;


import br.com.judev.eventotecbackend.domain.coupon.Coupon;
import br.com.judev.eventotecbackend.domain.coupon.CouponRequestDto;
import br.com.judev.eventotecbackend.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/coupon")
public class CouponController {


    @Autowired
    private CouponService couponService;

    @PostMapping("/to/event/{eventId}")
    public ResponseEntity<Coupon> addCouponsToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDto data) {
        Coupon coupons = couponService.addCouponToEvent(eventId,data);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/from/event/{eventId}/coupons")
    public ResponseEntity<List<Coupon>> consultCoupons(
            @PathVariable UUID eventId,
            @RequestParam Date currentDate) {
        List<Coupon> coupons = couponService.consultCoupons(eventId, currentDate);
        if (coupons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(coupons);
    }

}
