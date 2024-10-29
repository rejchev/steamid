package ru.rejchev.steamid;

import java.util.Arrays;

// e.g EAccountType
public enum SteamIDAccountType {

    Invalid('I'),
    Individual('U'),
    Multiseat('M'),
    GameServer('G'),
    AnonGameServer('A'),
    Pending('P'),
    ContentServer('C'),
    Clan('g'),
    Chat('T'),
    P2PSuperSeeder('\0'),
    AnonUser('a');

    public static final char InvalidCharacter = 'i';

    public static char getCharacterViaSteamID(SteamID steamID) {
        if(steamID == null || steamID.getAccountType() == null)
            return InvalidCharacter;

        if(steamID.getAccountType() != Chat)
            return steamID.getAccountType().getChar();

        if(steamID.getInstanceFlags() == ((SteamIDMaskType.AccountInstance.getMask() + 1) >> 1))
            return 'c';

        if(steamID.getInstanceFlags() == ((SteamIDMaskType.AccountInstance.getMask() + 1) >> 2))
            return 'L';

        return steamID.getAccountType().getChar();
    }

    public static SteamIDAccountType of(int id) {
        return values()[id];
    }

    public static SteamIDAccountType of(char value) {
        return Arrays.stream(values()).filter(x -> x.getChar() == value).findFirst().orElse(null);
    }

    private final char character;

    SteamIDAccountType(char c) {
        this.character = c;
    }

    public char getChar() {
        return character;
    }
}
