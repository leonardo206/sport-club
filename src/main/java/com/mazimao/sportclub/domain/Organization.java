package com.mazimao.sportclub.domain;

import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Organization.
 */
@Entity
@Table(name = "sc_organization")
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "organization_name", nullable = false, unique = true)
    private String organizationName;

    @NotNull
    @Pattern(regexp = "^[A-Za-z]{2,4}(?=.{2,12}$)[-_\\s0-9]*(?:[a-zA-Z][-_\\s0-9]*){0,2}$")
    @Column(name = "tax_number", nullable = false, unique = true)
    private String taxNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActiveInactiveStatus status;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "organization")
    private Set<ClubManager> clubManagers = new HashSet<>();

    @OneToMany(mappedBy = "organization")
    private Set<Club> clubs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public Organization organizationName(String organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public Organization taxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
        return this;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public ActiveInactiveStatus getStatus() {
        return status;
    }

    public Organization status(ActiveInactiveStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ActiveInactiveStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public Organization user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<ClubManager> getClubManagers() {
        return clubManagers;
    }

    public Organization clubManagers(Set<ClubManager> clubManagers) {
        this.clubManagers = clubManagers;
        return this;
    }

    public Organization addClubManagers(ClubManager clubManager) {
        this.clubManagers.add(clubManager);
        clubManager.setOrganization(this);
        return this;
    }

    public Organization removeClubManagers(ClubManager clubManager) {
        this.clubManagers.remove(clubManager);
        clubManager.setOrganization(null);
        return this;
    }

    public void setClubManagers(Set<ClubManager> clubManagers) {
        this.clubManagers = clubManagers;
    }

    public Set<Club> getClubs() {
        return clubs;
    }

    public Organization clubs(Set<Club> clubs) {
        this.clubs = clubs;
        return this;
    }

    public Organization addClubs(Club club) {
        this.clubs.add(club);
        club.setOrganization(this);
        return this;
    }

    public Organization removeClubs(Club club) {
        this.clubs.remove(club);
        club.setOrganization(null);
        return this;
    }

    public void setClubs(Set<Club> clubs) {
        this.clubs = clubs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", organizationName='" + getOrganizationName() + "'" +
            ", taxNumber='" + getTaxNumber() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
