package com.mazimao.sportclub.service.impl;

import com.mazimao.sportclub.domain.Client;
import com.mazimao.sportclub.repository.ClientRepository;
import com.mazimao.sportclub.service.ClientService;
import com.mazimao.sportclub.service.dto.ClientDTO;
import com.mazimao.sportclub.service.mapper.ClientMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Client}.
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public ClientDTO save(ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO);
        Client client = clientMapper.toEntity(clientDTO);
        client = clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Clients");
        return clientRepository.findAll(pageable).map(clientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientDTO> findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        return clientRepository.findById(id).map(clientMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
    }
}
