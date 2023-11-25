package com.github.rejchev.steamid;

import com.github.rejchev.steamid.exceptions.SteamViewException;

import java.util.Arrays;
import java.util.Optional;

public class SteamIDUtils {

    private SteamIDUtils() {}

    // parsing Steam2 & Steam3 from string
    public static SteamID parse(String steamId,
                                Optional<Integer> instance,
                                Optional<SteamIDType> type) throws SteamViewException {
        steamId = steamId.trim();

        boolean isSteam2 = steamId.indexOf("STEAM_") == 0;

        steamId = steamId.replaceAll("[\\[\\]]", "").replaceAll("STEAM_", "");

        String[] params = steamId.split(":");

        SteamID buffer;

        if(isSteam2) {
            buffer = SteamID.builder()
                    .universe(SteamIDUniverse.values()[Integer.parseInt(params[0])])
                    .y(Byte.parseByte(params[1]))
                    .z(Integer.parseInt(params[2]))
                    .type(type.orElse(SteamIDType.INDIVIDUAL))
                    .instance(instance.orElse(1))
                    .build();
        } else buffer = new SteamID(
                Long.parseLong(params[2]),
                (params.length > 3) ? Integer.parseInt(params[3]) : instance.orElse(1),
                Arrays.stream(SteamIDType.values())
                        .filter(x -> x.getLetter().equals(params[0]))
                        .findFirst().get(),
                SteamIDUniverse.values()[Integer.parseInt(params[1])]
        );

        return buffer;
    }


    public static String viewAsSteam2(SteamID steamID) {
        return String.format("STEAM_%d:%d:%d", steamID.getUniverse().ordinal(), steamID.getY(), steamID.getZ());
    }

    // https://github.com/SteamRE/open-steamworks/blob/f65c0439bf06981285da1e7639de82cd760755b7/Open%20Steamworks/CSteamID.h#L385
    public static String viewAsSteam3(SteamID steamID) {
        return (steamID.getType() == SteamIDType.ANON_GAME_SERVER || steamID.getType() == SteamIDType.MULTISEAT)
                    ?   String.format(
                            "[%s:%d:%d:%d]",
                            steamID.getType().getLetter(),
                            steamID.getUniverse().ordinal(),
                            (steamID.getZ()*2 + steamID.getY()),
                            steamID.getInstance())

                    :   String.format(
                            "[%s:%d:%d]",
                            steamID.getType().getLetter(),
                            steamID.getUniverse().ordinal(),
                            (steamID.getZ()*2 + steamID.getY()));
    }

    public static long viewAsSteam64(SteamID steamID) {
        return ((long) steamID.getUniverse().ordinal()  << 56
                | (long) steamID.getType().ordinal()    << 52
                | (long) steamID.getInstance()          << 32
                | (long) steamID.getZ()                 << 1
                | steamID.getY());
    }
}
