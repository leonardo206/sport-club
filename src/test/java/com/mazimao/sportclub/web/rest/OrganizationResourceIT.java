package com.mazimao.sportclub.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mazimao.sportclub.SportClubApp;
import com.mazimao.sportclub.config.TestSecurityConfiguration;
import com.mazimao.sportclub.domain.Club;
import com.mazimao.sportclub.domain.ClubManager;
import com.mazimao.sportclub.domain.Organization;
import com.mazimao.sportclub.domain.User;
import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import com.mazimao.sportclub.repository.OrganizationRepository;
import com.mazimao.sportclub.service.OrganizationQueryService;
import com.mazimao.sportclub.service.OrganizationService;
import com.mazimao.sportclub.service.dto.OrganizationCriteria;
import com.mazimao.sportclub.service.dto.OrganizationDTO;
import com.mazimao.sportclub.service.mapper.OrganizationMapper;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrganizationResource} REST controller.
 */
@SpringBootTest(classes = { SportClubApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class OrganizationResourceIT {
    private static final String DEFAULT_ORGANIZATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_NUMBER = "snCk1K_4";
    private static final String UPDATED_TAX_NUMBER = "wOL89-81";

    private static final ActiveInactiveStatus DEFAULT_STATUS = ActiveInactiveStatus.Active;
    private static final ActiveInactiveStatus UPDATED_STATUS = ActiveInactiveStatus.Inactive;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationQueryService organizationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationMockMvc;

    private Organization organization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createEntity(EntityManager em) {
        Organization organization = new Organization()
            .organizationName(DEFAULT_ORGANIZATION_NAME)
            .taxNumber(DEFAULT_TAX_NUMBER)
            .status(DEFAULT_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        organization.setUser(user);
        return organization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createUpdatedEntity(EntityManager em) {
        Organization organization = new Organization()
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .taxNumber(UPDATED_TAX_NUMBER)
            .status(UPDATED_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        organization.setUser(user);
        return organization;
    }

    @BeforeEach
    public void initTest() {
        organization = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganization() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();
        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);
        restOrganizationMockMvc
            .perform(
                post("/api/organizations")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate + 1);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getOrganizationName()).isEqualTo(DEFAULT_ORGANIZATION_NAME);
        assertThat(testOrganization.getTaxNumber()).isEqualTo(DEFAULT_TAX_NUMBER);
        assertThat(testOrganization.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createOrganizationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();

        // Create the Organization with an existing ID
        organization.setId(1L);
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMockMvc
            .perform(
                post("/api/organizations")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOrganizationNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setOrganizationName(null);

        // Create the Organization, which fails.
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        restOrganizationMockMvc
            .perform(
                post("/api/organizations")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setTaxNumber(null);

        // Create the Organization, which fails.
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        restOrganizationMockMvc
            .perform(
                post("/api/organizations")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganizations() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList
        restOrganizationMockMvc
            .perform(get("/api/organizations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get the organization
        restOrganizationMockMvc
            .perform(get("/api/organizations/{id}", organization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organization.getId().intValue()))
            .andExpect(jsonPath("$.organizationName").value(DEFAULT_ORGANIZATION_NAME))
            .andExpect(jsonPath("$.taxNumber").value(DEFAULT_TAX_NUMBER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getOrganizationsByIdFiltering() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        Long id = organization.getId();

        defaultOrganizationShouldBeFound("id.equals=" + id);
        defaultOrganizationShouldNotBeFound("id.notEquals=" + id);

        defaultOrganizationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrganizationShouldNotBeFound("id.greaterThan=" + id);

        defaultOrganizationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrganizationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByOrganizationNameIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where organizationName equals to DEFAULT_ORGANIZATION_NAME
        defaultOrganizationShouldBeFound("organizationName.equals=" + DEFAULT_ORGANIZATION_NAME);

        // Get all the organizationList where organizationName equals to UPDATED_ORGANIZATION_NAME
        defaultOrganizationShouldNotBeFound("organizationName.equals=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByOrganizationNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where organizationName not equals to DEFAULT_ORGANIZATION_NAME
        defaultOrganizationShouldNotBeFound("organizationName.notEquals=" + DEFAULT_ORGANIZATION_NAME);

        // Get all the organizationList where organizationName not equals to UPDATED_ORGANIZATION_NAME
        defaultOrganizationShouldBeFound("organizationName.notEquals=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByOrganizationNameIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where organizationName in DEFAULT_ORGANIZATION_NAME or UPDATED_ORGANIZATION_NAME
        defaultOrganizationShouldBeFound("organizationName.in=" + DEFAULT_ORGANIZATION_NAME + "," + UPDATED_ORGANIZATION_NAME);

        // Get all the organizationList where organizationName equals to UPDATED_ORGANIZATION_NAME
        defaultOrganizationShouldNotBeFound("organizationName.in=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByOrganizationNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where organizationName is not null
        defaultOrganizationShouldBeFound("organizationName.specified=true");

        // Get all the organizationList where organizationName is null
        defaultOrganizationShouldNotBeFound("organizationName.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganizationsByOrganizationNameContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where organizationName contains DEFAULT_ORGANIZATION_NAME
        defaultOrganizationShouldBeFound("organizationName.contains=" + DEFAULT_ORGANIZATION_NAME);

        // Get all the organizationList where organizationName contains UPDATED_ORGANIZATION_NAME
        defaultOrganizationShouldNotBeFound("organizationName.contains=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByOrganizationNameNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where organizationName does not contain DEFAULT_ORGANIZATION_NAME
        defaultOrganizationShouldNotBeFound("organizationName.doesNotContain=" + DEFAULT_ORGANIZATION_NAME);

        // Get all the organizationList where organizationName does not contain UPDATED_ORGANIZATION_NAME
        defaultOrganizationShouldBeFound("organizationName.doesNotContain=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByTaxNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where taxNumber equals to DEFAULT_TAX_NUMBER
        defaultOrganizationShouldBeFound("taxNumber.equals=" + DEFAULT_TAX_NUMBER);

        // Get all the organizationList where taxNumber equals to UPDATED_TAX_NUMBER
        defaultOrganizationShouldNotBeFound("taxNumber.equals=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByTaxNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where taxNumber not equals to DEFAULT_TAX_NUMBER
        defaultOrganizationShouldNotBeFound("taxNumber.notEquals=" + DEFAULT_TAX_NUMBER);

        // Get all the organizationList where taxNumber not equals to UPDATED_TAX_NUMBER
        defaultOrganizationShouldBeFound("taxNumber.notEquals=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByTaxNumberIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where taxNumber in DEFAULT_TAX_NUMBER or UPDATED_TAX_NUMBER
        defaultOrganizationShouldBeFound("taxNumber.in=" + DEFAULT_TAX_NUMBER + "," + UPDATED_TAX_NUMBER);

        // Get all the organizationList where taxNumber equals to UPDATED_TAX_NUMBER
        defaultOrganizationShouldNotBeFound("taxNumber.in=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByTaxNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where taxNumber is not null
        defaultOrganizationShouldBeFound("taxNumber.specified=true");

        // Get all the organizationList where taxNumber is null
        defaultOrganizationShouldNotBeFound("taxNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganizationsByTaxNumberContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where taxNumber contains DEFAULT_TAX_NUMBER
        defaultOrganizationShouldBeFound("taxNumber.contains=" + DEFAULT_TAX_NUMBER);

        // Get all the organizationList where taxNumber contains UPDATED_TAX_NUMBER
        defaultOrganizationShouldNotBeFound("taxNumber.contains=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByTaxNumberNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where taxNumber does not contain DEFAULT_TAX_NUMBER
        defaultOrganizationShouldNotBeFound("taxNumber.doesNotContain=" + DEFAULT_TAX_NUMBER);

        // Get all the organizationList where taxNumber does not contain UPDATED_TAX_NUMBER
        defaultOrganizationShouldBeFound("taxNumber.doesNotContain=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where status equals to DEFAULT_STATUS
        defaultOrganizationShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the organizationList where status equals to UPDATED_STATUS
        defaultOrganizationShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where status not equals to DEFAULT_STATUS
        defaultOrganizationShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the organizationList where status not equals to UPDATED_STATUS
        defaultOrganizationShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultOrganizationShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the organizationList where status equals to UPDATED_STATUS
        defaultOrganizationShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where status is not null
        defaultOrganizationShouldBeFound("status.specified=true");

        // Get all the organizationList where status is null
        defaultOrganizationShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganizationsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = organization.getUser();
        organizationRepository.saveAndFlush(organization);
        String userId = user.getId();

        // Get all the organizationList where user equals to userId
        defaultOrganizationShouldBeFound("userId.equals=" + userId);

        // Get all the organizationList where user equals to userId + 1
        defaultOrganizationShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    public void getAllOrganizationsByClubManagersIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        ClubManager clubManagers = ClubManagerResourceIT.createEntity(em);
        em.persist(clubManagers);
        em.flush();
        organization.addClubManagers(clubManagers);
        organizationRepository.saveAndFlush(organization);
        Long clubManagersId = clubManagers.getId();

        // Get all the organizationList where clubManagers equals to clubManagersId
        defaultOrganizationShouldBeFound("clubManagersId.equals=" + clubManagersId);

        // Get all the organizationList where clubManagers equals to clubManagersId + 1
        defaultOrganizationShouldNotBeFound("clubManagersId.equals=" + (clubManagersId + 1));
    }

    @Test
    @Transactional
    public void getAllOrganizationsByClubsIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        Club clubs = ClubResourceIT.createEntity(em);
        em.persist(clubs);
        em.flush();
        organization.addClubs(clubs);
        organizationRepository.saveAndFlush(organization);
        Long clubsId = clubs.getId();

        // Get all the organizationList where clubs equals to clubsId
        defaultOrganizationShouldBeFound("clubsId.equals=" + clubsId);

        // Get all the organizationList where clubs equals to clubsId + 1
        defaultOrganizationShouldNotBeFound("clubsId.equals=" + (clubsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrganizationShouldBeFound(String filter) throws Exception {
        restOrganizationMockMvc
            .perform(get("/api/organizations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restOrganizationMockMvc
            .perform(get("/api/organizations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrganizationShouldNotBeFound(String filter) throws Exception {
        restOrganizationMockMvc
            .perform(get("/api/organizations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganizationMockMvc
            .perform(get("/api/organizations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingOrganization() throws Exception {
        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization
        Organization updatedOrganization = organizationRepository.findById(organization.getId()).get();
        // Disconnect from session so that the updates on updatedOrganization are not directly saved in db
        em.detach(updatedOrganization);
        updatedOrganization.organizationName(UPDATED_ORGANIZATION_NAME).taxNumber(UPDATED_TAX_NUMBER).status(UPDATED_STATUS);
        OrganizationDTO organizationDTO = organizationMapper.toDto(updatedOrganization);

        restOrganizationMockMvc
            .perform(
                put("/api/organizations")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getOrganizationName()).isEqualTo(UPDATED_ORGANIZATION_NAME);
        assertThat(testOrganization.getTaxNumber()).isEqualTo(UPDATED_TAX_NUMBER);
        assertThat(testOrganization.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                put("/api/organizations")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeDelete = organizationRepository.findAll().size();

        // Delete the organization
        restOrganizationMockMvc
            .perform(delete("/api/organizations/{id}", organization.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
