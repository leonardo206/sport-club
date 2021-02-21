package com.mazimao.sportclub.service.dto;

import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mazimao.sportclub.domain.ClubCourtType} entity.
 */
public class ClubCourtTypeDTO implements Serializable {
    private Long id;

    @NotNull
    private String clubCourtTypeCode;

    @NotNull
    private String clubCourtTypeName;

    private String clubCourtTypeDescription;

    private ActiveInactiveStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClubCourtTypeCode() {
        return clubCourtTypeCode;
    }

    public void setClubCourtTypeCode(String clubCourtTypeCode) {
        this.clubCourtTypeCode = clubCourtTypeCode;
    }

    public String getClubCourtTypeName() {
        return clubCourtTypeName;
    }

    public void setClubCourtTypeName(String clubCourtTypeName) {
        this.clubCourtTypeName = clubCourtTypeName;
    }

    public String getClubCourtTypeDescription() {
        return clubCourtTypeDescription;
    }

    public void setClubCourtTypeDescription(String clubCourtTypeDescription) {
        this.clubCourtTypeDescription = clubCourtTypeDescription;
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
        if (!(o instanceof ClubCourtTypeDTO)) {
            return false;
        }

        return id != null && id.equals(((ClubCourtTypeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubCourtTypeDTO{" +
            "id=" + getId() +
            ", clubCourtTypeCode='" + getClubCourtTypeCode() + "'" +
            ", clubCourtTypeName='" + getClubCourtTypeName() + "'" +
            ", clubCourtTypeDescription='" + getClubCourtTypeDescription() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
