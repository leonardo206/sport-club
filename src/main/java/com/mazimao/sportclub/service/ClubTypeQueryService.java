package com.mazimao.sportclub.service;

import com.mazimao.sportclub.domain.*; // for static metamodels
import com.mazimao.sportclub.domain.ClubType;
import com.mazimao.sportclub.repository.ClubTypeRepository;
import com.mazimao.sportclub.service.dto.ClubTypeCriteria;
import com.mazimao.sportclub.service.dto.ClubTypeDTO;
import com.mazimao.sportclub.service.mapper.ClubTypeMapper;
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
 * Service for executing complex queries for {@link ClubType} entities in the database.
 * The main input is a {@link ClubTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClubTypeDTO} or a {@link Page} of {@link ClubTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClubTypeQueryService extends QueryService<ClubType> {
    private final Logger log = LoggerFactory.getLogger(ClubTypeQueryService.class);

    private final ClubTypeRepository clubTypeRepository;

    private final ClubTypeMapper clubTypeMapper;

    public ClubTypeQueryService(ClubTypeRepository clubTypeRepository, ClubTypeMapper clubTypeMapper) {
        this.clubTypeRepository = clubTypeRepository;
        this.clubTypeMapper = clubTypeMapper;
    }

    /**
     * Return a {@link List} of {@link ClubTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClubTypeDTO> findByCriteria(ClubTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClubType> specification = createSpecification(criteria);
        return clubTypeMapper.toDto(clubTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClubTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClubTypeDTO> findByCriteria(ClubTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClubType> specification = createSpecification(criteria);
        return clubTypeRepository.findAll(specification, page).map(clubTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClubTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClubType> specification = createSpecification(criteria);
        return clubTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ClubTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClubType> createSpecification(ClubTypeCriteria criteria) {
        Specification<ClubType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClubType_.id));
            }
            if (criteria.getClubTypeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClubTypeCode(), ClubType_.clubTypeCode));
            }
            if (criteria.getClubTypeName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClubTypeName(), ClubType_.clubTypeName));
            }
            if (criteria.getClubTypeDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getClubTypeDescription(), ClubType_.clubTypeDescription));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ClubType_.status));
            }
        }
        return specification;
    }
}
