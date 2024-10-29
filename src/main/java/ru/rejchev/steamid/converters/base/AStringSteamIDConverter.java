package ru.rejchev.steamid.converters.base;

import ru.rejchev.steamid.SteamID;
import ru.rejchev.steamid.converters.ISteamIDConverter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AStringSteamIDConverter implements ISteamIDConverter, Set<Pattern> {

    private final Set<Pattern> patterns;

    private Matcher matcher;

    protected AStringSteamIDConverter() {
        this.patterns = new HashSet<>();
    }

    @Override
    public SteamID convert(Object value) {
        if(!(value instanceof String steamId))
            return null;

        setMatcher(getPatterns().stream()
                .map(x -> x.matcher(steamId))
                .filter(Matcher::find)
                .findFirst()
                .orElse(null)
        );

        return getMatcher() != null ? SteamID.of(0) : null;
    }

    protected Set<Pattern> getPatterns() {
        return patterns;
    }

    protected Matcher getMatcher() {
        return matcher;
    }

    protected void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    public AStringSteamIDConverter add(Collection<? extends Pattern> patterns) {
        addAll(patterns);
        return this;
    }

    @Override
    public int size() {
        return getPatterns().size();
    }

    @Override
    public boolean isEmpty() {
        return getPatterns().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return getPatterns().contains(o);
    }

    @Override
    public Iterator<Pattern> iterator() {
        return getPatterns().iterator();
    }

    @Override
    public Object[] toArray() {
        return getPatterns().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return getPatterns().toArray(a);
    }

    @Override
    public boolean add(Pattern pattern) {
        return getPatterns().add(pattern);
    }

    @Override
    public boolean remove(Object o) {
        return getPatterns().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return getPatterns().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Pattern> c) {
        return getPatterns().addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return getPatterns().retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return getPatterns().removeAll(c);
    }

    @Override
    public void clear() {
        getPatterns().clear();
    }
}
