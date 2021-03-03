package com.mazimao.sportclub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ClubCourt.
 */
@Entity
@Table(name = "sc_club_court")
public class ClubCourt extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "court_code", nullable = false, unique = true)
    private String courtCode;

    @NotNull
    @Column(name = "court_name", nullable = false, unique = true)
    private String courtName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActiveInactiveStatus status;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private ClubCourtType clubCourtType;

    @OneToMany(mappedBy = "clubCourt")
    private Set<Booking> bookings = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "clubCourts", allowSetters = true)
    private Club club;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourtCode() {
        return courtCode;
    }

    public ClubCourt courtCode(String courtCode) {
        this.courtCode = courtCode;
        return this;
    }

    public void setCourtCode(String courtCode) {
        this.courtCode = courtCode;
    }

    public String getCourtName() {
        return courtName;
    }

    public ClubCourt courtName(String courtName) {
        this.courtName = courtName;
        return this;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public ActiveInactiveStatus getStatus() {
        return status;
    }

    public ClubCourt status(ActiveInactiveStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ActiveInactiveStatus status) {
        this.status = status;
    }

    public ClubCourtType getClubCourtType() {
        return clubCourtType;
    }

    public ClubCourt clubCourtType(ClubCourtType clubCourtType) {
        this.clubCourtType = clubCourtType;
        return this;
    }

    public void setClubCourtType(ClubCourtType clubCourtType) {
        this.clubCourtType = clubCourtType;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public ClubCourt bookings(Set<Booking> bookings) {
        this.bookings = bookings;
        return this;
    }

    public ClubCourt addBookings(Booking booking) {
        this.bookings.add(booking);
        booking.setClubCourt(this);
        return this;
    }

    public ClubCourt removeBookings(Booking booking) {
        this.bookings.remove(booking);
        booking.setClubCourt(null);
        return this;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Club getClub() {
        return club;
    }

    public ClubCourt club(Club club) {
        this.club = club;
        return this;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClubCourt)) {
            return false;
        }
        return id != null && id.equals(((ClubCourt) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubCourt{" +
            "id=" + getId() +
            ", courtCode='" + getCourtCode() + "'" +
            ", courtName='" + getCourtName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
