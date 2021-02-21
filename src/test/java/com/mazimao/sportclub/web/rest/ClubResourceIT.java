package com.mazimao.sportclub.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mazimao.sportclub.SportClubApp;
import com.mazimao.sportclub.config.TestSecurityConfiguration;
import com.mazimao.sportclub.domain.Club;
import com.mazimao.sportclub.domain.ClubCourt;
import com.mazimao.sportclub.domain.ClubManager;
import com.mazimao.sportclub.domain.ClubType;
import com.mazimao.sportclub.domain.Organization;
import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import com.mazimao.sportclub.repository.ClubRepository;
import com.mazimao.sportclub.service.ClubQueryService;
import com.mazimao.sportclub.service.ClubService;
import com.mazimao.sportclub.service.dto.ClubCriteria;
import com.mazimao.sportclub.service.dto.ClubDTO;
import com.mazimao.sportclub.service.mapper.ClubMapper;
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
 * Integration tests for the {@link ClubResource} REST controller.
 */
@SpringBootTest(classes = { SportClubApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ClubResourceIT {
    private static final String DEFAULT_CLUB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLUB_NAME = "BBBBBBBBBB";

    private static final ActiveInactiveStatus DEFAULT_STATUS = ActiveInactiveStatus.Active;
    private static final ActiveInactiveStatus UPDATED_STATUS = ActiveInactiveStatus.Inactive;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private ClubService clubService;

    @Autowired
    private ClubQueryService clubQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClubMockMvc;

    private Club club;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Club createEntity(EntityManager em) {
        Club club = new Club().clubName(DEFAULT_CLUB_NAME).status(DEFAULT_STATUS);
        // Add required entity
        ClubType clubType;
        if (TestUtil.findAll(em, ClubType.class).isEmpty()) {
            clubType = ClubTypeResourceIT.createEntity(em);
            em.persist(clubType);
            em.flush();
        } else {
            clubType = TestUtil.findAll(em, ClubType.class).get(0);
        }
        club.setClubType(clubType);
        return club;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Club createUpdatedEntity(EntityManager em) {
        Club club = new Club().clubName(UPDATED_CLUB_NAME).status(UPDATED_STATUS);
        // Add required entity
        ClubType clubType;
        if (TestUtil.findAll(em, ClubType.class).isEmpty()) {
            clubType = ClubTypeResourceIT.createUpdatedEntity(em);
            em.persist(clubType);
            em.flush();
        } else {
            clubType = TestUtil.findAll(em, ClubType.class).get(0);
        }
        club.setClubType(clubType);
        return club;
    }

    @BeforeEach
    public void initTest() {
        club = createEntity(em);
    }

    @Test
    @Transactional
    public void createClub() throws Exception {
        int databaseSizeBeforeCreate = clubRepository.findAll().size();
        // Create the Club
        ClubDTO clubDTO = clubMapper.toDto(club);
        restClubMockMvc
            .perform(
                post("/api/clubs").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clubDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeCreate + 1);
        Club testClub = clubList.get(clubList.size() - 1);
        assertThat(testClub.getClubName()).isEqualTo(DEFAULT_CLUB_NAME);
        assertThat(testClub.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createClubWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clubRepository.findAll().size();

        // Create the Club with an existing ID
        club.setId(1L);
        ClubDTO clubDTO = clubMapper.toDto(club);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClubMockMvc
            .perform(
                post("/api/clubs").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clubDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkClubNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clubRepository.findAll().size();
        // set the field null
        club.setClubName(null);

        // Create the Club, which fails.
        ClubDTO clubDTO = clubMapper.toDto(club);

        restClubMockMvc
            .perform(
                post("/api/clubs").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clubDTO))
            )
            .andExpect(status().isBadRequest());

        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClubs() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList
        restClubMockMvc
            .perform(get("/api/clubs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(club.getId().intValue())))
            .andExpect(jsonPath("$.[*].clubName").value(hasItem(DEFAULT_CLUB_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get the club
        restClubMockMvc
            .perform(get("/api/clubs/{id}", club.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(club.getId().intValue()))
            .andExpect(jsonPath("$.clubName").value(DEFAULT_CLUB_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getClubsByIdFiltering() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        Long id = club.getId();

        defaultClubShouldBeFound("id.equals=" + id);
        defaultClubShouldNotBeFound("id.notEquals=" + id);

        defaultClubShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClubShouldNotBeFound("id.greaterThan=" + id);

        defaultClubShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClubShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllClubsByClubNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where clubName equals to DEFAULT_CLUB_NAME
        defaultClubShouldBeFound("clubName.equals=" + DEFAULT_CLUB_NAME);

        // Get all the clubList where clubName equals to UPDATED_CLUB_NAME
        defaultClubShouldNotBeFound("clubName.equals=" + UPDATED_CLUB_NAME);
    }

    @Test
    @Transactional
    public void getAllClubsByClubNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where clubName not equals to DEFAULT_CLUB_NAME
        defaultClubShouldNotBeFound("clubName.notEquals=" + DEFAULT_CLUB_NAME);

        // Get all the clubList where clubName not equals to UPDATED_CLUB_NAME
        defaultClubShouldBeFound("clubName.notEquals=" + UPDATED_CLUB_NAME);
    }

    @Test
    @Transactional
    public void getAllClubsByClubNameIsInShouldWork() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where clubName in DEFAULT_CLUB_NAME or UPDATED_CLUB_NAME
        defaultClubShouldBeFound("clubName.in=" + DEFAULT_CLUB_NAME + "," + UPDATED_CLUB_NAME);

        // Get all the clubList where clubName equals to UPDATED_CLUB_NAME
        defaultClubShouldNotBeFound("clubName.in=" + UPDATED_CLUB_NAME);
    }

    @Test
    @Transactional
    public void getAllClubsByClubNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where clubName is not null
        defaultClubShouldBeFound("clubName.specified=true");

        // Get all the clubList where clubName is null
        defaultClubShouldNotBeFound("clubName.specified=false");
    }

    @Test
    @Transactional
    public void getAllClubsByClubNameContainsSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where clubName contains DEFAULT_CLUB_NAME
        defaultClubShouldBeFound("clubName.contains=" + DEFAULT_CLUB_NAME);

        // Get all the clubList where clubName contains UPDATED_CLUB_NAME
        defaultClubShouldNotBeFound("clubName.contains=" + UPDATED_CLUB_NAME);
    }

    @Test
    @Transactional
    public void getAllClubsByClubNameNotContainsSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where clubName does not contain DEFAULT_CLUB_NAME
        defaultClubShouldNotBeFound("clubName.doesNotContain=" + DEFAULT_CLUB_NAME);

        // Get all the clubList where clubName does not contain UPDATED_CLUB_NAME
        defaultClubShouldBeFound("clubName.doesNotContain=" + UPDATED_CLUB_NAME);
    }

    @Test
    @Transactional
    public void getAllClubsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where status equals to DEFAULT_STATUS
        defaultClubShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the clubList where status equals to UPDATED_STATUS
        defaultClubShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where status not equals to DEFAULT_STATUS
        defaultClubShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the clubList where status not equals to UPDATED_STATUS
        defaultClubShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultClubShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the clubList where status equals to UPDATED_STATUS
        defaultClubShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where status is not null
        defaultClubShouldBeFound("status.specified=true");

        // Get all the clubList where status is null
        defaultClubShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllClubsByClubTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        ClubType clubType = club.getClubType();
        clubRepository.saveAndFlush(club);
        Long clubTypeId = clubType.getId();

        // Get all the clubList where clubType equals to clubTypeId
        defaultClubShouldBeFound("clubTypeId.equals=" + clubTypeId);

        // Get all the clubList where clubType equals to clubTypeId + 1
        defaultClubShouldNotBeFound("clubTypeId.equals=" + (clubTypeId + 1));
    }

    @Test
    @Transactional
    public void getAllClubsByClubCourtsIsEqualToSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);
        ClubCourt clubCourts = ClubCourtResourceIT.createEntity(em);
        em.persist(clubCourts);
        em.flush();
        club.addClubCourts(clubCourts);
        clubRepository.saveAndFlush(club);
        Long clubCourtsId = clubCourts.getId();

        // Get all the clubList where clubCourts equals to clubCourtsId
        defaultClubShouldBeFound("clubCourtsId.equals=" + clubCourtsId);

        // Get all the clubList where clubCourts equals to clubCourtsId + 1
        defaultClubShouldNotBeFound("clubCourtsId.equals=" + (clubCourtsId + 1));
    }

    @Test
    @Transactional
    public void getAllClubsByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);
        Organization organization = OrganizationResourceIT.createEntity(em);
        em.persist(organization);
        em.flush();
        club.setOrganization(organization);
        clubRepository.saveAndFlush(club);
        Long organizationId = organization.getId();

        // Get all the clubList where organization equals to organizationId
        defaultClubShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the clubList where organization equals to organizationId + 1
        defaultClubShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    @Test
    @Transactional
    public void getAllClubsByClubManagerIsEqualToSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);
        ClubManager clubManager = ClubManagerResourceIT.createEntity(em);
        em.persist(clubManager);
        em.flush();
        club.addClubManager(clubManager);
        clubRepository.saveAndFlush(club);
        Long clubManagerId = clubManager.getId();

        // Get all the clubList where clubManager equals to clubManagerId
        defaultClubShouldBeFound("clubManagerId.equals=" + clubManagerId);

        // Get all the clubList where clubManager equals to clubManagerId + 1
        defaultClubShouldNotBeFound("clubManagerId.equals=" + (clubManagerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClubShouldBeFound(String filter) throws Exception {
        restClubMockMvc
            .perform(get("/api/clubs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(club.getId().intValue())))
            .andExpect(jsonPath("$.[*].clubName").value(hasItem(DEFAULT_CLUB_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restClubMockMvc
            .perform(get("/api/clubs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClubShouldNotBeFound(String filter) throws Exception {
        restClubMockMvc
            .perform(get("/api/clubs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClubMockMvc
            .perform(get("/api/clubs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingClub() throws Exception {
        // Get the club
        restClubMockMvc.perform(get("/api/clubs/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        int databaseSizeBeforeUpdate = clubRepository.findAll().size();

        // Update the club
        Club updatedClub = clubRepository.findById(club.getId()).get();
        // Disconnect from session so that the updates on updatedClub are not directly saved in db
        em.detach(updatedClub);
        updatedClub.clubName(UPDATED_CLUB_NAME).status(UPDATED_STATUS);
        ClubDTO clubDTO = clubMapper.toDto(updatedClub);

        restClubMockMvc
            .perform(
                put("/api/clubs").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clubDTO))
            )
            .andExpect(status().isOk());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
        Club testClub = clubList.get(clubList.size() - 1);
        assertThat(testClub.getClubName()).isEqualTo(UPDATED_CLUB_NAME);
        assertThat(testClub.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingClub() throws Exception {
        int databaseSizeBeforeUpdate = clubRepository.findAll().size();

        // Create the Club
        ClubDTO clubDTO = clubMapper.toDto(club);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClubMockMvc
            .perform(
                put("/api/clubs").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clubDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        int databaseSizeBeforeDelete = clubRepository.findAll().size();

        // Delete the club
        restClubMockMvc
            .perform(delete("/api/clubs/{id}", club.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
