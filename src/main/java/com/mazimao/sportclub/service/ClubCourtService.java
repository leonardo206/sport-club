package com.mazimao.sportclub.service;

import com.mazimao.sportclub.service.dto.ClubCourtDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mazimao.sportclub.domain.ClubCourt}.
 */
public interface ClubCourtService {
    /**
     * Save a clubCourt.
     *
     * @param clubCourtDTO the entity to save.
     * @return the persisted entity.
     */
    ClubCourtDTO save(ClubCourtDTO clubCourtDTO);

    /**
     * Get all the clubCourts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClubCourtDTO> findAll(Pageable pageable);

    /**
     * Get the "id" clubCourt.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClubCourtDTO> findOne(Long id);

    /**
     * Delete the "id" clubCourt.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
