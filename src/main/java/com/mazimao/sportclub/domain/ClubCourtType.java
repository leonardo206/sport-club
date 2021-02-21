package com.mazimao.sportclub.domain;

import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ClubCourtType.
 */
@Entity
@Table(name = "sc_club_court_type")
public class ClubCourtType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "club_court_type_code", nullable = false, unique = true)
    private String clubCourtTypeCode;

    @NotNull
    @Column(name = "club_court_type_name", nullable = false, unique = true)
    private String clubCourtTypeName;

    @Column(name = "club_court_type_description")
    private String clubCourtTypeDescription;

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

    public String getClubCourtTypeCode() {
        return clubCourtTypeCode;
    }

    public ClubCourtType clubCourtTypeCode(String clubCourtTypeCode) {
        this.clubCourtTypeCode = clubCourtTypeCode;
        return this;
    }

    public void setClubCourtTypeCode(String clubCourtTypeCode) {
        this.clubCourtTypeCode = clubCourtTypeCode;
    }

    public String getClubCourtTypeName() {
        return clubCourtTypeName;
    }

    public ClubCourtType clubCourtTypeName(String clubCourtTypeName) {
        this.clubCourtTypeName = clubCourtTypeName;
        return this;
    }

    public void setClubCourtTypeName(String clubCourtTypeName) {
        this.clubCourtTypeName = clubCourtTypeName;
    }

    public String getClubCourtTypeDescription() {
        return clubCourtTypeDescription;
    }

    public ClubCourtType clubCourtTypeDescription(String clubCourtTypeDescription) {
        this.clubCourtTypeDescription = clubCourtTypeDescription;
        return this;
    }

    public void setClubCourtTypeDescription(String clubCourtTypeDescription) {
        this.clubCourtTypeDescription = clubCourtTypeDescription;
    }

    public ActiveInactiveStatus getStatus() {
        return status;
    }

    public ClubCourtType status(ActiveInactiveStatus status) {
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
        if (!(o instanceof ClubCourtType)) {
            return false;
        }
        return id != null && id.equals(((ClubCourtType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubCourtType{" +
            "id=" + getId() +
            ", clubCourtTypeCode='" + getClubCourtTypeCode() + "'" +
            ", clubCourtTypeName='" + getClubCourtTypeName() + "'" +
            ", clubCourtTypeDescription='" + getClubCourtTypeDescription() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
