package br.com.judev.eventotecbackend.service;

import br.com.judev.eventotecbackend.domain.event.Event;
import br.com.judev.eventotecbackend.domain.event.EventRequestDto;
import br.com.judev.eventotecbackend.repositories.EventRepository;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {


    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

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

        return newEvent;
    }

    private String uploadImg(MultipartFile multipartFile){
        String filename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try {
            File file = this.convertMultipartToFile(multipartFile);
            s3Client.putObject(bucketName ,filename,file);
            file.delete();
            return s3Client.getUrl(bucketName, filename).toString();
        }catch (Exception e){
            System.out.println("erro ao subir arquivo");
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
        // Retorna o arquivo convertido
        return convFile;
    }

}
