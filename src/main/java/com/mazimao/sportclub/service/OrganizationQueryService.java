package com.mazimao.sportclub.service;

import com.mazimao.sportclub.domain.*; // for static metamodels
import com.mazimao.sportclub.domain.Organization;
import com.mazimao.sportclub.repository.OrganizationRepository;
import com.mazimao.sportclub.security.AuthoritiesConstants;
import com.mazimao.sportclub.security.SecurityFilter;
import com.mazimao.sportclub.security.SecurityUtils;
import com.mazimao.sportclub.service.dto.OrganizationCriteria;
import com.mazimao.sportclub.service.dto.OrganizationDTO;
import com.mazimao.sportclub.service.dto.UserDTO;
import com.mazimao.sportclub.service.mapper.OrganizationMapper;
import io.github.jhipster.service.QueryService;
import io.github.jhipster.service.filter.StringFilter;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link Organization} entities in the database.
 * The main input is a {@link OrganizationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganizationDTO} or a {@link Page} of {@link OrganizationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganizationQueryService extends QueryService<Organization> implements SecurityFilter<OrganizationCriteria> {
    private final Logger log = LoggerFactory.getLogger(OrganizationQueryService.class);

    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;

    private final UserService userService;

    private final ClubManagerService clubManagerService;

    public OrganizationQueryService(
        OrganizationRepository organizationRepository,
        OrganizationMapper organizationMapper,
        UserService userService,
        ClubManagerService clubManagerService
    ) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
        this.userService = userService;
        this.clubManagerService = clubManagerService;
    }

    /**
     * Return a {@link List} of {@link OrganizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganizationDTO> findByCriteria(OrganizationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Organization> specification = createSpecification(criteria);
        return organizationMapper.toDto(organizationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrganizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganizationDTO> findByCriteria(OrganizationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Organization> specification = createSpecification(criteria);
        return organizationRepository.findAll(specification, page).map(organizationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrganizationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Organization> specification = createSpecification(criteria);
        return organizationRepository.count(specification);
    }

    /**
     * Function to convert {@link OrganizationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Organization> createSpecification(OrganizationCriteria criteria) {
        criteria = setSecurityCriteria(criteria, SecurityUtils.getAuthorities());
        Specification<Organization> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Organization_.id));
            }
            if (criteria.getOrganizationName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrganizationName(), Organization_.organizationName));
            }
            if (criteria.getTaxNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaxNumber(), Organization_.taxNumber));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Organization_.status));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Organization_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getClubManagersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClubManagersId(),
                            root -> root.join(Organization_.clubManagers, JoinType.LEFT).get(ClubManager_.id)
                        )
                    );
            }
            if (criteria.getClubsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClubsId(), root -> root.join(Organization_.clubs, JoinType.LEFT).get(Club_.id))
                    );
            }
        }
        return specification;
    }

    @Override
    public OrganizationCriteria setSecurityCriteria(OrganizationCriteria organizationCriteria, List<String> authorities) {
        StringFilter stringFilter = new StringFilter();
        if (authorities.contains(AuthoritiesConstants.ORGANIZATOR)) {
            Optional<UserDTO> userLogin = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin().get());
            if (userLogin.isPresent()) {
                stringFilter.setEquals(userLogin.get().getId());
                organizationCriteria.setUserId(stringFilter);
            }
        }
        // TODO: 2/24/2021 for the time being CLUB_MANAGER cant access organization
        /*

         else if (authorities.contains(AuthoritiesConstants.CLUB_MANAGER)) {
            Optional<ClubManagerDTO> clubManager = clubManagerService.findByUserId(SecurityUtils.getCurrentUserId());
            if (clubManager.isPresent()) {
                stringFilter.setEquals(clubManager.get().getUserId());
                organizationCriteria.setUserId(stringFilter);
            }

        }*/
        return organizationCriteria;
    }
}
