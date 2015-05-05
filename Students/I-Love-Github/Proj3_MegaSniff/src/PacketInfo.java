public class PacketInfo {

    public String MAC;
    public long timeMillis;
    public int signalStrength;
    public int averageStrength;

    PacketInfo(String timeSeconds, String timeNano, String macAddr, String signalStrength) {

        this.MAC = macAddr;

        if (timeNano != null) {

            if (timeNano.length() > 0 && timeNano.length() <= 3) {
                this.timeMillis = Long.valueOf(timeSeconds + timeNano.substring(0, timeNano.length()));

                if (timeNano.length() < 3) {
                    for (int i = 0; i <= (3 - timeNano.length()); i++) {
                        this.timeMillis = this.timeMillis + '0';
                    }
                }
            } else {
                this.timeMillis = Long.valueOf(timeSeconds + timeNano.substring(0, 3));
            }
        }
        this.signalStrength = Integer.valueOf(signalStrength);
        this.averageStrength = Integer.valueOf(signalStrength);
    }

    public void newSigStr(String newSS) {
        int newStrength = Integer.valueOf(newSS);
        this.averageStrength = java.lang.Math.round(((float) this.signalStrength + (float) newStrength) / 2);
    }
}
