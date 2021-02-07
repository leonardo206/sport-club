package com.mazimao.sportclub.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Organization.
 */
@Entity
@Table(name = "sc_organization")
public class Organization extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_owner_jhi_user_id", unique = true)
    private String organizationOwnerJhiUserId;

    @NotNull
    @Column(name = "organization_name", nullable = false, unique = true)
    private String organizationName;

    @NotNull
    @Pattern(regexp = "^[A-Za-z]{2,4}(?=.{2,12}$)[-_\\s0-9]*(?:[a-zA-Z][-_\\s0-9]*){0,2}$")
    @Column(name = "tax_number", nullable = false, unique = true)
    private String taxNumber;

    @Column(name = "status")
    private String status;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationOwnerJhiUserId() {
        return organizationOwnerJhiUserId;
    }

    public Organization organizationOwnerJhiUserId(String organizationOwnerJhiUserId) {
        this.organizationOwnerJhiUserId = organizationOwnerJhiUserId;
        return this;
    }

    public void setOrganizationOwnerJhiUserId(String organizationOwnerJhiUserId) {
        this.organizationOwnerJhiUserId = organizationOwnerJhiUserId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public Organization organizationName(String organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public Organization taxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
        return this;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getStatus() {
        return status;
    }

    public Organization status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public Organization user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", organizationOwnerJhiUserId='" + getOrganizationOwnerJhiUserId() + "'" +
            ", organizationName='" + getOrganizationName() + "'" +
            ", taxNumber='" + getTaxNumber() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
