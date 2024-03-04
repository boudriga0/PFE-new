package com.reclamation.pfe.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PieceJointeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PieceJointe getPieceJointeSample1() {
        return new PieceJointe().id(1L).url("url1").type("type1").data("data1");
    }

    public static PieceJointe getPieceJointeSample2() {
        return new PieceJointe().id(2L).url("url2").type("type2").data("data2");
    }

    public static PieceJointe getPieceJointeRandomSampleGenerator() {
        return new PieceJointe()
            .id(longCount.incrementAndGet())
            .url(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .data(UUID.randomUUID().toString());
    }
}
