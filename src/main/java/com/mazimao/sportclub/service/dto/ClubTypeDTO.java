package com.mazimao.sportclub.service.dto;

import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mazimao.sportclub.domain.ClubType} entity.
 */
public class ClubTypeDTO implements Serializable {
    private Long id;

    @NotNull
    private String clubTypeCode;

    @NotNull
    private String clubTypeName;

    @NotNull
    private String clubTypeDescription;

    private ActiveInactiveStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClubTypeCode() {
        return clubTypeCode;
    }

    public void setClubTypeCode(String clubTypeCode) {
        this.clubTypeCode = clubTypeCode;
    }

    public String getClubTypeName() {
        return clubTypeName;
    }

    public void setClubTypeName(String clubTypeName) {
        this.clubTypeName = clubTypeName;
    }

    public String getClubTypeDescription() {
        return clubTypeDescription;
    }

    public void setClubTypeDescription(String clubTypeDescription) {
        this.clubTypeDescription = clubTypeDescription;
    }

    public ActiveInactiveStatus getStatus() {
        return status;
    }

    public void setStatus(ActiveInactiveStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClubTypeDTO)) {
            return false;
        }

        return id != null && id.equals(((ClubTypeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubTypeDTO{" +
            "id=" + getId() +
            ", clubTypeCode='" + getClubTypeCode() + "'" +
            ", clubTypeName='" + getClubTypeName() + "'" +
            ", clubTypeDescription='" + getClubTypeDescription() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
