package com.reclamation.pfe.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.reclamation.pfe.domain.Commentaire} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommentaireDTO implements Serializable {

    private Long id;

    private String contenu;

    private ReclamationDTO reclamation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public ReclamationDTO getReclamation() {
        return reclamation;
    }

    public void setReclamation(ReclamationDTO reclamation) {
        this.reclamation = reclamation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentaireDTO)) {
            return false;
        }

        CommentaireDTO commentaireDTO = (CommentaireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentaireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentaireDTO{" +
            "id=" + getId() +
            ", contenu='" + getContenu() + "'" +
            ", reclamation=" + getReclamation() +
            "}";
    }
}
