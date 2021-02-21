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
 * Criteria class for the {@link com.mazimao.sportclub.domain.ClubType} entity. This class is used
 * in {@link com.mazimao.sportclub.web.rest.ClubTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /club-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClubTypeCriteria implements Serializable, Criteria {

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

    private StringFilter clubTypeCode;

    private StringFilter clubTypeName;

    private StringFilter clubTypeDescription;

    private ActiveInactiveStatusFilter status;

    public ClubTypeCriteria() {}

    public ClubTypeCriteria(ClubTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.clubTypeCode = other.clubTypeCode == null ? null : other.clubTypeCode.copy();
        this.clubTypeName = other.clubTypeName == null ? null : other.clubTypeName.copy();
        this.clubTypeDescription = other.clubTypeDescription == null ? null : other.clubTypeDescription.copy();
        this.status = other.status == null ? null : other.status.copy();
    }

    @Override
    public ClubTypeCriteria copy() {
        return new ClubTypeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getClubTypeCode() {
        return clubTypeCode;
    }

    public void setClubTypeCode(StringFilter clubTypeCode) {
        this.clubTypeCode = clubTypeCode;
    }

    public StringFilter getClubTypeName() {
        return clubTypeName;
    }

    public void setClubTypeName(StringFilter clubTypeName) {
        this.clubTypeName = clubTypeName;
    }

    public StringFilter getClubTypeDescription() {
        return clubTypeDescription;
    }

    public void setClubTypeDescription(StringFilter clubTypeDescription) {
        this.clubTypeDescription = clubTypeDescription;
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
        final ClubTypeCriteria that = (ClubTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(clubTypeCode, that.clubTypeCode) &&
            Objects.equals(clubTypeName, that.clubTypeName) &&
            Objects.equals(clubTypeDescription, that.clubTypeDescription) &&
            Objects.equals(status, that.status)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clubTypeCode, clubTypeName, clubTypeDescription, status);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (clubTypeCode != null ? "clubTypeCode=" + clubTypeCode + ", " : "") +
                (clubTypeName != null ? "clubTypeName=" + clubTypeName + ", " : "") +
                (clubTypeDescription != null ? "clubTypeDescription=" + clubTypeDescription + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
            "}";
    }
}
