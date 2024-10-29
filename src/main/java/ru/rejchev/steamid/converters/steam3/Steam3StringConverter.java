package ru.rejchev.steamid.converters.steam3;

import ru.rejchev.steamid.SteamID;
import ru.rejchev.steamid.SteamIDAccountType;
import ru.rejchev.steamid.converters.ISteamIDConverter;

public class Steam3StringConverter implements ISteamIDConverter {

    public static Steam3StringConverter of() {
        return new Steam3StringConverter();
    }

    @Override
    public String convert(Object value) {
        if(!(value instanceof SteamID steamID))
            return null;

        final StringBuilder builder = (new StringBuilder("["))
                .append(SteamIDAccountType.getCharacterViaSteamID(steamID))
                .append(":").append(steamID.getAccountUniverse().ordinal())
                .append(":").append(steamID.getAccountID());

        if(steamID.getAccountType() == SteamIDAccountType.AnonGameServer
        || steamID.getAccountType() == SteamIDAccountType.Multiseat)
            builder.append(":").append(steamID.getInstance());

        return builder.append("]").toString();
    }
}
