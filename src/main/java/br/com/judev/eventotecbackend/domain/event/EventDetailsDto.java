package br.com.judev.eventotecbackend.domain.event;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record EventDetailsDto(
        UUID id,
        String title,
        String description,
        Date date,
        String city,
        String state,
        String imgUrl,
        String eventUrl,
        List<CouponDto> coupons) {

    public record CouponDto(
            String code,
            Integer discount,
            Date valid) {
    }
}
