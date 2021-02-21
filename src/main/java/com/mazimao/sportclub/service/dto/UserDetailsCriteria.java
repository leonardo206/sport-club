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
 * Criteria class for the {@link com.mazimao.sportclub.domain.UserDetails} entity. This class is used
 * in {@link com.mazimao.sportclub.web.rest.UserDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserDetailsCriteria implements Serializable, Criteria {

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

    private StringFilter mobileNumber;

    private ZonedDateTimeFilter dateOfBirth;

    private StringFilter description;

    private ActiveInactiveStatusFilter status;

    private StringFilter userId;

    public UserDetailsCriteria() {}

    public UserDetailsCriteria(UserDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.mobileNumber = other.mobileNumber == null ? null : other.mobileNumber.copy();
        this.dateOfBirth = other.dateOfBirth == null ? null : other.dateOfBirth.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public UserDetailsCriteria copy() {
        return new UserDetailsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(StringFilter mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public ZonedDateTimeFilter getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(ZonedDateTimeFilter dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserDetailsCriteria that = (UserDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(mobileNumber, that.mobileNumber) &&
            Objects.equals(dateOfBirth, that.dateOfBirth) &&
            Objects.equals(description, that.description) &&
            Objects.equals(status, that.status) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mobileNumber, dateOfBirth, description, status, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDetailsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (mobileNumber != null ? "mobileNumber=" + mobileNumber + ", " : "") +
                (dateOfBirth != null ? "dateOfBirth=" + dateOfBirth + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
