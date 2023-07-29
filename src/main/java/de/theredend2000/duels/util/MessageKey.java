package de.theredend2000.duels.util;

public enum MessageKey {
    PERMISSION_ERROR("duel-command-permission-error"),
    BANNED_WORLD_ERROR("banned-world-error"),
    ERROR("error"),
    ALREADY_IN_DUEL_ERROR("already-in-duel-error"),
    OPPONENT_ALREADY_IN_DUEL_ERROR("opponent-already-in-duel-error"),
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
    OPPONENT_NO_LONGER_AVAILABLE("opponent-no-longer-available"),
    ARENA_UNAVAILABLE("arena-unavailable"),
    DUEL_REQUEST_DENIED_BY_SENDER("duel-request-denied-by-sender"),
    ONLY_PLAYERS_ERROR("only-players-error"),
    ARENA_CREATED("arena-created"),
    ARENA_DELETED("arena-deleted"),
    ARENA_ALREADY_EXIST("arena-already-exist"),
    ARENA_NOT_FOUND("arena-not-found"),
    ARENA_ICON_SET("arena-icon-set"),
    ARENA_ICON_FAIL("arena-icon-fail"),
    TYPE_NAME_CONTENT("type-name-content"),
    KIT_SAVED("kit-saved"),
    KIT_DELETED("kit-deleted"),
    KIT_ALREADY_EXIST("kit-already-exist"),
    KIT_NOT_FOUND("kit-not-found"),
    KIT_RENAME("kit-rename"),
    KIT_RENAME_EXIST_NEW("kit-rename-exist-new"),
    KIT_LOAD("kit-load"),
    KIT_EDIT("kit-edit"),
    KIT_ICON_SET("kit-icon-set"),
    KIT_ICON_FAIL("kit-icon-fail"),
    RELOAD_FILE_SUCCESS("reload-file-success"),
    PLAYER_NOT_EXIST("player-not-exist"),
    OPTION_INVADABLE("option-invadable"),
    CANT_USE_OPTION_HERE("cant-use-option-here"),
    USE_NUMBER_AS_SCORE("use-number-as-score"),
    GET_STATS_FROM_PLAYER("get-stats-from-player"),
    ADD_STATS_TO_PLAYER("add-stats-to-player"),
    REMOVE_STATS_FROM_PLAYER("remove-stats-from-player"),
    SET_STATS_OF_PLAYER("set-stats-of-player"),
    RESET_STATS_OF_PLAYER("reset-stats-of-player"),
    BATTLE_START_TITLE("battle-start-title"),
    BATTLE_START_SUBTITLE("battle-start-subtitle"),
    BATTLE_STARTED_TITLE("battle-started-title"),
    BATTLE_STARTED_SUBTITLE("battle-started-subtitle"),
    BATTLE_START_MESSAGE("battle-start-message"),
    BATTLE_STARTED_MESSAGE("battle-started-message"),
    BATTLE_END_TITLE("battle-end-title"),
    BATTLE_END_SUBTITLE("battle-end-subtitle"),
    BATTLE_END_MESSAGE("battle-end-message"),
    BATTLE_ENDED_MESSAGE("battle-ended-message"),
    ALREADY_ON_FIRST_PAGE("already-on-first-page"),
    ALREADY_ON_LAST_PAGE("already-on-last-page"),
    FAILED_TO_READ_TEXT("failed-to-read-text");

    private final String path;

    MessageKey(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
