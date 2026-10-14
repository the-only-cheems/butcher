package name.cheems.butcher.client;

public final class CorruptionClientData {

    private static int stage = 0;

    private CorruptionClientData() {
    }

    public static int getStage() {
        return stage;
    }

    public static void setStage(int newStage) {
        stage = Math.max(0, Math.min(3, newStage));
    }

    public static void reset() {
        stage = 0;
    }
}