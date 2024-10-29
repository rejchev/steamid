package ru.rejchev.steamid;

public enum SteamIDMaskType {

    AccountID((char) 0, 0x00000000FFFFFFFFL),
    AccountInstance((char)32, 0x00000000000FFFFF),
    AccountType((char) 52, 0x000000000000000F),
    AccountUniverse((char)56, 0x00000000000000FF);

    private final char offset;

    private final long mask;

    SteamIDMaskType(char offset, long mask) {
        this.offset = offset;
        this.mask = mask;
    }

    public char getOffset() {
        return offset;
    }

    public long getMask() {
        return mask;
    }
}
