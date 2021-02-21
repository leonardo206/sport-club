package com.mazimao.sportclub.service.impl;

import com.mazimao.sportclub.domain.Booking;
import com.mazimao.sportclub.repository.BookingRepository;
import com.mazimao.sportclub.service.BookingService;
import com.mazimao.sportclub.service.dto.BookingDTO;
import com.mazimao.sportclub.service.mapper.BookingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Booking}.
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    private final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingDTO save(BookingDTO bookingDTO) {
        log.debug("Request to save Booking : {}", bookingDTO);
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bookings");
        return bookingRepository.findAll(pageable).map(bookingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookingDTO> findOne(Long id) {
        log.debug("Request to get Booking : {}", id);
        return bookingRepository.findById(id).map(bookingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Booking : {}", id);
        bookingRepository.deleteById(id);
    }
}
