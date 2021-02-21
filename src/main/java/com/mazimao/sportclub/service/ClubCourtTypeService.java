package com.mazimao.sportclub.service;

import com.mazimao.sportclub.service.dto.ClubCourtTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mazimao.sportclub.domain.ClubCourtType}.
 */
public interface ClubCourtTypeService {
    /**
     * Save a clubCourtType.
     *
     * @param clubCourtTypeDTO the entity to save.
     * @return the persisted entity.
     */
    ClubCourtTypeDTO save(ClubCourtTypeDTO clubCourtTypeDTO);

    /**
     * Get all the clubCourtTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClubCourtTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" clubCourtType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClubCourtTypeDTO> findOne(Long id);

    /**
     * Delete the "id" clubCourtType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
