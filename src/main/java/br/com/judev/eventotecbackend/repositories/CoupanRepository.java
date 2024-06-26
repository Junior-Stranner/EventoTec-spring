package br.com.judev.eventotecbackend.repositories;

import br.com.judev.eventotecbackend.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CoupanRepository extends JpaRepository<Coupon, UUID> {
}
