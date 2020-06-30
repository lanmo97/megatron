package com.uspacex.megatron.service;

import com.uspacex.megatron.entity.ClientEntity;
import com.uspacex.megatron.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public void saveClientAccessLog(String ip, String city, String userAgent, String info) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setIp(ip);
        clientEntity.setCity(city);
        clientEntity.setUserAgent(userAgent);
        clientEntity.setInfo(info);
        clientRepository.save(clientEntity);
    }
}
