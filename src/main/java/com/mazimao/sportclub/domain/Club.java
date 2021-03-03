package com.mazimao.sportclub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Club.
 */
@Entity
@Table(name = "sc_club")
public class Club extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "club_name", nullable = false, unique = true)
    private String clubName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActiveInactiveStatus status;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private ClubType clubType;

    @OneToMany(mappedBy = "club")
    private Set<ClubCourt> clubCourts = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "clubs", allowSetters = true)
    private Organization organization;

    @ManyToMany(mappedBy = "clubs")
    @JsonIgnore
    private Set<ClubManager> clubManagers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClubName() {
        return clubName;
    }

    public Club clubName(String clubName) {
        this.clubName = clubName;
        return this;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public ActiveInactiveStatus getStatus() {
        return status;
    }

    public Club status(ActiveInactiveStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ActiveInactiveStatus status) {
        this.status = status;
    }

    public ClubType getClubType() {
        return clubType;
    }

    public Club clubType(ClubType clubType) {
        this.clubType = clubType;
        return this;
    }

    public void setClubType(ClubType clubType) {
        this.clubType = clubType;
    }

    public Set<ClubCourt> getClubCourts() {
        return clubCourts;
    }

    public Club clubCourts(Set<ClubCourt> clubCourts) {
        this.clubCourts = clubCourts;
        return this;
    }

    public Club addClubCourts(ClubCourt clubCourt) {
        this.clubCourts.add(clubCourt);
        clubCourt.setClub(this);
        return this;
    }

    public Club removeClubCourts(ClubCourt clubCourt) {
        this.clubCourts.remove(clubCourt);
        clubCourt.setClub(null);
        return this;
    }

    public void setClubCourts(Set<ClubCourt> clubCourts) {
        this.clubCourts = clubCourts;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Club organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Set<ClubManager> getClubManagers() {
        return clubManagers;
    }

    public Club clubManagers(Set<ClubManager> clubManagers) {
        this.clubManagers = clubManagers;
        return this;
    }

    public Club addClubManager(ClubManager clubManager) {
        this.clubManagers.add(clubManager);
        clubManager.getClubs().add(this);
        return this;
    }

    public Club removeClubManager(ClubManager clubManager) {
        this.clubManagers.remove(clubManager);
        clubManager.getClubs().remove(this);
        return this;
    }

    public void setClubManagers(Set<ClubManager> clubManagers) {
        this.clubManagers = clubManagers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Club)) {
            return false;
        }
        return id != null && id.equals(((Club) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Club{" +
            "id=" + getId() +
            ", clubName='" + getClubName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
