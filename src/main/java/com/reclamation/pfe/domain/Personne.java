package com.reclamation.pfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Personne.
 */
@Entity
@Table(name = "personne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Personne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "c_in")
    private String cIN;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "phone")
    private String phone;

    @Column(name = "rib")
    private String rib;

    @Column(name = "email")
    private String email;

    @Column(name = "sex")
    private String sex;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "personne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pieceJointes", "commentaires", "personne" }, allowSetters = true)
    private Set<Reclamation> reclamations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Personne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Personne nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Personne prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getcIN() {
        return this.cIN;
    }

    public Personne cIN(String cIN) {
        this.setcIN(cIN);
        return this;
    }

    public void setcIN(String cIN) {
        this.cIN = cIN;
    }

    public LocalDate getDateNaissance() {
        return this.dateNaissance;
    }

    public Personne dateNaissance(LocalDate dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getPhone() {
        return this.phone;
    }

    public Personne phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRib() {
        return this.rib;
    }

    public Personne rib(String rib) {
        this.setRib(rib);
        return this;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public String getEmail() {
        return this.email;
    }

    public Personne email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return this.sex;
    }

    public Personne sex(String sex) {
        this.setSex(sex);
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Set<Reclamation> getReclamations() {
        return this.reclamations;
    }

    public void setReclamations(Set<Reclamation> reclamations) {
        if (this.reclamations != null) {
            this.reclamations.forEach(i -> i.setPersonne(null));
        }
        if (reclamations != null) {
            reclamations.forEach(i -> i.setPersonne(this));
        }
        this.reclamations = reclamations;
    }

    public Personne reclamations(Set<Reclamation> reclamations) {
        this.setReclamations(reclamations);
        return this;
    }

    public Personne addReclamation(Reclamation reclamation) {
        this.reclamations.add(reclamation);
        reclamation.setPersonne(this);
        return this;
    }

    public Personne removeReclamation(Reclamation reclamation) {
        this.reclamations.remove(reclamation);
        reclamation.setPersonne(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personne)) {
            return false;
        }
        return getId() != null && getId().equals(((Personne) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personne{" +
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
