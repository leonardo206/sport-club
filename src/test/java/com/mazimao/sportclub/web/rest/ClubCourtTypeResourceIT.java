package com.mazimao.sportclub.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mazimao.sportclub.SportClubApp;
import com.mazimao.sportclub.config.TestSecurityConfiguration;
import com.mazimao.sportclub.domain.ClubCourtType;
import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import com.mazimao.sportclub.repository.ClubCourtTypeRepository;
import com.mazimao.sportclub.service.ClubCourtTypeQueryService;
import com.mazimao.sportclub.service.ClubCourtTypeService;
import com.mazimao.sportclub.service.dto.ClubCourtTypeCriteria;
import com.mazimao.sportclub.service.dto.ClubCourtTypeDTO;
import com.mazimao.sportclub.service.mapper.ClubCourtTypeMapper;
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
 * Integration tests for the {@link ClubCourtTypeResource} REST controller.
 */
@SpringBootTest(classes = { SportClubApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ClubCourtTypeResourceIT {
    private static final String DEFAULT_CLUB_COURT_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CLUB_COURT_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CLUB_COURT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLUB_COURT_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLUB_COURT_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CLUB_COURT_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final ActiveInactiveStatus DEFAULT_STATUS = ActiveInactiveStatus.Active;
    private static final ActiveInactiveStatus UPDATED_STATUS = ActiveInactiveStatus.Inactive;

    @Autowired
    private ClubCourtTypeRepository clubCourtTypeRepository;

    @Autowired
    private ClubCourtTypeMapper clubCourtTypeMapper;

    @Autowired
    private ClubCourtTypeService clubCourtTypeService;

    @Autowired
    private ClubCourtTypeQueryService clubCourtTypeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClubCourtTypeMockMvc;

    private ClubCourtType clubCourtType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClubCourtType createEntity(EntityManager em) {
        ClubCourtType clubCourtType = new ClubCourtType()
            .clubCourtTypeCode(DEFAULT_CLUB_COURT_TYPE_CODE)
            .clubCourtTypeName(DEFAULT_CLUB_COURT_TYPE_NAME)
            .clubCourtTypeDescription(DEFAULT_CLUB_COURT_TYPE_DESCRIPTION)
            .status(DEFAULT_STATUS);
        return clubCourtType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClubCourtType createUpdatedEntity(EntityManager em) {
        ClubCourtType clubCourtType = new ClubCourtType()
            .clubCourtTypeCode(UPDATED_CLUB_COURT_TYPE_CODE)
            .clubCourtTypeName(UPDATED_CLUB_COURT_TYPE_NAME)
            .clubCourtTypeDescription(UPDATED_CLUB_COURT_TYPE_DESCRIPTION)
            .status(UPDATED_STATUS);
        return clubCourtType;
    }

    @BeforeEach
    public void initTest() {
        clubCourtType = createEntity(em);
    }

    @Test
    @Transactional
    public void createClubCourtType() throws Exception {
        int databaseSizeBeforeCreate = clubCourtTypeRepository.findAll().size();
        // Create the ClubCourtType
        ClubCourtTypeDTO clubCourtTypeDTO = clubCourtTypeMapper.toDto(clubCourtType);
        restClubCourtTypeMockMvc
            .perform(
                post("/api/club-court-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubCourtTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ClubCourtType in the database
        List<ClubCourtType> clubCourtTypeList = clubCourtTypeRepository.findAll();
        assertThat(clubCourtTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ClubCourtType testClubCourtType = clubCourtTypeList.get(clubCourtTypeList.size() - 1);
        assertThat(testClubCourtType.getClubCourtTypeCode()).isEqualTo(DEFAULT_CLUB_COURT_TYPE_CODE);
        assertThat(testClubCourtType.getClubCourtTypeName()).isEqualTo(DEFAULT_CLUB_COURT_TYPE_NAME);
        assertThat(testClubCourtType.getClubCourtTypeDescription()).isEqualTo(DEFAULT_CLUB_COURT_TYPE_DESCRIPTION);
        assertThat(testClubCourtType.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createClubCourtTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clubCourtTypeRepository.findAll().size();

        // Create the ClubCourtType with an existing ID
        clubCourtType.setId(1L);
        ClubCourtTypeDTO clubCourtTypeDTO = clubCourtTypeMapper.toDto(clubCourtType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClubCourtTypeMockMvc
            .perform(
                post("/api/club-court-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubCourtTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClubCourtType in the database
        List<ClubCourtType> clubCourtTypeList = clubCourtTypeRepository.findAll();
        assertThat(clubCourtTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkClubCourtTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clubCourtTypeRepository.findAll().size();
        // set the field null
        clubCourtType.setClubCourtTypeCode(null);

        // Create the ClubCourtType, which fails.
        ClubCourtTypeDTO clubCourtTypeDTO = clubCourtTypeMapper.toDto(clubCourtType);

        restClubCourtTypeMockMvc
            .perform(
                post("/api/club-court-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubCourtTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClubCourtType> clubCourtTypeList = clubCourtTypeRepository.findAll();
        assertThat(clubCourtTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClubCourtTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clubCourtTypeRepository.findAll().size();
        // set the field null
        clubCourtType.setClubCourtTypeName(null);

        // Create the ClubCourtType, which fails.
        ClubCourtTypeDTO clubCourtTypeDTO = clubCourtTypeMapper.toDto(clubCourtType);

        restClubCourtTypeMockMvc
            .perform(
                post("/api/club-court-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubCourtTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClubCourtType> clubCourtTypeList = clubCourtTypeRepository.findAll();
        assertThat(clubCourtTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypes() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList
        restClubCourtTypeMockMvc
            .perform(get("/api/club-court-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clubCourtType.getId().intValue())))
            .andExpect(jsonPath("$.[*].clubCourtTypeCode").value(hasItem(DEFAULT_CLUB_COURT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].clubCourtTypeName").value(hasItem(DEFAULT_CLUB_COURT_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].clubCourtTypeDescription").value(hasItem(DEFAULT_CLUB_COURT_TYPE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getClubCourtType() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get the clubCourtType
        restClubCourtTypeMockMvc
            .perform(get("/api/club-court-types/{id}", clubCourtType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clubCourtType.getId().intValue()))
            .andExpect(jsonPath("$.clubCourtTypeCode").value(DEFAULT_CLUB_COURT_TYPE_CODE))
            .andExpect(jsonPath("$.clubCourtTypeName").value(DEFAULT_CLUB_COURT_TYPE_NAME))
            .andExpect(jsonPath("$.clubCourtTypeDescription").value(DEFAULT_CLUB_COURT_TYPE_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getClubCourtTypesByIdFiltering() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        Long id = clubCourtType.getId();

        defaultClubCourtTypeShouldBeFound("id.equals=" + id);
        defaultClubCourtTypeShouldNotBeFound("id.notEquals=" + id);

        defaultClubCourtTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClubCourtTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultClubCourtTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClubCourtTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeCode equals to DEFAULT_CLUB_COURT_TYPE_CODE
        defaultClubCourtTypeShouldBeFound("clubCourtTypeCode.equals=" + DEFAULT_CLUB_COURT_TYPE_CODE);

        // Get all the clubCourtTypeList where clubCourtTypeCode equals to UPDATED_CLUB_COURT_TYPE_CODE
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeCode.equals=" + UPDATED_CLUB_COURT_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeCode not equals to DEFAULT_CLUB_COURT_TYPE_CODE
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeCode.notEquals=" + DEFAULT_CLUB_COURT_TYPE_CODE);

        // Get all the clubCourtTypeList where clubCourtTypeCode not equals to UPDATED_CLUB_COURT_TYPE_CODE
        defaultClubCourtTypeShouldBeFound("clubCourtTypeCode.notEquals=" + UPDATED_CLUB_COURT_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeCode in DEFAULT_CLUB_COURT_TYPE_CODE or UPDATED_CLUB_COURT_TYPE_CODE
        defaultClubCourtTypeShouldBeFound("clubCourtTypeCode.in=" + DEFAULT_CLUB_COURT_TYPE_CODE + "," + UPDATED_CLUB_COURT_TYPE_CODE);

        // Get all the clubCourtTypeList where clubCourtTypeCode equals to UPDATED_CLUB_COURT_TYPE_CODE
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeCode.in=" + UPDATED_CLUB_COURT_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeCode is not null
        defaultClubCourtTypeShouldBeFound("clubCourtTypeCode.specified=true");

        // Get all the clubCourtTypeList where clubCourtTypeCode is null
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeCode contains DEFAULT_CLUB_COURT_TYPE_CODE
        defaultClubCourtTypeShouldBeFound("clubCourtTypeCode.contains=" + DEFAULT_CLUB_COURT_TYPE_CODE);

        // Get all the clubCourtTypeList where clubCourtTypeCode contains UPDATED_CLUB_COURT_TYPE_CODE
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeCode.contains=" + UPDATED_CLUB_COURT_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeCode does not contain DEFAULT_CLUB_COURT_TYPE_CODE
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeCode.doesNotContain=" + DEFAULT_CLUB_COURT_TYPE_CODE);

        // Get all the clubCourtTypeList where clubCourtTypeCode does not contain UPDATED_CLUB_COURT_TYPE_CODE
        defaultClubCourtTypeShouldBeFound("clubCourtTypeCode.doesNotContain=" + UPDATED_CLUB_COURT_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeName equals to DEFAULT_CLUB_COURT_TYPE_NAME
        defaultClubCourtTypeShouldBeFound("clubCourtTypeName.equals=" + DEFAULT_CLUB_COURT_TYPE_NAME);

        // Get all the clubCourtTypeList where clubCourtTypeName equals to UPDATED_CLUB_COURT_TYPE_NAME
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeName.equals=" + UPDATED_CLUB_COURT_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeName not equals to DEFAULT_CLUB_COURT_TYPE_NAME
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeName.notEquals=" + DEFAULT_CLUB_COURT_TYPE_NAME);

        // Get all the clubCourtTypeList where clubCourtTypeName not equals to UPDATED_CLUB_COURT_TYPE_NAME
        defaultClubCourtTypeShouldBeFound("clubCourtTypeName.notEquals=" + UPDATED_CLUB_COURT_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeName in DEFAULT_CLUB_COURT_TYPE_NAME or UPDATED_CLUB_COURT_TYPE_NAME
        defaultClubCourtTypeShouldBeFound("clubCourtTypeName.in=" + DEFAULT_CLUB_COURT_TYPE_NAME + "," + UPDATED_CLUB_COURT_TYPE_NAME);

        // Get all the clubCourtTypeList where clubCourtTypeName equals to UPDATED_CLUB_COURT_TYPE_NAME
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeName.in=" + UPDATED_CLUB_COURT_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeName is not null
        defaultClubCourtTypeShouldBeFound("clubCourtTypeName.specified=true");

        // Get all the clubCourtTypeList where clubCourtTypeName is null
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeName.specified=false");
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeNameContainsSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeName contains DEFAULT_CLUB_COURT_TYPE_NAME
        defaultClubCourtTypeShouldBeFound("clubCourtTypeName.contains=" + DEFAULT_CLUB_COURT_TYPE_NAME);

        // Get all the clubCourtTypeList where clubCourtTypeName contains UPDATED_CLUB_COURT_TYPE_NAME
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeName.contains=" + UPDATED_CLUB_COURT_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeName does not contain DEFAULT_CLUB_COURT_TYPE_NAME
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeName.doesNotContain=" + DEFAULT_CLUB_COURT_TYPE_NAME);

        // Get all the clubCourtTypeList where clubCourtTypeName does not contain UPDATED_CLUB_COURT_TYPE_NAME
        defaultClubCourtTypeShouldBeFound("clubCourtTypeName.doesNotContain=" + UPDATED_CLUB_COURT_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeDescription equals to DEFAULT_CLUB_COURT_TYPE_DESCRIPTION
        defaultClubCourtTypeShouldBeFound("clubCourtTypeDescription.equals=" + DEFAULT_CLUB_COURT_TYPE_DESCRIPTION);

        // Get all the clubCourtTypeList where clubCourtTypeDescription equals to UPDATED_CLUB_COURT_TYPE_DESCRIPTION
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeDescription.equals=" + UPDATED_CLUB_COURT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeDescription not equals to DEFAULT_CLUB_COURT_TYPE_DESCRIPTION
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeDescription.notEquals=" + DEFAULT_CLUB_COURT_TYPE_DESCRIPTION);

        // Get all the clubCourtTypeList where clubCourtTypeDescription not equals to UPDATED_CLUB_COURT_TYPE_DESCRIPTION
        defaultClubCourtTypeShouldBeFound("clubCourtTypeDescription.notEquals=" + UPDATED_CLUB_COURT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeDescription in DEFAULT_CLUB_COURT_TYPE_DESCRIPTION or UPDATED_CLUB_COURT_TYPE_DESCRIPTION
        defaultClubCourtTypeShouldBeFound(
            "clubCourtTypeDescription.in=" + DEFAULT_CLUB_COURT_TYPE_DESCRIPTION + "," + UPDATED_CLUB_COURT_TYPE_DESCRIPTION
        );

        // Get all the clubCourtTypeList where clubCourtTypeDescription equals to UPDATED_CLUB_COURT_TYPE_DESCRIPTION
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeDescription.in=" + UPDATED_CLUB_COURT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeDescription is not null
        defaultClubCourtTypeShouldBeFound("clubCourtTypeDescription.specified=true");

        // Get all the clubCourtTypeList where clubCourtTypeDescription is null
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeDescriptionContainsSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeDescription contains DEFAULT_CLUB_COURT_TYPE_DESCRIPTION
        defaultClubCourtTypeShouldBeFound("clubCourtTypeDescription.contains=" + DEFAULT_CLUB_COURT_TYPE_DESCRIPTION);

        // Get all the clubCourtTypeList where clubCourtTypeDescription contains UPDATED_CLUB_COURT_TYPE_DESCRIPTION
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeDescription.contains=" + UPDATED_CLUB_COURT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByClubCourtTypeDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where clubCourtTypeDescription does not contain DEFAULT_CLUB_COURT_TYPE_DESCRIPTION
        defaultClubCourtTypeShouldNotBeFound("clubCourtTypeDescription.doesNotContain=" + DEFAULT_CLUB_COURT_TYPE_DESCRIPTION);

        // Get all the clubCourtTypeList where clubCourtTypeDescription does not contain UPDATED_CLUB_COURT_TYPE_DESCRIPTION
        defaultClubCourtTypeShouldBeFound("clubCourtTypeDescription.doesNotContain=" + UPDATED_CLUB_COURT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where status equals to DEFAULT_STATUS
        defaultClubCourtTypeShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the clubCourtTypeList where status equals to UPDATED_STATUS
        defaultClubCourtTypeShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where status not equals to DEFAULT_STATUS
        defaultClubCourtTypeShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the clubCourtTypeList where status not equals to UPDATED_STATUS
        defaultClubCourtTypeShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultClubCourtTypeShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the clubCourtTypeList where status equals to UPDATED_STATUS
        defaultClubCourtTypeShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubCourtTypesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        // Get all the clubCourtTypeList where status is not null
        defaultClubCourtTypeShouldBeFound("status.specified=true");

        // Get all the clubCourtTypeList where status is null
        defaultClubCourtTypeShouldNotBeFound("status.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClubCourtTypeShouldBeFound(String filter) throws Exception {
        restClubCourtTypeMockMvc
            .perform(get("/api/club-court-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clubCourtType.getId().intValue())))
            .andExpect(jsonPath("$.[*].clubCourtTypeCode").value(hasItem(DEFAULT_CLUB_COURT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].clubCourtTypeName").value(hasItem(DEFAULT_CLUB_COURT_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].clubCourtTypeDescription").value(hasItem(DEFAULT_CLUB_COURT_TYPE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restClubCourtTypeMockMvc
            .perform(get("/api/club-court-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClubCourtTypeShouldNotBeFound(String filter) throws Exception {
        restClubCourtTypeMockMvc
            .perform(get("/api/club-court-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClubCourtTypeMockMvc
            .perform(get("/api/club-court-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingClubCourtType() throws Exception {
        // Get the clubCourtType
        restClubCourtTypeMockMvc.perform(get("/api/club-court-types/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClubCourtType() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        int databaseSizeBeforeUpdate = clubCourtTypeRepository.findAll().size();

        // Update the clubCourtType
        ClubCourtType updatedClubCourtType = clubCourtTypeRepository.findById(clubCourtType.getId()).get();
        // Disconnect from session so that the updates on updatedClubCourtType are not directly saved in db
        em.detach(updatedClubCourtType);
        updatedClubCourtType
            .clubCourtTypeCode(UPDATED_CLUB_COURT_TYPE_CODE)
            .clubCourtTypeName(UPDATED_CLUB_COURT_TYPE_NAME)
            .clubCourtTypeDescription(UPDATED_CLUB_COURT_TYPE_DESCRIPTION)
            .status(UPDATED_STATUS);
        ClubCourtTypeDTO clubCourtTypeDTO = clubCourtTypeMapper.toDto(updatedClubCourtType);

        restClubCourtTypeMockMvc
            .perform(
                put("/api/club-court-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubCourtTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClubCourtType in the database
        List<ClubCourtType> clubCourtTypeList = clubCourtTypeRepository.findAll();
        assertThat(clubCourtTypeList).hasSize(databaseSizeBeforeUpdate);
        ClubCourtType testClubCourtType = clubCourtTypeList.get(clubCourtTypeList.size() - 1);
        assertThat(testClubCourtType.getClubCourtTypeCode()).isEqualTo(UPDATED_CLUB_COURT_TYPE_CODE);
        assertThat(testClubCourtType.getClubCourtTypeName()).isEqualTo(UPDATED_CLUB_COURT_TYPE_NAME);
        assertThat(testClubCourtType.getClubCourtTypeDescription()).isEqualTo(UPDATED_CLUB_COURT_TYPE_DESCRIPTION);
        assertThat(testClubCourtType.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingClubCourtType() throws Exception {
        int databaseSizeBeforeUpdate = clubCourtTypeRepository.findAll().size();

        // Create the ClubCourtType
        ClubCourtTypeDTO clubCourtTypeDTO = clubCourtTypeMapper.toDto(clubCourtType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClubCourtTypeMockMvc
            .perform(
                put("/api/club-court-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubCourtTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClubCourtType in the database
        List<ClubCourtType> clubCourtTypeList = clubCourtTypeRepository.findAll();
        assertThat(clubCourtTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClubCourtType() throws Exception {
        // Initialize the database
        clubCourtTypeRepository.saveAndFlush(clubCourtType);

        int databaseSizeBeforeDelete = clubCourtTypeRepository.findAll().size();

        // Delete the clubCourtType
        restClubCourtTypeMockMvc
            .perform(delete("/api/club-court-types/{id}", clubCourtType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClubCourtType> clubCourtTypeList = clubCourtTypeRepository.findAll();
        assertThat(clubCourtTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
