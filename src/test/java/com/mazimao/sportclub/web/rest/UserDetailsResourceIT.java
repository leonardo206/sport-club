package com.mazimao.sportclub.web.rest;

import static com.mazimao.sportclub.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mazimao.sportclub.SportClubApp;
import com.mazimao.sportclub.config.TestSecurityConfiguration;
import com.mazimao.sportclub.domain.User;
import com.mazimao.sportclub.domain.UserDetails;
import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import com.mazimao.sportclub.repository.UserDetailsRepository;
import com.mazimao.sportclub.service.UserDetailsQueryService;
import com.mazimao.sportclub.service.UserDetailsService;
import com.mazimao.sportclub.service.dto.UserDetailsCriteria;
import com.mazimao.sportclub.service.dto.UserDetailsDTO;
import com.mazimao.sportclub.service.mapper.UserDetailsMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link UserDetailsResource} REST controller.
 */
@SpringBootTest(classes = { SportClubApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class UserDetailsResourceIT {
    private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_OF_BIRTH = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_OF_BIRTH = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE_OF_BIRTH = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final byte[] DEFAULT_PROFILE_PIC = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROFILE_PIC = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROFILE_PIC_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROFILE_PIC_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ActiveInactiveStatus DEFAULT_STATUS = ActiveInactiveStatus.Active;
    private static final ActiveInactiveStatus UPDATED_STATUS = ActiveInactiveStatus.Inactive;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserDetailsQueryService userDetailsQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserDetailsMockMvc;

    private UserDetails userDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDetails createEntity(EntityManager em) {
        UserDetails userDetails = new UserDetails()
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .profilePic(DEFAULT_PROFILE_PIC)
            .profilePicContentType(DEFAULT_PROFILE_PIC_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userDetails.setUser(user);
        return userDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDetails createUpdatedEntity(EntityManager em) {
        UserDetails userDetails = new UserDetails()
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .profilePic(UPDATED_PROFILE_PIC)
            .profilePicContentType(UPDATED_PROFILE_PIC_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userDetails.setUser(user);
        return userDetails;
    }

    @BeforeEach
    public void initTest() {
        userDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserDetails() throws Exception {
        int databaseSizeBeforeCreate = userDetailsRepository.findAll().size();
        // Create the UserDetails
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(userDetails);
        restUserDetailsMockMvc
            .perform(
                post("/api/user-details")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testUserDetails.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testUserDetails.getProfilePic()).isEqualTo(DEFAULT_PROFILE_PIC);
        assertThat(testUserDetails.getProfilePicContentType()).isEqualTo(DEFAULT_PROFILE_PIC_CONTENT_TYPE);
        assertThat(testUserDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUserDetails.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createUserDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userDetailsRepository.findAll().size();

        // Create the UserDetails with an existing ID
        userDetails.setId(1L);
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(userDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDetailsMockMvc
            .perform(
                post("/api/user-details")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMobileNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = userDetailsRepository.findAll().size();
        // set the field null
        userDetails.setMobileNumber(null);

        // Create the UserDetails, which fails.
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(userDetails);

        restUserDetailsMockMvc
            .perform(
                post("/api/user-details")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = userDetailsRepository.findAll().size();
        // set the field null
        userDetails.setDateOfBirth(null);

        // Create the UserDetails, which fails.
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(userDetails);

        restUserDetailsMockMvc
            .perform(
                post("/api/user-details")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList
        restUserDetailsMockMvc
            .perform(get("/api/user-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(sameInstant(DEFAULT_DATE_OF_BIRTH))))
            .andExpect(jsonPath("$.[*].profilePicContentType").value(hasItem(DEFAULT_PROFILE_PIC_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profilePic").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFILE_PIC))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get the userDetails
        restUserDetailsMockMvc
            .perform(get("/api/user-details/{id}", userDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userDetails.getId().intValue()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER))
            .andExpect(jsonPath("$.dateOfBirth").value(sameInstant(DEFAULT_DATE_OF_BIRTH)))
            .andExpect(jsonPath("$.profilePicContentType").value(DEFAULT_PROFILE_PIC_CONTENT_TYPE))
            .andExpect(jsonPath("$.profilePic").value(Base64Utils.encodeToString(DEFAULT_PROFILE_PIC)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getUserDetailsByIdFiltering() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        Long id = userDetails.getId();

        defaultUserDetailsShouldBeFound("id.equals=" + id);
        defaultUserDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultUserDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultUserDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByMobileNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber equals to DEFAULT_MOBILE_NUMBER
        defaultUserDetailsShouldBeFound("mobileNumber.equals=" + DEFAULT_MOBILE_NUMBER);

        // Get all the userDetailsList where mobileNumber equals to UPDATED_MOBILE_NUMBER
        defaultUserDetailsShouldNotBeFound("mobileNumber.equals=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByMobileNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber not equals to DEFAULT_MOBILE_NUMBER
        defaultUserDetailsShouldNotBeFound("mobileNumber.notEquals=" + DEFAULT_MOBILE_NUMBER);

        // Get all the userDetailsList where mobileNumber not equals to UPDATED_MOBILE_NUMBER
        defaultUserDetailsShouldBeFound("mobileNumber.notEquals=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByMobileNumberIsInShouldWork() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber in DEFAULT_MOBILE_NUMBER or UPDATED_MOBILE_NUMBER
        defaultUserDetailsShouldBeFound("mobileNumber.in=" + DEFAULT_MOBILE_NUMBER + "," + UPDATED_MOBILE_NUMBER);

        // Get all the userDetailsList where mobileNumber equals to UPDATED_MOBILE_NUMBER
        defaultUserDetailsShouldNotBeFound("mobileNumber.in=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByMobileNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber is not null
        defaultUserDetailsShouldBeFound("mobileNumber.specified=true");

        // Get all the userDetailsList where mobileNumber is null
        defaultUserDetailsShouldNotBeFound("mobileNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserDetailsByMobileNumberContainsSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber contains DEFAULT_MOBILE_NUMBER
        defaultUserDetailsShouldBeFound("mobileNumber.contains=" + DEFAULT_MOBILE_NUMBER);

        // Get all the userDetailsList where mobileNumber contains UPDATED_MOBILE_NUMBER
        defaultUserDetailsShouldNotBeFound("mobileNumber.contains=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByMobileNumberNotContainsSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber does not contain DEFAULT_MOBILE_NUMBER
        defaultUserDetailsShouldNotBeFound("mobileNumber.doesNotContain=" + DEFAULT_MOBILE_NUMBER);

        // Get all the userDetailsList where mobileNumber does not contain UPDATED_MOBILE_NUMBER
        defaultUserDetailsShouldBeFound("mobileNumber.doesNotContain=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultUserDetailsShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the userDetailsList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultUserDetailsShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDateOfBirthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where dateOfBirth not equals to DEFAULT_DATE_OF_BIRTH
        defaultUserDetailsShouldNotBeFound("dateOfBirth.notEquals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the userDetailsList where dateOfBirth not equals to UPDATED_DATE_OF_BIRTH
        defaultUserDetailsShouldBeFound("dateOfBirth.notEquals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultUserDetailsShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the userDetailsList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultUserDetailsShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where dateOfBirth is not null
        defaultUserDetailsShouldBeFound("dateOfBirth.specified=true");

        // Get all the userDetailsList where dateOfBirth is null
        defaultUserDetailsShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where dateOfBirth is greater than or equal to DEFAULT_DATE_OF_BIRTH
        defaultUserDetailsShouldBeFound("dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the userDetailsList where dateOfBirth is greater than or equal to UPDATED_DATE_OF_BIRTH
        defaultUserDetailsShouldNotBeFound("dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where dateOfBirth is less than or equal to DEFAULT_DATE_OF_BIRTH
        defaultUserDetailsShouldBeFound("dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the userDetailsList where dateOfBirth is less than or equal to SMALLER_DATE_OF_BIRTH
        defaultUserDetailsShouldNotBeFound("dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where dateOfBirth is less than DEFAULT_DATE_OF_BIRTH
        defaultUserDetailsShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the userDetailsList where dateOfBirth is less than UPDATED_DATE_OF_BIRTH
        defaultUserDetailsShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where dateOfBirth is greater than DEFAULT_DATE_OF_BIRTH
        defaultUserDetailsShouldNotBeFound("dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the userDetailsList where dateOfBirth is greater than SMALLER_DATE_OF_BIRTH
        defaultUserDetailsShouldBeFound("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where description equals to DEFAULT_DESCRIPTION
        defaultUserDetailsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the userDetailsList where description equals to UPDATED_DESCRIPTION
        defaultUserDetailsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where description not equals to DEFAULT_DESCRIPTION
        defaultUserDetailsShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the userDetailsList where description not equals to UPDATED_DESCRIPTION
        defaultUserDetailsShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultUserDetailsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the userDetailsList where description equals to UPDATED_DESCRIPTION
        defaultUserDetailsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where description is not null
        defaultUserDetailsShouldBeFound("description.specified=true");

        // Get all the userDetailsList where description is null
        defaultUserDetailsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where description contains DEFAULT_DESCRIPTION
        defaultUserDetailsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the userDetailsList where description contains UPDATED_DESCRIPTION
        defaultUserDetailsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where description does not contain DEFAULT_DESCRIPTION
        defaultUserDetailsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the userDetailsList where description does not contain UPDATED_DESCRIPTION
        defaultUserDetailsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where status equals to DEFAULT_STATUS
        defaultUserDetailsShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the userDetailsList where status equals to UPDATED_STATUS
        defaultUserDetailsShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where status not equals to DEFAULT_STATUS
        defaultUserDetailsShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the userDetailsList where status not equals to UPDATED_STATUS
        defaultUserDetailsShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultUserDetailsShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the userDetailsList where status equals to UPDATED_STATUS
        defaultUserDetailsShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllUserDetailsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where status is not null
        defaultUserDetailsShouldBeFound("status.specified=true");

        // Get all the userDetailsList where status is null
        defaultUserDetailsShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserDetailsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = userDetails.getUser();
        userDetailsRepository.saveAndFlush(userDetails);
        String userId = user.getId();

        // Get all the userDetailsList where user equals to userId
        defaultUserDetailsShouldBeFound("userId.equals=" + userId);

        // Get all the userDetailsList where user equals to userId + 1
        defaultUserDetailsShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserDetailsShouldBeFound(String filter) throws Exception {
        restUserDetailsMockMvc
            .perform(get("/api/user-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(sameInstant(DEFAULT_DATE_OF_BIRTH))))
            .andExpect(jsonPath("$.[*].profilePicContentType").value(hasItem(DEFAULT_PROFILE_PIC_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profilePic").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFILE_PIC))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restUserDetailsMockMvc
            .perform(get("/api/user-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserDetailsShouldNotBeFound(String filter) throws Exception {
        restUserDetailsMockMvc
            .perform(get("/api/user-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserDetailsMockMvc
            .perform(get("/api/user-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingUserDetails() throws Exception {
        // Get the userDetails
        restUserDetailsMockMvc.perform(get("/api/user-details/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();

        // Update the userDetails
        UserDetails updatedUserDetails = userDetailsRepository.findById(userDetails.getId()).get();
        // Disconnect from session so that the updates on updatedUserDetails are not directly saved in db
        em.detach(updatedUserDetails);
        updatedUserDetails
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .profilePic(UPDATED_PROFILE_PIC)
            .profilePicContentType(UPDATED_PROFILE_PIC_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS);
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(updatedUserDetails);

        restUserDetailsMockMvc
            .perform(
                put("/api/user-details")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testUserDetails.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testUserDetails.getProfilePic()).isEqualTo(UPDATED_PROFILE_PIC);
        assertThat(testUserDetails.getProfilePicContentType()).isEqualTo(UPDATED_PROFILE_PIC_CONTENT_TYPE);
        assertThat(testUserDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserDetails.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();

        // Create the UserDetails
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(userDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                put("/api/user-details")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        int databaseSizeBeforeDelete = userDetailsRepository.findAll().size();

        // Delete the userDetails
        restUserDetailsMockMvc
            .perform(delete("/api/user-details/{id}", userDetails.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
