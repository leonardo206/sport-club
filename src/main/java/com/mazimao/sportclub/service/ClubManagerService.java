package com.mazimao.sportclub.service;

import com.mazimao.sportclub.service.dto.ClubManagerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mazimao.sportclub.domain.ClubManager}.
 */
public interface ClubManagerService {
    /**
     * Save a clubManager.
     *
     * @param clubManagerDTO the entity to save.
     * @return the persisted entity.
     */
    ClubManagerDTO save(ClubManagerDTO clubManagerDTO);

    /**
     * Get clubManagers by userId.
     *
     * @param id the user id.
     * @return the list of entities.
     */
    Optional<ClubManagerDTO> findByUserId(String id);

    /**
     * Get all the clubManagers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClubManagerDTO> findAll(Pageable pageable);

    /**
     * Get all the clubManagers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<ClubManagerDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" clubManager.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClubManagerDTO> findOne(Long id);

    /**
     * Delete the "id" clubManager.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
