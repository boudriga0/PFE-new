package com.reclamation.pfe.domain;

import static com.reclamation.pfe.domain.CommentaireTestSamples.*;
import static com.reclamation.pfe.domain.ReclamationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reclamation.pfe.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commentaire.class);
        Commentaire commentaire1 = getCommentaireSample1();
        Commentaire commentaire2 = new Commentaire();
        assertThat(commentaire1).isNotEqualTo(commentaire2);

        commentaire2.setId(commentaire1.getId());
        assertThat(commentaire1).isEqualTo(commentaire2);

        commentaire2 = getCommentaireSample2();
        assertThat(commentaire1).isNotEqualTo(commentaire2);
    }

    @Test
    void reclamationTest() throws Exception {
        Commentaire commentaire = getCommentaireRandomSampleGenerator();
        Reclamation reclamationBack = getReclamationRandomSampleGenerator();

        commentaire.setReclamation(reclamationBack);
        assertThat(commentaire.getReclamation()).isEqualTo(reclamationBack);

        commentaire.reclamation(null);
        assertThat(commentaire.getReclamation()).isNull();
    }
}
