package br.com.judev.eventotecbackend.service;

import br.com.judev.eventotecbackend.domain.address.Address;
import br.com.judev.eventotecbackend.domain.event.Event;
import br.com.judev.eventotecbackend.domain.event.EventRequestDto;
import br.com.judev.eventotecbackend.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address createAddress(EventRequestDto data, Event event) {
        Address address = new Address();
        address.setCity(data.city());
        address.setUf(data.state());
        address.setEvent(event);

        return addressRepository.save(address);
    }

}
