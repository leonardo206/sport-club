package com.mazimao.sportclub.service.impl;

import com.mazimao.sportclub.domain.UserDetails;
import com.mazimao.sportclub.repository.UserDetailsRepository;
import com.mazimao.sportclub.service.UserDetailsService;
import com.mazimao.sportclub.service.dto.UserDetailsDTO;
import com.mazimao.sportclub.service.mapper.UserDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserDetails}.
 */
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserDetailsRepository userDetailsRepository;

    private final UserDetailsMapper userDetailsMapper;

    public UserDetailsServiceImpl(UserDetailsRepository userDetailsRepository, UserDetailsMapper userDetailsMapper) {
        this.userDetailsRepository = userDetailsRepository;
        this.userDetailsMapper = userDetailsMapper;
    }

    @Override
    public UserDetailsDTO save(UserDetailsDTO userDetailsDTO) {
        log.debug("Request to save UserDetails : {}", userDetailsDTO);
        UserDetails userDetails = userDetailsMapper.toEntity(userDetailsDTO);
        userDetails = userDetailsRepository.save(userDetails);
        return userDetailsMapper.toDto(userDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserDetails");
        return userDetailsRepository.findAll(pageable).map(userDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDetailsDTO> findOne(Long id) {
        log.debug("Request to get UserDetails : {}", id);
        return userDetailsRepository.findById(id).map(userDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserDetails : {}", id);
        userDetailsRepository.deleteById(id);
    }
}
