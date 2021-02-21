package com.mazimao.sportclub.service.dto;

import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mazimao.sportclub.domain.ClubManager} entity.
 */
public class ClubManagerDTO implements Serializable {
    private Long id;

    private ActiveInactiveStatus status;

    private String userId;

    private String userLogin;
    private Set<ClubDTO> clubs = new HashSet<>();

    private Long organizationId;

    private String organizationOrganizationName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActiveInactiveStatus getStatus() {
        return status;
    }

    public void setStatus(ActiveInactiveStatus status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Set<ClubDTO> getClubs() {
        return clubs;
    }

    public void setClubs(Set<ClubDTO> clubs) {
        this.clubs = clubs;
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
        if (!(o instanceof ClubManagerDTO)) {
            return false;
        }

        return id != null && id.equals(((ClubManagerDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubManagerDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", userId='" + getUserId() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            ", clubs='" + getClubs() + "'" +
            ", organizationId=" + getOrganizationId() +
            ", organizationOrganizationName='" + getOrganizationOrganizationName() + "'" +
            "}";
    }
}
