package com.paxitalia.connectionlayerdemoredist;

/**
 * Defines receipt types that can be reprint using ICb2.reprint() method.
 *
 * Created by andrea.gimignani on 07/11/2017.
 */
public enum ReceiptType {
    /**
     * Payment, reversal receipt
     */
    PAYMENT,

    /**
     * Service receipt (e.g. DLL)
     */
    SERVICE,

    /**
     * Extended ICC data receipt (mainly for debug purposes)
     */
    EXTENDED_CHIP_DATA,

    /**
     * Not notified transaction receipt
     */
    NOT_NOTIFIED
}
