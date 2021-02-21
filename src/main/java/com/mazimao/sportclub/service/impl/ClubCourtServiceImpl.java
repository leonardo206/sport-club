package com.mazimao.sportclub.service.impl;

import com.mazimao.sportclub.domain.ClubCourt;
import com.mazimao.sportclub.repository.ClubCourtRepository;
import com.mazimao.sportclub.service.ClubCourtService;
import com.mazimao.sportclub.service.dto.ClubCourtDTO;
import com.mazimao.sportclub.service.mapper.ClubCourtMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ClubCourt}.
 */
@Service
@Transactional
public class ClubCourtServiceImpl implements ClubCourtService {
    private final Logger log = LoggerFactory.getLogger(ClubCourtServiceImpl.class);

    private final ClubCourtRepository clubCourtRepository;

    private final ClubCourtMapper clubCourtMapper;

    public ClubCourtServiceImpl(ClubCourtRepository clubCourtRepository, ClubCourtMapper clubCourtMapper) {
        this.clubCourtRepository = clubCourtRepository;
        this.clubCourtMapper = clubCourtMapper;
    }

    @Override
    public ClubCourtDTO save(ClubCourtDTO clubCourtDTO) {
        log.debug("Request to save ClubCourt : {}", clubCourtDTO);
        ClubCourt clubCourt = clubCourtMapper.toEntity(clubCourtDTO);
        clubCourt = clubCourtRepository.save(clubCourt);
        return clubCourtMapper.toDto(clubCourt);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClubCourtDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClubCourts");
        return clubCourtRepository.findAll(pageable).map(clubCourtMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClubCourtDTO> findOne(Long id) {
        log.debug("Request to get ClubCourt : {}", id);
        return clubCourtRepository.findById(id).map(clubCourtMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClubCourt : {}", id);
        clubCourtRepository.deleteById(id);
    }
}
