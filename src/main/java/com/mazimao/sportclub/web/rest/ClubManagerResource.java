package com.mazimao.sportclub.web.rest;

import com.mazimao.sportclub.security.AuthoritiesConstants;
import com.mazimao.sportclub.security.SecurityUtils;
import com.mazimao.sportclub.service.ClubManagerQueryService;
import com.mazimao.sportclub.service.ClubManagerService;
import com.mazimao.sportclub.service.OrganizationService;
import com.mazimao.sportclub.service.dto.ClubManagerCriteria;
import com.mazimao.sportclub.service.dto.ClubManagerDTO;
import com.mazimao.sportclub.service.dto.OrganizationDTO;
import com.mazimao.sportclub.web.rest.errors.BadRequestAlertException;
import com.mazimao.sportclub.web.rest.errors.ForbiddenAlertException;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.mazimao.sportclub.domain.ClubManager}.
 */
@RestController
@RequestMapping("/api")
public class ClubManagerResource {
    private final Logger log = LoggerFactory.getLogger(ClubManagerResource.class);

    private static final String ENTITY_NAME = "clubManager";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClubManagerService clubManagerService;

    private final ClubManagerQueryService clubManagerQueryService;

    private final OrganizationService organizationService;

    public ClubManagerResource(
        ClubManagerService clubManagerService,
        ClubManagerQueryService clubManagerQueryService,
        OrganizationService organizationService
    ) {
        this.clubManagerService = clubManagerService;
        this.clubManagerQueryService = clubManagerQueryService;
        this.organizationService = organizationService;
    }

    /**
     * {@code POST  /club-managers} : Create a new clubManager.
     *
     * @param clubManagerDTO the clubManagerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clubManagerDTO, or with status {@code 400 (Bad Request)} if the clubManager has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/club-managers")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")" + "|| hasRole(\"" + AuthoritiesConstants.ORGANIZATOR + "\")")
    public ResponseEntity<ClubManagerDTO> createClubManager(@Valid @RequestBody ClubManagerDTO clubManagerDTO) throws URISyntaxException {
        log.debug("REST request to save ClubManager : {}", clubManagerDTO);
        if (clubManagerDTO.getId() != null) {
            throw new BadRequestAlertException("A new clubManager cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String userId = SecurityUtils.getCurrentUserId();
        List<OrganizationDTO> organizationDTOList = organizationService.findAllByUser(userId);

        if (
            organizationDTOList.stream().anyMatch(organizationDTO -> organizationDTO.getId().equals(clubManagerDTO.getOrganizationId())) ||
            SecurityUtils.getAuthorities().contains(AuthoritiesConstants.ADMIN)
        ) {
            ClubManagerDTO result = clubManagerService.save(clubManagerDTO);
            return ResponseEntity
                .created(new URI("/api/club-managers/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } else {
            throw new ForbiddenAlertException("You dont have permissions", ENTITY_NAME, "forbidden");
        }
    }

    /**
     * {@code PUT  /club-managers} : Updates an existing clubManager.
     *
     * @param clubManagerDTO the clubManagerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clubManagerDTO,
     * or with status {@code 400 (Bad Request)} if the clubManagerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clubManagerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/club-managers")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")" + "|| hasRole(\"" + AuthoritiesConstants.ORGANIZATOR + "\")")
    public ResponseEntity<ClubManagerDTO> updateClubManager(@Valid @RequestBody ClubManagerDTO clubManagerDTO) throws URISyntaxException {
        log.debug("REST request to update ClubManager : {}", clubManagerDTO);
        if (clubManagerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        String userId = SecurityUtils.getCurrentUserId();
        List<OrganizationDTO> organizationDTOList = organizationService.findAllByUser(userId);

        if (
            organizationDTOList.stream().anyMatch(organizationDTO -> organizationDTO.getId().equals(clubManagerDTO.getOrganizationId())) ||
            SecurityUtils.getAuthorities().contains(AuthoritiesConstants.ADMIN)
        ) {
            ClubManagerDTO result = clubManagerService.save(clubManagerDTO);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clubManagerDTO.getId().toString()))
                .body(result);
        } else {
            throw new ForbiddenAlertException("You dont have permissions", ENTITY_NAME, "forbidden");
        }
    }

    /**
     * {@code GET  /club-managers} : get all the clubManagers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clubManagers in body.
     */
    @GetMapping("/club-managers")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")" + "|| hasRole(\"" + AuthoritiesConstants.ORGANIZATOR + "\")")
    public ResponseEntity<List<ClubManagerDTO>> getAllClubManagers(ClubManagerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ClubManagers by criteria: {}", criteria);
        Page<ClubManagerDTO> page = clubManagerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /club-managers/count} : count all the clubManagers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/club-managers/count")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")" + "|| hasRole(\"" + AuthoritiesConstants.ORGANIZATOR + "\")")
    public ResponseEntity<Long> countClubManagers(ClubManagerCriteria criteria) {
        log.debug("REST request to count ClubManagers by criteria: {}", criteria);
        return ResponseEntity.ok().body(clubManagerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /club-managers/:id} : get the "id" clubManager.
     *
     * @param id the id of the clubManagerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clubManagerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/club-managers/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ClubManagerDTO> getClubManager(@PathVariable Long id) {
        log.debug("REST request to get ClubManager : {}", id);
        Optional<ClubManagerDTO> clubManagerDTO = clubManagerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clubManagerDTO);
    }

    /**
     * {@code DELETE  /club-managers/:id} : delete the "id" clubManager.
     *
     * @param id the id of the clubManagerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/club-managers/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")" + "|| hasRole(\"" + AuthoritiesConstants.ORGANIZATOR + "\")")
    public ResponseEntity<Void> deleteClubManager(@PathVariable Long id) {
        log.debug("REST request to delete ClubManager : {}", id);
        clubManagerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
