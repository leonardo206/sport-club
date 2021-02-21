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
 * Criteria class for the {@link com.mazimao.sportclub.domain.Organization} entity. This class is used
 * in {@link com.mazimao.sportclub.web.rest.OrganizationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /organizations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrganizationCriteria implements Serializable, Criteria {

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

    private StringFilter organizationName;

    private StringFilter taxNumber;

    private ActiveInactiveStatusFilter status;

    private StringFilter userId;

    private LongFilter clubManagersId;

    private LongFilter clubsId;

    public OrganizationCriteria() {}

    public OrganizationCriteria(OrganizationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.organizationName = other.organizationName == null ? null : other.organizationName.copy();
        this.taxNumber = other.taxNumber == null ? null : other.taxNumber.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.clubManagersId = other.clubManagersId == null ? null : other.clubManagersId.copy();
        this.clubsId = other.clubsId == null ? null : other.clubsId.copy();
    }

    @Override
    public OrganizationCriteria copy() {
        return new OrganizationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(StringFilter organizationName) {
        this.organizationName = organizationName;
    }

    public StringFilter getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(StringFilter taxNumber) {
        this.taxNumber = taxNumber;
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

    public LongFilter getClubManagersId() {
        return clubManagersId;
    }

    public void setClubManagersId(LongFilter clubManagersId) {
        this.clubManagersId = clubManagersId;
    }

    public LongFilter getClubsId() {
        return clubsId;
    }

    public void setClubsId(LongFilter clubsId) {
        this.clubsId = clubsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrganizationCriteria that = (OrganizationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(organizationName, that.organizationName) &&
            Objects.equals(taxNumber, that.taxNumber) &&
            Objects.equals(status, that.status) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(clubManagersId, that.clubManagersId) &&
            Objects.equals(clubsId, that.clubsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, organizationName, taxNumber, status, userId, clubManagersId, clubsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (organizationName != null ? "organizationName=" + organizationName + ", " : "") +
                (taxNumber != null ? "taxNumber=" + taxNumber + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (clubManagersId != null ? "clubManagersId=" + clubManagersId + ", " : "") +
                (clubsId != null ? "clubsId=" + clubsId + ", " : "") +
            "}";
    }
}
