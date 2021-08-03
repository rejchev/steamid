import java.util.Arrays;

public enum SteamIDType {
    INVALID ("I"),
    INDIVIDUAL("U"),
    MULTISEAT("M"),
    GAME_SERVER("G"),
    ANON_GAME_SERVER("A"),
    PENDING("P"),
    CONTENT_SERVER("C"),
    CLAN("g"),
    CHAT("T"),
    P2P_SUPER_SEEDER(""),
    ANON_USER("a");

    private String t;

    SteamIDType(String i) {
        t = i;
    }

    public String getLetter() {
        return t;
    }
}
