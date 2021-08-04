import com.github.rejchev.steamid.exceptions.SteamViewException;
import org.junit.Assert;
import org.junit.Test;
import com.github.rejchev.steamid.SteamID;
import com.github.rejchev.steamid.SteamIDUtils;

public class SteamIDUtilsTest {

    @Test
    public void steamIDAsSteam2() {
        String expectedSteamID2 = "STEAM_1:0:453689426";

        SteamID steamID = new SteamID(expectedSteamID2);

        Assert.assertEquals(expectedSteamID2, SteamIDUtils.viewAsSteam2(steamID));
    }

    @Test
    public void steamIDAsSteam3() {
        String expectedSteamID3 = "[A:1:907378852:1]";

        SteamID steamID = new SteamID(expectedSteamID3);

        Assert.assertEquals(expectedSteamID3, SteamIDUtils.viewAsSteam3(steamID));
    }

    @Test
    public void steamIDAsSteam64() throws SteamViewException {
        long expectedSteamID64 = 76561198867644580L;

        SteamID steamID = new SteamID(expectedSteamID64);

        Assert.assertEquals(expectedSteamID64, SteamIDUtils.viewAsSteam64(steamID));
    }
}
