public class IPChecker {

    private IPChecker() {}

    private static final byte MIN_LENGTH_IP_ADDRESS = 7;    // |0.0.0.0| = 7
    private static final byte MAX_LENGTH_IP_ADDRESS = 15;   // |255.255.255.255| = 15

    private static final IpAutomaton ipRec = new IpAutomaton();

    // IP-address matches the required format.
    public static boolean check(String ip) {
        if (ip.length() < MIN_LENGTH_IP_ADDRESS ||
                ip.length() > MAX_LENGTH_IP_ADDRESS) return false;
        char[] symbols = ip.toCharArray();
        for (char sym : symbols) {
            ipRec.next(sym);
            if (!ipRec.accept()) {
                ipRec.reset();
                return false;
            }
        }
        ipRec.reset();
        return true;
    }
}
