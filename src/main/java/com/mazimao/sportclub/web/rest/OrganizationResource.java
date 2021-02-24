package com.mazimao.sportclub.web.rest;

import com.mazimao.sportclub.domain.ClubManager;
import com.mazimao.sportclub.domain.User;
import com.mazimao.sportclub.security.AuthoritiesConstants;
import com.mazimao.sportclub.security.SecurityUtils;
import com.mazimao.sportclub.service.ClubManagerService;
import com.mazimao.sportclub.service.OrganizationQueryService;
import com.mazimao.sportclub.service.OrganizationService;
import com.mazimao.sportclub.service.UserService;
import com.mazimao.sportclub.service.dto.ClubManagerDTO;
import com.mazimao.sportclub.service.dto.OrganizationCriteria;
import com.mazimao.sportclub.service.dto.OrganizationDTO;
import com.mazimao.sportclub.service.dto.UserDTO;
import com.mazimao.sportclub.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.service.filter.StringFilter;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.mazimao.sportclub.domain.Organization}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationResource {
    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

    private static final String ENTITY_NAME = "organization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationService organizationService;

    private final OrganizationQueryService organizationQueryService;

    public OrganizationResource(OrganizationService organizationService, OrganizationQueryService organizationQueryService) {
        this.organizationService = organizationService;
        this.organizationQueryService = organizationQueryService;
    }

    /**
     * {@code POST  /organizations} : Create a new organization.
     *
     * @param organizationDTO the organizationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationDTO, or with status {@code 400 (Bad Request)} if the organization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organizations")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")" + "|| hasRole(\"" + AuthoritiesConstants.ORGANIZATOR + "\")")
    public ResponseEntity<OrganizationDTO> createOrganization(@Valid @RequestBody OrganizationDTO organizationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Organization : {}", organizationDTO);
        if (organizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new organization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationDTO result = organizationService.save(organizationDTO);
        return ResponseEntity
            .created(new URI("/api/organizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organizations} : Updates an existing organization.
     *
     * @param organizationDTO the organizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationDTO,
     * or with status {@code 400 (Bad Request)} if the organizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organizations")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")" + "|| hasRole(\"" + AuthoritiesConstants.ORGANIZATOR + "\")")
    public ResponseEntity<OrganizationDTO> updateOrganization(@Valid @RequestBody OrganizationDTO organizationDTO)
        throws URISyntaxException {
        log.debug("REST request to update Organization : {}", organizationDTO);
        if (organizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if (
            userLogin.isPresent() &&
            userLogin.get().equals(organizationDTO.getUserLogin()) ||
            SecurityUtils.getAuthorities().contains(AuthoritiesConstants.ADMIN)
        ) {
            OrganizationDTO result = organizationService.save(organizationDTO);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationDTO.getId().toString()))
                .body(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * {@code GET  /organizations} : get all the organizations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizations in body.
     */
    @GetMapping("/organizations")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")" + "|| hasRole(\"" + AuthoritiesConstants.ORGANIZATOR + "\")")
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations(OrganizationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Organizations by criteria: {}", criteria);
        Page<OrganizationDTO> page = organizationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organizations/count} : count all the organizations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/organizations/count")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")" + "|| hasRole(\"" + AuthoritiesConstants.ORGANIZATOR + "\")")
    public ResponseEntity<Long> countOrganizations(OrganizationCriteria criteria) {
        log.debug("REST request to count Organizations by criteria: {}", criteria);
        return ResponseEntity.ok().body(organizationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /organizations/:id} : get the "id" organization.
     *
     * @param id the id of the organizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organizations/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")" + "|| hasRole(\"" + AuthoritiesConstants.ORGANIZATOR + "\")")
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable Long id) {
        log.debug("REST request to get Organization : {}", id);
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        Optional<OrganizationDTO> organizationDTO = organizationService.findOne(id);
        if (
            organizationDTO.isPresent() &&
            userLogin.isPresent() &&
            userLogin.get().equals(organizationDTO.get().getUserLogin()) ||
            SecurityUtils.getAuthorities().contains(AuthoritiesConstants.ADMIN)
        ) {
            return ResponseUtil.wrapOrNotFound(organizationDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * {@code DELETE  /organizations/:id} : delete the "id" organization.
     *
     * @param id the id of the organizationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organizations/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")" + "|| hasRole(\"" + AuthoritiesConstants.ORGANIZATOR + "\")")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        log.debug("REST request to delete Organization : {}", id);
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        Optional<OrganizationDTO> organizationDTO = organizationService.findOne(id);
        if (
            organizationDTO.isPresent() &&
            userLogin.isPresent() &&
            userLogin.get().equals(organizationDTO.get().getUserLogin()) ||
            SecurityUtils.getAuthorities().contains(AuthoritiesConstants.ADMIN)
        ) {
            organizationService.delete(id);
            return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
