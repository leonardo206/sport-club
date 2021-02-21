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
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.mazimao.sportclub.domain.ClubCourt} entity. This class is used
 * in {@link com.mazimao.sportclub.web.rest.ClubCourtResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /club-courts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClubCourtCriteria implements Serializable, Criteria {

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

    private StringFilter courtCode;

    private StringFilter courtName;

    private ActiveInactiveStatusFilter status;

    private LongFilter clubCourtTypeId;

    private LongFilter bookingsId;

    private LongFilter clubId;

    public ClubCourtCriteria() {}

    public ClubCourtCriteria(ClubCourtCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.courtCode = other.courtCode == null ? null : other.courtCode.copy();
        this.courtName = other.courtName == null ? null : other.courtName.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.clubCourtTypeId = other.clubCourtTypeId == null ? null : other.clubCourtTypeId.copy();
        this.bookingsId = other.bookingsId == null ? null : other.bookingsId.copy();
        this.clubId = other.clubId == null ? null : other.clubId.copy();
    }

    @Override
    public ClubCourtCriteria copy() {
        return new ClubCourtCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCourtCode() {
        return courtCode;
    }

    public void setCourtCode(StringFilter courtCode) {
        this.courtCode = courtCode;
    }

    public StringFilter getCourtName() {
        return courtName;
    }

    public void setCourtName(StringFilter courtName) {
        this.courtName = courtName;
    }

    public ActiveInactiveStatusFilter getStatus() {
        return status;
    }

    public void setStatus(ActiveInactiveStatusFilter status) {
        this.status = status;
    }

    public LongFilter getClubCourtTypeId() {
        return clubCourtTypeId;
    }

    public void setClubCourtTypeId(LongFilter clubCourtTypeId) {
        this.clubCourtTypeId = clubCourtTypeId;
    }

    public LongFilter getBookingsId() {
        return bookingsId;
    }

    public void setBookingsId(LongFilter bookingsId) {
        this.bookingsId = bookingsId;
    }

    public LongFilter getClubId() {
        return clubId;
    }

    public void setClubId(LongFilter clubId) {
        this.clubId = clubId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClubCourtCriteria that = (ClubCourtCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(courtCode, that.courtCode) &&
            Objects.equals(courtName, that.courtName) &&
            Objects.equals(status, that.status) &&
            Objects.equals(clubCourtTypeId, that.clubCourtTypeId) &&
            Objects.equals(bookingsId, that.bookingsId) &&
            Objects.equals(clubId, that.clubId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courtCode, courtName, status, clubCourtTypeId, bookingsId, clubId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubCourtCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (courtCode != null ? "courtCode=" + courtCode + ", " : "") +
                (courtName != null ? "courtName=" + courtName + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (clubCourtTypeId != null ? "clubCourtTypeId=" + clubCourtTypeId + ", " : "") +
                (bookingsId != null ? "bookingsId=" + bookingsId + ", " : "") +
                (clubId != null ? "clubId=" + clubId + ", " : "") +
            "}";
    }
}
