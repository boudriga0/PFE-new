package com.reclamation.pfe.domain;

import static com.reclamation.pfe.domain.PieceJointeTestSamples.*;
import static com.reclamation.pfe.domain.ReclamationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reclamation.pfe.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PieceJointeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PieceJointe.class);
        PieceJointe pieceJointe1 = getPieceJointeSample1();
        PieceJointe pieceJointe2 = new PieceJointe();
        assertThat(pieceJointe1).isNotEqualTo(pieceJointe2);

        pieceJointe2.setId(pieceJointe1.getId());
        assertThat(pieceJointe1).isEqualTo(pieceJointe2);

        pieceJointe2 = getPieceJointeSample2();
        assertThat(pieceJointe1).isNotEqualTo(pieceJointe2);
    }

    @Test
    void reclamationTest() throws Exception {
        PieceJointe pieceJointe = getPieceJointeRandomSampleGenerator();
        Reclamation reclamationBack = getReclamationRandomSampleGenerator();

        pieceJointe.setReclamation(reclamationBack);
        assertThat(pieceJointe.getReclamation()).isEqualTo(reclamationBack);

        pieceJointe.reclamation(null);
        assertThat(pieceJointe.getReclamation()).isNull();
    }
}
