package ru.rejchev.steamid.converters.steam2;

import ru.rejchev.steamid.SteamID;
import ru.rejchev.steamid.SteamIDAccountType;
import ru.rejchev.steamid.converters.ISteamIDConverter;

public class Steam2StringConverter implements ISteamIDConverter {

    public static Steam2StringConverter of() {
        return new Steam2StringConverter();
    }

    @Override
    public String convert(Object value) {

        if(!(value instanceof SteamID steamID))
            return null;

        if(steamID.getAccountType() != SteamIDAccountType.Number.Invalid.ordinal()
        && steamID.getAccountType() != SteamIDAccountType.Number.Individual.ordinal())
            return null;

        return "STEAM_" + steamID.getAccountUniverse()
                + ":" + (steamID.getAccountID() & 1)
                + ":" + (steamID.getAccountID() >> 1);
    }
}
