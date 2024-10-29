package ru.rejchev.steamid.converters.steam3;

import ru.rejchev.steamid.SteamID;
import ru.rejchev.steamid.converters.base.AStringSteamIDConverter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class StringSteam3Converter extends AStringSteamIDConverter {

    public static final Pattern BasePattern = Pattern
            .compile("\\[(?<type>[AGMPCgcLTIUai]):(?<universe>[0-4]):(?<account>[0-9]+)(:(?<instance>[0-9]+))?]");

    public static final String[] RequiredBinds = { "type", "universe", "account", "instance" };

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
        try {  }
        catch (NumberFormatException e) { return null; }

        return steamID;
    }

    @Override
    public StringSteam3Converter add(Collection<? extends Pattern> patterns) {
        return (StringSteam3Converter) super.add(patterns);
    }
}
