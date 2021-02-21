package com.mazimao.sportclub.web.rest;

import com.mazimao.sportclub.service.ClubCourtQueryService;
import com.mazimao.sportclub.service.ClubCourtService;
import com.mazimao.sportclub.service.dto.ClubCourtCriteria;
import com.mazimao.sportclub.service.dto.ClubCourtDTO;
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
 * REST controller for managing {@link com.mazimao.sportclub.domain.ClubCourt}.
 */
@RestController
@RequestMapping("/api")
public class ClubCourtResource {
    private final Logger log = LoggerFactory.getLogger(ClubCourtResource.class);

    private static final String ENTITY_NAME = "clubCourt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClubCourtService clubCourtService;

    private final ClubCourtQueryService clubCourtQueryService;

    public ClubCourtResource(ClubCourtService clubCourtService, ClubCourtQueryService clubCourtQueryService) {
        this.clubCourtService = clubCourtService;
        this.clubCourtQueryService = clubCourtQueryService;
    }

    /**
     * {@code POST  /club-courts} : Create a new clubCourt.
     *
     * @param clubCourtDTO the clubCourtDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clubCourtDTO, or with status {@code 400 (Bad Request)} if the clubCourt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/club-courts")
    public ResponseEntity<ClubCourtDTO> createClubCourt(@Valid @RequestBody ClubCourtDTO clubCourtDTO) throws URISyntaxException {
        log.debug("REST request to save ClubCourt : {}", clubCourtDTO);
        if (clubCourtDTO.getId() != null) {
            throw new BadRequestAlertException("A new clubCourt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClubCourtDTO result = clubCourtService.save(clubCourtDTO);
        return ResponseEntity
            .created(new URI("/api/club-courts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /club-courts} : Updates an existing clubCourt.
     *
     * @param clubCourtDTO the clubCourtDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clubCourtDTO,
     * or with status {@code 400 (Bad Request)} if the clubCourtDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clubCourtDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/club-courts")
    public ResponseEntity<ClubCourtDTO> updateClubCourt(@Valid @RequestBody ClubCourtDTO clubCourtDTO) throws URISyntaxException {
        log.debug("REST request to update ClubCourt : {}", clubCourtDTO);
        if (clubCourtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClubCourtDTO result = clubCourtService.save(clubCourtDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clubCourtDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /club-courts} : get all the clubCourts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clubCourts in body.
     */
    @GetMapping("/club-courts")
    public ResponseEntity<List<ClubCourtDTO>> getAllClubCourts(ClubCourtCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ClubCourts by criteria: {}", criteria);
        Page<ClubCourtDTO> page = clubCourtQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /club-courts/count} : count all the clubCourts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/club-courts/count")
    public ResponseEntity<Long> countClubCourts(ClubCourtCriteria criteria) {
        log.debug("REST request to count ClubCourts by criteria: {}", criteria);
        return ResponseEntity.ok().body(clubCourtQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /club-courts/:id} : get the "id" clubCourt.
     *
     * @param id the id of the clubCourtDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clubCourtDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/club-courts/{id}")
    public ResponseEntity<ClubCourtDTO> getClubCourt(@PathVariable Long id) {
        log.debug("REST request to get ClubCourt : {}", id);
        Optional<ClubCourtDTO> clubCourtDTO = clubCourtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clubCourtDTO);
    }

    /**
     * {@code DELETE  /club-courts/:id} : delete the "id" clubCourt.
     *
     * @param id the id of the clubCourtDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/club-courts/{id}")
    public ResponseEntity<Void> deleteClubCourt(@PathVariable Long id) {
        log.debug("REST request to delete ClubCourt : {}", id);
        clubCourtService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
