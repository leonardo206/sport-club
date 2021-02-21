package com.mazimao.sportclub.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mazimao.sportclub.SportClubApp;
import com.mazimao.sportclub.config.TestSecurityConfiguration;
import com.mazimao.sportclub.domain.ClubType;
import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import com.mazimao.sportclub.repository.ClubTypeRepository;
import com.mazimao.sportclub.service.ClubTypeQueryService;
import com.mazimao.sportclub.service.ClubTypeService;
import com.mazimao.sportclub.service.dto.ClubTypeCriteria;
import com.mazimao.sportclub.service.dto.ClubTypeDTO;
import com.mazimao.sportclub.service.mapper.ClubTypeMapper;
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
 * Integration tests for the {@link ClubTypeResource} REST controller.
 */
@SpringBootTest(classes = { SportClubApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ClubTypeResourceIT {
    private static final String DEFAULT_CLUB_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CLUB_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CLUB_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLUB_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLUB_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CLUB_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final ActiveInactiveStatus DEFAULT_STATUS = ActiveInactiveStatus.Active;
    private static final ActiveInactiveStatus UPDATED_STATUS = ActiveInactiveStatus.Inactive;

    @Autowired
    private ClubTypeRepository clubTypeRepository;

    @Autowired
    private ClubTypeMapper clubTypeMapper;

    @Autowired
    private ClubTypeService clubTypeService;

    @Autowired
    private ClubTypeQueryService clubTypeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClubTypeMockMvc;

    private ClubType clubType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClubType createEntity(EntityManager em) {
        ClubType clubType = new ClubType()
            .clubTypeCode(DEFAULT_CLUB_TYPE_CODE)
            .clubTypeName(DEFAULT_CLUB_TYPE_NAME)
            .clubTypeDescription(DEFAULT_CLUB_TYPE_DESCRIPTION)
            .status(DEFAULT_STATUS);
        return clubType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClubType createUpdatedEntity(EntityManager em) {
        ClubType clubType = new ClubType()
            .clubTypeCode(UPDATED_CLUB_TYPE_CODE)
            .clubTypeName(UPDATED_CLUB_TYPE_NAME)
            .clubTypeDescription(UPDATED_CLUB_TYPE_DESCRIPTION)
            .status(UPDATED_STATUS);
        return clubType;
    }

    @BeforeEach
    public void initTest() {
        clubType = createEntity(em);
    }

    @Test
    @Transactional
    public void createClubType() throws Exception {
        int databaseSizeBeforeCreate = clubTypeRepository.findAll().size();
        // Create the ClubType
        ClubTypeDTO clubTypeDTO = clubTypeMapper.toDto(clubType);
        restClubTypeMockMvc
            .perform(
                post("/api/club-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ClubType in the database
        List<ClubType> clubTypeList = clubTypeRepository.findAll();
        assertThat(clubTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ClubType testClubType = clubTypeList.get(clubTypeList.size() - 1);
        assertThat(testClubType.getClubTypeCode()).isEqualTo(DEFAULT_CLUB_TYPE_CODE);
        assertThat(testClubType.getClubTypeName()).isEqualTo(DEFAULT_CLUB_TYPE_NAME);
        assertThat(testClubType.getClubTypeDescription()).isEqualTo(DEFAULT_CLUB_TYPE_DESCRIPTION);
        assertThat(testClubType.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createClubTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clubTypeRepository.findAll().size();

        // Create the ClubType with an existing ID
        clubType.setId(1L);
        ClubTypeDTO clubTypeDTO = clubTypeMapper.toDto(clubType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClubTypeMockMvc
            .perform(
                post("/api/club-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClubType in the database
        List<ClubType> clubTypeList = clubTypeRepository.findAll();
        assertThat(clubTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkClubTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clubTypeRepository.findAll().size();
        // set the field null
        clubType.setClubTypeCode(null);

        // Create the ClubType, which fails.
        ClubTypeDTO clubTypeDTO = clubTypeMapper.toDto(clubType);

        restClubTypeMockMvc
            .perform(
                post("/api/club-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClubType> clubTypeList = clubTypeRepository.findAll();
        assertThat(clubTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClubTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clubTypeRepository.findAll().size();
        // set the field null
        clubType.setClubTypeName(null);

        // Create the ClubType, which fails.
        ClubTypeDTO clubTypeDTO = clubTypeMapper.toDto(clubType);

        restClubTypeMockMvc
            .perform(
                post("/api/club-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClubType> clubTypeList = clubTypeRepository.findAll();
        assertThat(clubTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClubTypeDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = clubTypeRepository.findAll().size();
        // set the field null
        clubType.setClubTypeDescription(null);

        // Create the ClubType, which fails.
        ClubTypeDTO clubTypeDTO = clubTypeMapper.toDto(clubType);

        restClubTypeMockMvc
            .perform(
                post("/api/club-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClubType> clubTypeList = clubTypeRepository.findAll();
        assertThat(clubTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClubTypes() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList
        restClubTypeMockMvc
            .perform(get("/api/club-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clubType.getId().intValue())))
            .andExpect(jsonPath("$.[*].clubTypeCode").value(hasItem(DEFAULT_CLUB_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].clubTypeName").value(hasItem(DEFAULT_CLUB_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].clubTypeDescription").value(hasItem(DEFAULT_CLUB_TYPE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getClubType() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get the clubType
        restClubTypeMockMvc
            .perform(get("/api/club-types/{id}", clubType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clubType.getId().intValue()))
            .andExpect(jsonPath("$.clubTypeCode").value(DEFAULT_CLUB_TYPE_CODE))
            .andExpect(jsonPath("$.clubTypeName").value(DEFAULT_CLUB_TYPE_NAME))
            .andExpect(jsonPath("$.clubTypeDescription").value(DEFAULT_CLUB_TYPE_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getClubTypesByIdFiltering() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        Long id = clubType.getId();

        defaultClubTypeShouldBeFound("id.equals=" + id);
        defaultClubTypeShouldNotBeFound("id.notEquals=" + id);

        defaultClubTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClubTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultClubTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClubTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeCode equals to DEFAULT_CLUB_TYPE_CODE
        defaultClubTypeShouldBeFound("clubTypeCode.equals=" + DEFAULT_CLUB_TYPE_CODE);

        // Get all the clubTypeList where clubTypeCode equals to UPDATED_CLUB_TYPE_CODE
        defaultClubTypeShouldNotBeFound("clubTypeCode.equals=" + UPDATED_CLUB_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeCode not equals to DEFAULT_CLUB_TYPE_CODE
        defaultClubTypeShouldNotBeFound("clubTypeCode.notEquals=" + DEFAULT_CLUB_TYPE_CODE);

        // Get all the clubTypeList where clubTypeCode not equals to UPDATED_CLUB_TYPE_CODE
        defaultClubTypeShouldBeFound("clubTypeCode.notEquals=" + UPDATED_CLUB_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeCode in DEFAULT_CLUB_TYPE_CODE or UPDATED_CLUB_TYPE_CODE
        defaultClubTypeShouldBeFound("clubTypeCode.in=" + DEFAULT_CLUB_TYPE_CODE + "," + UPDATED_CLUB_TYPE_CODE);

        // Get all the clubTypeList where clubTypeCode equals to UPDATED_CLUB_TYPE_CODE
        defaultClubTypeShouldNotBeFound("clubTypeCode.in=" + UPDATED_CLUB_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeCode is not null
        defaultClubTypeShouldBeFound("clubTypeCode.specified=true");

        // Get all the clubTypeList where clubTypeCode is null
        defaultClubTypeShouldNotBeFound("clubTypeCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeCode contains DEFAULT_CLUB_TYPE_CODE
        defaultClubTypeShouldBeFound("clubTypeCode.contains=" + DEFAULT_CLUB_TYPE_CODE);

        // Get all the clubTypeList where clubTypeCode contains UPDATED_CLUB_TYPE_CODE
        defaultClubTypeShouldNotBeFound("clubTypeCode.contains=" + UPDATED_CLUB_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeCode does not contain DEFAULT_CLUB_TYPE_CODE
        defaultClubTypeShouldNotBeFound("clubTypeCode.doesNotContain=" + DEFAULT_CLUB_TYPE_CODE);

        // Get all the clubTypeList where clubTypeCode does not contain UPDATED_CLUB_TYPE_CODE
        defaultClubTypeShouldBeFound("clubTypeCode.doesNotContain=" + UPDATED_CLUB_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeName equals to DEFAULT_CLUB_TYPE_NAME
        defaultClubTypeShouldBeFound("clubTypeName.equals=" + DEFAULT_CLUB_TYPE_NAME);

        // Get all the clubTypeList where clubTypeName equals to UPDATED_CLUB_TYPE_NAME
        defaultClubTypeShouldNotBeFound("clubTypeName.equals=" + UPDATED_CLUB_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeName not equals to DEFAULT_CLUB_TYPE_NAME
        defaultClubTypeShouldNotBeFound("clubTypeName.notEquals=" + DEFAULT_CLUB_TYPE_NAME);

        // Get all the clubTypeList where clubTypeName not equals to UPDATED_CLUB_TYPE_NAME
        defaultClubTypeShouldBeFound("clubTypeName.notEquals=" + UPDATED_CLUB_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeName in DEFAULT_CLUB_TYPE_NAME or UPDATED_CLUB_TYPE_NAME
        defaultClubTypeShouldBeFound("clubTypeName.in=" + DEFAULT_CLUB_TYPE_NAME + "," + UPDATED_CLUB_TYPE_NAME);

        // Get all the clubTypeList where clubTypeName equals to UPDATED_CLUB_TYPE_NAME
        defaultClubTypeShouldNotBeFound("clubTypeName.in=" + UPDATED_CLUB_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeName is not null
        defaultClubTypeShouldBeFound("clubTypeName.specified=true");

        // Get all the clubTypeList where clubTypeName is null
        defaultClubTypeShouldNotBeFound("clubTypeName.specified=false");
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeNameContainsSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeName contains DEFAULT_CLUB_TYPE_NAME
        defaultClubTypeShouldBeFound("clubTypeName.contains=" + DEFAULT_CLUB_TYPE_NAME);

        // Get all the clubTypeList where clubTypeName contains UPDATED_CLUB_TYPE_NAME
        defaultClubTypeShouldNotBeFound("clubTypeName.contains=" + UPDATED_CLUB_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeName does not contain DEFAULT_CLUB_TYPE_NAME
        defaultClubTypeShouldNotBeFound("clubTypeName.doesNotContain=" + DEFAULT_CLUB_TYPE_NAME);

        // Get all the clubTypeList where clubTypeName does not contain UPDATED_CLUB_TYPE_NAME
        defaultClubTypeShouldBeFound("clubTypeName.doesNotContain=" + UPDATED_CLUB_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeDescription equals to DEFAULT_CLUB_TYPE_DESCRIPTION
        defaultClubTypeShouldBeFound("clubTypeDescription.equals=" + DEFAULT_CLUB_TYPE_DESCRIPTION);

        // Get all the clubTypeList where clubTypeDescription equals to UPDATED_CLUB_TYPE_DESCRIPTION
        defaultClubTypeShouldNotBeFound("clubTypeDescription.equals=" + UPDATED_CLUB_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeDescription not equals to DEFAULT_CLUB_TYPE_DESCRIPTION
        defaultClubTypeShouldNotBeFound("clubTypeDescription.notEquals=" + DEFAULT_CLUB_TYPE_DESCRIPTION);

        // Get all the clubTypeList where clubTypeDescription not equals to UPDATED_CLUB_TYPE_DESCRIPTION
        defaultClubTypeShouldBeFound("clubTypeDescription.notEquals=" + UPDATED_CLUB_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeDescription in DEFAULT_CLUB_TYPE_DESCRIPTION or UPDATED_CLUB_TYPE_DESCRIPTION
        defaultClubTypeShouldBeFound("clubTypeDescription.in=" + DEFAULT_CLUB_TYPE_DESCRIPTION + "," + UPDATED_CLUB_TYPE_DESCRIPTION);

        // Get all the clubTypeList where clubTypeDescription equals to UPDATED_CLUB_TYPE_DESCRIPTION
        defaultClubTypeShouldNotBeFound("clubTypeDescription.in=" + UPDATED_CLUB_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeDescription is not null
        defaultClubTypeShouldBeFound("clubTypeDescription.specified=true");

        // Get all the clubTypeList where clubTypeDescription is null
        defaultClubTypeShouldNotBeFound("clubTypeDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeDescriptionContainsSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeDescription contains DEFAULT_CLUB_TYPE_DESCRIPTION
        defaultClubTypeShouldBeFound("clubTypeDescription.contains=" + DEFAULT_CLUB_TYPE_DESCRIPTION);

        // Get all the clubTypeList where clubTypeDescription contains UPDATED_CLUB_TYPE_DESCRIPTION
        defaultClubTypeShouldNotBeFound("clubTypeDescription.contains=" + UPDATED_CLUB_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllClubTypesByClubTypeDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where clubTypeDescription does not contain DEFAULT_CLUB_TYPE_DESCRIPTION
        defaultClubTypeShouldNotBeFound("clubTypeDescription.doesNotContain=" + DEFAULT_CLUB_TYPE_DESCRIPTION);

        // Get all the clubTypeList where clubTypeDescription does not contain UPDATED_CLUB_TYPE_DESCRIPTION
        defaultClubTypeShouldBeFound("clubTypeDescription.doesNotContain=" + UPDATED_CLUB_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllClubTypesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where status equals to DEFAULT_STATUS
        defaultClubTypeShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the clubTypeList where status equals to UPDATED_STATUS
        defaultClubTypeShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubTypesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where status not equals to DEFAULT_STATUS
        defaultClubTypeShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the clubTypeList where status not equals to UPDATED_STATUS
        defaultClubTypeShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubTypesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultClubTypeShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the clubTypeList where status equals to UPDATED_STATUS
        defaultClubTypeShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubTypesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        // Get all the clubTypeList where status is not null
        defaultClubTypeShouldBeFound("status.specified=true");

        // Get all the clubTypeList where status is null
        defaultClubTypeShouldNotBeFound("status.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClubTypeShouldBeFound(String filter) throws Exception {
        restClubTypeMockMvc
            .perform(get("/api/club-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clubType.getId().intValue())))
            .andExpect(jsonPath("$.[*].clubTypeCode").value(hasItem(DEFAULT_CLUB_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].clubTypeName").value(hasItem(DEFAULT_CLUB_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].clubTypeDescription").value(hasItem(DEFAULT_CLUB_TYPE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restClubTypeMockMvc
            .perform(get("/api/club-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClubTypeShouldNotBeFound(String filter) throws Exception {
        restClubTypeMockMvc
            .perform(get("/api/club-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClubTypeMockMvc
            .perform(get("/api/club-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingClubType() throws Exception {
        // Get the clubType
        restClubTypeMockMvc.perform(get("/api/club-types/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClubType() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        int databaseSizeBeforeUpdate = clubTypeRepository.findAll().size();

        // Update the clubType
        ClubType updatedClubType = clubTypeRepository.findById(clubType.getId()).get();
        // Disconnect from session so that the updates on updatedClubType are not directly saved in db
        em.detach(updatedClubType);
        updatedClubType
            .clubTypeCode(UPDATED_CLUB_TYPE_CODE)
            .clubTypeName(UPDATED_CLUB_TYPE_NAME)
            .clubTypeDescription(UPDATED_CLUB_TYPE_DESCRIPTION)
            .status(UPDATED_STATUS);
        ClubTypeDTO clubTypeDTO = clubTypeMapper.toDto(updatedClubType);

        restClubTypeMockMvc
            .perform(
                put("/api/club-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClubType in the database
        List<ClubType> clubTypeList = clubTypeRepository.findAll();
        assertThat(clubTypeList).hasSize(databaseSizeBeforeUpdate);
        ClubType testClubType = clubTypeList.get(clubTypeList.size() - 1);
        assertThat(testClubType.getClubTypeCode()).isEqualTo(UPDATED_CLUB_TYPE_CODE);
        assertThat(testClubType.getClubTypeName()).isEqualTo(UPDATED_CLUB_TYPE_NAME);
        assertThat(testClubType.getClubTypeDescription()).isEqualTo(UPDATED_CLUB_TYPE_DESCRIPTION);
        assertThat(testClubType.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingClubType() throws Exception {
        int databaseSizeBeforeUpdate = clubTypeRepository.findAll().size();

        // Create the ClubType
        ClubTypeDTO clubTypeDTO = clubTypeMapper.toDto(clubType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClubTypeMockMvc
            .perform(
                put("/api/club-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClubType in the database
        List<ClubType> clubTypeList = clubTypeRepository.findAll();
        assertThat(clubTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClubType() throws Exception {
        // Initialize the database
        clubTypeRepository.saveAndFlush(clubType);

        int databaseSizeBeforeDelete = clubTypeRepository.findAll().size();

        // Delete the clubType
        restClubTypeMockMvc
            .perform(delete("/api/club-types/{id}", clubType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClubType> clubTypeList = clubTypeRepository.findAll();
        assertThat(clubTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
