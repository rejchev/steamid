package ru.rejchev.steamid.converters.steam3;

import ru.rejchev.steamid.*;
import ru.rejchev.steamid.converters.base.AStringSteamIDConverter;

import java.util.*;
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

        // TODO: testing
        try {

            String buff;
            if((buff = getMatcher().group(RequiredBinds[0])) == null || buff.length() != 1)
                return null;

            SteamIDAccountType.Holder type = SteamIDRegistry.types().getBySignature(buff.charAt(0));

            steamID
                .setAccountId(Long.parseUnsignedLong(getMatcher().group(RequiredBinds[2])))
                .setAccountUniverse(Integer.parseUnsignedInt(getMatcher().group(RequiredBinds[1])))
                .setAccountType(type.id());

            if((buff = getMatcher().group(OptionalBinds[0])) != null && !buff.isEmpty())
                return steamID.setInstance(Long.parseUnsignedLong(buff));

            if(type.id() != SteamIDAccountType.Number.Chat.ordinal())
                return steamID.setInstance(((isZeroInstanceType(type)) ? 0 : 1));

            steamID.setInstance((type.data(Object.class) instanceof Long data) ? data : 0);
        }
        catch (NumberFormatException e) { return null; }

        return steamID;
    }

    @Override
    public StringSteam3Converter add(Collection<? extends Pattern> patterns) {
        return (StringSteam3Converter) super.add(patterns);
    }

    // g; T; L; c
    private boolean isZeroInstanceType(SteamIDAccountType.Holder t) {
        return t.id() == SteamIDAccountType.Number.Clan.ordinal()
            || t.id() == SteamIDAccountType.Number.Chat.ordinal();
    }
}
