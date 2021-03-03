package com.mazimao.sportclub.service;

import com.mazimao.sportclub.service.dto.OrganizationDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mazimao.sportclub.domain.Organization}.
 */
public interface OrganizationService {
    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save.
     * @return the persisted entity.
     */
    OrganizationDTO save(OrganizationDTO organizationDTO);

    /**
     * Get all the organizations.
     *
     * @return the list of entities.
     */
    List<OrganizationDTO> findAll();

    /**
     * Get all the organizations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrganizationDTO> findAll(Pageable pageable);

    /**
     * Get all the organizations of a user.
     *
     * @param userId the id of the user.
     * @return the list of entities.
     */
    List<OrganizationDTO> findAllByUser(String userId);

    /**
     * Get the "id" organization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationDTO> findOne(Long id);

    /**
     * Get by name
     *
     * @param name the name of the entity.
     * @return the entity.
     */
    Optional<OrganizationDTO> findOneByName(String name);

    /**
     * Get the organization by id and userid.
     *
     * @param id the id of the entity.
     * @param userId the id of the user.
     * @return the entity.
     */
    Optional<OrganizationDTO> findOneByUser(Long id, String userId);

    /**
     * Get the organization by userid.
     *
     * @param userId the id of the user.
     * @return the entity.
     */
    Optional<OrganizationDTO> findByUser(String userId);

    /**
     * Delete the "id" organization.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
