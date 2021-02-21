package com.mazimao.sportclub.service.impl;

import com.mazimao.sportclub.domain.ClubType;
import com.mazimao.sportclub.repository.ClubTypeRepository;
import com.mazimao.sportclub.service.ClubTypeService;
import com.mazimao.sportclub.service.dto.ClubTypeDTO;
import com.mazimao.sportclub.service.mapper.ClubTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ClubType}.
 */
@Service
@Transactional
public class ClubTypeServiceImpl implements ClubTypeService {
    private final Logger log = LoggerFactory.getLogger(ClubTypeServiceImpl.class);

    private final ClubTypeRepository clubTypeRepository;

    private final ClubTypeMapper clubTypeMapper;

    public ClubTypeServiceImpl(ClubTypeRepository clubTypeRepository, ClubTypeMapper clubTypeMapper) {
        this.clubTypeRepository = clubTypeRepository;
        this.clubTypeMapper = clubTypeMapper;
    }

    @Override
    public ClubTypeDTO save(ClubTypeDTO clubTypeDTO) {
        log.debug("Request to save ClubType : {}", clubTypeDTO);
        ClubType clubType = clubTypeMapper.toEntity(clubTypeDTO);
        clubType = clubTypeRepository.save(clubType);
        return clubTypeMapper.toDto(clubType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClubTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClubTypes");
        return clubTypeRepository.findAll(pageable).map(clubTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClubTypeDTO> findOne(Long id) {
        log.debug("Request to get ClubType : {}", id);
        return clubTypeRepository.findById(id).map(clubTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClubType : {}", id);
        clubTypeRepository.deleteById(id);
    }
}
