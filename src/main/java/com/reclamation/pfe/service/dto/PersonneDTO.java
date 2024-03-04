package com.reclamation.pfe.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.reclamation.pfe.domain.Personne} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonneDTO implements Serializable {

    private Long id;

    private String nom;

    private String prenom;

    private String cIN;

    private LocalDate dateNaissance;

    private String phone;

    private String rib;

    private String email;

    private String sex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getcIN() {
        return cIN;
    }

    public void setcIN(String cIN) {
        this.cIN = cIN;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonneDTO)) {
            return false;
        }

        PersonneDTO personneDTO = (PersonneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonneDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", cIN='" + getcIN() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", phone='" + getPhone() + "'" +
            ", rib='" + getRib() + "'" +
            ", email='" + getEmail() + "'" +
            ", sex='" + getSex() + "'" +
            "}";
    }
}
