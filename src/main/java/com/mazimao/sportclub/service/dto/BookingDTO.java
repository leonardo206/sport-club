package com.mazimao.sportclub.service.dto;

import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mazimao.sportclub.domain.Booking} entity.
 */
public class BookingDTO extends AbstractAuditingDTO implements Serializable {
    private Long id;

    @NotNull
    private String bookingCode;

    @NotNull
    private ZonedDateTime bookingTime;

    private ActiveInactiveStatus status;

    private Long clientId;

    private Long clubCourtId;

    private String clubCourtCourtName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public ZonedDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(ZonedDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public ActiveInactiveStatus getStatus() {
        return status;
    }

    public void setStatus(ActiveInactiveStatus status) {
        this.status = status;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getClubCourtId() {
        return clubCourtId;
    }

    public void setClubCourtId(Long clubCourtId) {
        this.clubCourtId = clubCourtId;
    }

    public String getClubCourtCourtName() {
        return clubCourtCourtName;
    }

    public void setClubCourtCourtName(String clubCourtCourtName) {
        this.clubCourtCourtName = clubCourtCourtName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingDTO)) {
            return false;
        }

        return id != null && id.equals(((BookingDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingDTO{" +
            "id=" + getId() +
            ", bookingCode='" + getBookingCode() + "'" +
            ", bookingTime='" + getBookingTime() + "'" +
            ", status='" + getStatus() + "'" +
            ", clientId=" + getClientId() +
            ", clubCourtId=" + getClubCourtId() +
            ", clubCourtCourtName='" + getClubCourtCourtName() + "'" +
            "}";
    }
}
