package com.mazimao.sportclub.service;

import com.mazimao.sportclub.domain.*; // for static metamodels
import com.mazimao.sportclub.domain.Club;
import com.mazimao.sportclub.repository.ClubRepository;
import com.mazimao.sportclub.service.dto.ClubCriteria;
import com.mazimao.sportclub.service.dto.ClubDTO;
import com.mazimao.sportclub.service.mapper.ClubMapper;
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
 * Service for executing complex queries for {@link Club} entities in the database.
 * The main input is a {@link ClubCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClubDTO} or a {@link Page} of {@link ClubDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClubQueryService extends QueryService<Club> {
    private final Logger log = LoggerFactory.getLogger(ClubQueryService.class);

    private final ClubRepository clubRepository;

    private final ClubMapper clubMapper;

    public ClubQueryService(ClubRepository clubRepository, ClubMapper clubMapper) {
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
    }

    /**
     * Return a {@link List} of {@link ClubDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClubDTO> findByCriteria(ClubCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Club> specification = createSpecification(criteria);
        return clubMapper.toDto(clubRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClubDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClubDTO> findByCriteria(ClubCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Club> specification = createSpecification(criteria);
        return clubRepository.findAll(specification, page).map(clubMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClubCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Club> specification = createSpecification(criteria);
        return clubRepository.count(specification);
    }

    /**
     * Function to convert {@link ClubCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Club> createSpecification(ClubCriteria criteria) {
        Specification<Club> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Club_.id));
            }
            if (criteria.getClubName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClubName(), Club_.clubName));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Club_.status));
            }
            if (criteria.getClubTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClubTypeId(), root -> root.join(Club_.clubType, JoinType.LEFT).get(ClubType_.id))
                    );
            }
            if (criteria.getClubCourtsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClubCourtsId(),
                            root -> root.join(Club_.clubCourts, JoinType.LEFT).get(ClubCourt_.id)
                        )
                    );
            }
            if (criteria.getOrganizationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationId(),
                            root -> root.join(Club_.organization, JoinType.LEFT).get(Organization_.id)
                        )
                    );
            }
            if (criteria.getClubManagerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClubManagerId(),
                            root -> root.join(Club_.clubManagers, JoinType.LEFT).get(ClubManager_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
