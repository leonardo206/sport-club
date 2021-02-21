package com.mazimao.sportclub.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mazimao.sportclub.SportClubApp;
import com.mazimao.sportclub.config.TestSecurityConfiguration;
import com.mazimao.sportclub.domain.Booking;
import com.mazimao.sportclub.domain.Club;
import com.mazimao.sportclub.domain.ClubCourt;
import com.mazimao.sportclub.domain.ClubCourtType;
import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import com.mazimao.sportclub.repository.ClubCourtRepository;
import com.mazimao.sportclub.service.ClubCourtQueryService;
import com.mazimao.sportclub.service.ClubCourtService;
import com.mazimao.sportclub.service.dto.ClubCourtCriteria;
import com.mazimao.sportclub.service.dto.ClubCourtDTO;
import com.mazimao.sportclub.service.mapper.ClubCourtMapper;
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
 * Integration tests for the {@link ClubCourtResource} REST controller.
 */
@SpringBootTest(classes = { SportClubApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ClubCourtResourceIT {
    private static final String DEFAULT_COURT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COURT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COURT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COURT_NAME = "BBBBBBBBBB";

    private static final ActiveInactiveStatus DEFAULT_STATUS = ActiveInactiveStatus.Active;
    private static final ActiveInactiveStatus UPDATED_STATUS = ActiveInactiveStatus.Inactive;

    @Autowired
    private ClubCourtRepository clubCourtRepository;

    @Autowired
    private ClubCourtMapper clubCourtMapper;

    @Autowired
    private ClubCourtService clubCourtService;

    @Autowired
    private ClubCourtQueryService clubCourtQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClubCourtMockMvc;

    private ClubCourt clubCourt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClubCourt createEntity(EntityManager em) {
        ClubCourt clubCourt = new ClubCourt().courtCode(DEFAULT_COURT_CODE).courtName(DEFAULT_COURT_NAME).status(DEFAULT_STATUS);
        // Add required entity
        ClubCourtType clubCourtType;
        if (TestUtil.findAll(em, ClubCourtType.class).isEmpty()) {
            clubCourtType = ClubCourtTypeResourceIT.createEntity(em);
            em.persist(clubCourtType);
            em.flush();
        } else {
            clubCourtType = TestUtil.findAll(em, ClubCourtType.class).get(0);
        }
        clubCourt.setClubCourtType(clubCourtType);
        return clubCourt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClubCourt createUpdatedEntity(EntityManager em) {
        ClubCourt clubCourt = new ClubCourt().courtCode(UPDATED_COURT_CODE).courtName(UPDATED_COURT_NAME).status(UPDATED_STATUS);
        // Add required entity
        ClubCourtType clubCourtType;
        if (TestUtil.findAll(em, ClubCourtType.class).isEmpty()) {
            clubCourtType = ClubCourtTypeResourceIT.createUpdatedEntity(em);
            em.persist(clubCourtType);
            em.flush();
        } else {
            clubCourtType = TestUtil.findAll(em, ClubCourtType.class).get(0);
        }
        clubCourt.setClubCourtType(clubCourtType);
        return clubCourt;
    }

    @BeforeEach
    public void initTest() {
        clubCourt = createEntity(em);
    }

    @Test
    @Transactional
    public void createClubCourt() throws Exception {
        int databaseSizeBeforeCreate = clubCourtRepository.findAll().size();
        // Create the ClubCourt
        ClubCourtDTO clubCourtDTO = clubCourtMapper.toDto(clubCourt);
        restClubCourtMockMvc
            .perform(
                post("/api/club-courts")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubCourtDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ClubCourt in the database
        List<ClubCourt> clubCourtList = clubCourtRepository.findAll();
        assertThat(clubCourtList).hasSize(databaseSizeBeforeCreate + 1);
        ClubCourt testClubCourt = clubCourtList.get(clubCourtList.size() - 1);
        assertThat(testClubCourt.getCourtCode()).isEqualTo(DEFAULT_COURT_CODE);
        assertThat(testClubCourt.getCourtName()).isEqualTo(DEFAULT_COURT_NAME);
        assertThat(testClubCourt.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createClubCourtWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clubCourtRepository.findAll().size();

        // Create the ClubCourt with an existing ID
        clubCourt.setId(1L);
        ClubCourtDTO clubCourtDTO = clubCourtMapper.toDto(clubCourt);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClubCourtMockMvc
            .perform(
                post("/api/club-courts")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubCourtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClubCourt in the database
        List<ClubCourt> clubCourtList = clubCourtRepository.findAll();
        assertThat(clubCourtList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCourtCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clubCourtRepository.findAll().size();
        // set the field null
        clubCourt.setCourtCode(null);

        // Create the ClubCourt, which fails.
        ClubCourtDTO clubCourtDTO = clubCourtMapper.toDto(clubCourt);

        restClubCourtMockMvc
            .perform(
                post("/api/club-courts")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubCourtDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClubCourt> clubCourtList = clubCourtRepository.findAll();
        assertThat(clubCourtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCourtNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clubCourtRepository.findAll().size();
        // set the field null
        clubCourt.setCourtName(null);

        // Create the ClubCourt, which fails.
        ClubCourtDTO clubCourtDTO = clubCourtMapper.toDto(clubCourt);

        restClubCourtMockMvc
            .perform(
                post("/api/club-courts")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubCourtDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClubCourt> clubCourtList = clubCourtRepository.findAll();
        assertThat(clubCourtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClubCourts() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList
        restClubCourtMockMvc
            .perform(get("/api/club-courts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clubCourt.getId().intValue())))
            .andExpect(jsonPath("$.[*].courtCode").value(hasItem(DEFAULT_COURT_CODE)))
            .andExpect(jsonPath("$.[*].courtName").value(hasItem(DEFAULT_COURT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getClubCourt() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get the clubCourt
        restClubCourtMockMvc
            .perform(get("/api/club-courts/{id}", clubCourt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clubCourt.getId().intValue()))
            .andExpect(jsonPath("$.courtCode").value(DEFAULT_COURT_CODE))
            .andExpect(jsonPath("$.courtName").value(DEFAULT_COURT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getClubCourtsByIdFiltering() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        Long id = clubCourt.getId();

        defaultClubCourtShouldBeFound("id.equals=" + id);
        defaultClubCourtShouldNotBeFound("id.notEquals=" + id);

        defaultClubCourtShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClubCourtShouldNotBeFound("id.greaterThan=" + id);

        defaultClubCourtShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClubCourtShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByCourtCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where courtCode equals to DEFAULT_COURT_CODE
        defaultClubCourtShouldBeFound("courtCode.equals=" + DEFAULT_COURT_CODE);

        // Get all the clubCourtList where courtCode equals to UPDATED_COURT_CODE
        defaultClubCourtShouldNotBeFound("courtCode.equals=" + UPDATED_COURT_CODE);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByCourtCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where courtCode not equals to DEFAULT_COURT_CODE
        defaultClubCourtShouldNotBeFound("courtCode.notEquals=" + DEFAULT_COURT_CODE);

        // Get all the clubCourtList where courtCode not equals to UPDATED_COURT_CODE
        defaultClubCourtShouldBeFound("courtCode.notEquals=" + UPDATED_COURT_CODE);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByCourtCodeIsInShouldWork() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where courtCode in DEFAULT_COURT_CODE or UPDATED_COURT_CODE
        defaultClubCourtShouldBeFound("courtCode.in=" + DEFAULT_COURT_CODE + "," + UPDATED_COURT_CODE);

        // Get all the clubCourtList where courtCode equals to UPDATED_COURT_CODE
        defaultClubCourtShouldNotBeFound("courtCode.in=" + UPDATED_COURT_CODE);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByCourtCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where courtCode is not null
        defaultClubCourtShouldBeFound("courtCode.specified=true");

        // Get all the clubCourtList where courtCode is null
        defaultClubCourtShouldNotBeFound("courtCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllClubCourtsByCourtCodeContainsSomething() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where courtCode contains DEFAULT_COURT_CODE
        defaultClubCourtShouldBeFound("courtCode.contains=" + DEFAULT_COURT_CODE);

        // Get all the clubCourtList where courtCode contains UPDATED_COURT_CODE
        defaultClubCourtShouldNotBeFound("courtCode.contains=" + UPDATED_COURT_CODE);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByCourtCodeNotContainsSomething() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where courtCode does not contain DEFAULT_COURT_CODE
        defaultClubCourtShouldNotBeFound("courtCode.doesNotContain=" + DEFAULT_COURT_CODE);

        // Get all the clubCourtList where courtCode does not contain UPDATED_COURT_CODE
        defaultClubCourtShouldBeFound("courtCode.doesNotContain=" + UPDATED_COURT_CODE);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByCourtNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where courtName equals to DEFAULT_COURT_NAME
        defaultClubCourtShouldBeFound("courtName.equals=" + DEFAULT_COURT_NAME);

        // Get all the clubCourtList where courtName equals to UPDATED_COURT_NAME
        defaultClubCourtShouldNotBeFound("courtName.equals=" + UPDATED_COURT_NAME);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByCourtNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where courtName not equals to DEFAULT_COURT_NAME
        defaultClubCourtShouldNotBeFound("courtName.notEquals=" + DEFAULT_COURT_NAME);

        // Get all the clubCourtList where courtName not equals to UPDATED_COURT_NAME
        defaultClubCourtShouldBeFound("courtName.notEquals=" + UPDATED_COURT_NAME);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByCourtNameIsInShouldWork() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where courtName in DEFAULT_COURT_NAME or UPDATED_COURT_NAME
        defaultClubCourtShouldBeFound("courtName.in=" + DEFAULT_COURT_NAME + "," + UPDATED_COURT_NAME);

        // Get all the clubCourtList where courtName equals to UPDATED_COURT_NAME
        defaultClubCourtShouldNotBeFound("courtName.in=" + UPDATED_COURT_NAME);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByCourtNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where courtName is not null
        defaultClubCourtShouldBeFound("courtName.specified=true");

        // Get all the clubCourtList where courtName is null
        defaultClubCourtShouldNotBeFound("courtName.specified=false");
    }

    @Test
    @Transactional
    public void getAllClubCourtsByCourtNameContainsSomething() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where courtName contains DEFAULT_COURT_NAME
        defaultClubCourtShouldBeFound("courtName.contains=" + DEFAULT_COURT_NAME);

        // Get all the clubCourtList where courtName contains UPDATED_COURT_NAME
        defaultClubCourtShouldNotBeFound("courtName.contains=" + UPDATED_COURT_NAME);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByCourtNameNotContainsSomething() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where courtName does not contain DEFAULT_COURT_NAME
        defaultClubCourtShouldNotBeFound("courtName.doesNotContain=" + DEFAULT_COURT_NAME);

        // Get all the clubCourtList where courtName does not contain UPDATED_COURT_NAME
        defaultClubCourtShouldBeFound("courtName.doesNotContain=" + UPDATED_COURT_NAME);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where status equals to DEFAULT_STATUS
        defaultClubCourtShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the clubCourtList where status equals to UPDATED_STATUS
        defaultClubCourtShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where status not equals to DEFAULT_STATUS
        defaultClubCourtShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the clubCourtList where status not equals to UPDATED_STATUS
        defaultClubCourtShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultClubCourtShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the clubCourtList where status equals to UPDATED_STATUS
        defaultClubCourtShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubCourtsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        // Get all the clubCourtList where status is not null
        defaultClubCourtShouldBeFound("status.specified=true");

        // Get all the clubCourtList where status is null
        defaultClubCourtShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllClubCourtsByClubCourtTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        ClubCourtType clubCourtType = clubCourt.getClubCourtType();
        clubCourtRepository.saveAndFlush(clubCourt);
        Long clubCourtTypeId = clubCourtType.getId();

        // Get all the clubCourtList where clubCourtType equals to clubCourtTypeId
        defaultClubCourtShouldBeFound("clubCourtTypeId.equals=" + clubCourtTypeId);

        // Get all the clubCourtList where clubCourtType equals to clubCourtTypeId + 1
        defaultClubCourtShouldNotBeFound("clubCourtTypeId.equals=" + (clubCourtTypeId + 1));
    }

    @Test
    @Transactional
    public void getAllClubCourtsByBookingsIsEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);
        Booking bookings = BookingResourceIT.createEntity(em);
        em.persist(bookings);
        em.flush();
        clubCourt.addBookings(bookings);
        clubCourtRepository.saveAndFlush(clubCourt);
        Long bookingsId = bookings.getId();

        // Get all the clubCourtList where bookings equals to bookingsId
        defaultClubCourtShouldBeFound("bookingsId.equals=" + bookingsId);

        // Get all the clubCourtList where bookings equals to bookingsId + 1
        defaultClubCourtShouldNotBeFound("bookingsId.equals=" + (bookingsId + 1));
    }

    @Test
    @Transactional
    public void getAllClubCourtsByClubIsEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);
        Club club = ClubResourceIT.createEntity(em);
        em.persist(club);
        em.flush();
        clubCourt.setClub(club);
        clubCourtRepository.saveAndFlush(clubCourt);
        Long clubId = club.getId();

        // Get all the clubCourtList where club equals to clubId
        defaultClubCourtShouldBeFound("clubId.equals=" + clubId);

        // Get all the clubCourtList where club equals to clubId + 1
        defaultClubCourtShouldNotBeFound("clubId.equals=" + (clubId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClubCourtShouldBeFound(String filter) throws Exception {
        restClubCourtMockMvc
            .perform(get("/api/club-courts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clubCourt.getId().intValue())))
            .andExpect(jsonPath("$.[*].courtCode").value(hasItem(DEFAULT_COURT_CODE)))
            .andExpect(jsonPath("$.[*].courtName").value(hasItem(DEFAULT_COURT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restClubCourtMockMvc
            .perform(get("/api/club-courts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClubCourtShouldNotBeFound(String filter) throws Exception {
        restClubCourtMockMvc
            .perform(get("/api/club-courts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClubCourtMockMvc
            .perform(get("/api/club-courts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingClubCourt() throws Exception {
        // Get the clubCourt
        restClubCourtMockMvc.perform(get("/api/club-courts/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClubCourt() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        int databaseSizeBeforeUpdate = clubCourtRepository.findAll().size();

        // Update the clubCourt
        ClubCourt updatedClubCourt = clubCourtRepository.findById(clubCourt.getId()).get();
        // Disconnect from session so that the updates on updatedClubCourt are not directly saved in db
        em.detach(updatedClubCourt);
        updatedClubCourt.courtCode(UPDATED_COURT_CODE).courtName(UPDATED_COURT_NAME).status(UPDATED_STATUS);
        ClubCourtDTO clubCourtDTO = clubCourtMapper.toDto(updatedClubCourt);

        restClubCourtMockMvc
            .perform(
                put("/api/club-courts")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubCourtDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClubCourt in the database
        List<ClubCourt> clubCourtList = clubCourtRepository.findAll();
        assertThat(clubCourtList).hasSize(databaseSizeBeforeUpdate);
        ClubCourt testClubCourt = clubCourtList.get(clubCourtList.size() - 1);
        assertThat(testClubCourt.getCourtCode()).isEqualTo(UPDATED_COURT_CODE);
        assertThat(testClubCourt.getCourtName()).isEqualTo(UPDATED_COURT_NAME);
        assertThat(testClubCourt.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingClubCourt() throws Exception {
        int databaseSizeBeforeUpdate = clubCourtRepository.findAll().size();

        // Create the ClubCourt
        ClubCourtDTO clubCourtDTO = clubCourtMapper.toDto(clubCourt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClubCourtMockMvc
            .perform(
                put("/api/club-courts")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubCourtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClubCourt in the database
        List<ClubCourt> clubCourtList = clubCourtRepository.findAll();
        assertThat(clubCourtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClubCourt() throws Exception {
        // Initialize the database
        clubCourtRepository.saveAndFlush(clubCourt);

        int databaseSizeBeforeDelete = clubCourtRepository.findAll().size();

        // Delete the clubCourt
        restClubCourtMockMvc
            .perform(delete("/api/club-courts/{id}", clubCourt.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClubCourt> clubCourtList = clubCourtRepository.findAll();
        assertThat(clubCourtList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
