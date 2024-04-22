package com.reclamation.pfe.service.dto;

import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.reclamation.pfe.domain.Reclamation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReclamationDTO implements Serializable {

    private Long id;

    private String categorie;
    private String email;


    private String etat;

    private String numero;

    private LocalDate date;

    private PersonneDTO personne;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public PersonneDTO getPersonne() {
        return personne;
    }

    public void setPersonne(PersonneDTO personne) {
        this.personne = personne;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReclamationDTO)) {
            return false;
        }

        ReclamationDTO reclamationDTO = (ReclamationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reclamationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReclamationDTO{" +
            "id=" + getId() +
            ", categorie='" + getCategorie() + "'" +
            ", etat='" + getEtat() + "'" +
            ", numero='" + getNumero() + "'" +
            ", date='" + getDate() + "'" +
            ", personne=" + getPersonne() +
            ", email=" + getEmail() +
            "}";
    }
}
