package com.mazimao.sportclub.service;

import com.mazimao.sportclub.domain.*; // for static metamodels
import com.mazimao.sportclub.domain.ClubCourt;
import com.mazimao.sportclub.repository.ClubCourtRepository;
import com.mazimao.sportclub.service.dto.ClubCourtCriteria;
import com.mazimao.sportclub.service.dto.ClubCourtDTO;
import com.mazimao.sportclub.service.mapper.ClubCourtMapper;
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
 * Service for executing complex queries for {@link ClubCourt} entities in the database.
 * The main input is a {@link ClubCourtCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClubCourtDTO} or a {@link Page} of {@link ClubCourtDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClubCourtQueryService extends QueryService<ClubCourt> {
    private final Logger log = LoggerFactory.getLogger(ClubCourtQueryService.class);

    private final ClubCourtRepository clubCourtRepository;

    private final ClubCourtMapper clubCourtMapper;

    public ClubCourtQueryService(ClubCourtRepository clubCourtRepository, ClubCourtMapper clubCourtMapper) {
        this.clubCourtRepository = clubCourtRepository;
        this.clubCourtMapper = clubCourtMapper;
    }

    /**
     * Return a {@link List} of {@link ClubCourtDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClubCourtDTO> findByCriteria(ClubCourtCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClubCourt> specification = createSpecification(criteria);
        return clubCourtMapper.toDto(clubCourtRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClubCourtDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClubCourtDTO> findByCriteria(ClubCourtCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClubCourt> specification = createSpecification(criteria);
        return clubCourtRepository.findAll(specification, page).map(clubCourtMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClubCourtCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClubCourt> specification = createSpecification(criteria);
        return clubCourtRepository.count(specification);
    }

    /**
     * Function to convert {@link ClubCourtCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClubCourt> createSpecification(ClubCourtCriteria criteria) {
        Specification<ClubCourt> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClubCourt_.id));
            }
            if (criteria.getCourtCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCourtCode(), ClubCourt_.courtCode));
            }
            if (criteria.getCourtName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCourtName(), ClubCourt_.courtName));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ClubCourt_.status));
            }
            if (criteria.getClubCourtTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClubCourtTypeId(),
                            root -> root.join(ClubCourt_.clubCourtType, JoinType.LEFT).get(ClubCourtType_.id)
                        )
                    );
            }
            if (criteria.getBookingsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBookingsId(), root -> root.join(ClubCourt_.bookings, JoinType.LEFT).get(Booking_.id))
                    );
            }
            if (criteria.getClubId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClubId(), root -> root.join(ClubCourt_.club, JoinType.LEFT).get(Club_.id))
                    );
            }
        }
        return specification;
    }
}
