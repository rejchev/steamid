public class SteamIDUtils {
    public SteamIDUtils() {}

    public static String viewAsSteam2(SteamID steamID) {
        return String.format("STEAM_%d:%d:%d", steamID.getUniverse().ordinal(), steamID.getY(), steamID.getZ());
    }

    // TODO: Extend the flexibility of presentation
    public static String viewAsSteam3(SteamID steamID) {
        return String.format("[%s:1:%d]", steamID.getType().getLetter(), (steamID.getZ()*2 + steamID.getY()));
    }

    public static long viewAsSteam64(SteamID steamID) {
        return ((long) steamID.getUniverse().ordinal()  << 56
                | (long) steamID.getType().ordinal()    << 52
                | (long) steamID.getInstance()          << 32
                | (long) steamID.getZ()                 << 1
                | steamID.getY());
    }
}
