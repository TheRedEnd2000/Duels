package de.theredend2000.duels.util;

public enum MessageKey {
    PERMISSION_ERROR("duel-command-permission-error"),
    BANNED_WORLD_ERROR("banned-world-error"),
    ALREADY_IN_DUEL_ERROR("already-in-duel-error"),
    NO_ARENAS_AVAILABLE_WITH_PERMISSION("no-arenas-available"),
    NO_ARENAS_AVAILABLE("no-arenas-available"),
    NO_KITS_AVAILABLE("no-kits-available"),
    PLAYER_NOT_AVAILABLE("player-not-available"),
    CANNOT_DUEL_YOURSELF("cannot-duel-yourself"),
    OPPONENT_BANNED_WORLD("opponent-banned-world"),
    DUEL_REQUEST_SENT("duel-request-sent"),
    DUEL_REQUEST_RECEIVED("duel-request-received"),
    DUEL_REQUEST_DENIED("duel-request-denied"),
    DUEL_REQUEST_EXPIRED("duel-request-expired"),
    NO_DUEL_REQUEST("no-duel-request"),
    OPPONENT_ALREADY_IN_DUEL("opponent-already-in-duel"),
    ARENA_UNAVAILABLE("arena-unavailable"),
    DUEL_REQUEST_DENIED_BY_SENDER("duel-request-denied-by-sender"),
    ONLY_PLAYERS_ERROR("only-players-error");

    private final String path;

    MessageKey(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
