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
 * Criteria class for the {@link com.mazimao.sportclub.domain.ClubManager} entity. This class is used
 * in {@link com.mazimao.sportclub.web.rest.ClubManagerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /club-managers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClubManagerCriteria implements Serializable, Criteria {

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

    private ActiveInactiveStatusFilter status;

    private StringFilter userId;

    private LongFilter clubsId;

    private LongFilter organizationId;

    public ClubManagerCriteria() {}

    public ClubManagerCriteria(ClubManagerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.clubsId = other.clubsId == null ? null : other.clubsId.copy();
        this.organizationId = other.organizationId == null ? null : other.organizationId.copy();
    }

    @Override
    public ClubManagerCriteria copy() {
        return new ClubManagerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ActiveInactiveStatusFilter getStatus() {
        return status;
    }

    public void setStatus(ActiveInactiveStatusFilter status) {
        this.status = status;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    public LongFilter getClubsId() {
        return clubsId;
    }

    public void setClubsId(LongFilter clubsId) {
        this.clubsId = clubsId;
    }

    public LongFilter getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(LongFilter organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClubManagerCriteria that = (ClubManagerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(clubsId, that.clubsId) &&
            Objects.equals(organizationId, that.organizationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, userId, clubsId, organizationId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubManagerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (clubsId != null ? "clubsId=" + clubsId + ", " : "") +
                (organizationId != null ? "organizationId=" + organizationId + ", " : "") +
            "}";
    }
}
