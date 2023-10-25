package com.paxitalia.connectionlayerdemoredist;

class Utility {
    public static String byteArrayToAsciiHex(byte [] array, int fromIndex, int toIndex) {

        StringBuffer byteArrayAsciiHexBuffer = new StringBuffer();

        for (int i=fromIndex; i<toIndex; i++)
        {
            byteArrayAsciiHexBuffer.append(byteToHighNibble(array[i]));
            byteArrayAsciiHexBuffer.append(byteToLowNibble(array[i]));
        }

        return byteArrayAsciiHexBuffer.toString();
    }

    public static String byteArrayToAsciiHex(byte [] array) {
        return byteArrayToAsciiHex(array, 0, array.length);
    }

    public static char byteToLowNibble(byte b) {
        return nibbleToChar(b & 0x0f);
    }

    public static char byteToHighNibble(byte b) {
        return nibbleToChar((b & 0xf0) >> 4);
    }

    public static char nibbleToChar(int nibbleValue) {
        char digitChar;
        if (nibbleValue > 9)
            digitChar = (char)(nibbleValue - 10 + 'A');
        else
            digitChar = (char)(nibbleValue  + '0');
        return digitChar;
    }
}
