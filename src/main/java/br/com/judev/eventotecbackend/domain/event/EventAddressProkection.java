package br.com.judev.eventotecbackend.domain.event;

import java.util.Date;
import java.util.UUID;

/*A interface EventAddressProkection define um contrato de projeção para um evento,
 especificando um conjunto de métodos que devem ser implementados para acessar os detalhes do evento.
 Em particular, essa interface parece ser usada em um contexto de Spring Data JPA para projetar dados
 de uma entidade de evento em uma forma simplificada ou específica. Isso pode ser útil para retornar
 apenas os dados necessários em uma consulta, em vez de toda a entidade, melhorando a performance e a eficiência.*/

public interface EventAddressProkection {
    UUID getId();
    String getTitle();
    String getDescription();
    Date getDate();
    String getImgUrl();
    String getEventUrl();
    Boolean getRemote();
    String getCity();
    String getUf();
}
