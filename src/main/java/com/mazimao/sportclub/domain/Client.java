package com.mazimao.sportclub.domain;

import com.mazimao.sportclub.domain.enumeration.ActiveInactiveStatus;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Client.
 */
@Entity
@Table(name = "sc_client")
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActiveInactiveStatus status;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "client")
    private Set<Booking> bookings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActiveInactiveStatus getStatus() {
        return status;
    }

    public Client status(ActiveInactiveStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ActiveInactiveStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public Client user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public Client bookings(Set<Booking> bookings) {
        this.bookings = bookings;
        return this;
    }

    public Client addBookings(Booking booking) {
        this.bookings.add(booking);
        booking.setClient(this);
        return this;
    }

    public Client removeBookings(Booking booking) {
        this.bookings.remove(booking);
        booking.setClient(null);
        return this;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
