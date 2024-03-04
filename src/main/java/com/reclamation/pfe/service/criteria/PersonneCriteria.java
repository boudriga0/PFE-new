package com.reclamation.pfe.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.reclamation.pfe.domain.Personne} entity. This class is used
 * in {@link com.reclamation.pfe.web.rest.PersonneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /personnes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonneCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter prenom;

    private StringFilter cIN;

    private LocalDateFilter dateNaissance;

    private StringFilter phone;

    private StringFilter rib;

    private StringFilter email;

    private StringFilter sex;

    private LongFilter reclamationId;

    private Boolean distinct;

    public PersonneCriteria() {}

    public PersonneCriteria(PersonneCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.cIN = other.cIN == null ? null : other.cIN.copy();
        this.dateNaissance = other.dateNaissance == null ? null : other.dateNaissance.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.rib = other.rib == null ? null : other.rib.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.sex = other.sex == null ? null : other.sex.copy();
        this.reclamationId = other.reclamationId == null ? null : other.reclamationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PersonneCriteria copy() {
        return new PersonneCriteria(this);
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

    public StringFilter getNom() {
        return nom;
    }

    public StringFilter nom() {
        if (nom == null) {
            nom = new StringFilter();
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public StringFilter prenom() {
        if (prenom == null) {
            prenom = new StringFilter();
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
    }

    public StringFilter getcIN() {
        return cIN;
    }

    public StringFilter cIN() {
        if (cIN == null) {
            cIN = new StringFilter();
        }
        return cIN;
    }

    public void setcIN(StringFilter cIN) {
        this.cIN = cIN;
    }

    public LocalDateFilter getDateNaissance() {
        return dateNaissance;
    }

    public LocalDateFilter dateNaissance() {
        if (dateNaissance == null) {
            dateNaissance = new LocalDateFilter();
        }
        return dateNaissance;
    }

    public void setDateNaissance(LocalDateFilter dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getRib() {
        return rib;
    }

    public StringFilter rib() {
        if (rib == null) {
            rib = new StringFilter();
        }
        return rib;
    }

    public void setRib(StringFilter rib) {
        this.rib = rib;
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

    public StringFilter getSex() {
        return sex;
    }

    public StringFilter sex() {
        if (sex == null) {
            sex = new StringFilter();
        }
        return sex;
    }

    public void setSex(StringFilter sex) {
        this.sex = sex;
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
        final PersonneCriteria that = (PersonneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(cIN, that.cIN) &&
            Objects.equals(dateNaissance, that.dateNaissance) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(rib, that.rib) &&
            Objects.equals(email, that.email) &&
            Objects.equals(sex, that.sex) &&
            Objects.equals(reclamationId, that.reclamationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, cIN, dateNaissance, phone, rib, email, sex, reclamationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonneCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (prenom != null ? "prenom=" + prenom + ", " : "") +
            (cIN != null ? "cIN=" + cIN + ", " : "") +
            (dateNaissance != null ? "dateNaissance=" + dateNaissance + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (rib != null ? "rib=" + rib + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (sex != null ? "sex=" + sex + ", " : "") +
            (reclamationId != null ? "reclamationId=" + reclamationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
