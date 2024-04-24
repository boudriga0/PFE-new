package com.reclamation.pfe.service.criteria;

import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.reclamation.pfe.domain.Reclamation} entity. This class is used
 * in {@link com.reclamation.pfe.web.rest.ReclamationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reclamations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReclamationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter categorie;
    private StringFilter piece;
    private StringFilter email;

    private StringFilter etat;

    private StringFilter numero;

    private LocalDateFilter date;
    private StringFilter isDeveloping;

    private LongFilter pieceJointeId;

    private LongFilter commentaireId;

    private LongFilter personneId;

    private Boolean distinct;

    public ReclamationCriteria() {}

    public ReclamationCriteria(ReclamationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.categorie = other.categorie == null ? null : other.categorie.copy();
        this.piece = other.piece == null ? null : other.piece.copy();

        this.email = other.email == null ? null : other.email.copy();
        this.etat = other.etat == null ? null : other.etat.copy();
        this.numero = other.numero == null ? null : other.numero.copy();
        this.isDeveloping = other.isDeveloping == null ? null : other.isDeveloping.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.pieceJointeId = other.pieceJointeId == null ? null : other.pieceJointeId.copy();
        this.commentaireId = other.commentaireId == null ? null : other.commentaireId.copy();
        this.personneId = other.personneId == null ? null : other.personneId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ReclamationCriteria copy() {
        return new ReclamationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCategorie() {
        return categorie;
    }


    public StringFilter categorie() {
        if (categorie == null) {
            categorie = new StringFilter();
        }
        return categorie;
    }

    public void setCategorie(StringFilter categorie) {
        this.categorie = categorie;
    }
    public StringFilter getPiece() {
        return piece;
    }


    public StringFilter piece() {
        if (piece == null) {
            piece = new StringFilter();
        }
        return piece;
    }

    public void setPiece(StringFilter piece) {
        this.piece = piece;
    }



    public StringFilter getEmail() {
        return email;
    }


    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }


    public StringFilter getIsDeveloping() {
        return isDeveloping;
    }

    public StringFilter isDeveloping() {
        if (isDeveloping == null) {
            isDeveloping = new StringFilter();
        }
        return isDeveloping;
    }

    public void setIsDeveloping(StringFilter isDeveloping) {
        this.isDeveloping = isDeveloping;
    }

    public StringFilter getEtat() {
        return etat;
    }

    public StringFilter etat() {
        if (etat == null) {
            etat = new StringFilter();
        }
        return etat;
    }

    public void setEtat(StringFilter etat) {
        this.etat = etat;
    }

    public StringFilter getNumero() {
        return numero;
    }

    public StringFilter numero() {
        if (numero == null) {
            numero = new StringFilter();
        }
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public LongFilter getPieceJointeId() {
        return pieceJointeId;
    }

    public LongFilter pieceJointeId() {
        if (pieceJointeId == null) {
            pieceJointeId = new LongFilter();
        }
        return pieceJointeId;
    }

    public void setPieceJointeId(LongFilter pieceJointeId) {
        this.pieceJointeId = pieceJointeId;
    }

    public LongFilter getCommentaireId() {
        return commentaireId;
    }

    public LongFilter commentaireId() {
        if (commentaireId == null) {
            commentaireId = new LongFilter();
        }
        return commentaireId;
    }

    public void setCommentaireId(LongFilter commentaireId) {
        this.commentaireId = commentaireId;
    }

    public LongFilter getPersonneId() {
        return personneId;
    }

    public LongFilter personneId() {
        if (personneId == null) {
            personneId = new LongFilter();
        }
        return personneId;
    }

    public void setPersonneId(LongFilter personneId) {
        this.personneId = personneId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReclamationCriteria that = (ReclamationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(categorie, that.categorie) &&
                Objects.equals(piece, that.piece) &&
                Objects.equals(etat, that.etat) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(date, that.date) &&
            Objects.equals(isDeveloping, that.isDeveloping) &&
            Objects.equals(pieceJointeId, that.pieceJointeId) &&
            Objects.equals(commentaireId, that.commentaireId) &&
            Objects.equals(personneId, that.personneId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categorie ,piece ,email, isDeveloping, etat, numero, date, pieceJointeId, commentaireId, personneId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReclamationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (categorie != null ? "categorie=" + categorie + ", " : "") +
            (categorie != null ? "isDeveloping=" + isDeveloping + ", " : "") +
            (piece != null ? "piece=" + piece + ", " : "") +
            (piece != null ? "isDeveloping=" + isDeveloping + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (email != null ? "isDeveloping=" + isDeveloping + ", " : "") +
            (etat != null ? "etat=" + etat + ", " : "") +
            (numero != null ? "numero=" + numero + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (pieceJointeId != null ? "pieceJointeId=" + pieceJointeId + ", " : "") +
            (commentaireId != null ? "commentaireId=" + commentaireId + ", " : "") +
            (personneId != null ? "personneId=" + personneId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
