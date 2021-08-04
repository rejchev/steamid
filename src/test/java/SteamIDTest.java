import exceptions.SteamViewException;
import org.junit.Assert;
import org.junit.Test;

public class SteamIDTest {

    @Test
    public void steamIDFromSteam3() {
        SteamID steamID = new SteamID("[U:1:907378852]");

        SteamID expectedSteamID = new SteamID();
        expectedSteamID.setInstance((short) 1);
        expectedSteamID.setType(SteamIDType.INDIVIDUAL);
        expectedSteamID.setUniverse(SteamIDUniverse.PUBLIC);
        expectedSteamID.setY((byte) 0);
        expectedSteamID.setZ(453689426);


        Assert.assertEquals(expectedSteamID, steamID);
    }

    @Test
    public void steamIDFromSteam2() {
        SteamID steamID = new SteamID("STEAM_1:0:453689426");

        SteamID expectedSteamID = new SteamID();
        expectedSteamID.setInstance((short) 1);
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
        expectedSteamID.setInstance((short) 1);
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
            steamID = new SteamID(76561L);
        } catch(SteamViewException e) {
            steamID = null;
        }

        Assert.assertNull(steamID);
    }
}
