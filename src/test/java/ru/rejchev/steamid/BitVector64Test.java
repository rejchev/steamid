package ru.rejchev.steamid;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rejchev.steamid.containers.BitVector64;

import java.util.Arrays;
import java.util.function.LongUnaryOperator;

public class BitVector64Test {

    public static final long[] sourceData = {
            0,
            76561198092541763L,
            Long.MAX_VALUE
    };

    @Test
    void thatGet_0_0x0L_correct() {
        final char offset = 0;
        final long mask = 0x0L;

        final long[] expectedData = { 0, 0, 0 };

        assertArrayEquals(simpleGetMethodMapper(offset, mask), expectedData);
    }

    @Test
    void thatGet_0_0xFL_correct() {
        final char offset = 0;
        final long mask = 0xFFFF_FFFF_FFFF_FFFFL;

        assertArrayEquals(simpleGetMethodMapper(offset, mask), sourceData);
    }

    @Test
    void thatGet_1_0xF_correct() {

        final char offset = 1;
        final long mask = 0xFFFF_FFFF_FFFF_FFFFL;

        final long[] expected = { 0, 0x88_0000_83F1_2FA1L, 0x3FFF_FFFF_FFFF_FFFFL };

        assertArrayEquals(simpleGetMethodMapper(offset, mask), expected);
    }

    void thatSet_s_0_0x0_correct() {
        final char offset = 0;
        final long mask = 0x0L;
        final long value = 0x0000_FFFF_0000_FFFFL; // FFFF 0000 FFFF 0000

        final long[] expected = sourceData;

        // source[1]:
        // ~(mask << offset) = 0xFFFF_FFFF_FFFF_FFFF
        // (source & 0xFFFF_FFFF_FFFF_FFFF) = source
        // ((value & mask) << offset = 0
        // 0x0110_0000_07E2_0000 | 0 = 0x0110_0000_07E2_0000

        // source[2]
        // (source & 0xFFFF_FFFF_FFFF_FFFF) = source

        Assertions.assertEquals(0, part1OfSetMethod(value, offset, mask));
        assertArrayEquals(simpleSetMethodMapper(value, offset, mask), expected);
    }

    static LongUnaryOperator simpleGetMethodMapper(char offset, long mask) {
        return operand -> (new BitVector64(operand)).get(offset, mask);
    }

    static LongUnaryOperator simpleSetMethodMapper(long value, char offset, long mask) {
        return operand -> (new BitVector64(operand)).set(value, offset, mask).data();
    }

    void assertArrayEquals(LongUnaryOperator mapper, long[] expected) {
        Assertions.assertArrayEquals(expected, Arrays.stream(BitVector64Test.sourceData).map(mapper).toArray());
    }

    long part1OfSetMethod(long value, char offset, long mask) {
        return ((value & mask) << offset);
    }

    long part0OfSetMethod(long data, char offset, long mask) {
        return (data & ~(mask << offset));
    }

    long combineSetMethod(long data, long value, char offset, long mask) {
        return part0OfSetMethod(data, offset, mask) | part1OfSetMethod(value, offset, mask);
    }
}
