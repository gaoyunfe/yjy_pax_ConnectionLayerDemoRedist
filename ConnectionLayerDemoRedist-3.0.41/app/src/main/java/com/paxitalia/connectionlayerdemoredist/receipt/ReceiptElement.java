package com.paxitalia.connectionlayerdemoredist.receipt;

public class ReceiptElement {
    private String text;
    private TicketAlignment alignment;
    private ReceiptFont font;

    public ReceiptElement() {
        this.font = ReceiptFont.NORMAL;
        this.alignment = TicketAlignment.LEFT;
    }

    public ReceiptElement(String text) {
        this.text = text;
        this.alignment = TicketAlignment.LEFT;
        this.font = ReceiptFont.NORMAL;
    }

    public ReceiptElement(String text, TicketAlignment alignment) {
        this.text = text;
        this.alignment = alignment;
        this.font = ReceiptFont.NORMAL;
    }

    public ReceiptElement(String text, TicketAlignment alignment, ReceiptFont font) {
        this.text = text;
        this.alignment = alignment;
        this.font = font;
    }

    public void setText(String text){
        this.text = text;
    }

    public void setFont(ReceiptFont font){
        this.font = font;
    }

    public void setAlignment(TicketAlignment alignment){
        this.alignment = alignment;
    }

    public String getText() {
        if (text == null)
            return "";
        return text;
    }

    public TicketAlignment getAlignment() {
        return alignment;
    }

    public ReceiptFont getFont() {
        return font;
    }
}
