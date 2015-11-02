package com.catalyst.express.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Muffin.
 */
@Entity
@Table(name = "muffin")
public class Muffin implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Min(value = 0)
    @Column(name = "cost")
    private Double cost;

    @Column(name = "vendor")
    private String vendor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Muffin muffin = (Muffin) o;

        if ( ! Objects.equals(id, muffin.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Muffin{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", cost='" + cost + "'" +
            ", vendor='" + vendor + "'" +
            '}';
    }
}
