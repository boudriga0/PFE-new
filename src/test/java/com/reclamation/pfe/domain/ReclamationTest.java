package com.reclamation.pfe.domain;

import static com.reclamation.pfe.domain.CommentaireTestSamples.*;
import static com.reclamation.pfe.domain.PersonneTestSamples.*;
import static com.reclamation.pfe.domain.PieceJointeTestSamples.*;
import static com.reclamation.pfe.domain.ReclamationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reclamation.pfe.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ReclamationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reclamation.class);
        Reclamation reclamation1 = getReclamationSample1();
        Reclamation reclamation2 = new Reclamation();
        assertThat(reclamation1).isNotEqualTo(reclamation2);

        reclamation2.setId(reclamation1.getId());
        assertThat(reclamation1).isEqualTo(reclamation2);

        reclamation2 = getReclamationSample2();
        assertThat(reclamation1).isNotEqualTo(reclamation2);
    }

    @Test
    void pieceJointeTest() throws Exception {
        Reclamation reclamation = getReclamationRandomSampleGenerator();
        PieceJointe pieceJointeBack = getPieceJointeRandomSampleGenerator();

        reclamation.addPieceJointe(pieceJointeBack);
        assertThat(reclamation.getPieceJointes()).containsOnly(pieceJointeBack);
        assertThat(pieceJointeBack.getReclamation()).isEqualTo(reclamation);

        reclamation.removePieceJointe(pieceJointeBack);
        assertThat(reclamation.getPieceJointes()).doesNotContain(pieceJointeBack);
        assertThat(pieceJointeBack.getReclamation()).isNull();

        reclamation.pieceJointes(new HashSet<>(Set.of(pieceJointeBack)));
        assertThat(reclamation.getPieceJointes()).containsOnly(pieceJointeBack);
        assertThat(pieceJointeBack.getReclamation()).isEqualTo(reclamation);

        reclamation.setPieceJointes(new HashSet<>());
        assertThat(reclamation.getPieceJointes()).doesNotContain(pieceJointeBack);
        assertThat(pieceJointeBack.getReclamation()).isNull();
    }

    @Test
    void commentaireTest() throws Exception {
        Reclamation reclamation = getReclamationRandomSampleGenerator();
        Commentaire commentaireBack = getCommentaireRandomSampleGenerator();

        reclamation.addCommentaire(commentaireBack);
        assertThat(reclamation.getCommentaires()).containsOnly(commentaireBack);
        assertThat(commentaireBack.getReclamation()).isEqualTo(reclamation);

        reclamation.removeCommentaire(commentaireBack);
        assertThat(reclamation.getCommentaires()).doesNotContain(commentaireBack);
        assertThat(commentaireBack.getReclamation()).isNull();

        reclamation.commentaires(new HashSet<>(Set.of(commentaireBack)));
        assertThat(reclamation.getCommentaires()).containsOnly(commentaireBack);
        assertThat(commentaireBack.getReclamation()).isEqualTo(reclamation);

        reclamation.setCommentaires(new HashSet<>());
        assertThat(reclamation.getCommentaires()).doesNotContain(commentaireBack);
        assertThat(commentaireBack.getReclamation()).isNull();
    }

    @Test
    void personneTest() throws Exception {
        Reclamation reclamation = getReclamationRandomSampleGenerator();
        Personne personneBack = getPersonneRandomSampleGenerator();

        reclamation.setPersonne(personneBack);
        assertThat(reclamation.getPersonne()).isEqualTo(personneBack);

        reclamation.personne(null);
        assertThat(reclamation.getPersonne()).isNull();
    }
}
