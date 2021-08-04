public class SteamIDUtils {
    public SteamIDUtils() {}

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
