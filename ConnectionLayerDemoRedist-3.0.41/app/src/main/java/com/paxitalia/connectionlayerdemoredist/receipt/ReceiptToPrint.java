package com.paxitalia.connectionlayerdemoredist.receipt;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.pax.dal.IDAL;
import com.pax.dal.IPrinter;
import com.pax.dal.exceptions.PrinterDevException;
import com.pax.gl.page.IPage;
import com.pax.gl.page.PaxGLPage;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.paxitalia.connectionlayerdemoredist.CLDemoApp;
import com.paxitalia.connectionlayerdemoredist.ErrorCodes;
import com.paxitalia.connectionlayerdemoredist.ReceiptType;

import java.util.ArrayList;

public class ReceiptToPrint {
    private final static String LOG_TAG = "Ticket";

    private ReceiptType receiptType;
    ArrayList<ReceiptLine> lines;
    private transient PrinterStatus bufferedPrinterStatus;

    public ReceiptToPrint(ReceiptType receiptType) {
        lines = new ArrayList<>();
        bufferedPrinterStatus = PrinterStatus.IDLE;
        this.receiptType = receiptType;
    }

    public void add(ReceiptLine t) {
        lines.add(t);
    }

    public void addLine(String left) {
        ReceiptLine l = new ReceiptLine();
        ReceiptElement e = new ReceiptElement(left);
        l.addElement(e);
        lines.add(l);
    }

    public void addCompressedLine(String text) {
        ReceiptLine l = new ReceiptLine();
        ReceiptElement e = new ReceiptElement(text, TicketAlignment.LEFT, ReceiptFont.COMPRESSED);
        l.addElement(e);
        lines.add(l);
    }

    public void addNameValueLine(String name, String value) {
        ReceiptLine l = new ReceiptLine();
        ReceiptElement e = new ReceiptElement(name);
        l.addElement(e);
        e = new ReceiptElement(value, TicketAlignment.RIGHT);
        l.addElement(e);
        lines.add(l);
    }

    public void addCompressedNameValueLine(String lname, String lvalue, String rname, String rvalue) {
        ReceiptLine tmpLine = new ReceiptLine();
        ReceiptElement e = null;

        if (lname != null) {
            e = new ReceiptElement(lname, TicketAlignment.LEFT, ReceiptFont.NORMAL);
            tmpLine.addElement(e);
        }

        if (lvalue != null) {
            e = new ReceiptElement(lvalue, TicketAlignment.RIGHT, ReceiptFont.NORMAL);
            tmpLine.addElement(e);
        }
        String formattedLeft = tmpLine.format();

        tmpLine = new ReceiptLine();
        if (rname != null) {
            e = new ReceiptElement(rname, TicketAlignment.LEFT);
            tmpLine.addElement(e);
        }
        if (rvalue != null) {
            e = new ReceiptElement(rvalue, TicketAlignment.RIGHT);
            tmpLine.addElement(e);
        }
        String formattedRight = tmpLine.format();

        ReceiptLine defLine = new ReceiptLine();
        ReceiptElement defElement = null;

//        // if both lines exists, add the two lines as a single left formatted line
//        if ((formattedLeft != null) && (formattedRight != null)) {
//            formattedLeft += formattedRight;
//            formattedRight = null;
//        }

        if (formattedLeft != null) {
            defElement = new ReceiptElement(formattedLeft.trim(), TicketAlignment.LEFT, ReceiptFont.COMPRESSED);
            defLine.addElement(defElement);
        }

        if (formattedRight != null) {
            defElement = new ReceiptElement(formattedRight.trim(), TicketAlignment.RIGHT, ReceiptFont.COMPRESSED);
            defLine.addElement(defElement);
        }

        if (defLine.getElements().size() > 0)
            lines.add(defLine);
    }

    public void addCenterAlignedLine(String center) {
        ReceiptLine l = new ReceiptLine();
        ReceiptElement e = new ReceiptElement(center);
        e.setAlignment(TicketAlignment.CENTER);
        l.addElement(e);
        lines.add(l);
    }

    public void addCenterAlignedTitle(String title) {
        ReceiptLine l = new ReceiptLine();
        ReceiptElement e = new ReceiptElement(title, TicketAlignment.CENTER, ReceiptFont.DOUBLE_HEIGHT);
        l.addElement(e);
        lines.add(l);
    }

    public void addTitle(String title) {
        ReceiptLine l = new ReceiptLine();
        ReceiptElement e = new ReceiptElement(title, TicketAlignment.LEFT, ReceiptFont.DOUBLE_HEIGHT);
        l.addElement(e);
        lines.add(l);
    }

    public void addRightAlignedLine(String center) {
        ReceiptLine l = new ReceiptLine();
        ReceiptElement e = new ReceiptElement(center);
        e.setAlignment(TicketAlignment.RIGHT);
        l.addElement(e);
        lines.add(l);
    }

    public int getLineLength(ReceiptFont font) {
        if (font.equals(ReceiptFont.COMPRESSED)) {
            return MAX_COMPRESSED_LINE_LEN;
        } else {
            return MAX_LINE_LEN;
        }
    }

    public final static int MAX_LINE_LEN = 24;
    public final static int MAX_COMPRESSED_LINE_LEN = 48;

    ArrayList<String> formattedLines;

    protected void format() {
        formattedLines = new ArrayList<>();
        for (ReceiptLine l : lines) {
            String tmp = l.format();
            formattedLines.add(tmp);
            //Log.d(LOG_TAG, tmp);
        }
    }

    private int lineSpaceAdjust = -6;

    protected ArrayList<Bitmap> toBitmap(int lineOptions, int maxPixelHeigth, boolean addSpace) {
        String standardFontPath;

        PaxGLPage glPage = PaxGLPage.getInstance(CLDemoApp.getAppContext());
        IPage page = glPage.createPage();

        standardFontPath = CLDemoApp.getAppContext().getFilesDir().getAbsolutePath() + "/" + "RobotoMono-Regular.ttf";

        int lineSpace = page.getLineSpaceAdjustment();
        //Log.d(LOG_TAG, "page.getLineSpaceAdjustment() -> " + String.format("%d", lineSpace));
        page.adjustLineSpace(lineSpaceAdjust);

        lineSpace = page.getLineSpaceAdjustment();
        //Log.d(LOG_TAG, "page.getLineSpaceAdjustment() -> " + String.format("%d", lineSpace));

        ArrayList<Bitmap> ticketPagesBmp = new ArrayList<>();

        int numLines = 0;
        ReceiptFont currentFont = null;

        // generate different pages when font changes or 50 lines limit is hit
        for (ReceiptLine l : lines) {

            if ((l.elements == null) || (l.elements.isEmpty())) {
                //Log.d(LOG_TAG, "Skip empty line");
                continue;
            }

            // skip unnedded lines
            if ((l.getFlags() != 0) && (lineOptions != 0) /*&& (BitmapCheck.enabled(l.getFlags(), (byte)lineOptions) != true)*/) {
                continue;
            }

            // get next line font
            ReceiptFont thisLineFont = currentFont;
            if (/*(l.elements != null) && (l.elements.isEmpty() != true) && */(l.elements.get(0) != null) && (l.elements.get(0).getFont() != null)) {
                thisLineFont = l.elements.get(0).getFont();
            }

            if ((thisLineFont.equals(currentFont) != true) || (numLines > 50)) {
                if (numLines > 0) {

                    Bitmap pageBmp;

                    if (currentFont.equals(ReceiptFont.COMPRESSED)) {
                        pageBmp = page.toBitmap(768);
                        pageBmp = Bitmap.createScaledBitmap(pageBmp, 384, pageBmp.getHeight(), false);
                    } else {
                        pageBmp = page.toBitmap(384);
                        if (currentFont.equals(ReceiptFont.DOUBLE_HEIGHT)) {
                            pageBmp = Bitmap.createScaledBitmap(pageBmp, pageBmp.getWidth(), 2*pageBmp.getHeight(), false);
                        }
                    }

                    ticketPagesBmp.add(pageBmp);
                    // create a new page
                    page = glPage.createPage();
                    numLines = 0;
                }
                currentFont = thisLineFont;

                if (currentFont.equals(ReceiptFont.COMPRESSED)) {
                    //page.setTypeFace(compressedFontPath);
                    page.setTypeFace(standardFontPath);
                    page.adjustLineSpace(lineSpaceAdjust);
                } else {
                    page.setTypeFace(standardFontPath);
                    page.adjustLineSpace(lineSpaceAdjust);
                }
            }

            IPage.ILine line = page.addLine();
            l.format(line);

            numLines++;
        }

        // process last page
        if ((page != null) && (page.getLines() != null) && (page.getLines().isEmpty() != true)) {
            Bitmap pageBmp = page.toBitmap(384);
            ticketPagesBmp.add(pageBmp);
        }


        // now combine the different bitmaps into one or more bitmaps, ready for printing or displaying
        Bitmap combined = ticketPagesBmp.get(0);

        ArrayList<Bitmap> outputBitmaps = new ArrayList<>();

        // do one loop more than necessary to print last combined bitmap
        for (int i = 1; i < ticketPagesBmp.size() + 1; i++) {

            Bitmap b = null;
            if (i < ticketPagesBmp.size()) {
                b = ticketPagesBmp.get(i);
            }

            if ((b != null) && ((maxPixelHeigth < 0) || (combined.getHeight() + b.getHeight()) < maxPixelHeigth)) {
                // create a new bitmap that is the sum of combined and b

                int height = combined.getHeight() + b.getHeight();
                if ((addSpace) && (i == (ticketPagesBmp.size() - 1))) {
                    // ad some lines at the end of the ticket
                    height += 120;
                }

                Bitmap newBitmap = Bitmap.createBitmap(384, height, b.getConfig());
                newBitmap.eraseColor(android.graphics.Color.WHITE);

                Canvas comboImage = new Canvas(newBitmap);

                comboImage.drawBitmap(combined, 0f, 0f, null);
                comboImage.drawBitmap(b, 0f, combined.getHeight(), null);

                combined = newBitmap;

                continue;
            }


            outputBitmaps.add(combined);
            combined = b;
        }

        return outputBitmaps;
    }

    public ErrorCodes print(int lineOptions) {
        Log.i("INFO","Access Method Log:  print ( " + lineOptions + " )");
        ErrorCodes res = ErrorCodes.SUCCESS;

        if ("PAX".equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
            ArrayList<Bitmap> ticketPagesBmp = this.toBitmap(lineOptions, 5000, true);

            IPrinter printer = null;
            IDAL dal = null;
            try {
                dal = NeptuneLiteUser.getInstance().getDal(CLDemoApp.getAppContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            printer = dal.getPrinter();

            PrinterStatus result = PrinterStatus.BUSY;

            Bitmap toPrint = null;

            if ((ticketPagesBmp != null) && (ticketPagesBmp.size() > 0)) {
                int i = 0;
                while (i < ticketPagesBmp.size()) {
                    toPrint = ticketPagesBmp.get(i);
                    if (toPrint != null) {
                        result = printBitmap(toPrint, printer);
                        if (result.equals(PrinterStatus.NO_PAPER)) {
                            res = ErrorCodes.OUT_OF_PAPER;
                            break;
                        }

                        if (!result.equals(PrinterStatus.IDLE)) {
                            // HW or SW error, abort printing
                            res = ErrorCodes.ERR_PRINTER;
                            break;
                        }
                    }
                    i++;
                }
            }

            if (result.equals(PrinterStatus.IDLE)) {
                try {
                    printer.cutPaper(0);
                } catch (PrinterDevException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }

        }
        return res;
    }

    protected Bitmap getBitmap(int lineOptions) {

        ArrayList<Bitmap> ticketPagesBmp = this.toBitmap(lineOptions, 5000, true);

        PrinterStatus result;

        Bitmap toPrint = null;

        if ((ticketPagesBmp != null) && (ticketPagesBmp.size() > 0)) {
            int i = 0;
            while (i < ticketPagesBmp.size()) {
                toPrint = ticketPagesBmp.get(i);
                i++;
            }
        }

        return toPrint;


    }

    private boolean printFinished;
    PrinterStatus printerState;

    private PrinterStatus printBitmap(Bitmap bitmap, final IPrinter printer) {

        printFinished = false;
        printerState = PrinterStatus.IDLE;

        final IPrinter.IPinterListener listener = new IPrinter.IPinterListener() {

            @Override
            public void onSucc() {
                int p = 0;
                printFinished = true;
                printerState = PrinterStatus.IDLE;
            }

            @Override
            public void onError(int i) {
                switch (i) {

                    case 0:
                        printFinished = true;
                        printerState = PrinterStatus.IDLE;
                        break;

                    case 1:
                        printerState = PrinterStatus.BUSY;
                        break;

                    case 2:
                        printerState = PrinterStatus.NO_PAPER;
                        break;

                    case 4:
                        printerState = PrinterStatus.HW_FAILURE;
                        break;

                    case 8:
                        printerState = PrinterStatus.HW_OVER_HEAT;
                        break;

                    case 9:
                        printerState = PrinterStatus.HW_VOLTAGE_TOO_LOW;
                        break;

                    default:
                        printerState = PrinterStatus.SW_FAILURE;
                        break;
                }
            }
        };

        printer.print(bitmap, listener);

        while (!printFinished) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if ((!printerState.equals(PrinterStatus.IDLE)) && (!printerState.equals(PrinterStatus.BUSY))) {
                break;
            }
        }

        return printerState;
    }

}
