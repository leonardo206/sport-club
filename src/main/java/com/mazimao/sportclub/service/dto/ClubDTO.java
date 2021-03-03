package com.mazimao.sportclub.service.dto;

import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mazimao.sportclub.domain.Club} entity.
 */
public class ClubDTO extends AbstractAuditingDTO implements Serializable {
    private Long id;

    @NotNull
    private String clubName;

    private ActiveInactiveStatus status;

    private Long clubTypeId;

    private String clubTypeClubTypeName;

    private Long organizationId;

    private String organizationOrganizationName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public ActiveInactiveStatus getStatus() {
        return status;
    }

    public void setStatus(ActiveInactiveStatus status) {
        this.status = status;
    }

    public Long getClubTypeId() {
        return clubTypeId;
    }

    public void setClubTypeId(Long clubTypeId) {
        this.clubTypeId = clubTypeId;
    }

    public String getClubTypeClubTypeName() {
        return clubTypeClubTypeName;
    }

    public void setClubTypeClubTypeName(String clubTypeClubTypeName) {
        this.clubTypeClubTypeName = clubTypeClubTypeName;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationOrganizationName() {
        return organizationOrganizationName;
    }

    public void setOrganizationOrganizationName(String organizationOrganizationName) {
        this.organizationOrganizationName = organizationOrganizationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClubDTO)) {
            return false;
        }

        return id != null && id.equals(((ClubDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubDTO{" +
            "id=" + getId() +
            ", clubName='" + getClubName() + "'" +
            ", status='" + getStatus() + "'" +
            ", clubTypeId=" + getClubTypeId() +
            ", clubTypeClubTypeName='" + getClubTypeClubTypeName() + "'" +
            ", organizationId=" + getOrganizationId() +
            ", organizationOrganizationName='" + getOrganizationOrganizationName() + "'" +
            "}";
    }
}
