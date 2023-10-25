package com.paxitalia.connectionlayerdemoredist.receipt;

import com.pax.gl.page.IPage;

import java.util.ArrayList;

public class ReceiptLine {

    public static final byte DEFAULT = 0;
    public static final byte ONLY_CUSTOMER_COPY = 1;
    public static final byte ONLY_MERCHANT_COPY = 2;
    public static final byte SIGNATURE_SPACE = 4;
    public static final byte SIGNATURE_CAPTURE_ERROR_MSG = 8;

    public final static int MAX_LINE_LEN = 24;
    public final static int MAX_COMPRESSED_LINE_LEN = 48;

    public final static int SMALL_FONT_SIZE = 26;
    public final static int NORMAL_FONT_SIZE = 26;
    public final static int DOUBLE_HEIGHT_FONT_SIZE = 26;

    private byte flags;

    ArrayList<ReceiptElement> elements;

    public ReceiptLine() {
        flags = 0;
        elements = new ArrayList<>();
    }

    public ReceiptLine(byte flags) {
        this.flags = flags;
        elements = new ArrayList<>();
    }

    public void addElement(ReceiptElement e) {
        elements.add(e);
    }

    public ArrayList<ReceiptElement> getElements() {
        return elements;
    }

    public byte getFlags() {
        return flags;
    }

    public String format() {
        String lineLeft = null, lineRight = null, lineCenter = null;
        boolean compressed = false;

        for (ReceiptElement e : elements) {
            if (e.getFont() == ReceiptFont.COMPRESSED) {
                compressed = true;
            }

            if (e.getAlignment() == TicketAlignment.CENTER) {
                lineCenter = e.getText();
            } else if (e.getAlignment() == TicketAlignment.RIGHT) {
                lineRight = e.getText();
            } else {
                lineLeft = e.getText();
            }
        }

        char[] tmp;
        int maxLineLen = MAX_LINE_LEN;
        if (compressed) {
            maxLineLen = MAX_COMPRESSED_LINE_LEN;
        }
        tmp = new char[maxLineLen];

        for (int i = 0; i < maxLineLen; i++) {
            tmp[i] = ' ';
        }

        if (lineLeft != null) {
            int strLen = lineLeft.length();
            if (strLen > maxLineLen) {
                strLen = maxLineLen;
            }
            for (int i = 0; i < strLen; i++) {
                tmp[i] = lineLeft.charAt(i);
            }
        }

        if (lineRight != null) {
            int strLen = lineRight.length();
            if (strLen > maxLineLen) {
                strLen = maxLineLen;
            }

            for (int i = (strLen - 1); i >= 0; i--) {
                tmp[maxLineLen - (strLen-i)] = lineRight.charAt(i);
            }
        }

        if (lineCenter != null) {
            int strLen = lineCenter.length();
            if (strLen > maxLineLen) {
                strLen = maxLineLen;
            }

            int offset = (maxLineLen - strLen) / 2;

            for (int i = offset; i < strLen+offset; i++) {
                tmp[i] = lineCenter.charAt(i-offset);
            }
        }
        return new String(tmp);
    }

    public void format(IPage.ILine line) {
        for (ReceiptElement e : elements) {

            if (e.getText() == null) {
                continue;
            }

            int fontSize;
            IPage.EAlign align;
            int style = IPage.ILine.IUnit.TEXT_STYLE_NORMAL;
            //float weigth = (e.getText().length() * 100) / 24;
            float weigth = e.getText().length();

            // get alignment
            if (e.getAlignment() == TicketAlignment.CENTER) {
                align = IPage.EAlign.CENTER;
            } else if (e.getAlignment() == TicketAlignment.RIGHT) {
                align = IPage.EAlign.RIGHT;
            } else {
                align = IPage.EAlign.LEFT;
            }

            // get font size
            if (e.getFont().equals(ReceiptFont.COMPRESSED)) {
                fontSize = SMALL_FONT_SIZE;
                //weigth = (e.getText().length() * 100) / 48;
            } else if (e.getFont().equals(ReceiptFont.DOUBLE_HEIGHT)) {
                fontSize = DOUBLE_HEIGHT_FONT_SIZE;
                style = IPage.ILine.IUnit.TEXT_STYLE_BOLD;
            } else {
                fontSize = NORMAL_FONT_SIZE;
            }

            line.addUnit(e.getText(), fontSize, align, style, weigth);
        }
    }

}
