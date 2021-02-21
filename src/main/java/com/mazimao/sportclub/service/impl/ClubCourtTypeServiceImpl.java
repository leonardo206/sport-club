package com.mazimao.sportclub.service.impl;

import com.mazimao.sportclub.domain.ClubCourtType;
import com.mazimao.sportclub.repository.ClubCourtTypeRepository;
import com.mazimao.sportclub.service.ClubCourtTypeService;
import com.mazimao.sportclub.service.dto.ClubCourtTypeDTO;
import com.mazimao.sportclub.service.mapper.ClubCourtTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ClubCourtType}.
 */
@Service
@Transactional
public class ClubCourtTypeServiceImpl implements ClubCourtTypeService {
    private final Logger log = LoggerFactory.getLogger(ClubCourtTypeServiceImpl.class);

    private final ClubCourtTypeRepository clubCourtTypeRepository;

    private final ClubCourtTypeMapper clubCourtTypeMapper;

    public ClubCourtTypeServiceImpl(ClubCourtTypeRepository clubCourtTypeRepository, ClubCourtTypeMapper clubCourtTypeMapper) {
        this.clubCourtTypeRepository = clubCourtTypeRepository;
        this.clubCourtTypeMapper = clubCourtTypeMapper;
    }

    @Override
    public ClubCourtTypeDTO save(ClubCourtTypeDTO clubCourtTypeDTO) {
        log.debug("Request to save ClubCourtType : {}", clubCourtTypeDTO);
        ClubCourtType clubCourtType = clubCourtTypeMapper.toEntity(clubCourtTypeDTO);
        clubCourtType = clubCourtTypeRepository.save(clubCourtType);
        return clubCourtTypeMapper.toDto(clubCourtType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClubCourtTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClubCourtTypes");
        return clubCourtTypeRepository.findAll(pageable).map(clubCourtTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClubCourtTypeDTO> findOne(Long id) {
        log.debug("Request to get ClubCourtType : {}", id);
        return clubCourtTypeRepository.findById(id).map(clubCourtTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClubCourtType : {}", id);
        clubCourtTypeRepository.deleteById(id);
    }
}
