package com.reclamation.pfe.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.reclamation.pfe.domain.Commentaire} entity. This class is used
 * in {@link com.reclamation.pfe.web.rest.CommentaireResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /commentaires?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommentaireCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter contenu;

    private LongFilter reclamationId;

    private Boolean distinct;

    public CommentaireCriteria() {}

    public CommentaireCriteria(CommentaireCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.contenu = other.contenu == null ? null : other.contenu.copy();
        this.reclamationId = other.reclamationId == null ? null : other.reclamationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CommentaireCriteria copy() {
        return new CommentaireCriteria(this);
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

    public StringFilter getContenu() {
        return contenu;
    }

    public StringFilter contenu() {
        if (contenu == null) {
            contenu = new StringFilter();
        }
        return contenu;
    }

    public void setContenu(StringFilter contenu) {
        this.contenu = contenu;
    }

    public LongFilter getReclamationId() {
        return reclamationId;
    }

    public LongFilter reclamationId() {
        if (reclamationId == null) {
            reclamationId = new LongFilter();
        }
        return reclamationId;
    }

    public void setReclamationId(LongFilter reclamationId) {
        this.reclamationId = reclamationId;
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
        final CommentaireCriteria that = (CommentaireCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(contenu, that.contenu) &&
            Objects.equals(reclamationId, that.reclamationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contenu, reclamationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentaireCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (contenu != null ? "contenu=" + contenu + ", " : "") +
            (reclamationId != null ? "reclamationId=" + reclamationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
