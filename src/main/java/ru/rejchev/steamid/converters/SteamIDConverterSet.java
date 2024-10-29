package ru.rejchev.steamid.converters;

import java.util.*;

public class SteamIDConverterSet implements ISteamIDConverter, Set<ISteamIDConverter> {

    public static SteamIDConverterSet of(ISteamIDConverter... converters) {
        return (new SteamIDConverterSet()).add(Set.of(
                Arrays.stream(converters)
                        .filter(Objects::nonNull)
                        .toArray(ISteamIDConverter[]::new)));
    }

    private final Set<ISteamIDConverter> converters;

    public SteamIDConverterSet() {
        this.converters = new HashSet<>();
    }

    @Override
    public Object convert(Object value) {
        return converters.stream().map(x -> x.convert(value))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public int size() {
        return converters.size();
    }

    @Override
    public boolean isEmpty() {
        return converters.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return converters.contains(o);
    }

    @Override
    public Iterator<ISteamIDConverter> iterator() {
        return converters.iterator();
    }

    @Override
    public Object[] toArray() {
        return converters.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return converters.toArray(a);
    }

    @Override
    public boolean add(ISteamIDConverter iSteamIDConverter) {
        return converters.add(iSteamIDConverter);
    }

    public SteamIDConverterSet add(Collection<? extends ISteamIDConverter> c) {
        addAll(c);
        return this;
    }

    @Override
    public boolean remove(Object o) {
        return converters.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return converters.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends ISteamIDConverter> c) {
        return converters.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return converters.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return converters.removeAll(c);
    }

    @Override
    public void clear() {
        converters.clear();
    }
}
