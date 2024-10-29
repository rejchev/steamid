package ru.rejchev.steamid.containers;

import ru.rejchev.steamid.SteamID;

import java.util.Objects;

/**
 * Class {@link BitVector64} is  a container that
 * provides bit manipulation features onto unsigned data
 */
public class BitVector64 {

    private long data;

    public BitVector64(long data) {
        this.data = data;
    }

    /**
     * @return {@link BitVector64#data} e.g {@code SteamID64} representation
     */
    public long data() {
        return data;
    }


    /**
     * @param offset power of unsigned right shift
     * @param mask bit mask (e.g. *Mask {@link SteamID} constants)
     * @return encoded part of {@link BitVector64#data}
     */
    public long get(char offset, long mask) {
        return (data >>> offset) & mask;
    }

    /**
     * @param value some value to write in {@link BitVector64#data}
     * @param offset power of unsigned right shift
     * @param mask bit mask (e.g. *Mask {@link SteamID} constants)
     */
    public void set(long value, char offset, long mask) {
        data = (data & ~(mask << offset)) | ((value & mask) << offset);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        return data() == ((BitVector64) o).data();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data());
    }

    /**
     * @return {@link SteamID.BitVector64#data()}
     */
    @Override
    public String toString() {
        return "" + data();
    }
}
