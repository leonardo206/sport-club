package com.mazimao.sportclub.service.dto;

import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mazimao.sportclub.domain.Client} entity.
 */
public class ClientDTO implements Serializable {
    private Long id;

    private ActiveInactiveStatus status;

    private String userId;

    private String userLogin;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientDTO)) {
            return false;
        }

        return id != null && id.equals(((ClientDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", userId='" + getUserId() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
