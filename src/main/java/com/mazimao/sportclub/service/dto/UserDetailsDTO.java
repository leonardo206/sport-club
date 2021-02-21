package com.mazimao.sportclub.service.dto;

import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mazimao.sportclub.domain.UserDetails} entity.
 */
public class UserDetailsDTO implements Serializable {
    private Long id;

    @NotNull
    private String mobileNumber;

    @NotNull
    private ZonedDateTime dateOfBirth;

    @Lob
    private byte[] profilePic;

    private String profilePicContentType;
    private String description;

    private ActiveInactiveStatus status;

    private String userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public ZonedDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(ZonedDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePicContentType() {
        return profilePicContentType;
    }

    public void setProfilePicContentType(String profilePicContentType) {
        this.profilePicContentType = profilePicContentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDetailsDTO)) {
            return false;
        }

        return id != null && id.equals(((UserDetailsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDetailsDTO{" +
            "id=" + getId() +
            ", mobileNumber='" + getMobileNumber() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", profilePic='" + getProfilePic() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", userId='" + getUserId() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
