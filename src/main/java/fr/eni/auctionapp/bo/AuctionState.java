package fr.eni.auctionapp.bo;

public enum AuctionState {
    UNSTARTED("auction_state.unstarted"), IN_PROGRESS("auction_state.in_progress"), CANCELED("auction_state.canceled"), EXPIRED("auction_state.expired"), FINISHED("auction_state.finished");

    private final String label;

    AuctionState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public String getValue() {
        return switch (this) {
            case UNSTARTED -> "UNSTARTED";
            case IN_PROGRESS -> "IN_PROGRESS";
            case CANCELED -> "CANCELED";
            case EXPIRED -> "EXPIRED";
            case FINISHED -> "FINISHED";
        };
    }

    public static AuctionState getFromString(String state) {
        return switch (state.toUpperCase()) {
            case "UNSTARTED" -> UNSTARTED;
            case "IN_PROGRESS" -> IN_PROGRESS;
            case "CANCELED" -> CANCELED;
            case "EXPIRED" -> EXPIRED;
            case "FINISHED" -> FINISHED;
            default -> throw new IllegalStateException("Unexpected value: " + state);
        };
    }
}
