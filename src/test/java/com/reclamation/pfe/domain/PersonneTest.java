package com.reclamation.pfe.domain;

import static com.reclamation.pfe.domain.PersonneTestSamples.*;
import static com.reclamation.pfe.domain.ReclamationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reclamation.pfe.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PersonneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personne.class);
        Personne personne1 = getPersonneSample1();
        Personne personne2 = new Personne();
        assertThat(personne1).isNotEqualTo(personne2);

        personne2.setId(personne1.getId());
        assertThat(personne1).isEqualTo(personne2);

        personne2 = getPersonneSample2();
        assertThat(personne1).isNotEqualTo(personne2);
    }

    @Test
    void reclamationTest() throws Exception {
        Personne personne = getPersonneRandomSampleGenerator();
        Reclamation reclamationBack = getReclamationRandomSampleGenerator();

        personne.addReclamation(reclamationBack);
        assertThat(personne.getReclamations()).containsOnly(reclamationBack);
        assertThat(reclamationBack.getPersonne()).isEqualTo(personne);

        personne.removeReclamation(reclamationBack);
        assertThat(personne.getReclamations()).doesNotContain(reclamationBack);
        assertThat(reclamationBack.getPersonne()).isNull();

        personne.reclamations(new HashSet<>(Set.of(reclamationBack)));
        assertThat(personne.getReclamations()).containsOnly(reclamationBack);
        assertThat(reclamationBack.getPersonne()).isEqualTo(personne);

        personne.setReclamations(new HashSet<>());
        assertThat(personne.getReclamations()).doesNotContain(reclamationBack);
        assertThat(reclamationBack.getPersonne()).isNull();
    }
}
