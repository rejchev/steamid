package ru.rejchev.steamid.converters.steam2;

import ru.rejchev.steamid.SteamID;
import ru.rejchev.steamid.SteamIDAccountType;
import ru.rejchev.steamid.SteamIDUniverse;
import ru.rejchev.steamid.converters.base.AStringSteamIDConverter;

import java.util.*;
import java.util.regex.Pattern;

// STEAM_x:y:z:
public class StringSteam2Converter extends AStringSteamIDConverter {

    public static final Pattern BasePattern = Pattern
        .compile("STEAM_(?<universe>[0-4]):(?<authserver>[0-1]):(?<accountid>[0-9]+)", Pattern.CASE_INSENSITIVE);

    public static final String[] RequiredBinds = { "universe", "authserver", "accountid" };

    public static StringSteam2Converter of(Pattern... patterns) {

        if(patterns != null)
            patterns = Arrays.stream(patterns)
                .filter(Objects::nonNull)
                .filter(x -> Arrays.stream(RequiredBinds).allMatch(y -> x.pattern().contains(y)))
                .toArray(Pattern[]::new);

        if(patterns == null || patterns.length == 0)
            patterns = new Pattern[] { BasePattern };

        return (new StringSteam2Converter()).add(Set.of(patterns));
    }

    @Override
    public SteamID convert(Object value) {

        SteamID steamID;

        if((steamID = super.convert(value)) == null)
            return null;

        try { steamID
                .setAccountId((Long.parseLong(getMatcher().group("accountid")) << 1) | Integer.parseInt(getMatcher().group("authserver")))
                .setInstance(1)
                .setAccountUniverse(SteamIDUniverse.values()[Integer.parseInt(getMatcher().group("universe"))])
                .setAccountType(SteamIDAccountType.Individual);
        } catch (NumberFormatException e) { return null; }

        return steamID;
    }

    @Override
    public StringSteam2Converter add(Collection<? extends Pattern> c) {
        return (StringSteam2Converter) super.add(c);
    }
}
