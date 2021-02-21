package com.mazimao.sportclub.service.dto;

import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.mazimao.sportclub.domain.Booking} entity. This class is used
 * in {@link com.mazimao.sportclub.web.rest.BookingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bookings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BookingCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ActiveInactiveStatus
     */
    public static class ActiveInactiveStatusFilter extends Filter<ActiveInactiveStatus> {

        public ActiveInactiveStatusFilter() {}

        public ActiveInactiveStatusFilter(ActiveInactiveStatusFilter filter) {
            super(filter);
        }

        @Override
        public ActiveInactiveStatusFilter copy() {
            return new ActiveInactiveStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bookingCode;

    private ZonedDateTimeFilter bookingTime;

    private ActiveInactiveStatusFilter status;

    private LongFilter clientId;

    private LongFilter clubCourtId;

    public BookingCriteria() {}

    public BookingCriteria(BookingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bookingCode = other.bookingCode == null ? null : other.bookingCode.copy();
        this.bookingTime = other.bookingTime == null ? null : other.bookingTime.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
        this.clubCourtId = other.clubCourtId == null ? null : other.clubCourtId.copy();
    }

    @Override
    public BookingCriteria copy() {
        return new BookingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(StringFilter bookingCode) {
        this.bookingCode = bookingCode;
    }

    public ZonedDateTimeFilter getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(ZonedDateTimeFilter bookingTime) {
        this.bookingTime = bookingTime;
    }

    public ActiveInactiveStatusFilter getStatus() {
        return status;
    }

    public void setStatus(ActiveInactiveStatusFilter status) {
        this.status = status;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getClubCourtId() {
        return clubCourtId;
    }

    public void setClubCourtId(LongFilter clubCourtId) {
        this.clubCourtId = clubCourtId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BookingCriteria that = (BookingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(bookingCode, that.bookingCode) &&
            Objects.equals(bookingTime, that.bookingTime) &&
            Objects.equals(status, that.status) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(clubCourtId, that.clubCourtId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookingCode, bookingTime, status, clientId, clubCourtId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (bookingCode != null ? "bookingCode=" + bookingCode + ", " : "") +
                (bookingTime != null ? "bookingTime=" + bookingTime + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
                (clubCourtId != null ? "clubCourtId=" + clubCourtId + ", " : "") +
            "}";
    }
}
