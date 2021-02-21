package com.mazimao.sportclub.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
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
import com.mazimao.sportclub.repository.ClubManagerRepository;
import com.mazimao.sportclub.service.ClubManagerQueryService;
import com.mazimao.sportclub.service.ClubManagerService;
import com.mazimao.sportclub.service.dto.ClubManagerCriteria;
import com.mazimao.sportclub.service.dto.ClubManagerDTO;
import com.mazimao.sportclub.service.mapper.ClubManagerMapper;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ClubManagerResource} REST controller.
 */
@SpringBootTest(classes = { SportClubApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ClubManagerResourceIT {
    private static final ActiveInactiveStatus DEFAULT_STATUS = ActiveInactiveStatus.Active;
    private static final ActiveInactiveStatus UPDATED_STATUS = ActiveInactiveStatus.Inactive;

    @Autowired
    private ClubManagerRepository clubManagerRepository;

    @Mock
    private ClubManagerRepository clubManagerRepositoryMock;

    @Autowired
    private ClubManagerMapper clubManagerMapper;

    @Mock
    private ClubManagerService clubManagerServiceMock;

    @Autowired
    private ClubManagerService clubManagerService;

    @Autowired
    private ClubManagerQueryService clubManagerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClubManagerMockMvc;

    private ClubManager clubManager;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClubManager createEntity(EntityManager em) {
        ClubManager clubManager = new ClubManager().status(DEFAULT_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        clubManager.setUser(user);
        return clubManager;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClubManager createUpdatedEntity(EntityManager em) {
        ClubManager clubManager = new ClubManager().status(UPDATED_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        clubManager.setUser(user);
        return clubManager;
    }

    @BeforeEach
    public void initTest() {
        clubManager = createEntity(em);
    }

    @Test
    @Transactional
    public void createClubManager() throws Exception {
        int databaseSizeBeforeCreate = clubManagerRepository.findAll().size();
        // Create the ClubManager
        ClubManagerDTO clubManagerDTO = clubManagerMapper.toDto(clubManager);
        restClubManagerMockMvc
            .perform(
                post("/api/club-managers")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubManagerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ClubManager in the database
        List<ClubManager> clubManagerList = clubManagerRepository.findAll();
        assertThat(clubManagerList).hasSize(databaseSizeBeforeCreate + 1);
        ClubManager testClubManager = clubManagerList.get(clubManagerList.size() - 1);
        assertThat(testClubManager.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createClubManagerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clubManagerRepository.findAll().size();

        // Create the ClubManager with an existing ID
        clubManager.setId(1L);
        ClubManagerDTO clubManagerDTO = clubManagerMapper.toDto(clubManager);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClubManagerMockMvc
            .perform(
                post("/api/club-managers")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubManagerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClubManager in the database
        List<ClubManager> clubManagerList = clubManagerRepository.findAll();
        assertThat(clubManagerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllClubManagers() throws Exception {
        // Initialize the database
        clubManagerRepository.saveAndFlush(clubManager);

        // Get all the clubManagerList
        restClubManagerMockMvc
            .perform(get("/api/club-managers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clubManager.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    public void getAllClubManagersWithEagerRelationshipsIsEnabled() throws Exception {
        when(clubManagerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClubManagerMockMvc.perform(get("/api/club-managers?eagerload=true")).andExpect(status().isOk());

        verify(clubManagerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    public void getAllClubManagersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(clubManagerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClubManagerMockMvc.perform(get("/api/club-managers?eagerload=true")).andExpect(status().isOk());

        verify(clubManagerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getClubManager() throws Exception {
        // Initialize the database
        clubManagerRepository.saveAndFlush(clubManager);

        // Get the clubManager
        restClubManagerMockMvc
            .perform(get("/api/club-managers/{id}", clubManager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clubManager.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getClubManagersByIdFiltering() throws Exception {
        // Initialize the database
        clubManagerRepository.saveAndFlush(clubManager);

        Long id = clubManager.getId();

        defaultClubManagerShouldBeFound("id.equals=" + id);
        defaultClubManagerShouldNotBeFound("id.notEquals=" + id);

        defaultClubManagerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClubManagerShouldNotBeFound("id.greaterThan=" + id);

        defaultClubManagerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClubManagerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllClubManagersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        clubManagerRepository.saveAndFlush(clubManager);

        // Get all the clubManagerList where status equals to DEFAULT_STATUS
        defaultClubManagerShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the clubManagerList where status equals to UPDATED_STATUS
        defaultClubManagerShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubManagersByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubManagerRepository.saveAndFlush(clubManager);

        // Get all the clubManagerList where status not equals to DEFAULT_STATUS
        defaultClubManagerShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the clubManagerList where status not equals to UPDATED_STATUS
        defaultClubManagerShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubManagersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        clubManagerRepository.saveAndFlush(clubManager);

        // Get all the clubManagerList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultClubManagerShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the clubManagerList where status equals to UPDATED_STATUS
        defaultClubManagerShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClubManagersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubManagerRepository.saveAndFlush(clubManager);

        // Get all the clubManagerList where status is not null
        defaultClubManagerShouldBeFound("status.specified=true");

        // Get all the clubManagerList where status is null
        defaultClubManagerShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllClubManagersByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = clubManager.getUser();
        clubManagerRepository.saveAndFlush(clubManager);
        String userId = user.getId();

        // Get all the clubManagerList where user equals to userId
        defaultClubManagerShouldBeFound("userId.equals=" + userId);

        // Get all the clubManagerList where user equals to userId + 1
        defaultClubManagerShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    public void getAllClubManagersByClubsIsEqualToSomething() throws Exception {
        // Initialize the database
        clubManagerRepository.saveAndFlush(clubManager);
        Club clubs = ClubResourceIT.createEntity(em);
        em.persist(clubs);
        em.flush();
        clubManager.addClubs(clubs);
        clubManagerRepository.saveAndFlush(clubManager);
        Long clubsId = clubs.getId();

        // Get all the clubManagerList where clubs equals to clubsId
        defaultClubManagerShouldBeFound("clubsId.equals=" + clubsId);

        // Get all the clubManagerList where clubs equals to clubsId + 1
        defaultClubManagerShouldNotBeFound("clubsId.equals=" + (clubsId + 1));
    }

    @Test
    @Transactional
    public void getAllClubManagersByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        clubManagerRepository.saveAndFlush(clubManager);
        Organization organization = OrganizationResourceIT.createEntity(em);
        em.persist(organization);
        em.flush();
        clubManager.setOrganization(organization);
        clubManagerRepository.saveAndFlush(clubManager);
        Long organizationId = organization.getId();

        // Get all the clubManagerList where organization equals to organizationId
        defaultClubManagerShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the clubManagerList where organization equals to organizationId + 1
        defaultClubManagerShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClubManagerShouldBeFound(String filter) throws Exception {
        restClubManagerMockMvc
            .perform(get("/api/club-managers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clubManager.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restClubManagerMockMvc
            .perform(get("/api/club-managers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClubManagerShouldNotBeFound(String filter) throws Exception {
        restClubManagerMockMvc
            .perform(get("/api/club-managers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClubManagerMockMvc
            .perform(get("/api/club-managers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingClubManager() throws Exception {
        // Get the clubManager
        restClubManagerMockMvc.perform(get("/api/club-managers/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClubManager() throws Exception {
        // Initialize the database
        clubManagerRepository.saveAndFlush(clubManager);

        int databaseSizeBeforeUpdate = clubManagerRepository.findAll().size();

        // Update the clubManager
        ClubManager updatedClubManager = clubManagerRepository.findById(clubManager.getId()).get();
        // Disconnect from session so that the updates on updatedClubManager are not directly saved in db
        em.detach(updatedClubManager);
        updatedClubManager.status(UPDATED_STATUS);
        ClubManagerDTO clubManagerDTO = clubManagerMapper.toDto(updatedClubManager);

        restClubManagerMockMvc
            .perform(
                put("/api/club-managers")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubManagerDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClubManager in the database
        List<ClubManager> clubManagerList = clubManagerRepository.findAll();
        assertThat(clubManagerList).hasSize(databaseSizeBeforeUpdate);
        ClubManager testClubManager = clubManagerList.get(clubManagerList.size() - 1);
        assertThat(testClubManager.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingClubManager() throws Exception {
        int databaseSizeBeforeUpdate = clubManagerRepository.findAll().size();

        // Create the ClubManager
        ClubManagerDTO clubManagerDTO = clubManagerMapper.toDto(clubManager);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClubManagerMockMvc
            .perform(
                put("/api/club-managers")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clubManagerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClubManager in the database
        List<ClubManager> clubManagerList = clubManagerRepository.findAll();
        assertThat(clubManagerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClubManager() throws Exception {
        // Initialize the database
        clubManagerRepository.saveAndFlush(clubManager);

        int databaseSizeBeforeDelete = clubManagerRepository.findAll().size();

        // Delete the clubManager
        restClubManagerMockMvc
            .perform(delete("/api/club-managers/{id}", clubManager.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClubManager> clubManagerList = clubManagerRepository.findAll();
        assertThat(clubManagerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
