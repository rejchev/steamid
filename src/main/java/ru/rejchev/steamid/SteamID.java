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
     * Create {@link SteamID} via Community ID (32 bits) or SteamID (64 bits)
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
     * @param uInstance Instance of the account
     *
     * @see <a href="https://developer.valvesoftware.com/wiki/SteamID">SteamID</a>
     * -> ID as a Steam Community ID (constant <b>W</b>)
     * @since 1.0
     */
    public SteamID(long uAccountId, long uInstance) {
        this(0);

        setAccountId(uAccountId);
        setInstance(uInstance);
    }

    /**
     * Create {@link SteamID} via Steam Community ID (32 bits) & Instance & Universe
     *
     * @param uAccountId Steam Community ID (32 bits) representation
     * @param uInstance instance of the account
     * @param eUniverse steam account universe
     *
     * @since 1.0
     */
    public SteamID(long uAccountId, long uInstance, SteamIDUniverse eUniverse) {
        this(uAccountId, uInstance);

        setAccountUniverse(eUniverse);
    }

    /**
     * Create {@link SteamID} via Steam Community ID (32 bits) & Instance & Universe
     *
     * @param uAccountId Steam Community ID (32 bits) representation
     * @param uInstance instance of the account
     * @param eUniverse steam account universe
     * @param eAccountType type of account
     *
     * @since 1.0
     */
    public SteamID(long uAccountId, long uInstance, SteamIDUniverse eUniverse, SteamIDAccountType eAccountType) {
        this(uAccountId, uInstance, eUniverse);

        setAccountType(eAccountType);
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
    public long getInstance() {
        return steamId.get(SteamIDMaskType.AccountInstance.getOffset(), SteamIDMaskType.AccountInstance.getMask());
    }

    /**
     * Getting an account type (4 bits)
     *
     * @return {@link SteamIDAccountType}
     *
     * @see SteamIDMaskType#AccountType
     */
    public SteamIDAccountType getAccountType() {
        return SteamIDAccountType.of((int) steamId.get(
                SteamIDMaskType.AccountType.getOffset(),
                SteamIDMaskType.AccountType.getMask()
        ));
    }

    /**
     * Getting an account universe (8 bits)
     *
     * @return {@link SteamIDUniverse}
     *
     * @see SteamIDMaskType#AccountUniverse
     */
    public SteamIDUniverse getAccountUniverse() {
        return SteamIDUniverse.values()[(int) steamId.get(
                SteamIDMaskType.AccountUniverse.getOffset(),
                SteamIDMaskType.AccountUniverse.getMask()
        )];
    }

    /**
     * Setting an account id (32 bits)
     *
     * @param accountId steam32
     * @return self {@link SteamID}
     */
    public SteamID setAccountId(long accountId) {
        steamId.set(
                (accountId & SteamIDMaskType.AccountID.getMask()),
                SteamIDMaskType.AccountID.getOffset(),
                SteamIDMaskType.AccountID.getMask()
        );

        return this;
    }

    /**
     * Setting an instance (20 bits)
     *
     * @param instance instance
     * @return self {@link SteamID}
     */
    public SteamID setInstance(long instance) {
        steamId.set(
                (instance & SteamIDMaskType.AccountInstance.getMask()),
                SteamIDMaskType.AccountInstance.getOffset(),
                SteamIDMaskType.AccountInstance.getMask()
        );

        return this;
    }

    /**
     * Setting an account type (4 bits)
     *
     * @param accountType account type (on {@code null} is {@link SteamIDAccountType#Invalid})
     * @return self {@link SteamID}
     */
    public SteamID setAccountType(SteamIDAccountType accountType) {
        steamId.set(accountType == null ? SteamIDAccountType.Invalid.ordinal() : accountType.ordinal(),
                SteamIDMaskType.AccountType.getOffset(),
                SteamIDMaskType.AccountType.getMask());

        return this;
    }

    /**
     * Setting an account universe (8 bits)
     *
     * @param eUniverse universe (on {@code null} is {@link SteamIDUniverse#Invalid})
     * @return self {@link SteamID}
     */
    public SteamID setAccountUniverse(SteamIDUniverse eUniverse) {
        steamId.set(eUniverse == null ? SteamIDUniverse.Invalid.ordinal() : eUniverse.ordinal(),
                SteamIDMaskType.AccountUniverse.getOffset(),
                SteamIDMaskType.AccountUniverse.getMask());

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
        return ((long)getAccountUniverse().ordinal() << SteamIDMaskType.AccountUniverse.getOffset()) +
                ((long) getAccountType().ordinal() << SteamIDMaskType.AccountType.getOffset()) +
                getAccountID();
    }

    @Override
    public String toString() {
        return Long.toUnsignedString(steamId.data());
    }
}