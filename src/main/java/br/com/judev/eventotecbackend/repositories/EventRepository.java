package br.com.judev.eventotecbackend.repositories;

import br.com.judev.eventotecbackend.domain.event.Event;
import br.com.judev.eventotecbackend.event.EventAddressProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository <Event , UUID>{

/*Este método define uma consulta personalizada que busca eventos futuros
(com data maior ou igual à data atual) e inclui informações do evento (ID, título, descrição, data, URL da imagem,
URL do evento, indicação de evento remoto) bem como informações de endereço associadas (cidade e estado).
 A consulta suporta paginação para dividir os resultados em páginas.*/
    @Query("SELECT e.id AS id, e.title AS title, e.description AS description," +
            " e.date AS date, e.imgUrl AS imgUrl, e.eventUrl AS eventUrl, e.remote AS remote," +
            " a.city AS city, a.uf AS uf " +
            "FROM Event e LEFT JOIN Address a ON e.id = a.event.id " +
            "WHERE e.date >= :currentDate")
    public Page<EventAddressProjection> findUpcomingEvents(@Param("currentDate") Date currentDate, Pageable pageable);


    /*Este método define uma consulta personalizada que busca eventos filtrados por cidade, estado e intervalo de datas.
     A cidade e o estado são opcionais, permitindo a busca parcial
     (ou sem filtro se os parâmetros forem vazios). A consulta também suporta paginação para dividir os resultados em páginas.*/
    @Query("SELECT e.id AS id, e.title AS title, e.description AS description, e.date AS date, e.imgUrl AS imgUrl," +
            " e.eventUrl AS eventUrl, e.remote AS remote, a.city AS city, a.uf AS uf " +
            "FROM Event e JOIN Address a ON e.id = a.event.id " +
            "WHERE (:city = '' OR a.city LIKE %:city%) " +
            "AND (:uf = '' OR a.uf LIKE %:uf%) " +
            "AND (e.date >= :startDate AND e.date <= :endDate)")
    Page<EventAddressProjection> findFilteredEvents(@Param("city") String city,
                                                    @Param("uf") String uf,
                                                    @Param("startDate") Date startDate,
                                                    @Param("endDate") Date endDate,
                                                    Pageable pageable);

    /*Este método define uma consulta personalizada que busca eventos filtrados pelo título.
     Se o parâmetro title estiver vazio, todos os eventos serão retornados.
      A consulta usa uma junção interna para associar cada evento ao seu endereço correspondente.
       A lista resultante contém projeções dos eventos com seus respectivos dados de endereço.*/
    @Query("SELECT e.id AS id, e.title AS title, e.description AS description, e.date AS date, e.imgUrl AS imgUrl, e.eventUrl AS eventUrl, e.remote AS remote, a.city AS city, a.uf AS uf " +
            "FROM Event e JOIN Address a ON e.id = a.event.id " +
            "WHERE (:title = '' OR e.title LIKE %:title%)")
    List<EventAddressProjection> findEventsByTitle(@Param("title") String title);


}
