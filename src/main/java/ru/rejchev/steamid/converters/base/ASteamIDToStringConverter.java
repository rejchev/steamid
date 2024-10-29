package ru.rejchev.steamid.converters.base;

import ru.rejchev.steamid.SteamID;
import ru.rejchev.steamid.converters.ISteamIDConverter;

public abstract class ASteamIDToStringConverter implements ISteamIDConverter {

    private final String pattern;

    public ASteamIDToStringConverter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String convert(Object value) {

        if(!(value instanceof SteamID steamId))
            return null;

        return steamId.toString();
    }

    protected String getPattern() {
        return pattern;
    }
}
