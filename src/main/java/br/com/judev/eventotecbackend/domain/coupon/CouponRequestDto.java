package br.com.judev.eventotecbackend.domain.coupon;

public record CouponRequestDto(String code, Integer discount, Long valid) {
}
