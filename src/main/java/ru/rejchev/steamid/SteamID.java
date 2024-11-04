package ru.rejchev.steamid;

import ru.rejchev.steamid.containers.BitVector64;

/**
 * Class {@link SteamID} is an implementation of Valve unique account identifier
 * used to identify Steam accounts
 *
 * @see <a href="https://developer.valvesoftware.com/wiki/SteamID">SteamID</a>
 */
public final class SteamID {

    /**
     * SteamID container
     *
     * @see BitVector64
     */
    private final BitVector64 steamId;

    /**
     * Creating {@link SteamID} via steam64
     *
     * @param steam64 steam64 (example: {@code 76561198188347379})
     * @return {@link SteamID}
     */
    public static SteamID of(long steam64) {
        return new SteamID(steam64);
    }

    /**
     * Creating {@link SteamID} via string with steam64 or steam32
     *
     * @param steam64 Steam64
     * @return {@link SteamID} or {@code null} on {@link NumberFormatException}
     */
    public static SteamID of(String steam64) {

        try { return new SteamID(Long.parseUnsignedLong(steam64)); }
        catch (NumberFormatException ignored) {}

        return null;
    }

    /**
     * Create {@link SteamID} via steam32 or steam64
     *
     * @param steamId steamID 32/64 bit representation (example: {@code 76561198188347379} or {@code 228081651})
     */
    public SteamID(long steamId) {
        this.steamId = new BitVector64(steamId);
    }

    /**
     * Create {@link SteamID} via Steam32 & Instance
     *
     * @param uAccountId Steam Community ID (32 bits) representation
     * @param uInstance 20bits of account instance
     *
     * @see <a href="https://developer.valvesoftware.com/wiki/SteamID">SteamID</a>
     * -> ID as a Steam Community ID (constant <b>W</b>)
     * @since 1.0
     */
    public SteamID(long uAccountId, int uInstance) {
        this(0);

        setAccountId(uAccountId);
        setInstance(uInstance);
    }

    /**
     * Create {@link SteamID} via Steam Community ID (32 bits) & Instance & Universe
     *
     * @param uAccountId Steam Community ID (32 bits) representation
     * @param uInstance 20bits of account instance
     * @param unUniverse 8bits of account universe
     *
     * @since 1.0
     */
    public SteamID(long uAccountId, int uInstance, int unUniverse) {
        this(uAccountId, uInstance);

        setAccountUniverse(unUniverse);
    }

    /**
     * Create {@link SteamID} via Steam Community ID (32 bits) & Instance & Universe
     *
     * @param uAccountId Steam Community ID (32 bits) representation
     * @param uInstance 20bits of account instance
     * @param unUniverse 8bits of account universe
     * @param unAccountType 4bits of account type
     *
     * @since 1.0
     */
    public SteamID(long uAccountId, int uInstance, int unUniverse, int unAccountType) {
        this(uAccountId, uInstance, unUniverse);

        setAccountType(unAccountType);
    }

    /**
     * Getting {@code SteamID (64 bits)} also known as {@code Steam64}
     *
     * @return steam64
     */
    public long getSteamID() {
        return steamId.data();
    }

    /**
     * Getting {@code SteamID (32 bits)} also known as {@code Steam32}
     *
     * @return steam32
     *
     * @see SteamIDMaskType#AccountID
     */
    public long getAccountID() {
        return steamId.get(SteamIDMaskType.AccountID.getOffset(), SteamIDMaskType.AccountID.getMask());
    }

    /**
     * Getting an instance of account (20 bits)
     *
     * @return instance
     *
     * @see SteamIDMaskType#AccountInstance
     */
    public int getInstance() {
        return (int)steamId.get(SteamIDMaskType.AccountInstance.getOffset(), SteamIDMaskType.AccountInstance.getMask());
    }

    /**
     * Getting an account type (4 bits)
     *
     * @return 4bits of account type
     *
     * @see SteamIDMaskType#AccountType
     * @see SteamIDAccountType
     */
    public int getAccountType() {
        return (int)steamId.get(SteamIDMaskType.AccountType.getOffset(), SteamIDMaskType.AccountType.getMask());
    }

    /**
     * Getting an account universe (8 bits)
     *
     * @return 8bits of account universe
     *
     * @see SteamIDMaskType#AccountUniverse
     * @see SteamIDUniverse
     */
    public int getAccountUniverse() {
        return (int)steamId.get(SteamIDMaskType.AccountUniverse.getOffset(), SteamIDMaskType.AccountUniverse.getMask());
    }

    /**
     * Setting an account id (32 bits)
     *
     * @param accountId steam32
     * @return self {@link SteamID}
     */
    public SteamID setAccountId(long accountId) {
        steamId.set(accountId, SteamIDMaskType.AccountID.getOffset(), SteamIDMaskType.AccountID.getMask());
        return this;
    }

    /**
     * Setting an instance (20 bits)
     *
     * @param instance instance
     * @return self {@link SteamID}
     */
    public SteamID setInstance(long instance) {
        steamId.set(instance, SteamIDMaskType.AccountInstance.getOffset(), SteamIDMaskType.AccountInstance.getMask());
        return this;
    }

    /**
     * Setting an account type (4 bits)
     *
     * @param unAccountType 4bits of account type
     * @return self {@link SteamID}
     *
     * @see SteamIDAccountType
     */
    public SteamID setAccountType(int unAccountType) {
        steamId.set(unAccountType, SteamIDMaskType.AccountType.getOffset(), SteamIDMaskType.AccountType.getMask());
        return this;
    }

    /**
     * Setting an account universe (8 bits)
     *
     * @param unUniverse 8bits of account universe
     * @return self {@link SteamID}
     *
     * @see SteamIDUniverse
     */
    public SteamID setAccountUniverse(int unUniverse) {
        steamId.set(unUniverse, SteamIDMaskType.AccountUniverse.getOffset(), SteamIDMaskType.AccountUniverse.getMask());
        return this;
    }

    /**
     * @return an account encoded flags
     */
    public long getInstanceFlags() {
        return (getInstance() & ~SteamIDMaskType.AccountInstance.getMask());
    }


    /**
     * @return data without instance
     */
    public long getStaticAccountKey() {
        return ((long)getAccountUniverse() << SteamIDMaskType.AccountUniverse.getOffset())
            +  ((long)getAccountType() << SteamIDMaskType.AccountType.getOffset())
            +  getAccountID();
    }

    @Override
    public String toString() {
        return Long.toUnsignedString(steamId.data());
    }
}