package br.com.judev.eventotecbackend.repositories;

import br.com.judev.eventotecbackend.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    List<Coupon> findByEventIdAndValidAfter(UUID eventId, Date currentDate);
}
