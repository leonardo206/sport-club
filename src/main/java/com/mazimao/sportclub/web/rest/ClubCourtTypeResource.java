package com.mazimao.sportclub.web.rest;

import com.mazimao.sportclub.service.ClubCourtTypeQueryService;
import com.mazimao.sportclub.service.ClubCourtTypeService;
import com.mazimao.sportclub.service.dto.ClubCourtTypeCriteria;
import com.mazimao.sportclub.service.dto.ClubCourtTypeDTO;
import com.mazimao.sportclub.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.mazimao.sportclub.domain.ClubCourtType}.
 */
@RestController
@RequestMapping("/api")
public class ClubCourtTypeResource {
    private final Logger log = LoggerFactory.getLogger(ClubCourtTypeResource.class);

    private static final String ENTITY_NAME = "clubCourtType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClubCourtTypeService clubCourtTypeService;

    private final ClubCourtTypeQueryService clubCourtTypeQueryService;

    public ClubCourtTypeResource(ClubCourtTypeService clubCourtTypeService, ClubCourtTypeQueryService clubCourtTypeQueryService) {
        this.clubCourtTypeService = clubCourtTypeService;
        this.clubCourtTypeQueryService = clubCourtTypeQueryService;
    }

    /**
     * {@code POST  /club-court-types} : Create a new clubCourtType.
     *
     * @param clubCourtTypeDTO the clubCourtTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clubCourtTypeDTO, or with status {@code 400 (Bad Request)} if the clubCourtType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/club-court-types")
    public ResponseEntity<ClubCourtTypeDTO> createClubCourtType(@Valid @RequestBody ClubCourtTypeDTO clubCourtTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save ClubCourtType : {}", clubCourtTypeDTO);
        if (clubCourtTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new clubCourtType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClubCourtTypeDTO result = clubCourtTypeService.save(clubCourtTypeDTO);
        return ResponseEntity
            .created(new URI("/api/club-court-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /club-court-types} : Updates an existing clubCourtType.
     *
     * @param clubCourtTypeDTO the clubCourtTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clubCourtTypeDTO,
     * or with status {@code 400 (Bad Request)} if the clubCourtTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clubCourtTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/club-court-types")
    public ResponseEntity<ClubCourtTypeDTO> updateClubCourtType(@Valid @RequestBody ClubCourtTypeDTO clubCourtTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to update ClubCourtType : {}", clubCourtTypeDTO);
        if (clubCourtTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClubCourtTypeDTO result = clubCourtTypeService.save(clubCourtTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clubCourtTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /club-court-types} : get all the clubCourtTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clubCourtTypes in body.
     */
    @GetMapping("/club-court-types")
    public ResponseEntity<List<ClubCourtTypeDTO>> getAllClubCourtTypes(ClubCourtTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ClubCourtTypes by criteria: {}", criteria);
        Page<ClubCourtTypeDTO> page = clubCourtTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /club-court-types/count} : count all the clubCourtTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/club-court-types/count")
    public ResponseEntity<Long> countClubCourtTypes(ClubCourtTypeCriteria criteria) {
        log.debug("REST request to count ClubCourtTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(clubCourtTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /club-court-types/:id} : get the "id" clubCourtType.
     *
     * @param id the id of the clubCourtTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clubCourtTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/club-court-types/{id}")
    public ResponseEntity<ClubCourtTypeDTO> getClubCourtType(@PathVariable Long id) {
        log.debug("REST request to get ClubCourtType : {}", id);
        Optional<ClubCourtTypeDTO> clubCourtTypeDTO = clubCourtTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clubCourtTypeDTO);
    }

    /**
     * {@code DELETE  /club-court-types/:id} : delete the "id" clubCourtType.
     *
     * @param id the id of the clubCourtTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/club-court-types/{id}")
    public ResponseEntity<Void> deleteClubCourtType(@PathVariable Long id) {
        log.debug("REST request to delete ClubCourtType : {}", id);
        clubCourtTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
