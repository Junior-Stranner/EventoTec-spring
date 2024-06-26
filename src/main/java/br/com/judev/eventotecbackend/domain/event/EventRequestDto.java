package br.com.judev.eventotecbackend.domain.event;

import org.springframework.web.multipart.MultipartFile;

public record EventRequestDto(String title, String description,
                              Long date, String city, String state,
                              Boolean remote, String eventUrl,
                              MultipartFile image) {
}