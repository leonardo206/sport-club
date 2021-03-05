package com.mazimao.sportclub.web.rest;

import com.mazimao.sportclub.config.Constants;
import com.mazimao.sportclub.security.AuthoritiesConstants;
import com.mazimao.sportclub.service.OrganizationService;
import com.mazimao.sportclub.service.UserQueryService;
import com.mazimao.sportclub.service.UserService;
import com.mazimao.sportclub.service.dto.*;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link com.mazimao.sportclub.domain.User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {
    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "user";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserQueryService userQueryService;

    private final OrganizationService organizationService;

    public UserResource(UserService userService, UserQueryService userQueryService, OrganizationService organizationService) {
        this.userService = userService;
        this.userQueryService = userQueryService;
        this.organizationService = organizationService;
    }

    /*    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsersByAuthorities(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }*/

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsersByCriteria(UserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get User by criteria: {}", criteria);
        List<UserDTO> listFilteredUsers = userService.getAllManagedUsersByAuthorities();
        if (criteria != null) {
            //in case we call from organization we use organization notIn id
            listFilteredUsers = userQueryService.findByCriteria(criteria, pageable).filter(listFilteredUsers::contains).toList();
        }
        Page<UserDTO> page = new PageImpl<>(listFilteredUsers);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET /users/available-to-organization : get all users can be set to an organization.
     *
     * @param organizationId the organization id we want to filter for.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users/available-to-organization")
    public ResponseEntity<List<UserDTO>> getUsersAvailableToOrganization(@RequestParam(required = false) Long organizationId) {
        log.debug("REST request to get Users available to organization: {}", organizationId);
        final List<UserDTO> listFilteredUsers = userService.getAllManagedUsersByAuthorities();
        Set<String> listUsersInAnOrganization = userService
            .getAllUsersInOrganization()
            .stream()
            .map(UserDTO::getId)
            .collect(Collectors.toSet());
        if (organizationId != null) {
            organizationService
                .findOne(organizationId)
                .ifPresent(organizationDTO -> listUsersInAnOrganization.removeIf(s -> s.equals(organizationDTO.getUserId())));
        }
        listFilteredUsers.removeIf(userDTO -> listUsersInAnOrganization.contains(userDTO.getId()));
        return new ResponseEntity<>(listFilteredUsers, HttpStatus.OK);
    }

    /**
     * Gets a list of all roles.
     * @return a string list of all roles.
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(userService.getUserWithAuthoritiesByLogin(login));
    }
}
