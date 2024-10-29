package ru.rejchev.steamid.converters.steam3;

import ru.rejchev.steamid.SteamID;
import ru.rejchev.steamid.SteamIDAccountType;
import ru.rejchev.steamid.SteamIDUniverse;
import ru.rejchev.steamid.converters.base.AStringSteamIDConverter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class StringSteam3Converter extends AStringSteamIDConverter {

    public static final Pattern BasePattern = Pattern
            .compile("\\[(?<type>[AGMPCgcLTIUai]):(?<universe>[0-4]):(?<account>[0-9]+)(:(?<instance>[0-9]+))?]");

    public static final String[] RequiredBinds = { "type", "universe", "account" };
    public static final String[] OptionalBinds = { "instance" };

    public static StringSteam3Converter of(Pattern... patterns) {
        if(patterns != null)
            patterns = Arrays.stream(patterns)
                    .filter(Objects::nonNull)
                    .filter(x -> Arrays.stream(RequiredBinds).allMatch(y -> x.pattern().contains(y)))
                    .toArray(Pattern[]::new);

        if(patterns == null || patterns.length == 0)
            patterns = new Pattern[] { BasePattern };

        return (new StringSteam3Converter()).add(Set.of(patterns));
    }

    @Override
    public SteamID convert(Object value) {

        SteamID steamID;
        if((steamID = super.convert(value)) == null)
            return null;

        // TODO: ...
        try { steamID
                .setAccountId((Long.parseUnsignedLong(getMatcher().group(RequiredBinds[2]))))
                .setInstance(1)
                .setAccountUniverse(SteamIDUniverse.values()[Integer.parseUnsignedInt(getMatcher().group(RequiredBinds[1]))])
                .setAccountType(SteamIDAccountType.of(Integer.parseUnsignedInt(getMatcher().group(OptionalBinds[0]))));

                if(getMatcher().group(OptionalBinds[1]) != null)
                    steamID.setInstance(Long.parseUnsignedLong(getMatcher().group(OptionalBinds[0])));
        }
        catch (NumberFormatException e) { return null; }

        return steamID;
    }

    @Override
    public StringSteam3Converter add(Collection<? extends Pattern> patterns) {
        return (StringSteam3Converter) super.add(patterns);
    }
}
