package com.mazimao.sportclub.service;

import com.mazimao.sportclub.domain.*; // for static metamodels
import com.mazimao.sportclub.domain.ClubCourtType;
import com.mazimao.sportclub.repository.ClubCourtTypeRepository;
import com.mazimao.sportclub.service.dto.ClubCourtTypeCriteria;
import com.mazimao.sportclub.service.dto.ClubCourtTypeDTO;
import com.mazimao.sportclub.service.mapper.ClubCourtTypeMapper;
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
 * Service for executing complex queries for {@link ClubCourtType} entities in the database.
 * The main input is a {@link ClubCourtTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClubCourtTypeDTO} or a {@link Page} of {@link ClubCourtTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClubCourtTypeQueryService extends QueryService<ClubCourtType> {
    private final Logger log = LoggerFactory.getLogger(ClubCourtTypeQueryService.class);

    private final ClubCourtTypeRepository clubCourtTypeRepository;

    private final ClubCourtTypeMapper clubCourtTypeMapper;

    public ClubCourtTypeQueryService(ClubCourtTypeRepository clubCourtTypeRepository, ClubCourtTypeMapper clubCourtTypeMapper) {
        this.clubCourtTypeRepository = clubCourtTypeRepository;
        this.clubCourtTypeMapper = clubCourtTypeMapper;
    }

    /**
     * Return a {@link List} of {@link ClubCourtTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClubCourtTypeDTO> findByCriteria(ClubCourtTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClubCourtType> specification = createSpecification(criteria);
        return clubCourtTypeMapper.toDto(clubCourtTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClubCourtTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClubCourtTypeDTO> findByCriteria(ClubCourtTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClubCourtType> specification = createSpecification(criteria);
        return clubCourtTypeRepository.findAll(specification, page).map(clubCourtTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClubCourtTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClubCourtType> specification = createSpecification(criteria);
        return clubCourtTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ClubCourtTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClubCourtType> createSpecification(ClubCourtTypeCriteria criteria) {
        Specification<ClubCourtType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClubCourtType_.id));
            }
            if (criteria.getClubCourtTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getClubCourtTypeCode(), ClubCourtType_.clubCourtTypeCode));
            }
            if (criteria.getClubCourtTypeName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getClubCourtTypeName(), ClubCourtType_.clubCourtTypeName));
            }
            if (criteria.getClubCourtTypeDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getClubCourtTypeDescription(), ClubCourtType_.clubCourtTypeDescription)
                    );
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ClubCourtType_.status));
            }
        }
        return specification;
    }
}
