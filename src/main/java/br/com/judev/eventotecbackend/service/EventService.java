package br.com.judev.eventotecbackend.service;

import br.com.judev.eventotecbackend.domain.event.Event;
import br.com.judev.eventotecbackend.domain.event.EventRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EventService {

    public Event createEvent(EventRequestDto data) {
        String imgUrl = null;

        if (data.image() != null) {
            imgUrl = this.uploadImg(data.image());
        }
    }

    private String uploadImg(MultipartFile multipartFile){

    }

}
