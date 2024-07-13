package br.com.judev.eventotecbackend.service;

import br.com.judev.eventotecbackend.domain.event.Event;
import br.com.judev.eventotecbackend.domain.event.EventRequestDto;
import br.com.judev.eventotecbackend.domain.event.EventResponseDto;
import br.com.judev.eventotecbackend.event.EventAddressProjection;
import br.com.judev.eventotecbackend.repositories.EventRepository;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    public List<EventResponseDto> getUpcomingEvents(int page, int size) {
        // Cria um objeto Pageable para definir a página e o tamanho da página
        Pageable pageable = PageRequest.of(page, size);

        // Busca eventos futuros no repositório usando a data atual e paginação
        Page<EventAddressProjection> eventsPage = eventRepository.findUpcomingEvents(new Date(), pageable);

        // Mapeia os eventos encontrados para o DTO EventResponseDto e retorna como uma lista
        return eventsPage.map(event -> new EventResponseDto(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getCity() != null ? event.getCity() : "", // Verifica se a cidade é nula
                        event.getUf() != null ? event.getUf() : "", // Verifica se a UF é nula
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
                )
                .stream().toList();
    }

    public List<EventResponseDto> getFilteredEvents(int page, int size, String city, String uf, Date startDate, Date endDate) {
        // Define valores padrão para city, uf, startDate e endDate se forem nulos
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        startDate = (startDate != null) ? startDate : new Date(0); // Data inicial padrão é 01/01/1970
        endDate = (endDate != null) ? endDate : new Date(); // Data final padrão é a data atual

        // Cria um objeto Pageable para definir a página e o tamanho da página
        Pageable pageable = PageRequest.of(page, size);

        // Busca eventos filtrados no repositório usando os filtros fornecidos e paginação
        Page<EventAddressProjection> eventsPage = eventRepository.findFilteredEvents(city, uf, startDate, endDate, pageable);

        // Mapeia os eventos encontrados para o DTO EventResponseDto e retorna como uma lista
        return eventsPage.map(event -> new EventResponseDto(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getCity() != null ? event.getCity() : "", // Verifica se a cidade é nula
                        event.getUf() != null ? event.getUf() : "", // Verifica se a UF é nula
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
                )
                .stream().toList();
    }

    public List<EventResponseDto> searchEvents(String title) {
        // Define valor padrão para title se for nulo
        title = (title != null) ? title : "";

        // Busca eventos no repositório com título correspondente
        List<EventAddressProjection> eventsList = eventRepository.findEventsByTitle(title);

        // Mapeia os eventos encontrados para o DTO EventResponseDto e retorna como uma lista
        return eventsList.stream().map(event -> new EventResponseDto(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getCity() != null ? event.getCity() : "", // Verifica se a cidade é nula
                        event.getUf() != null ? event.getUf() : "", // Verifica se a UF é nula
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
                )
                .toList();
    }

}
