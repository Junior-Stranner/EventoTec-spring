package br.com.judev.eventotecbackend.domain.event;

import java.util.Date;
import java.util.UUID;

/*Projections: São usados para otimizar consultas ao banco de dados, recuperando apenas os campos necessários e evitando o
carregamento de entidades completas. Eles melhoram o desempenho e reduzem a quantidade de dados transferidos do banco de
dados para a aplicação.*/

public interface EventAddressProjection {
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
