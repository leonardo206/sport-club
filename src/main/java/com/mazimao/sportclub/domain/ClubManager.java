package com.mazimao.sportclub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ClubManager.
 */
@Entity
@Table(name = "sc_club_manager")
public class ClubManager implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActiveInactiveStatus status;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @JoinTable(
        name = "sc_club_manager_clubs",
        joinColumns = @JoinColumn(name = "club_manager_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "clubs_id", referencedColumnName = "id")
    )
    private Set<Club> clubs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "clubManagers", allowSetters = true)
    private Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActiveInactiveStatus getStatus() {
        return status;
    }

    public ClubManager status(ActiveInactiveStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ActiveInactiveStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public ClubManager user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Club> getClubs() {
        return clubs;
    }

    public ClubManager clubs(Set<Club> clubs) {
        this.clubs = clubs;
        return this;
    }

    public ClubManager addClubs(Club club) {
        this.clubs.add(club);
        club.getClubManagers().add(this);
        return this;
    }

    public ClubManager removeClubs(Club club) {
        this.clubs.remove(club);
        club.getClubManagers().remove(this);
        return this;
    }

    public void setClubs(Set<Club> clubs) {
        this.clubs = clubs;
    }

    public Organization getOrganization() {
        return organization;
    }

    public ClubManager organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClubManager)) {
            return false;
        }
        return id != null && id.equals(((ClubManager) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubManager{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
