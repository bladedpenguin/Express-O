package com.catalyst.express.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Allergen.
 */
@Entity
@Table(name = "allergen")
public class Allergen implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "allergens")
    @JsonIgnore
    private Set<Muffin> muffins = new HashSet<>();

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

    public Set<Muffin> getMuffins() {
        return muffins;
    }

    public void setMuffins(Set<Muffin> muffins) {
        this.muffins = muffins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Allergen allergen = (Allergen) o;

        if ( ! Objects.equals(id, allergen.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Allergen{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
