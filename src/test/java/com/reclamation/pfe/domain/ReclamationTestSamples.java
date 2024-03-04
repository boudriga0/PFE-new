package com.reclamation.pfe.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReclamationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reclamation getReclamationSample1() {
        return new Reclamation().id(1L).categorie("categorie1").etat("etat1").numero("numero1");
    }

    public static Reclamation getReclamationSample2() {
        return new Reclamation().id(2L).categorie("categorie2").etat("etat2").numero("numero2");
    }

    public static Reclamation getReclamationRandomSampleGenerator() {
        return new Reclamation()
            .id(longCount.incrementAndGet())
            .categorie(UUID.randomUUID().toString())
            .etat(UUID.randomUUID().toString())
            .numero(UUID.randomUUID().toString());
    }
}
