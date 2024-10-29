package ru.rejchev.steamid.converters.steam3;

import ru.rejchev.steamid.SteamID;
import ru.rejchev.steamid.SteamIDAccountType;
import ru.rejchev.steamid.SteamIDMaskType;
import ru.rejchev.steamid.SteamIDUniverse;
import ru.rejchev.steamid.converters.base.AStringSteamIDConverter;

import java.util.*;
import java.util.regex.Matcher;
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

            char type = buff.charAt(0);

            long instance = (type != 'g' && type != 'T' && type != 'c' && type != 'L') ? 0 : 1;

            if((buff = getMatcher().group(OptionalBinds[0])) != null && !buff.isEmpty())
                instance = Long.parseLong(buff);

            if(type == 'c') {
                instance |= ((SteamIDMaskType.AccountInstance.getMask() + 1) >> 1);
                type = SteamIDAccountType.Chat.getChar();
            }

            if(type == 'L') {
                instance |= ((SteamIDMaskType.AccountInstance.getMask() + 1) >> 2);
                type = SteamIDAccountType.Chat.getChar();
            }

            steamID
                .setAccountId((Long.parseUnsignedLong(getMatcher().group(RequiredBinds[2]))))
                .setInstance(instance)
                .setAccountUniverse(SteamIDUniverse.values()[Integer.parseUnsignedInt(getMatcher().group(RequiredBinds[1]))])
                .setAccountType((type == SteamIDAccountType.InvalidCharacter) ? SteamIDAccountType.Invalid : SteamIDAccountType.of(type));
        }
        catch (NumberFormatException e) { return null; }

        return steamID;
    }

    @Override
    public StringSteam3Converter add(Collection<? extends Pattern> patterns) {
        return (StringSteam3Converter) super.add(patterns);
    }
}
