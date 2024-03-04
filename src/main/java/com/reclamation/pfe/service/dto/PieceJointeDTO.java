package com.reclamation.pfe.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.reclamation.pfe.domain.PieceJointe} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PieceJointeDTO implements Serializable {

    private Long id;

    private String url;

    private String type;

    private String data;

    private ReclamationDTO reclamation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        if (!(o instanceof PieceJointeDTO)) {
            return false;
        }

        PieceJointeDTO pieceJointeDTO = (PieceJointeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pieceJointeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PieceJointeDTO{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", type='" + getType() + "'" +
            ", data='" + getData() + "'" +
            ", reclamation=" + getReclamation() +
            "}";
    }
}
