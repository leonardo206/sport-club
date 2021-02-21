package com.mazimao.sportclub.service;

import com.mazimao.sportclub.domain.*; // for static metamodels
import com.mazimao.sportclub.domain.ClubManager;
import com.mazimao.sportclub.repository.ClubManagerRepository;
import com.mazimao.sportclub.service.dto.ClubManagerCriteria;
import com.mazimao.sportclub.service.dto.ClubManagerDTO;
import com.mazimao.sportclub.service.mapper.ClubManagerMapper;
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
 * Service for executing complex queries for {@link ClubManager} entities in the database.
 * The main input is a {@link ClubManagerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClubManagerDTO} or a {@link Page} of {@link ClubManagerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClubManagerQueryService extends QueryService<ClubManager> {
    private final Logger log = LoggerFactory.getLogger(ClubManagerQueryService.class);

    private final ClubManagerRepository clubManagerRepository;

    private final ClubManagerMapper clubManagerMapper;

    public ClubManagerQueryService(ClubManagerRepository clubManagerRepository, ClubManagerMapper clubManagerMapper) {
        this.clubManagerRepository = clubManagerRepository;
        this.clubManagerMapper = clubManagerMapper;
    }

    /**
     * Return a {@link List} of {@link ClubManagerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClubManagerDTO> findByCriteria(ClubManagerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClubManager> specification = createSpecification(criteria);
        return clubManagerMapper.toDto(clubManagerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClubManagerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClubManagerDTO> findByCriteria(ClubManagerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClubManager> specification = createSpecification(criteria);
        return clubManagerRepository.findAll(specification, page).map(clubManagerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClubManagerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClubManager> specification = createSpecification(criteria);
        return clubManagerRepository.count(specification);
    }

    /**
     * Function to convert {@link ClubManagerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClubManager> createSpecification(ClubManagerCriteria criteria) {
        Specification<ClubManager> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClubManager_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ClubManager_.status));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(ClubManager_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getClubsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClubsId(), root -> root.join(ClubManager_.clubs, JoinType.LEFT).get(Club_.id))
                    );
            }
            if (criteria.getOrganizationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationId(),
                            root -> root.join(ClubManager_.organization, JoinType.LEFT).get(Organization_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
