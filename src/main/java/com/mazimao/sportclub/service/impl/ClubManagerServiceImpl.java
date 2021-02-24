package com.mazimao.sportclub.service.impl;

import com.mazimao.sportclub.domain.ClubManager;
import com.mazimao.sportclub.repository.ClubManagerRepository;
import com.mazimao.sportclub.service.ClubManagerService;
import com.mazimao.sportclub.service.dto.ClubManagerDTO;
import com.mazimao.sportclub.service.mapper.ClubManagerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ClubManager}.
 */
@Service
@Transactional
public class ClubManagerServiceImpl implements ClubManagerService {
    private final Logger log = LoggerFactory.getLogger(ClubManagerServiceImpl.class);

    private final ClubManagerRepository clubManagerRepository;

    private final ClubManagerMapper clubManagerMapper;

    public ClubManagerServiceImpl(ClubManagerRepository clubManagerRepository, ClubManagerMapper clubManagerMapper) {
        this.clubManagerRepository = clubManagerRepository;
        this.clubManagerMapper = clubManagerMapper;
    }

    @Override
    public ClubManagerDTO save(ClubManagerDTO clubManagerDTO) {
        log.debug("Request to save ClubManager : {}", clubManagerDTO);
        ClubManager clubManager = clubManagerMapper.toEntity(clubManagerDTO);
        clubManager = clubManagerRepository.save(clubManager);
        return clubManagerMapper.toDto(clubManager);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClubManagerDTO> findByUserId(String id) {
        log.debug("Request to get all ClubManagers");
        return clubManagerRepository.findByUserId(id).map(clubManagerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClubManagerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClubManagers");
        return clubManagerRepository.findAll(pageable).map(clubManagerMapper::toDto);
    }

    public Page<ClubManagerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return clubManagerRepository.findAllWithEagerRelationships(pageable).map(clubManagerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClubManagerDTO> findOne(Long id) {
        log.debug("Request to get ClubManager : {}", id);
        return clubManagerRepository.findOneWithEagerRelationships(id).map(clubManagerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClubManager : {}", id);
        clubManagerRepository.deleteById(id);
    }
}
