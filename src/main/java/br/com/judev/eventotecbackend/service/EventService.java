package br.com.judev.eventotecbackend.service;

import br.com.judev.eventotecbackend.domain.event.Event;
import br.com.judev.eventotecbackend.domain.event.EventRequestDto;
import br.com.judev.eventotecbackend.domain.event.EventResponseDto;
import br.com.judev.eventotecbackend.repositories.EventRepository;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {


    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private AddressService addressService;

    @Autowired
    private EventRepository eventRepository;


    public Event createEvent(EventRequestDto data) {
        String imgUrl = null;

        if (data.image() != null) {
            imgUrl = this.uploadImg(data.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());

        eventRepository.save(newEvent);

        if(!data.remote()) {
            this.addressService.createAddress(data, newEvent);
        }

        return newEvent;
    }

    public List<EventResponseDto> getUpcomingEvents(int page , int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Event> eventsPages = eventRepository.findUpcomingEvents(new Date(), pageable);
        return eventsPages.map(event -> new EventResponseDto(event.getId(),event.getTitle(), event.getDescription(), event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl())).stream().toList();
    }

    private String uploadImg(MultipartFile multipartFile) {
        // Gera um nome único para o arquivo usando UUID e o nome original do arquivo
        String filename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try {
            // Converte o MultipartFile em um arquivo File temporário
            File file = this.convertMultipartToFile(multipartFile);

            // Faz o upload do arquivo para o Amazon S3
            s3Client.putObject(bucketName, filename, file);

            // Deleta o arquivo temporário depois do upload
            file.delete();

            // Retorna a URL pública do arquivo no Amazon S3
            return s3Client.getUrl(bucketName, filename).toString();
        } catch (Exception e) {
            // Em caso de erro, imprime a mensagem de erro e retorna uma string vazia
            System.out.println("Erro ao subir arquivo para o Amazon S3");
            System.out.println(e.getMessage());
            return "";
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        // Cria um novo arquivo usando o nome original do arquivo multipart
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        // Abre um fluxo de saída para escrever no arquivo
        FileOutputStream fos = new FileOutputStream(convFile);

        // Escreve os bytes do arquivo multipart para o arquivo criado
        fos.write(multipartFile.getBytes());

        // Fecha o fluxo de saída
        fos.close();

        return convFile;
    }

}
