package br.com.judev.eventotecbackend.repositories;

import br.com.judev.eventotecbackend.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository <Event , UUID>{
}
