package de.theredend2000.duels.util;

public enum MessageKey {
    PERMISSION_ERROR("duel-command-permission-error"),
    BANNED_WORLD_ERROR("banned-world-error"),
    ALREADY_IN_DUEL_ERROR("already-in-duel-error"),
    NO_ARENAS_AVAILABLE_WITH_PERMISSION("no-arenas-available"),
    NO_ARENAS_AVAILABLE("no-arenas-available"),
    NO_KITS_AVAILABLE("no-kits-available"),
    NO_KITS_AVAILABLE_WITH_PERMISSION("no-kits-available-with-permission"),
    PLAYER_NOT_AVAILABLE("player-not-available"),
    CANNOT_DUEL_YOURSELF("cannot-duel-yourself"),
    OPPONENT_BANNED_WORLD("opponent-banned-world"),
    DUEL_REQUEST_SENT("duel-request-sent"),
    DUEL_REQUEST_DENIED("duel-request-denied"),
    DUEL_REQUEST_EXPIRED("duel-request-expired"),
    DUEL_REQUEST_ALREADY_SENT("duel-request-already-sent"),
    NO_DUEL_REQUEST("no-duel-request"),
    OPPONENT_ALREADY_IN_DUEL("opponent-already-in-duel"),
    ARENA_UNAVAILABLE("arena-unavailable"),
    DUEL_REQUEST_DENIED_BY_SENDER("duel-request-denied-by-sender"),
    ONLY_PLAYERS_ERROR("only-players-error"),
    ARENA_CREATED("arena-created"),
    ARENA_DELETED("arena-deleted"),
    ARENA_ALREADY_EXIST("arena-already-exist"),
    ARENA_NOT_FOUND("arena-not-found"),
    ARENA_ICON_SET("arena-icon-set"),
    ARENA_ICON_FAIL("arena-icon-fail"),
    KIT_SAVED("kit-saved"),
    KIT_DELETED("kit-deleted"),
    KIT_ALREADY_EXIST("kit-already-exist"),
    KIT_NOT_FOUND("kit-not-found"),
    KIT_RENAME("kit-rename"),
    KIT_RENAME_EXIST_NEW("kit-rename-exist-new"),
    KIT_LOAD("kit-load"),
    KIT_EDIT("kit-edit"),
    KIT_ICON_SET("kit-icon-set"),
    KIT_ICON_FAIL("kit-icon-fail");

    private final String path;

    MessageKey(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
