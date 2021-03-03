package com.mazimao.sportclub.service.dto;

import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mazimao.sportclub.domain.ClubCourt} entity.
 */
public class ClubCourtDTO extends AbstractAuditingDTO implements Serializable {
    private Long id;

    @NotNull
    private String courtCode;

    @NotNull
    private String courtName;

    private ActiveInactiveStatus status;

    private Long clubCourtTypeId;

    private String clubCourtTypeClubCourtTypeName;

    private Long clubId;

    private String clubClubName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourtCode() {
        return courtCode;
    }

    public void setCourtCode(String courtCode) {
        this.courtCode = courtCode;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public ActiveInactiveStatus getStatus() {
        return status;
    }

    public void setStatus(ActiveInactiveStatus status) {
        this.status = status;
    }

    public Long getClubCourtTypeId() {
        return clubCourtTypeId;
    }

    public void setClubCourtTypeId(Long clubCourtTypeId) {
        this.clubCourtTypeId = clubCourtTypeId;
    }

    public String getClubCourtTypeClubCourtTypeName() {
        return clubCourtTypeClubCourtTypeName;
    }

    public void setClubCourtTypeClubCourtTypeName(String clubCourtTypeClubCourtTypeName) {
        this.clubCourtTypeClubCourtTypeName = clubCourtTypeClubCourtTypeName;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getClubClubName() {
        return clubClubName;
    }

    public void setClubClubName(String clubClubName) {
        this.clubClubName = clubClubName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClubCourtDTO)) {
            return false;
        }

        return id != null && id.equals(((ClubCourtDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubCourtDTO{" +
            "id=" + getId() +
            ", courtCode='" + getCourtCode() + "'" +
            ", courtName='" + getCourtName() + "'" +
            ", status='" + getStatus() + "'" +
            ", clubCourtTypeId=" + getClubCourtTypeId() +
            ", clubCourtTypeClubCourtTypeName='" + getClubCourtTypeClubCourtTypeName() + "'" +
            ", clubId=" + getClubId() +
            ", clubClubName='" + getClubClubName() + "'" +
            "}";
    }
}
