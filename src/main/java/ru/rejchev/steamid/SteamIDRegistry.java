package ru.rejchev.steamid;

import ru.rejchev.steamid.converters.SteamIDConverterMap;
import ru.rejchev.steamid.converters.SteamIDConverterSet;
import ru.rejchev.steamid.converters.steam2.Steam2StringConverter;
import ru.rejchev.steamid.converters.steam2.StringSteam2Converter;
import ru.rejchev.steamid.converters.steam3.Steam3StringConverter;
import ru.rejchev.steamid.converters.steam3.StringSteam3Converter;

import java.util.regex.Pattern;

public final class SteamIDRegistry {

    private static volatile SteamIDConverterMap conv;

    public static SteamIDConverterMap converters() {
        SteamIDConverterMap m;
        if((m = conv) == null) {
            synchronized (SteamIDRegistry.class) {
                if((m = conv) == null) {
                    conv = m = new SteamIDConverterMap();

                    conv.register("Steam2", SteamIDConverterSet.of(
                            StringSteam2Converter.of(),
                            Steam2StringConverter.of()));

                    conv.register("Steam3", SteamIDConverterSet.of(
                            StringSteam3Converter.of(
                                    StringSteam3Converter.BasePattern,
                                    Pattern.compile("\\[(?<type>[AGMPCgcLTIUai]):(?<universe>[0-4]):(?<account>[0-9]+)(\\((?<instance>[0-9]+)\\))?]")),
                            Steam3StringConverter.of()));
                }
            }
        }

        return conv;
    }

    private static volatile SteamIDAccountType typeMap;

    public static SteamIDAccountType types() {
        SteamIDAccountType m;
        if((m = typeMap) == null) {
            synchronized (SteamIDRegistry.class) {
                if ((m = typeMap) == null) {
                    typeMap = m = new SteamIDAccountType();
                }

            }
        }

        return typeMap;
    }
}
