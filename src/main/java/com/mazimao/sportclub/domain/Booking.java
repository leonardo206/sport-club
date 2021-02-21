package com.mazimao.sportclub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Booking.
 */
@Entity
@Table(name = "sc_booking")
public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "booking_code", nullable = false)
    private String bookingCode;

    @NotNull
    @Column(name = "booking_time", nullable = false)
    private ZonedDateTime bookingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActiveInactiveStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = "bookings", allowSetters = true)
    private Client client;

    @ManyToOne
    @JsonIgnoreProperties(value = "bookings", allowSetters = true)
    private ClubCourt clubCourt;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public Booking bookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
        return this;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public ZonedDateTime getBookingTime() {
        return bookingTime;
    }

    public Booking bookingTime(ZonedDateTime bookingTime) {
        this.bookingTime = bookingTime;
        return this;
    }

    public void setBookingTime(ZonedDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public ActiveInactiveStatus getStatus() {
        return status;
    }

    public Booking status(ActiveInactiveStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ActiveInactiveStatus status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public Booking client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ClubCourt getClubCourt() {
        return clubCourt;
    }

    public Booking clubCourt(ClubCourt clubCourt) {
        this.clubCourt = clubCourt;
        return this;
    }

    public void setClubCourt(ClubCourt clubCourt) {
        this.clubCourt = clubCourt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Booking)) {
            return false;
        }
        return id != null && id.equals(((Booking) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Booking{" +
            "id=" + getId() +
            ", bookingCode='" + getBookingCode() + "'" +
            ", bookingTime='" + getBookingTime() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
