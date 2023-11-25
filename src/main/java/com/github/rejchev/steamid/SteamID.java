package com.github.rejchev.steamid;

import com.github.rejchev.steamid.exceptions.SteamViewException;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SteamID {
    /** NOTE:
     *
     *  The lowest bit of Steam64 represents Y.
     *  Y is part of the ID number for the account. Y is either 0 or 1.
     *
     */
    Byte y;

    /** NOTE:
     *
     *  31 bits represents the account number.
     *  Also known as constant Z
     *
     */
    Integer z;

    /** NOTE:
     *
     *  20 bits represents the instance of the account.
     *  It is usually set to 1 for user accounts.
     *
     */
    Integer instance;

    /** NOTE:
     *
     *  4 bits represents the type of account. Also known as constant: V
     *  Used in generating the W constant for Steam64:
     *      (https://steamcommunity.com/path/W : W=Z*2+V+Y)
     *
     */
    SteamIDType type;

    /** NOTE:
     *
     *  8 bits represents the "Universe" the steam account belongs to.
     *  Also known as constant: X
     *
     */
    SteamIDUniverse universe;

    public SteamID(long sid64) throws SteamViewException {
        if(sid64 < 0)
            throw new SteamViewException("Invalid SteamID64 value: " + sid64);

        y = (byte) (sid64 & 0x1);
        z = ((int) sid64) >> 1;
        instance = (int) ((sid64 >> 32) & 0xFFFFF);
        type = SteamIDType.values()[(int) ((sid64 >> 52) & 0xF)];
        universe = SteamIDUniverse.values()[(int) (sid64 >> 56)];
    }


    // SteamID32 = z*2+Y
    // https://developer.valvesoftware.com/wiki/SteamID
    public SteamID(Long sid32,
                   Integer instance,
                   SteamIDType type,
                   SteamIDUniverse universe) throws SteamViewException {
        if(sid32 < 0)
            throw new SteamViewException("Invalid SteamId32 value: " + sid32);

        y = (byte) (sid32 % 2);
        z = (int) ((sid32 - y) / 2);

        this.instance = instance;
        this.type = type;
        this.universe = universe;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SteamID) {
            SteamID input = (SteamID) obj;

            return Objects.equals(this.y, input.getY()) &&
                    Objects.equals(this.z, input.getZ()) &&
                    Objects.equals(this.instance, input.getInstance()) &&
                    this.universe.ordinal() == input.getUniverse().ordinal() &&
                    this.type.ordinal() == input.getType().ordinal();
        }

        return false;
    }
}
