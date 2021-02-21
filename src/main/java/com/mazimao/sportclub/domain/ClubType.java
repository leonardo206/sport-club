package com.mazimao.sportclub.domain;

import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ClubType.
 */
@Entity
@Table(name = "sc_club_type")
public class ClubType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "club_type_code", nullable = false, unique = true)
    private String clubTypeCode;

    @NotNull
    @Column(name = "club_type_name", nullable = false, unique = true)
    private String clubTypeName;

    @NotNull
    @Column(name = "club_type_description", nullable = false)
    private String clubTypeDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActiveInactiveStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClubTypeCode() {
        return clubTypeCode;
    }

    public ClubType clubTypeCode(String clubTypeCode) {
        this.clubTypeCode = clubTypeCode;
        return this;
    }

    public void setClubTypeCode(String clubTypeCode) {
        this.clubTypeCode = clubTypeCode;
    }

    public String getClubTypeName() {
        return clubTypeName;
    }

    public ClubType clubTypeName(String clubTypeName) {
        this.clubTypeName = clubTypeName;
        return this;
    }

    public void setClubTypeName(String clubTypeName) {
        this.clubTypeName = clubTypeName;
    }

    public String getClubTypeDescription() {
        return clubTypeDescription;
    }

    public ClubType clubTypeDescription(String clubTypeDescription) {
        this.clubTypeDescription = clubTypeDescription;
        return this;
    }

    public void setClubTypeDescription(String clubTypeDescription) {
        this.clubTypeDescription = clubTypeDescription;
    }

    public ActiveInactiveStatus getStatus() {
        return status;
    }

    public ClubType status(ActiveInactiveStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ActiveInactiveStatus status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClubType)) {
            return false;
        }
        return id != null && id.equals(((ClubType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubType{" +
            "id=" + getId() +
            ", clubTypeCode='" + getClubTypeCode() + "'" +
            ", clubTypeName='" + getClubTypeName() + "'" +
            ", clubTypeDescription='" + getClubTypeDescription() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
