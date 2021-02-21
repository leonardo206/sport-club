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
 * Criteria class for the {@link com.mazimao.sportclub.domain.ClubCourtType} entity. This class is used
 * in {@link com.mazimao.sportclub.web.rest.ClubCourtTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /club-court-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClubCourtTypeCriteria implements Serializable, Criteria {

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

    private StringFilter clubCourtTypeCode;

    private StringFilter clubCourtTypeName;

    private StringFilter clubCourtTypeDescription;

    private ActiveInactiveStatusFilter status;

    public ClubCourtTypeCriteria() {}

    public ClubCourtTypeCriteria(ClubCourtTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.clubCourtTypeCode = other.clubCourtTypeCode == null ? null : other.clubCourtTypeCode.copy();
        this.clubCourtTypeName = other.clubCourtTypeName == null ? null : other.clubCourtTypeName.copy();
        this.clubCourtTypeDescription = other.clubCourtTypeDescription == null ? null : other.clubCourtTypeDescription.copy();
        this.status = other.status == null ? null : other.status.copy();
    }

    @Override
    public ClubCourtTypeCriteria copy() {
        return new ClubCourtTypeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getClubCourtTypeCode() {
        return clubCourtTypeCode;
    }

    public void setClubCourtTypeCode(StringFilter clubCourtTypeCode) {
        this.clubCourtTypeCode = clubCourtTypeCode;
    }

    public StringFilter getClubCourtTypeName() {
        return clubCourtTypeName;
    }

    public void setClubCourtTypeName(StringFilter clubCourtTypeName) {
        this.clubCourtTypeName = clubCourtTypeName;
    }

    public StringFilter getClubCourtTypeDescription() {
        return clubCourtTypeDescription;
    }

    public void setClubCourtTypeDescription(StringFilter clubCourtTypeDescription) {
        this.clubCourtTypeDescription = clubCourtTypeDescription;
    }

    public ActiveInactiveStatusFilter getStatus() {
        return status;
    }

    public void setStatus(ActiveInactiveStatusFilter status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClubCourtTypeCriteria that = (ClubCourtTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(clubCourtTypeCode, that.clubCourtTypeCode) &&
            Objects.equals(clubCourtTypeName, that.clubCourtTypeName) &&
            Objects.equals(clubCourtTypeDescription, that.clubCourtTypeDescription) &&
            Objects.equals(status, that.status)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clubCourtTypeCode, clubCourtTypeName, clubCourtTypeDescription, status);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubCourtTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (clubCourtTypeCode != null ? "clubCourtTypeCode=" + clubCourtTypeCode + ", " : "") +
                (clubCourtTypeName != null ? "clubCourtTypeName=" + clubCourtTypeName + ", " : "") +
                (clubCourtTypeDescription != null ? "clubCourtTypeDescription=" + clubCourtTypeDescription + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
            "}";
    }
}
