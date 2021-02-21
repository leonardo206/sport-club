package com.mazimao.sportclub.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mazimao.sportclub.SportClubApp;
import com.mazimao.sportclub.config.TestSecurityConfiguration;
import com.mazimao.sportclub.domain.Booking;
import com.mazimao.sportclub.domain.Client;
import com.mazimao.sportclub.domain.User;
import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import com.mazimao.sportclub.repository.ClientRepository;
import com.mazimao.sportclub.service.ClientQueryService;
import com.mazimao.sportclub.service.ClientService;
import com.mazimao.sportclub.service.dto.ClientCriteria;
import com.mazimao.sportclub.service.dto.ClientDTO;
import com.mazimao.sportclub.service.mapper.ClientMapper;
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
 * Integration tests for the {@link ClientResource} REST controller.
 */
@SpringBootTest(classes = { SportClubApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ClientResourceIT {
    private static final ActiveInactiveStatus DEFAULT_STATUS = ActiveInactiveStatus.Active;
    private static final ActiveInactiveStatus UPDATED_STATUS = ActiveInactiveStatus.Inactive;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientQueryService clientQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private Client client;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity(EntityManager em) {
        Client client = new Client().status(DEFAULT_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        client.setUser(user);
        return client;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity(EntityManager em) {
        Client client = new Client().status(UPDATED_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        client.setUser(user);
        return client;
    }

    @BeforeEach
    public void initTest() {
        client = createEntity(em);
    }

    @Test
    @Transactional
    public void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();
        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);
        restClientMockMvc
            .perform(
                post("/api/clients")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createClientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client with an existing ID
        client.setId(1L);
        ClientDTO clientDTO = clientMapper.toDto(client);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc
            .perform(
                post("/api/clients")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc
            .perform(get("/api/clients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc
            .perform(get("/api/clients/{id}", client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getClientsByIdFiltering() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        Long id = client.getId();

        defaultClientShouldBeFound("id.equals=" + id);
        defaultClientShouldNotBeFound("id.notEquals=" + id);

        defaultClientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClientShouldNotBeFound("id.greaterThan=" + id);

        defaultClientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClientShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllClientsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where status equals to DEFAULT_STATUS
        defaultClientShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the clientList where status equals to UPDATED_STATUS
        defaultClientShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClientsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where status not equals to DEFAULT_STATUS
        defaultClientShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the clientList where status not equals to UPDATED_STATUS
        defaultClientShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClientsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultClientShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the clientList where status equals to UPDATED_STATUS
        defaultClientShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClientsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where status is not null
        defaultClientShouldBeFound("status.specified=true");

        // Get all the clientList where status is null
        defaultClientShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = client.getUser();
        clientRepository.saveAndFlush(client);
        String userId = user.getId();

        // Get all the clientList where user equals to userId
        defaultClientShouldBeFound("userId.equals=" + userId);

        // Get all the clientList where user equals to userId + 1
        defaultClientShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    public void getAllClientsByBookingsIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        Booking bookings = BookingResourceIT.createEntity(em);
        em.persist(bookings);
        em.flush();
        client.addBookings(bookings);
        clientRepository.saveAndFlush(client);
        Long bookingsId = bookings.getId();

        // Get all the clientList where bookings equals to bookingsId
        defaultClientShouldBeFound("bookingsId.equals=" + bookingsId);

        // Get all the clientList where bookings equals to bookingsId + 1
        defaultClientShouldNotBeFound("bookingsId.equals=" + (bookingsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientShouldBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get("/api/clients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restClientMockMvc
            .perform(get("/api/clients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientShouldNotBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get("/api/clients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientMockMvc
            .perform(get("/api/clients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).get();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient.status(UPDATED_STATUS);
        ClientDTO clientDTO = clientMapper.toDto(updatedClient);

        restClientMockMvc
            .perform(
                put("/api/clients")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put("/api/clients")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Delete the client
        restClientMockMvc
            .perform(delete("/api/clients/{id}", client.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
