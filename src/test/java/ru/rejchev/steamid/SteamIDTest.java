package ru.rejchev.steamid;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class SteamIDTest {

    public static final long[] source = {
            76561198092541763L
    };

    @Test
    void thatToString64Conversion_correct() {
        Assertions.assertArrayEquals(sourceAsString(), Arrays.stream(source)
                .mapToObj(x -> (new SteamID(x)).toString())
                .toArray(String[]::new));
    }

    static String[] sourceAsString() {
        return Arrays.stream(source).mapToObj(String::valueOf).toArray(String[]::new);
    }
}
