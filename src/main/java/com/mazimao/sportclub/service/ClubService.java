package com.mazimao.sportclub.service;

import com.mazimao.sportclub.service.dto.ClubDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mazimao.sportclub.domain.Club}.
 */
public interface ClubService {
    /**
     * Save a club.
     *
     * @param clubDTO the entity to save.
     * @return the persisted entity.
     */
    ClubDTO save(ClubDTO clubDTO);

    /**
     * Get all the clubs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClubDTO> findAll(Pageable pageable);

    /**
     * Get the "id" club.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClubDTO> findOne(Long id);

    /**
     * Delete the "id" club.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
