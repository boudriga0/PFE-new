package com.reclamation.pfe.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PersonneTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Personne getPersonneSample1() {
        return new Personne().id(1L).nom("nom1").prenom("prenom1").cIN("cIN1").phone("phone1").rib("rib1").email("email1").sex("sex1");
    }

    public static Personne getPersonneSample2() {
        return new Personne().id(2L).nom("nom2").prenom("prenom2").cIN("cIN2").phone("phone2").rib("rib2").email("email2").sex("sex2");
    }

    public static Personne getPersonneRandomSampleGenerator() {
        return new Personne()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .cIN(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .rib(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .sex(UUID.randomUUID().toString());
    }
}
