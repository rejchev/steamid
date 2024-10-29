package ru.rejchev.steamid;

import ru.rejchev.steamid.converters.ISteamIDConverter;
import ru.rejchev.steamid.converters.SteamIDConverterSet;
import ru.rejchev.steamid.converters.steam2.Steam2StringConverter;
import ru.rejchev.steamid.converters.steam2.StringSteam2Converter;
import ru.rejchev.steamid.converters.steam3.Steam3StringConverter;
import ru.rejchev.steamid.converters.steam3.StringSteam3Converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class SteamIDConverterRegistry {

    private static SteamIDConverterRegistry instance;

    public static SteamIDConverterRegistry getInstance() {
        if(instance == null) {

            synchronized (SteamIDConverterRegistry.class) {
                instance = new SteamIDConverterRegistry();
            }
        }

        return instance;
    }

    public static <T, R> R cast(T value, Class<R> clazz, final String steamIDViewType) {
        return getInstance().convert(value, clazz, steamIDViewType);
    }

    private final Map<String, ISteamIDConverter> registry;

    private SteamIDConverterRegistry() {
        registry = new HashMap<>();

        register("Steam2", SteamIDConverterSet.of(StringSteam2Converter.of(), Steam2StringConverter.of()));
        register("Steam3", SteamIDConverterSet.of(
                StringSteam3Converter.of(
                    StringSteam3Converter.BasePattern,
                    Pattern.compile("\\[(?<type>[AGMPCgcLTIUai]):(?<universe>[0-4]):(?<account>[0-9]+)(\\((?<instance>[0-9]+)\\))?]")),
                Steam3StringConverter.of()
        ));
    }

    public <T> T get(String type, Class<T> clazz) {
        try { return clazz.cast(registry.get(type)); }
        catch (ClassCastException ignored) {}

        return null;
    }

    public SteamID convert(String steamId) {
        return convert(steamId, SteamID.class, "*");
    }

    public <T, R> R convert(T value, Class<R> clazzCast, final String steamIDViewType) {
        if(value == null || steamIDViewType == null || steamIDViewType.isEmpty())
            return null;

        return clazzCast.cast(registry.entrySet().stream()
                .filter(x -> steamIDViewType.equals("*") || x.getKey().equals(steamIDViewType))
                .map(x -> x.getValue().convert(value))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null));
    }

    public boolean exist(String type) {
        return registry.containsKey(type);
    }

    public void register(String type, ISteamIDConverter converter) {
        registry.put(type, converter);
    }

    public void unregister(String type) {
        registry.remove(type);
    }
}
