/** Integer parsing based ip-checking */
private static boolean ipCheck(String ip) {
    // Length validation:
    if (ip.length() < 7 || ip.length() > 15) { return false; }

    // Check missing first dot:
    if (ip.charAt(0) == '.') { return false; }

    // Check the number of dots:
    // Check invalid characters:
    byte dotCount = 0;
    byte[] dotPos = new byte[3];
    char[] symbols = ip.toCharArray();
    for (byte i = 0; i < symbols.length; i++) {
        char sym = symbols[i];
        if (sym == '.') {
            if (dotCount == 3) { return false; }
            dotPos[dotCount] = i;
            dotCount++;
        } else if (!Character.isDigit(sym)) {
            return false;
        }
    }

    // IP-address consists of four parts.
    // Address-parts value and length validation:
    String[] parts = new String[] {
            ip.substring(0, dotPos[0]),
            ip.substring(dotPos[0] + 1, dotPos[1]),
            ip.substring(dotPos[1] + 1, dotPos[2]),
            ip.substring(dotPos[2] + 1)
    };
    for (String part : parts) {
        int value = Integer.parseInt(part);
        if (value > 255) { return false; }
        if (value >= 0 && value <= 9 && part.length() > 1) { return false; }
        if (value >= 10 && value <= 99 && part.length() > 2) { return false; }
        if (value >= 100 && part.length() > 3) { return false; }
    }

    return true;
}