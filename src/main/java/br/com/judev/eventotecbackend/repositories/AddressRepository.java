package br.com.judev.eventotecbackend.repositories;

import br.com.judev.eventotecbackend.domain.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address , UUID> {
     Optional<Address> findByEventId(UUID eventId);


}
