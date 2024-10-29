package ru.rejchev.steamid.converters;

@FunctionalInterface
public interface ISteamIDConverter {
    Object convert(Object value);
}
