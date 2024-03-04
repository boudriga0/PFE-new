package com.reclamation.pfe.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.reclamation.pfe.domain.PieceJointe} entity. This class is used
 * in {@link com.reclamation.pfe.web.rest.PieceJointeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /piece-jointes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PieceJointeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter url;

    private StringFilter type;

    private StringFilter data;

    private LongFilter reclamationId;

    private Boolean distinct;

    public PieceJointeCriteria() {}

    public PieceJointeCriteria(PieceJointeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.data = other.data == null ? null : other.data.copy();
        this.reclamationId = other.reclamationId == null ? null : other.reclamationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PieceJointeCriteria copy() {
        return new PieceJointeCriteria(this);
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

    public StringFilter getUrl() {
        return url;
    }

    public StringFilter url() {
        if (url == null) {
            url = new StringFilter();
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getData() {
        return data;
    }

    public StringFilter data() {
        if (data == null) {
            data = new StringFilter();
        }
        return data;
    }

    public void setData(StringFilter data) {
        this.data = data;
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
        final PieceJointeCriteria that = (PieceJointeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(url, that.url) &&
            Objects.equals(type, that.type) &&
            Objects.equals(data, that.data) &&
            Objects.equals(reclamationId, that.reclamationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, type, data, reclamationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PieceJointeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (data != null ? "data=" + data + ", " : "") +
            (reclamationId != null ? "reclamationId=" + reclamationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
