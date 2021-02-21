package com.mazimao.sportclub.web.rest;

import com.mazimao.sportclub.service.ClubTypeQueryService;
import com.mazimao.sportclub.service.ClubTypeService;
import com.mazimao.sportclub.service.dto.ClubTypeCriteria;
import com.mazimao.sportclub.service.dto.ClubTypeDTO;
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
 * REST controller for managing {@link com.mazimao.sportclub.domain.ClubType}.
 */
@RestController
@RequestMapping("/api")
public class ClubTypeResource {
    private final Logger log = LoggerFactory.getLogger(ClubTypeResource.class);

    private static final String ENTITY_NAME = "clubType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClubTypeService clubTypeService;

    private final ClubTypeQueryService clubTypeQueryService;

    public ClubTypeResource(ClubTypeService clubTypeService, ClubTypeQueryService clubTypeQueryService) {
        this.clubTypeService = clubTypeService;
        this.clubTypeQueryService = clubTypeQueryService;
    }

    /**
     * {@code POST  /club-types} : Create a new clubType.
     *
     * @param clubTypeDTO the clubTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clubTypeDTO, or with status {@code 400 (Bad Request)} if the clubType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/club-types")
    public ResponseEntity<ClubTypeDTO> createClubType(@Valid @RequestBody ClubTypeDTO clubTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ClubType : {}", clubTypeDTO);
        if (clubTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new clubType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClubTypeDTO result = clubTypeService.save(clubTypeDTO);
        return ResponseEntity
            .created(new URI("/api/club-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /club-types} : Updates an existing clubType.
     *
     * @param clubTypeDTO the clubTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clubTypeDTO,
     * or with status {@code 400 (Bad Request)} if the clubTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clubTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/club-types")
    public ResponseEntity<ClubTypeDTO> updateClubType(@Valid @RequestBody ClubTypeDTO clubTypeDTO) throws URISyntaxException {
        log.debug("REST request to update ClubType : {}", clubTypeDTO);
        if (clubTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClubTypeDTO result = clubTypeService.save(clubTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clubTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /club-types} : get all the clubTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clubTypes in body.
     */
    @GetMapping("/club-types")
    public ResponseEntity<List<ClubTypeDTO>> getAllClubTypes(ClubTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ClubTypes by criteria: {}", criteria);
        Page<ClubTypeDTO> page = clubTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /club-types/count} : count all the clubTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/club-types/count")
    public ResponseEntity<Long> countClubTypes(ClubTypeCriteria criteria) {
        log.debug("REST request to count ClubTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(clubTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /club-types/:id} : get the "id" clubType.
     *
     * @param id the id of the clubTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clubTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/club-types/{id}")
    public ResponseEntity<ClubTypeDTO> getClubType(@PathVariable Long id) {
        log.debug("REST request to get ClubType : {}", id);
        Optional<ClubTypeDTO> clubTypeDTO = clubTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clubTypeDTO);
    }

    /**
     * {@code DELETE  /club-types/:id} : delete the "id" clubType.
     *
     * @param id the id of the clubTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/club-types/{id}")
    public ResponseEntity<Void> deleteClubType(@PathVariable Long id) {
        log.debug("REST request to delete ClubType : {}", id);
        clubTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
