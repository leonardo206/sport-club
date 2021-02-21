package com.mazimao.sportclub.web.rest;

import static com.mazimao.sportclub.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mazimao.sportclub.SportClubApp;
import com.mazimao.sportclub.config.TestSecurityConfiguration;
import com.mazimao.sportclub.domain.Booking;
import com.mazimao.sportclub.domain.Client;
import com.mazimao.sportclub.domain.ClubCourt;
import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import com.mazimao.sportclub.repository.BookingRepository;
import com.mazimao.sportclub.service.BookingQueryService;
import com.mazimao.sportclub.service.BookingService;
import com.mazimao.sportclub.service.dto.BookingCriteria;
import com.mazimao.sportclub.service.dto.BookingDTO;
import com.mazimao.sportclub.service.mapper.BookingMapper;
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

/**
 * Integration tests for the {@link BookingResource} REST controller.
 */
@SpringBootTest(classes = { SportClubApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class BookingResourceIT {
    private static final String DEFAULT_BOOKING_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BOOKING_CODE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_BOOKING_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_BOOKING_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_BOOKING_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ActiveInactiveStatus DEFAULT_STATUS = ActiveInactiveStatus.Active;
    private static final ActiveInactiveStatus UPDATED_STATUS = ActiveInactiveStatus.Inactive;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingQueryService bookingQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingMockMvc;

    private Booking booking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createEntity(EntityManager em) {
        Booking booking = new Booking().bookingCode(DEFAULT_BOOKING_CODE).bookingTime(DEFAULT_BOOKING_TIME).status(DEFAULT_STATUS);
        return booking;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createUpdatedEntity(EntityManager em) {
        Booking booking = new Booking().bookingCode(UPDATED_BOOKING_CODE).bookingTime(UPDATED_BOOKING_TIME).status(UPDATED_STATUS);
        return booking;
    }

    @BeforeEach
    public void initTest() {
        booking = createEntity(em);
    }

    @Test
    @Transactional
    public void createBooking() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();
        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);
        restBookingMockMvc
            .perform(
                post("/api/bookings")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate + 1);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getBookingCode()).isEqualTo(DEFAULT_BOOKING_CODE);
        assertThat(testBooking.getBookingTime()).isEqualTo(DEFAULT_BOOKING_TIME);
        assertThat(testBooking.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createBookingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        // Create the Booking with an existing ID
        booking.setId(1L);
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingMockMvc
            .perform(
                post("/api/bookings")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBookingCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setBookingCode(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(
                post("/api/bookings")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBookingTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setBookingTime(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(
                post("/api/bookings")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookings() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList
        restBookingMockMvc
            .perform(get("/api/bookings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingCode").value(hasItem(DEFAULT_BOOKING_CODE)))
            .andExpect(jsonPath("$.[*].bookingTime").value(hasItem(sameInstant(DEFAULT_BOOKING_TIME))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc
            .perform(get("/api/bookings/{id}", booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(booking.getId().intValue()))
            .andExpect(jsonPath("$.bookingCode").value(DEFAULT_BOOKING_CODE))
            .andExpect(jsonPath("$.bookingTime").value(sameInstant(DEFAULT_BOOKING_TIME)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getBookingsByIdFiltering() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        Long id = booking.getId();

        defaultBookingShouldBeFound("id.equals=" + id);
        defaultBookingShouldNotBeFound("id.notEquals=" + id);

        defaultBookingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBookingShouldNotBeFound("id.greaterThan=" + id);

        defaultBookingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBookingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingCode equals to DEFAULT_BOOKING_CODE
        defaultBookingShouldBeFound("bookingCode.equals=" + DEFAULT_BOOKING_CODE);

        // Get all the bookingList where bookingCode equals to UPDATED_BOOKING_CODE
        defaultBookingShouldNotBeFound("bookingCode.equals=" + UPDATED_BOOKING_CODE);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingCode not equals to DEFAULT_BOOKING_CODE
        defaultBookingShouldNotBeFound("bookingCode.notEquals=" + DEFAULT_BOOKING_CODE);

        // Get all the bookingList where bookingCode not equals to UPDATED_BOOKING_CODE
        defaultBookingShouldBeFound("bookingCode.notEquals=" + UPDATED_BOOKING_CODE);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingCodeIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingCode in DEFAULT_BOOKING_CODE or UPDATED_BOOKING_CODE
        defaultBookingShouldBeFound("bookingCode.in=" + DEFAULT_BOOKING_CODE + "," + UPDATED_BOOKING_CODE);

        // Get all the bookingList where bookingCode equals to UPDATED_BOOKING_CODE
        defaultBookingShouldNotBeFound("bookingCode.in=" + UPDATED_BOOKING_CODE);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingCode is not null
        defaultBookingShouldBeFound("bookingCode.specified=true");

        // Get all the bookingList where bookingCode is null
        defaultBookingShouldNotBeFound("bookingCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingCodeContainsSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingCode contains DEFAULT_BOOKING_CODE
        defaultBookingShouldBeFound("bookingCode.contains=" + DEFAULT_BOOKING_CODE);

        // Get all the bookingList where bookingCode contains UPDATED_BOOKING_CODE
        defaultBookingShouldNotBeFound("bookingCode.contains=" + UPDATED_BOOKING_CODE);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingCodeNotContainsSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingCode does not contain DEFAULT_BOOKING_CODE
        defaultBookingShouldNotBeFound("bookingCode.doesNotContain=" + DEFAULT_BOOKING_CODE);

        // Get all the bookingList where bookingCode does not contain UPDATED_BOOKING_CODE
        defaultBookingShouldBeFound("bookingCode.doesNotContain=" + UPDATED_BOOKING_CODE);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingTime equals to DEFAULT_BOOKING_TIME
        defaultBookingShouldBeFound("bookingTime.equals=" + DEFAULT_BOOKING_TIME);

        // Get all the bookingList where bookingTime equals to UPDATED_BOOKING_TIME
        defaultBookingShouldNotBeFound("bookingTime.equals=" + UPDATED_BOOKING_TIME);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingTime not equals to DEFAULT_BOOKING_TIME
        defaultBookingShouldNotBeFound("bookingTime.notEquals=" + DEFAULT_BOOKING_TIME);

        // Get all the bookingList where bookingTime not equals to UPDATED_BOOKING_TIME
        defaultBookingShouldBeFound("bookingTime.notEquals=" + UPDATED_BOOKING_TIME);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingTimeIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingTime in DEFAULT_BOOKING_TIME or UPDATED_BOOKING_TIME
        defaultBookingShouldBeFound("bookingTime.in=" + DEFAULT_BOOKING_TIME + "," + UPDATED_BOOKING_TIME);

        // Get all the bookingList where bookingTime equals to UPDATED_BOOKING_TIME
        defaultBookingShouldNotBeFound("bookingTime.in=" + UPDATED_BOOKING_TIME);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingTime is not null
        defaultBookingShouldBeFound("bookingTime.specified=true");

        // Get all the bookingList where bookingTime is null
        defaultBookingShouldNotBeFound("bookingTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingTime is greater than or equal to DEFAULT_BOOKING_TIME
        defaultBookingShouldBeFound("bookingTime.greaterThanOrEqual=" + DEFAULT_BOOKING_TIME);

        // Get all the bookingList where bookingTime is greater than or equal to UPDATED_BOOKING_TIME
        defaultBookingShouldNotBeFound("bookingTime.greaterThanOrEqual=" + UPDATED_BOOKING_TIME);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingTime is less than or equal to DEFAULT_BOOKING_TIME
        defaultBookingShouldBeFound("bookingTime.lessThanOrEqual=" + DEFAULT_BOOKING_TIME);

        // Get all the bookingList where bookingTime is less than or equal to SMALLER_BOOKING_TIME
        defaultBookingShouldNotBeFound("bookingTime.lessThanOrEqual=" + SMALLER_BOOKING_TIME);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingTime is less than DEFAULT_BOOKING_TIME
        defaultBookingShouldNotBeFound("bookingTime.lessThan=" + DEFAULT_BOOKING_TIME);

        // Get all the bookingList where bookingTime is less than UPDATED_BOOKING_TIME
        defaultBookingShouldBeFound("bookingTime.lessThan=" + UPDATED_BOOKING_TIME);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingTime is greater than DEFAULT_BOOKING_TIME
        defaultBookingShouldNotBeFound("bookingTime.greaterThan=" + DEFAULT_BOOKING_TIME);

        // Get all the bookingList where bookingTime is greater than SMALLER_BOOKING_TIME
        defaultBookingShouldBeFound("bookingTime.greaterThan=" + SMALLER_BOOKING_TIME);
    }

    @Test
    @Transactional
    public void getAllBookingsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status equals to DEFAULT_STATUS
        defaultBookingShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the bookingList where status equals to UPDATED_STATUS
        defaultBookingShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllBookingsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status not equals to DEFAULT_STATUS
        defaultBookingShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the bookingList where status not equals to UPDATED_STATUS
        defaultBookingShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllBookingsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultBookingShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the bookingList where status equals to UPDATED_STATUS
        defaultBookingShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllBookingsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status is not null
        defaultBookingShouldBeFound("status.specified=true");

        // Get all the bookingList where status is null
        defaultBookingShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllBookingsByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);
        Client client = ClientResourceIT.createEntity(em);
        em.persist(client);
        em.flush();
        booking.setClient(client);
        bookingRepository.saveAndFlush(booking);
        Long clientId = client.getId();

        // Get all the bookingList where client equals to clientId
        defaultBookingShouldBeFound("clientId.equals=" + clientId);

        // Get all the bookingList where client equals to clientId + 1
        defaultBookingShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    @Test
    @Transactional
    public void getAllBookingsByClubCourtIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);
        ClubCourt clubCourt = ClubCourtResourceIT.createEntity(em);
        em.persist(clubCourt);
        em.flush();
        booking.setClubCourt(clubCourt);
        bookingRepository.saveAndFlush(booking);
        Long clubCourtId = clubCourt.getId();

        // Get all the bookingList where clubCourt equals to clubCourtId
        defaultBookingShouldBeFound("clubCourtId.equals=" + clubCourtId);

        // Get all the bookingList where clubCourt equals to clubCourtId + 1
        defaultBookingShouldNotBeFound("clubCourtId.equals=" + (clubCourtId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookingShouldBeFound(String filter) throws Exception {
        restBookingMockMvc
            .perform(get("/api/bookings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingCode").value(hasItem(DEFAULT_BOOKING_CODE)))
            .andExpect(jsonPath("$.[*].bookingTime").value(hasItem(sameInstant(DEFAULT_BOOKING_TIME))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restBookingMockMvc
            .perform(get("/api/bookings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookingShouldNotBeFound(String filter) throws Exception {
        restBookingMockMvc
            .perform(get("/api/bookings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookingMockMvc
            .perform(get("/api/bookings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingBooking() throws Exception {
        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking
        Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
        // Disconnect from session so that the updates on updatedBooking are not directly saved in db
        em.detach(updatedBooking);
        updatedBooking.bookingCode(UPDATED_BOOKING_CODE).bookingTime(UPDATED_BOOKING_TIME).status(UPDATED_STATUS);
        BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

        restBookingMockMvc
            .perform(
                put("/api/bookings")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getBookingCode()).isEqualTo(UPDATED_BOOKING_CODE);
        assertThat(testBooking.getBookingTime()).isEqualTo(UPDATED_BOOKING_TIME);
        assertThat(testBooking.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                put("/api/bookings")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeDelete = bookingRepository.findAll().size();

        // Delete the booking
        restBookingMockMvc
            .perform(delete("/api/bookings/{id}", booking.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
