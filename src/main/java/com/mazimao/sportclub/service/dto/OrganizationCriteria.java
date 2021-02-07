package com.mazimao.sportclub.service.dto;

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
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter organizationOwnerJhiUserId;

    private StringFilter organizationName;

    private StringFilter taxNumber;

    private StringFilter status;

    private StringFilter userId;

    public OrganizationCriteria() {}

    public OrganizationCriteria(OrganizationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.organizationOwnerJhiUserId = other.organizationOwnerJhiUserId == null ? null : other.organizationOwnerJhiUserId.copy();
        this.organizationName = other.organizationName == null ? null : other.organizationName.copy();
        this.taxNumber = other.taxNumber == null ? null : other.taxNumber.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
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

    public StringFilter getOrganizationOwnerJhiUserId() {
        return organizationOwnerJhiUserId;
    }

    public void setOrganizationOwnerJhiUserId(StringFilter organizationOwnerJhiUserId) {
        this.organizationOwnerJhiUserId = organizationOwnerJhiUserId;
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

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
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
            Objects.equals(organizationOwnerJhiUserId, that.organizationOwnerJhiUserId) &&
            Objects.equals(organizationName, that.organizationName) &&
            Objects.equals(taxNumber, that.taxNumber) &&
            Objects.equals(status, that.status) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, organizationOwnerJhiUserId, organizationName, taxNumber, status, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (organizationOwnerJhiUserId != null ? "organizationOwnerJhiUserId=" + organizationOwnerJhiUserId + ", " : "") +
                (organizationName != null ? "organizationName=" + organizationName + ", " : "") +
                (taxNumber != null ? "taxNumber=" + taxNumber + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
