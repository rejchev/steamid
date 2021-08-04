import exceptions.SteamViewException;

import java.util.Arrays;

public class SteamID {
    /** NOTE:
     *
     *  The lowest bit of Steam64 represents Y.
     *  Y is part of the ID number for the account. Y is either 0 or 1.
     *
     */
    private byte y;

    /** NOTE:
     *
     *  31 bits represents the account number.
     *  Also known as constant Z
     *
     */
    private int z;

    /** NOTE:
     *
     *  20 bits represents the instance of the account.
     *  It is usually set to 1 for user accounts.
     *
     */
    private short instance;

    /** NOTE:
     *
     *  4 bits represents the type of account. Also known as constant: V
     *  Used in generating the W constant for Steam64:
     *      (https://steamcommunity.com/path/W : W=Z*2+V+Y)
     *
     */
    private SteamIDType type;

    /** NOTE:
     *
     *  8 bits represents the "Universe" the steam account belongs to.
     *  Also known as constant: X
     *
     */
    private SteamIDUniverse universe;


    public SteamID(){}

    public SteamID(long id) throws SteamViewException {
        if(id < Integer.MAX_VALUE)
            throw new SteamViewException("Invalid Steam ID constructor type");

        y = (byte) (id & 0x1);
        z = ((int) id) >> 1;
        instance = (short) ((id >> 32) & 0xFFFFF);
        type = SteamIDType.values()[(int) ((id >> 52) & 0xF)];
        universe = SteamIDUniverse.values()[(int) (id >> 56)];
    }

    public SteamID(String value) {
        boolean isSteam2 = value.contains("STEAM_");
        value = value
                .trim()
                .replaceAll("[\\[\\]]", "")
                .replaceAll("STEAM_", "");

        String[] params = value.split(":");

        if(isSteam2) {
            universe = SteamIDUniverse.values()[Integer.parseInt(params[0])];
            y = Byte.parseByte(params[1]);
            z = Integer.parseInt(params[2]);

            type = SteamIDType.INDIVIDUAL;
            instance = 1;
        } else {

            // https://github.com/SteamRE/open-steamworks/blob/f65c0439bf06981285da1e7639de82cd760755b7/Open%20Steamworks/CSteamID.h#L385
            type = Arrays.stream(SteamIDType.values())
                    .filter(x -> x.getLetter().equals(params[0]))
                    .findFirst().get();

            universe = SteamIDUniverse.values()[Integer.parseInt(params[1])];

            // W = z*2+Y
            // https://developer.valvesoftware.com/wiki/SteamID
            long w = Long.parseLong(params[2]);
            z = (int) w/2;
            y = (byte) (w - z*2);

            instance = (params.length > 3) ? Short.parseShort(params[3]) : 1;
        }
    }

    public byte getY() {
        return y;
    }

    public void setY(byte y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public short getInstance() {
        return instance;
    }

    public void setInstance(short instance) {
        this.instance = instance;
    }

    public SteamIDType getType() {
        return type;
    }

    public void setType(SteamIDType type) {
        this.type = type;
    }

    public SteamIDUniverse getUniverse() {
        return universe;
    }

    public void setUniverse(SteamIDUniverse universe) {
        this.universe = universe;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SteamID) {
            SteamID input = (SteamID) obj;

            return  this.y == input.getY() &&
                    this.z == input.getZ() &&
                    this.instance == input.getInstance() &&
                    this.universe.ordinal() == input.getUniverse().ordinal() &&
                    this.type.ordinal() == input.getType().ordinal();
        }

        return false;
    }
}
