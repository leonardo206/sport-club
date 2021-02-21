package com.mazimao.sportclub.service;

import com.mazimao.sportclub.service.dto.ClubTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mazimao.sportclub.domain.ClubType}.
 */
public interface ClubTypeService {
    /**
     * Save a clubType.
     *
     * @param clubTypeDTO the entity to save.
     * @return the persisted entity.
     */
    ClubTypeDTO save(ClubTypeDTO clubTypeDTO);

    /**
     * Get all the clubTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClubTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" clubType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClubTypeDTO> findOne(Long id);

    /**
     * Delete the "id" clubType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
