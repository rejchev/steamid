package ru.rejchev.steamid.converters;

import ru.rejchev.steamid.SteamID;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SteamIDConverterMap {

    private final Map<String, ISteamIDConverter> converterMap;

    public SteamIDConverterMap() {
        converterMap = new HashMap<>();
    }

    public <T> T get(String type, Class<T> clazz) {
        try { return clazz.cast(converterMap.get(type)); }
        catch (ClassCastException ignored) {}

        return null;
    }

    public SteamID convert(String steamId) {
        return convert(steamId, SteamID.class, "*");
    }

    public <T, R> R convert(T value, Class<R> clazzCast, final String steamIDViewType) {
        if(value == null || steamIDViewType == null || steamIDViewType.isEmpty())
            return null;

        return clazzCast.cast(converterMap.entrySet().stream()
                .filter(x -> steamIDViewType.equals("*") || x.getKey().equals(steamIDViewType))
                .map(x -> x.getValue().convert(value))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null));
    }

    public boolean exist(String type) {
        return converterMap.containsKey(type);
    }

    public ISteamIDConverter get(String type) { return converterMap.get(type); }

    public synchronized void register(String type, ISteamIDConverter converter) {
        converterMap.put(type, converter);
    }

    public synchronized void unregister(String type) {
        converterMap.remove(type);
    }
}
