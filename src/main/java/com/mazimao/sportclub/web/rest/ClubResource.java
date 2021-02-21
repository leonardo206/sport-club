package com.mazimao.sportclub.web.rest;

import com.mazimao.sportclub.service.ClubQueryService;
import com.mazimao.sportclub.service.ClubService;
import com.mazimao.sportclub.service.dto.ClubCriteria;
import com.mazimao.sportclub.service.dto.ClubDTO;
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
 * REST controller for managing {@link com.mazimao.sportclub.domain.Club}.
 */
@RestController
@RequestMapping("/api")
public class ClubResource {
    private final Logger log = LoggerFactory.getLogger(ClubResource.class);

    private static final String ENTITY_NAME = "club";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClubService clubService;

    private final ClubQueryService clubQueryService;

    public ClubResource(ClubService clubService, ClubQueryService clubQueryService) {
        this.clubService = clubService;
        this.clubQueryService = clubQueryService;
    }

    /**
     * {@code POST  /clubs} : Create a new club.
     *
     * @param clubDTO the clubDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clubDTO, or with status {@code 400 (Bad Request)} if the club has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clubs")
    public ResponseEntity<ClubDTO> createClub(@Valid @RequestBody ClubDTO clubDTO) throws URISyntaxException {
        log.debug("REST request to save Club : {}", clubDTO);
        if (clubDTO.getId() != null) {
            throw new BadRequestAlertException("A new club cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClubDTO result = clubService.save(clubDTO);
        return ResponseEntity
            .created(new URI("/api/clubs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clubs} : Updates an existing club.
     *
     * @param clubDTO the clubDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clubDTO,
     * or with status {@code 400 (Bad Request)} if the clubDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clubDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clubs")
    public ResponseEntity<ClubDTO> updateClub(@Valid @RequestBody ClubDTO clubDTO) throws URISyntaxException {
        log.debug("REST request to update Club : {}", clubDTO);
        if (clubDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClubDTO result = clubService.save(clubDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clubDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /clubs} : get all the clubs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clubs in body.
     */
    @GetMapping("/clubs")
    public ResponseEntity<List<ClubDTO>> getAllClubs(ClubCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Clubs by criteria: {}", criteria);
        Page<ClubDTO> page = clubQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /clubs/count} : count all the clubs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/clubs/count")
    public ResponseEntity<Long> countClubs(ClubCriteria criteria) {
        log.debug("REST request to count Clubs by criteria: {}", criteria);
        return ResponseEntity.ok().body(clubQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /clubs/:id} : get the "id" club.
     *
     * @param id the id of the clubDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clubDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clubs/{id}")
    public ResponseEntity<ClubDTO> getClub(@PathVariable Long id) {
        log.debug("REST request to get Club : {}", id);
        Optional<ClubDTO> clubDTO = clubService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clubDTO);
    }

    /**
     * {@code DELETE  /clubs/:id} : delete the "id" club.
     *
     * @param id the id of the clubDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clubs/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long id) {
        log.debug("REST request to delete Club : {}", id);
        clubService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
