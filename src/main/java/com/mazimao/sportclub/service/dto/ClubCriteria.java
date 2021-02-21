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
 * Criteria class for the {@link com.mazimao.sportclub.domain.Club} entity. This class is used
 * in {@link com.mazimao.sportclub.web.rest.ClubResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clubs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClubCriteria implements Serializable, Criteria {

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

    private StringFilter clubName;

    private ActiveInactiveStatusFilter status;

    private LongFilter clubTypeId;

    private LongFilter clubCourtsId;

    private LongFilter organizationId;

    private LongFilter clubManagerId;

    public ClubCriteria() {}

    public ClubCriteria(ClubCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.clubName = other.clubName == null ? null : other.clubName.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.clubTypeId = other.clubTypeId == null ? null : other.clubTypeId.copy();
        this.clubCourtsId = other.clubCourtsId == null ? null : other.clubCourtsId.copy();
        this.organizationId = other.organizationId == null ? null : other.organizationId.copy();
        this.clubManagerId = other.clubManagerId == null ? null : other.clubManagerId.copy();
    }

    @Override
    public ClubCriteria copy() {
        return new ClubCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getClubName() {
        return clubName;
    }

    public void setClubName(StringFilter clubName) {
        this.clubName = clubName;
    }

    public ActiveInactiveStatusFilter getStatus() {
        return status;
    }

    public void setStatus(ActiveInactiveStatusFilter status) {
        this.status = status;
    }

    public LongFilter getClubTypeId() {
        return clubTypeId;
    }

    public void setClubTypeId(LongFilter clubTypeId) {
        this.clubTypeId = clubTypeId;
    }

    public LongFilter getClubCourtsId() {
        return clubCourtsId;
    }

    public void setClubCourtsId(LongFilter clubCourtsId) {
        this.clubCourtsId = clubCourtsId;
    }

    public LongFilter getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(LongFilter organizationId) {
        this.organizationId = organizationId;
    }

    public LongFilter getClubManagerId() {
        return clubManagerId;
    }

    public void setClubManagerId(LongFilter clubManagerId) {
        this.clubManagerId = clubManagerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClubCriteria that = (ClubCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(clubName, that.clubName) &&
            Objects.equals(status, that.status) &&
            Objects.equals(clubTypeId, that.clubTypeId) &&
            Objects.equals(clubCourtsId, that.clubCourtsId) &&
            Objects.equals(organizationId, that.organizationId) &&
            Objects.equals(clubManagerId, that.clubManagerId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clubName, status, clubTypeId, clubCourtsId, organizationId, clubManagerId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (clubName != null ? "clubName=" + clubName + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (clubTypeId != null ? "clubTypeId=" + clubTypeId + ", " : "") +
                (clubCourtsId != null ? "clubCourtsId=" + clubCourtsId + ", " : "") +
                (organizationId != null ? "organizationId=" + organizationId + ", " : "") +
                (clubManagerId != null ? "clubManagerId=" + clubManagerId + ", " : "") +
            "}";
    }
}
