package com.reclamation.pfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reclamation.
 */
@Entity
@Table(name = "reclamation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reclamation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "categorie")
    private String categorie;

    @Column(name = "email")
    private String email;

    @Column(name = "etat")
    private String etat;

    @Column(name = "numero")
    private String numero;

    @Column(name = "date")
    private LocalDate date;

    @Lob
    @Column(name = "jointpiece")
    private byte[] jointpiece;

    @Column(name = "jointpiece_content_type")
    private String jointpieceContentType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reclamation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reclamation" }, allowSetters = true)
    private Set<PieceJointe> pieceJointes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reclamation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reclamation" }, allowSetters = true)
    private Set<Commentaire> commentaires = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reclamations" }, allowSetters = true)
    private Personne personne;

    public Long getId() {
        return this.id;
    }

    public Reclamation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategorie() {
        return this.categorie;
    }

    public Reclamation categorie(String categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getEmail() {
        return this.email;
    }

    public Reclamation email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEtat() {
        return this.etat;
    }

    public Reclamation etat(String etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getNumero() {
        return this.numero;
    }

    public Reclamation numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Reclamation date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<PieceJointe> getPieceJointes() {
        return this.pieceJointes;
    }

    public void setPieceJointes(Set<PieceJointe> pieceJointes) {
        if (this.pieceJointes != null) {
            this.pieceJointes.forEach(i -> i.setReclamation(null));
        }
        if (pieceJointes != null) {
            pieceJointes.forEach(i -> i.setReclamation(this));
        }
        this.pieceJointes = pieceJointes;
    }

    public Reclamation pieceJointes(Set<PieceJointe> pieceJointes) {
        this.setPieceJointes(pieceJointes);
        return this;
    }

    public Reclamation addPieceJointe(PieceJointe pieceJointe) {
        this.pieceJointes.add(pieceJointe);
        pieceJointe.setReclamation(this);
        return this;
    }

    public Reclamation removePieceJointe(PieceJointe pieceJointe) {
        this.pieceJointes.remove(pieceJointe);
        pieceJointe.setReclamation(null);
        return this;
    }

    public String getJointpieceContentType() {
        return jointpieceContentType;
    }

    public void setJointpieceContentType(String jointpieceContentType) {
        this.jointpieceContentType = jointpieceContentType;
    }

    public Set<Commentaire> getCommentaires() {
        return this.commentaires;
    }

    public void setCommentaires(Set<Commentaire> commentaires) {
        if (this.commentaires != null) {
            this.commentaires.forEach(i -> i.setReclamation(null));
        }
        if (commentaires != null) {
            commentaires.forEach(i -> i.setReclamation(this));
        }
        this.commentaires = commentaires;
    }

    public Reclamation commentaires(Set<Commentaire> commentaires) {
        this.setCommentaires(commentaires);
        return this;
    }

    public Reclamation addCommentaire(Commentaire commentaire) {
        this.commentaires.add(commentaire);
        commentaire.setReclamation(this);
        return this;
    }

    public Reclamation removeCommentaire(Commentaire commentaire) {
        this.commentaires.remove(commentaire);
        commentaire.setReclamation(null);
        return this;
    }

    public Personne getPersonne() {
        return this.personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public Reclamation personne(Personne personne) {
        this.setPersonne(personne);
        return this;
    }

    public byte[] getJointpiece() {
        return jointpiece;
    }

    public void setJointpiece(byte[] jointpiece) {
        this.jointpiece = jointpiece;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reclamation)) {
            return false;
        }
        return getId() != null && getId().equals(((Reclamation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore


    @Override
    public String toString() {
        return "Reclamation{" +
            "id=" + id +
            ", categorie='" + categorie + '\'' +
            ", email='" + email + '\'' +
            ", etat='" + etat + '\'' +
            ", numero='" + numero + '\'' +
            ", date=" + date +
            ", jointpiece=" + Arrays.toString(jointpiece) +
            ", jointpieceContentType='" + jointpieceContentType + '\'' +
            ", pieceJointes=" + pieceJointes +
            ", commentaires=" + commentaires +
            ", personne=" + personne +
            '}';
    }
}
