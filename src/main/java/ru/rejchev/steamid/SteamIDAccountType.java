package ru.rejchev.steamid;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class SteamIDAccountType {

    public static final class Holder {

        private static Holder of(Number id, String name, Object any, char... signatures) {
            return new Holder(id, name, signatures, any);
        }

        private final Number id;

        private final char[] signatures;

        private final String name;

        private final Object data;

        private Holder(Number id, String name, char[] signatures, Object any) {

            assert signatures != null;
            assert signatures.length != 0;

            this.id = id;
            this.data = any;
            this.name = name;
            this.signatures = signatures;
        }

        public int id() {
            return id.ordinal();
        }

        public char[] signatures() {
            return signatures;
        }

        public String name() {
            return name;
        }

        public <T> T data(Class<T> clazz) {
            try { return clazz.cast(data);}
            catch (ClassCastException ignored) {}
            return null;
        }
    }

    // 4 bits - max
    public enum Number {
        Invalid,
        Individual,
        Multiseat,
        GameServer,
        AnonGameServer,
        Pending,
        ContentServer,
        Clan,
        Chat,
        P2PSuperSeeder,
        AnonUser;


        public static final int TypeNumberMask = 0xF;
    }

    private final Holder[] types;

    public SteamIDAccountType() {
        types = new Holder[] {
                Holder.of(Number.Invalid, "Invalid", null, 'i', 'I'),
                Holder.of(Number.Individual, "Individual", null, 'U'),
                Holder.of(Number.Multiseat, "Multiseat", null, 'M'),
                Holder.of(Number.GameServer, "GameServer", null, 'G'),
                Holder.of(Number.AnonGameServer, "AnonGameServer", null, 'A'),
                Holder.of(Number.Pending, "Pending", null, 'P'),
                Holder.of(Number.ContentServer, "ContentServer",null, 'C'),
                Holder.of(Number.Clan, "Clan", null, 'g'),
                Holder.of(Number.Chat, "Chat", null, 'T'),
                Holder.of(Number.Chat, "ClanChat", ((SteamIDMaskType.AccountInstance.getMask() + 1) >> 1), 'c'),
                Holder.of(Number.Chat, "LobbyChat", ((SteamIDMaskType.AccountInstance.getMask() + 1) >> 2), 'L'),
                Holder.of(Number.P2PSuperSeeder, "P2PSuperSeeder", null, '\0'),
                Holder.of(Number.AnonUser, "AnonUser", null, 'a')
        };
    }

    public Holder getBySignature(char signature) {
        return find(x -> {
            for (char c : x.signatures())
                if(c == signature)
                    return true;

            return false;
        }).findFirst().orElse(getInvalidType());
    }

    public Holder getByName(String name) {
        return find(x -> x.name().equals(name)).findFirst().orElse(getInvalidType());
    }

    public Iterable<Holder> getByNumber(Number typeNumber) {
        return getById(typeNumber.ordinal());
    }

    public Iterable<Holder> getById(int id) {
        return find(x -> x.id() == id).toList();
    }

    public Holder getBySteamID(SteamID steamID) {
        List<Holder> types;

        if((types = find(x -> x.id() == steamID.getAccountType()).toList()).size() == 1)
            return types.get(0);

        final Holder invalidType = getInvalidType();

        if(types.isEmpty())
            return invalidType;

        return types.stream()
                .filter(x -> (x.data(Object.class) instanceof Long data) && data == steamID.getInstanceFlags())
                .findFirst()
                .orElse(types.stream().filter(x -> x.data(Object.class) == null).findFirst().orElse(invalidType));
    }

    public Stream<Holder> find(Predicate<Holder> predicate) {
        return stream().filter(predicate);
    }

    public Stream<Holder> stream() {
        return Arrays.stream(types);
    }

    public Holder getInvalidType() {
        return getByNumber(Number.Invalid).iterator().next();
    }
}
