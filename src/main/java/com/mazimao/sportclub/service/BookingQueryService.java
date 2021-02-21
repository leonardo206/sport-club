package com.mazimao.sportclub.service;

import com.mazimao.sportclub.domain.*; // for static metamodels
import com.mazimao.sportclub.domain.Booking;
import com.mazimao.sportclub.repository.BookingRepository;
import com.mazimao.sportclub.service.dto.BookingCriteria;
import com.mazimao.sportclub.service.dto.BookingDTO;
import com.mazimao.sportclub.service.mapper.BookingMapper;
import io.github.jhipster.service.QueryService;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link Booking} entities in the database.
 * The main input is a {@link BookingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BookingDTO} or a {@link Page} of {@link BookingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BookingQueryService extends QueryService<Booking> {
    private final Logger log = LoggerFactory.getLogger(BookingQueryService.class);

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    public BookingQueryService(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    /**
     * Return a {@link List} of {@link BookingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BookingDTO> findByCriteria(BookingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingMapper.toDto(bookingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BookingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookingDTO> findByCriteria(BookingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingRepository.findAll(specification, page).map(bookingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BookingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingRepository.count(specification);
    }

    /**
     * Function to convert {@link BookingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Booking> createSpecification(BookingCriteria criteria) {
        Specification<Booking> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Booking_.id));
            }
            if (criteria.getBookingCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBookingCode(), Booking_.bookingCode));
            }
            if (criteria.getBookingTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBookingTime(), Booking_.bookingTime));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Booking_.status));
            }
            if (criteria.getClientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClientId(), root -> root.join(Booking_.client, JoinType.LEFT).get(Client_.id))
                    );
            }
            if (criteria.getClubCourtId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClubCourtId(),
                            root -> root.join(Booking_.clubCourt, JoinType.LEFT).get(ClubCourt_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
