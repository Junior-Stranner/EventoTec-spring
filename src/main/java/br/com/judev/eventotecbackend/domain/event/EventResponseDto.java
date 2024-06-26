package br.com.judev.eventotecbackend.domain.event;

import java.util.Date;
import java.util.UUID;

public record EventResponseDto(UUID id, String title, String description,
                               Date date, String city,
                               String state, Boolean remote,
                               String eventUrl, String imgUrl) {
}
