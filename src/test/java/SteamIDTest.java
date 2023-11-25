import com.github.rejchev.steamid.SteamIDUtils;
import com.github.rejchev.steamid.exceptions.SteamViewException;
import org.junit.Assert;
import org.junit.Test;
import com.github.rejchev.steamid.SteamID;
import com.github.rejchev.steamid.SteamIDType;
import com.github.rejchev.steamid.SteamIDUniverse;

import java.util.Optional;

public class SteamIDTest {

    @Test
    public void steamIDFromSteam3() throws SteamViewException {

        SteamID steamID = SteamIDUtils.parse("[U:1:907378852]", Optional.empty(), Optional.empty());

        SteamID expectedSteamID = SteamID.builder()
                .instance(1)
                .type(SteamIDType.INDIVIDUAL)
                .universe(SteamIDUniverse.PUBLIC)
                .y((byte) 0)
                .z(453689426)
                .build();

        Assert.assertEquals(expectedSteamID, steamID);
    }

    @Test
    public void steamIDFromSteam2() throws SteamViewException {
        SteamID steamID = SteamIDUtils.parse("STEAM_1:0:453689426", Optional.empty(), Optional.empty());

        SteamID expectedSteamID = new SteamID();
        expectedSteamID.setInstance(1);
        expectedSteamID.setType(SteamIDType.INDIVIDUAL);
        expectedSteamID.setUniverse(SteamIDUniverse.PUBLIC);
        expectedSteamID.setY((byte) 0);
        expectedSteamID.setZ(453689426);


        Assert.assertEquals(expectedSteamID, steamID);
    }

    @Test
    public void steamIDFromSteam64() throws SteamViewException {

        SteamID steamID = new SteamID(76561198867644580L);

        SteamID expectedSteamID = new SteamID();
        expectedSteamID.setInstance(1);
        expectedSteamID.setType(SteamIDType.INDIVIDUAL);
        expectedSteamID.setUniverse(SteamIDUniverse.PUBLIC);
        expectedSteamID.setY((byte) 0);
        expectedSteamID.setZ(453689426);


        Assert.assertEquals(expectedSteamID, steamID);
    }

    @Test
    public void steamIDViewException() throws SteamViewException {
        SteamID steamID;

        try{
            steamID = new SteamID(-1);
        } catch(SteamViewException e) {
            steamID = null;
        }

        Assert.assertNull(steamID);
    }
}
