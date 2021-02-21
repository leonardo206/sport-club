package com.mazimao.sportclub.service.impl;

import com.mazimao.sportclub.domain.Club;
import com.mazimao.sportclub.repository.ClubRepository;
import com.mazimao.sportclub.service.ClubService;
import com.mazimao.sportclub.service.dto.ClubDTO;
import com.mazimao.sportclub.service.mapper.ClubMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Club}.
 */
@Service
@Transactional
public class ClubServiceImpl implements ClubService {
    private final Logger log = LoggerFactory.getLogger(ClubServiceImpl.class);

    private final ClubRepository clubRepository;

    private final ClubMapper clubMapper;

    public ClubServiceImpl(ClubRepository clubRepository, ClubMapper clubMapper) {
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
    }

    @Override
    public ClubDTO save(ClubDTO clubDTO) {
        log.debug("Request to save Club : {}", clubDTO);
        Club club = clubMapper.toEntity(clubDTO);
        club = clubRepository.save(club);
        return clubMapper.toDto(club);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClubDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Clubs");
        return clubRepository.findAll(pageable).map(clubMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClubDTO> findOne(Long id) {
        log.debug("Request to get Club : {}", id);
        return clubRepository.findById(id).map(clubMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Club : {}", id);
        clubRepository.deleteById(id);
    }
}
