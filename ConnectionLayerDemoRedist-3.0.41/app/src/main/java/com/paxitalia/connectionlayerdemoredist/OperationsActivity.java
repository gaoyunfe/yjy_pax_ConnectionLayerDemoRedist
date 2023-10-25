package com.paxitalia.connectionlayerdemoredist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.paxitalia.connectionlayerdemoredist.receipt.ReceiptToPrint;
import com.paxitalia.mpos.connectionlayer.BankHost;
import com.paxitalia.mpos.connectionlayer.BluetoothConnectionDropped;
import com.paxitalia.mpos.connectionlayer.BluetoothDataReceived;
import com.paxitalia.mpos.connectionlayer.CardInteractionCommand;
import com.paxitalia.mpos.connectionlayer.CardInteractionCompleted;
import com.paxitalia.mpos.connectionlayer.CardInteractionRequest;
import com.paxitalia.mpos.connectionlayer.CardInteractionResult;
import com.paxitalia.mpos.connectionlayer.CloseSessionCompleted;
import com.paxitalia.mpos.connectionlayer.CloseSessionResult;
import com.paxitalia.mpos.connectionlayer.CommDeviceType;
import com.paxitalia.mpos.connectionlayer.ConnectionError;
import com.paxitalia.mpos.connectionlayer.ConnectionErrorCode;
import com.paxitalia.mpos.connectionlayer.ConnectionLayer;
import com.paxitalia.mpos.connectionlayer.ConnectionLayerStarted;
import com.paxitalia.mpos.connectionlayer.ConnectionLayerStopped;
import com.paxitalia.mpos.connectionlayer.CustomCardReadingCompleted;
import com.paxitalia.mpos.connectionlayer.CustomCardReadingResult;
import com.paxitalia.mpos.connectionlayer.DataAcquisitionActionValue;
import com.paxitalia.mpos.connectionlayer.DataAcquisitionCompleted;
import com.paxitalia.mpos.connectionlayer.DataAcquisitionRequest;
import com.paxitalia.mpos.connectionlayer.DataAcquisitionResponse;
import com.paxitalia.mpos.connectionlayer.DataAcquisitionResultCode;
import com.paxitalia.mpos.connectionlayer.DllCompleted;
import com.paxitalia.mpos.connectionlayer.DllResult;
import com.paxitalia.mpos.connectionlayer.FileType;
import com.paxitalia.mpos.connectionlayer.HostConnectionType;
import com.paxitalia.mpos.connectionlayer.HostTransportProtocol;
import com.paxitalia.mpos.connectionlayer.ImgShowOnPosCompleted;
import com.paxitalia.mpos.connectionlayer.ImgShowOnPosResult;
import com.paxitalia.mpos.connectionlayer.LogLevel;
import com.paxitalia.mpos.connectionlayer.Logger;
import com.paxitalia.mpos.connectionlayer.OperationType;
import com.paxitalia.mpos.connectionlayer.PaxD200DeviceIdentifier;
import com.paxitalia.mpos.connectionlayer.PaymentCompleted;
import com.paxitalia.mpos.connectionlayer.PaymentInputData;
import com.paxitalia.mpos.connectionlayer.PaymentResult;
import com.paxitalia.mpos.connectionlayer.PosConnection;
import com.paxitalia.mpos.connectionlayer.PosConnectionError;
import com.paxitalia.mpos.connectionlayer.PosInfo;
import com.paxitalia.mpos.connectionlayer.PosSoftwareUpdateCompleted;
import com.paxitalia.mpos.connectionlayer.PosSoftwareUpdateProgress;
import com.paxitalia.mpos.connectionlayer.PosSoftwareUpdateProgressValue;
import com.paxitalia.mpos.connectionlayer.PosSoftwareUpdateResult;
import com.paxitalia.mpos.connectionlayer.PosStatus;
import com.paxitalia.mpos.connectionlayer.PosStatusChanged;
import com.paxitalia.mpos.connectionlayer.PosStatusCode;
import com.paxitalia.mpos.connectionlayer.PosWifiAPconn;
import com.paxitalia.mpos.connectionlayer.PosWifiAPdata;
import com.paxitalia.mpos.connectionlayer.PosWifiOpReqCode;
import com.paxitalia.mpos.connectionlayer.PosWifiOpReqCompleted;
import com.paxitalia.mpos.connectionlayer.PosWifiOpReqResult;
import com.paxitalia.mpos.connectionlayer.PosWifiOpReqResultStatusCode;
import com.paxitalia.mpos.connectionlayer.PreAuthInputDataCustom;
import com.paxitalia.mpos.connectionlayer.PreAuthorizationCompleted;
import com.paxitalia.mpos.connectionlayer.PreAuthorizationResult;
import com.paxitalia.mpos.connectionlayer.RebootCompleted;
import com.paxitalia.mpos.connectionlayer.RebootResult;
import com.paxitalia.mpos.connectionlayer.Receipt;
import com.paxitalia.mpos.connectionlayer.ReceiptReceived;
import com.paxitalia.mpos.connectionlayer.ReceiptRow;
import com.paxitalia.mpos.connectionlayer.RefundCompleted;
import com.paxitalia.mpos.connectionlayer.RefundResult;
import com.paxitalia.mpos.connectionlayer.RetroactiveReversalInputData;
import com.paxitalia.mpos.connectionlayer.ReversalCompleted;
import com.paxitalia.mpos.connectionlayer.ReversalInfoCompleted;
import com.paxitalia.mpos.connectionlayer.ReversalInfoResult;
import com.paxitalia.mpos.connectionlayer.ReversalResult;
import com.paxitalia.mpos.connectionlayer.SendFileToTerminalCompleted;
import com.paxitalia.mpos.connectionlayer.SendFileToTerminalResult;
import com.paxitalia.mpos.connectionlayer.ServiceOperationReceipt;
import com.paxitalia.mpos.connectionlayer.StatusMessageEvent;
import com.paxitalia.mpos.connectionlayer.StatusMessageResult;
import com.paxitalia.mpos.connectionlayer.TIDInfo;
import com.paxitalia.mpos.connectionlayer.TmsHost;
import com.paxitalia.mpos.connectionlayer.TotalsCompleted;
import com.paxitalia.mpos.connectionlayer.TotalsResult;
import com.paxitalia.mpos.connectionlayer.TransactionResult;
import com.paxitalia.mpos.connectionlayer.TransactionResultCode;
import com.paxitalia.mpos.connectionlayer.WlanConnectionDropped;
import com.paxitalia.mpos.connectionlayer.WlanDataReceived;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class OperationsActivity extends AppCompatActivity {

    private static final String TRANSACTION_ID = "00000001";

    private static final int BLUETOOTH_CONNECT_PERMISSION_REQ_CODE = 1337;

    TextView tvUpdate;
    private TextView connectionStatus;
    private TextView amount_pay;
    private TextView amount_refund;
    private TextView amount_preAuth;
    private TextView amount_intPreAuth;
    private TextView amount_closurePreAuth;

    private Spinner connectSelectionSpinner;
    private Spinner connectionTypeSpinner;

    private ImageView connIcon;

    private CardView connectCard;
    private CardView disconnectCard;
    private CardView dllCard;
    CardView posMenu;
    private CardView paymentCard;
    private CardView reverseCard;
    private CardView retroactiveReverseCard;
    private CardView cancelTransactionCard;
    private CardView refundCard;
    private CardView preAuthCard;
    private CardView intPreAuthCard;
    private CardView closurePreAuthCard;
    private CardView dailyCard;
    private CardView closeCard;
    private CardView posSwUpdateCard;

    private CardView isAsyncSwUpdateEnabledCard;
    private CardView isSwUpdateRunningCard;
    private CardView isSwUpdatePausedCard;
    private CardView percentageSwUpdateProgressCard;
    private CardView pauseSwUpdateRunningCard;
    private CardView resumeSwUpdateRunningCard;

    private CardView lastTransactionResultCard;
    private CardView recoveryCard;
    private CardView devicesCard;
    private CardView dataAcquisitionPicture;
    private CardView dataAcquisitionQRCode;
    private CardView reboot;
    private ProgressBar updateStatus;
    private Button pauseUpdateBtn;
    private CardView posWifiScanReq;
    private CardView posWifiConnReq;
    private CardView posWifiDisconnReq;
    private CardView posWifiStatusReq;
    private CardView posTmsTunnelSwUpdReq;
    private CardView posTmsWifiSwUpdReq;
    private CardView reversalInfoReqCard;
    private CardView sendFileToTerminalCmd;
    private TextView clDemo_version;
    private TextView clLib_Version;
    private TextView posStatusTxt;
    private TextView infoTxt;

    private CardView startupCard;
    private CardView isStartedUpCard;
    private CardView shutdownCard;

    private Button btnCI_ATCA;
    private Button btnCI_ATBO;
    private Button btnCI_ATBS;
    private Button btnCI_ATBB;
    private Button btnCI_ATBP;
    private Button btnCI_ATCT;
    private Button btnCI_ATSO;
    private Button btnCI_ATSC;
    private Button btnCI_ATMF;
    private Button btnCI_ATSR;
    private Button btnCI_ATDA;
    private Button btnCI_ATDH;
    private Button btnCI_ATZC;

    private EditText huntingTime_EditText;
    private EditText magOptions_EditText;
    private EditText iccOptions_EditText;
    private EditText ctlsOptions_EditText;
    private Button bATBODialog_CancelButton;
    private Button bATBODialog_ConfirmButton;

    protected ConnectionLayer connectionLayer;
    protected Logger logger;
    private long updateRunning;

    private String currentOperationId = null;
    private int nextOperationId;

    private static String terminalID = "01322498";

    private final PaxD200DeviceIdentifier deviceIdentifier = new PaxD200DeviceIdentifier(this);

    private String nameFile = "";
    //private final String nameFileToUpdate = "D200XXP000_6.0.1.enc";
    //private final String nameFileToUpdate = "D200XXP000_4.1.8.enc";
    //private final String nameFileToUpdate = "D200XXP000_4.2.1.enc";
    //private final String nameFileToUpdate = "D200XXP000_4.2.3.enc";
    private final String nameFileToUpdate = "D200XXP000_4.2.4.enc";
    //private final String nameFileToUpdate = "IM20XXP000_1.2.9.enc";
    private final String nameFileToSend = "logo_bpm.png";
    //private final String nameFileToSend = "ga_logo.png";
    //private final String nameFileToSend = "ga_mpay_logo.png";
    //private final String nameFileToSend = "pax_logo.png";
    //private final String nameFileToSend = "logo.png";
    //private final String nameFileToSend = "siaPayLogo.bmp";
    //private final String nameFileToSend = "startup_image.png";
    //private final String nameFileToSend = "logo38.png";

    private Set<BluetoothDevice> mpayBluetoothDeviceSet = new HashSet<>();
    ArrayList<String> mpayDeviceArrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapterForConnectSelSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations);

        setupConnectionLayer();
        bindViews();
        setupClickListeners();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // request BLUETOOTH_CONNECT permission if not granted
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH_CONNECT)) {
                    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.BLUETOOTH_CONNECT }, BLUETOOTH_CONNECT_PERMISSION_REQ_CODE);
                } else {
                    logger.logWarning("BLUETOOTH_CONNECT permission NOT granted and should NOT request again");
                }
            } else {
                logger.logInfo("BLUETOOTH_CONNECT permission already granted");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == BLUETOOTH_CONNECT_PERMISSION_REQ_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                logger.logInfo("BLUETOOTH_CONNECT permission granted");
                // load devices on permission granted
                loadBtPairedDevices();
                // notify dataset changed to reload spinner
                arrayAdapterForConnectSelSpinner.notifyDataSetChanged();
            } else {
                logger.logWarning("BLUETOOTH_CONNECT permission NOT granted");
            }
        }
    }

    private void setupConnectionLayer() {
        logger = new Logger("ConnectionLayerDemo");
        updateRunning = 0;
        connectionLayer = ConnectionLayer.getInstance();
        connectionLayer.addOnConnectionLayerStartedListener(this::onConnectionLayerStarted);
        connectionLayer.addOnConnectionLayerStoppedListener(this::onConnectionLayerStopped);
        connectionLayer.addOnPosConnectionListener(this::onPosConnection);
        connectionLayer.addOnPosConnectionErrorListener(this::onPosConnectionError);
        connectionLayer.addOnPosStatusChangedListener(this::onPosStatusChanged);
        connectionLayer.addOnServiceOperationReceiptReceivedListener(this::onServiceOperationReceiptReceived);
        connectionLayer.addOnStatusMessageEventListener(this::onStatusMessageReceivingEvent);

        //ConnectionLayer.setLogLevel(LogLevel.LOGGER_LEVEL_INFO);
        ConnectionLayer.setLogLevel(LogLevel.LOGGER_LEVEL_TRACE);
    }

    private void bindViews() {
        tvUpdate = findViewById(R.id.textView_pos_sw_update_card);
        connectionStatus = findViewById(R.id.connection_status);
        amount_pay = findViewById(R.id.amount_pay);
        amount_refund = findViewById(R.id.amount_refund);
        amount_preAuth = findViewById(R.id.amount_preauth);
        amount_intPreAuth = findViewById(R.id.amount_int_preauth);
        amount_closurePreAuth = findViewById(R.id.amount_closure_preauth);

        connIcon = findViewById(R.id.imageView_connection);

        connectSelectionSpinner = (Spinner) findViewById(R.id.connect_device_selection_spinner);
        loadBtPairedDevices();
        arrayAdapterForConnectSelSpinner = new ArrayAdapter<>(this, R.layout.layout_spinner_item, mpayDeviceArrayList);
        connectSelectionSpinner.setAdapter(arrayAdapterForConnectSelSpinner);

        connectionTypeSpinner = (Spinner) findViewById(R.id.connect_conn_type_spinner);
        List<String> items = Arrays.asList("USB", "SERIAL", "BLUETOOTH", "TCPIP");
        connectionTypeSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.layout_spinner_item, items));

        connectCard = findViewById(R.id.connect_card);
        disconnectCard = findViewById(R.id.disconnect_card);
        devicesCard = findViewById(R.id.devices_card);
        dataAcquisitionPicture = findViewById(R.id.data_acquisition_picture);
        dataAcquisitionQRCode = findViewById(R.id.data_acquisition_qr_code);
        reboot = findViewById(R.id.reboot);
        dllCard = findViewById(R.id.dll_card);
        posMenu = findViewById(R.id.posMenu);
        paymentCard = findViewById(R.id.pay_card);
        reverseCard = findViewById(R.id.reverse_card);
        retroactiveReverseCard = findViewById(R.id.retroactive_reverse_card);
        cancelTransactionCard = findViewById(R.id.cancel_transaction_card);
        refundCard = findViewById(R.id.refund_card);
        preAuthCard = findViewById(R.id.preauth_card);
        intPreAuthCard = findViewById(R.id.int_preauth_card);
        closurePreAuthCard = findViewById(R.id.closure_preauth_card);
        lastTransactionResultCard = findViewById(R.id.last_transaction_result_card);
        recoveryCard = findViewById(R.id.recovery_card);
        dailyCard = findViewById(R.id.daily_card);
        closeCard = findViewById(R.id.close_card);
        posSwUpdateCard = findViewById(R.id.pos_sw_update_card);

        isAsyncSwUpdateEnabledCard = findViewById(R.id.is_async_sw_update_enabled_card);
        isSwUpdateRunningCard = findViewById(R.id.is_sw_update_running_card);
        isSwUpdatePausedCard = findViewById(R.id.is_sw_update_paused_card);
        percentageSwUpdateProgressCard = findViewById(R.id.percentage_sw_update_progress_card);
        pauseSwUpdateRunningCard = findViewById(R.id.pause_sw_update_running_card);
        resumeSwUpdateRunningCard = findViewById(R.id.resume_sw_update_running_card);

        updateStatus = findViewById(R.id.update_status);
        pauseUpdateBtn = findViewById(R.id.pause_update_btn);
        posWifiScanReq = findViewById(R.id.posWifiScanReq);
        posWifiConnReq = findViewById(R.id.posWifiConnReq);
        posWifiDisconnReq = findViewById(R.id.posWifiDisconnReq);
        posWifiStatusReq = findViewById(R.id.posWifiStatusReq);
        posTmsTunnelSwUpdReq = findViewById(R.id.posTmsTunnelSwUpdReq);
        posTmsWifiSwUpdReq = findViewById(R.id.posTmsWifiSwUpdReq);
        reversalInfoReqCard = findViewById(R.id.reversalInfoReqCard);
        sendFileToTerminalCmd = findViewById(R.id.sendFileToTerminal_card);

        startupCard = findViewById(R.id.startup_card);
        isStartedUpCard = findViewById(R.id.isstartedup_card);
        shutdownCard = findViewById(R.id.shutdown_card);

        btnCI_ATCA = findViewById(R.id.button);
        btnCI_ATBO = findViewById(R.id.button2);
        btnCI_ATBS = findViewById(R.id.button3);
        btnCI_ATBB = findViewById(R.id.button4);
        btnCI_ATBP = findViewById(R.id.button5);
        btnCI_ATCT = findViewById(R.id.button6);
        btnCI_ATSO = findViewById(R.id.button7);
        btnCI_ATSC = findViewById(R.id.button8);
        btnCI_ATMF = findViewById(R.id.button9);
        btnCI_ATSR = findViewById(R.id.button10);
        btnCI_ATDA = findViewById(R.id.button11);
        btnCI_ATDH = findViewById(R.id.button12);
        btnCI_ATZC = findViewById(R.id.button13);

        clDemo_version = (TextView) findViewById(R.id.cldemo_version);
        clLib_Version = (TextView) findViewById(R.id.cllib_version);
        //clStatusTxt = (TextView) findViewById(R.id.cl_status_text);
        posStatusTxt = (TextView) findViewById(R.id.pos_status_text);
        infoTxt = (TextView) findViewById(R.id.info_text);
        clDemo_version.setText("CL Demo version: 1.0");
        clLib_Version.setText("CL Lib version:  " + connectionLayer.getVersion());
        //clStatusTxt.setText("CL Status:  ");
        posStatusTxt.setText("POS status:  ");
        infoTxt.setText("Info Text");

//        huntingTime_EditText = findViewById(R.id.hunting_time_param_edit);
//        magOptions_EditText = findViewById(R.id.mag_opt_param_edit);
//        iccOptions_EditText = findViewById(R.id.icc_opt_param_edit);
//        ctlsOptions_EditText = findViewById(R.id.ctls_opt_param_edit);
//        bATBODialog_CancelButton = findViewById(R.id.atboDialog_cancelButton);
//        bATBODialog_ConfirmButton = findViewById(R.id.atboDialog_confirmButton);

    }

    private void setupClickListeners() {
        connectSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                logger.logInfo(">>> onItemSelected(...)");

                // do not manage bluetooth devices if BLUETOOTH_CONNECT permission is not granted
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || ActivityCompat.checkSelfPermission(OperationsActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), getString(R.string.selected_item) + " " + mpayDeviceArrayList.get(position) + "[" + position + "]", Toast.LENGTH_LONG).show();

                    for (BluetoothDevice device : mpayBluetoothDeviceSet) {
                        logger.logInfo("device.getName()  -->  " + device.getName());
                        if (device.getName().equals(mpayDeviceArrayList.get(position))) {
                            deviceIdentifier.setDeviceName(device.getName());
                            deviceIdentifier.setMacAddress(device.getAddress());
                            break;
                        }
                    }

                    logger.logInfo("deviceIdentifier.getDeviceName()  -->  " + deviceIdentifier.getDeviceName());
                    logger.logInfo("deviceIdentifier.getMacAddress()  -->  " + deviceIdentifier.getMacAddress());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadBtPairedDevices();
            }
        });

        connectionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        connIcon.setImageResource(R.drawable.ic_usb_black_24dp);
                        connectSelectionSpinner.setVisibility(View.GONE);
                        deviceIdentifier.setCommDeviceType(CommDeviceType.USB);
                        break;
                    case 1:
                        connIcon.setImageResource(R.drawable.ic_usb_black_24dp);
                        connectSelectionSpinner.setVisibility(View.GONE);
                        deviceIdentifier.setCommDeviceType(CommDeviceType.SERIAL);
                        break;
                    case 2:
                        connIcon.setImageResource(R.drawable.ic_bluetooth_24dp);
                        connectSelectionSpinner.setVisibility(View.VISIBLE);
                        deviceIdentifier.setCommDeviceType(CommDeviceType.BLUETOOTH);
                        break;
                    case 3:
                        connIcon.setImageResource(R.drawable.ic_baseline_wifi_24);
                        connectSelectionSpinner.setVisibility(View.GONE);
                        deviceIdentifier.setCommDeviceType(CommDeviceType.TCPIP);
                        // put the right ip and port
//                        deviceIdentifier.setIpAddress("192.168.188.118");
//                        deviceIdentifier.setIpPort(5030);
                        //95.230.172.102:8083
                        deviceIdentifier.setIpAddress("95.230.172.102");
                        deviceIdentifier.setIpPort(8083);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        connectCard.setOnClickListener(v -> connectToPos());
        disconnectCard.setOnClickListener(v -> disconnectFromPos());
        paymentCard.setOnClickListener(v -> payment());
        reverseCard.setOnClickListener(v -> reversal());
        retroactiveReverseCard.setOnClickListener(v -> retroactiveReversal());
        cancelTransactionCard.setOnClickListener(v -> cancelTransaction());
        refundCard.setOnClickListener(v -> refund());
        preAuthCard.setOnClickListener(v -> preAuth());
        intPreAuthCard.setOnClickListener(v -> intPreAuth());
        closurePreAuthCard.setOnClickListener(v -> closurePreAuth());
        devicesCard.setOnClickListener(v -> mPayDevices());
        dailyCard.setOnClickListener(v -> dailyTotals());
        closeCard.setOnClickListener(v -> closeSession());
        dllCard.setOnClickListener(v -> dll());
        posMenu.setOnClickListener(v -> showPosMenu());
        posSwUpdateCard.setOnClickListener(v -> posSwUpdateCmd());

        isAsyncSwUpdateEnabledCard.setOnClickListener(v -> isAsyncPosSoftwareUpdateSupportedReq());
        isSwUpdateRunningCard.setOnClickListener(v -> isPosSoftwareUpdateRunningReq());
        isSwUpdatePausedCard.setOnClickListener(v -> isPosSoftwareUpdatePausedReq());
        percentageSwUpdateProgressCard.setOnClickListener(v -> posSoftwareUpdatePercentageReq());
        pauseSwUpdateRunningCard.setOnClickListener(v -> pausePosSoftwareUpdateCmd());
        resumeSwUpdateRunningCard.setOnClickListener(v -> resumePosSoftwareUpdateCmd());

        lastTransactionResultCard.setOnClickListener(v -> lastTransactionResult());
        recoveryCard.setOnClickListener(v -> recoveryTransaction());
        pauseUpdateBtn.setOnClickListener(v -> pauseUpdate());
        dataAcquisitionPicture.setOnClickListener(v -> dataAcquisitionPicture());
        dataAcquisitionQRCode.setOnClickListener(v -> dataAcquisitionQRCode());
        reboot.setOnClickListener(v -> reboot());
        posWifiScanReq.setOnClickListener(v -> posWifiScanReq());
        posWifiConnReq.setOnClickListener(v -> posWifiConnReq());
        posWifiDisconnReq.setOnClickListener(v -> posWifiDisconnReq());
        posWifiStatusReq.setOnClickListener(v -> posWifiStatusReq());
        posTmsTunnelSwUpdReq.setOnClickListener(v -> posTmsTunnelSwUpdReq());
        posTmsWifiSwUpdReq.setOnClickListener(v -> posTmsWifiSwUpdReq());
        reversalInfoReqCard.setOnClickListener(v -> reversalInfoRequest());
        sendFileToTerminalCmd.setOnClickListener(v -> sendFileToTerminalCmd());

        startupCard.setOnClickListener(v -> startupCmd());
        isStartedUpCard.setOnClickListener(v -> isStartedUpCmd());
        shutdownCard.setOnClickListener(v -> shutdownCmd());

        btnCI_ATCA.setOnClickListener(v -> cardInteraction_ATCA());
        btnCI_ATBO.setOnClickListener(v -> cardInteraction_ATBO());
        btnCI_ATBS.setOnClickListener(v -> cardInteraction_ATBS());
        btnCI_ATBB.setOnClickListener(v -> cardInteraction_ATBB());
        btnCI_ATBP.setOnClickListener(v -> cardInteraction_ATBP());
        btnCI_ATCT.setOnClickListener(v -> cardInteraction_ATCT());
        btnCI_ATSO.setOnClickListener(v -> cardInteraction_ATSO());
        btnCI_ATSC.setOnClickListener(v -> cardInteraction_ATSC());
        btnCI_ATMF.setOnClickListener(v -> cardInteraction_ATMF());
        btnCI_ATSR.setOnClickListener(v -> cardInteraction_ATSR());
        btnCI_ATDA.setOnClickListener(v -> cardInteraction_ATDA());
        btnCI_ATDH.setOnClickListener(v -> cardInteraction_ATDH());
        btnCI_ATZC.setOnClickListener(v -> cardInteraction_ATZC());

//        bATBODialog_CancelButton.setOnClickListener(v -> cardInteraction_ATBO_Cancel());
//        bATBODialog_ConfirmButton.setOnClickListener(v -> cardInteraction_ATBO_Confirm());

    }

    private void loadBtPairedDevices() {
        logger.logInfo(">>> loadBtPairedDevices()");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // do not manage bluetooth devices if BLUETOOTH_CONNECT permission is not granted
            return;
        }

        // reset mpayBluetoothDeviceSet
        if (mpayBluetoothDeviceSet != null) {
            mpayBluetoothDeviceSet.clear();
        }

        // reset mpayDeviceArrayList
        if (mpayDeviceArrayList != null) {
            mpayDeviceArrayList.clear();
        }

        BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter == null) {
            // Device won't support Bluetooth
            logger.logInfo("BluetoothAdapter.getDefaultAdapter()  -->  null");
            logger.logInfo("Android-Device doesn't support BLUETOOTH!!!");
            Toast.makeText(this, "WARNING: Android-Device doesn't support BLUETOOTH!!!", Toast.LENGTH_LONG).show();
        } else {
            logger.logInfo("Android-Device support BLUETOOTH!!!");

            if (!bAdapter.isEnabled()) {
                logger.logInfo("Android-Device has BLUETOOTH disabled!!!");
                Toast.makeText(this, "WARNING: Android-Device has BLUETOOTH disabled!!!", Toast.LENGTH_LONG).show();
            } else {
                logger.logInfo("Android-Device has BLUETOOTH enabled!!!");
                // Command to get Bluetooth paired devices
                // Get paired devices.
                Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
                if (pairedDevices == null) {
                    logger.logInfo("bAdapter.getBondedDevices()  -->  null");
                } else {
                    logger.logInfo("pairedDevices.size()  -->  " + pairedDevices.size());
                    if (pairedDevices.size() > 0) {
                        // There are paired devices. Get the name and address of each paired device.
                        for (BluetoothDevice device : pairedDevices) {
                            logger.logInfo("pairedDevices.iterator().toString()  -->  " + pairedDevices.iterator().toString());
                            String deviceName = device.getName();
                            String deviceHardwareAddress = device.getAddress(); // MAC address

                            logger.logInfo("device.getName()  -->  " + deviceName);
                            logger.logInfo("device.getAddress()  -->  " + deviceHardwareAddress);

                            if (deviceName != null) {
                                if (deviceName.contains("MPAY")) {
                                    mpayBluetoothDeviceSet.add(device);
                                    mpayDeviceArrayList.add(deviceName);
                                } else {
                                    logger.logInfo("The Android-Device has BLUETOOTH enabled!!!");
                                }
                            } else {
                                logger.logInfo("deviceName == null");
                            }
                        }
                    }
                }
            }
        }


        if (mpayDeviceArrayList != null) {

            if (mpayDeviceArrayList.size() > 0) {
                //TODO:
            } else {
                //TODO:
            }
        } else {
            //TODO:
        }


    }

    private void connectToPos() {
        updateStatusText(getString(R.string.status_connecting));
        connectionLayer.setUserID("12345678901234567");
        connectionLayer.connectPos(deviceIdentifier, "");
        enableConnectionSpinner(false);
    }

    private void disconnectFromPos() {
        if (connectionLayer.getPosStatus().getPosStatusCode() != PosStatusCode.POS_STATUS_DISCONNECTED) {
            updateStatusText("disconnecting");
        }

        connectionLayer.disconnectPos();
        enableConnectionSpinner(true);
    }

    private void dll() {
        if (notConnected()) {
            logger.logInfo("cannotConnected  -->  TRUE:  POS NOT CONNECTED!");
            return;
        }

//        terminalID = "11223344";
        terminalID = "99951801";
        HostTransportProtocol hostTransportProtocol = HostTransportProtocol.HOST_TRANSPORT_PROTOCOL_BT;
        HostConnectionType hostConnectionType = HostConnectionType.HOST_CONNECTION_TYPE_SSL;
        String gtId = "11122";
        String ipAddress = "195.188.150.133";
        int tcpPort = 46409;
        int tlsCertificateId = 0x4302;           // SecureTrust
        int gtIndex = 0;
        String persId = "001"; // default
        String sBillPayment = "4000";

        BankHost bankHost = new BankHost();
        bankHost.setTransportProtocol(hostTransportProtocol);
        bankHost.setConnectionType(hostConnectionType);
        bankHost.setGtId(gtId);
        bankHost.setIpAddress(ipAddress);
        bankHost.setTcpPort(tcpPort);
        bankHost.setTlsCertificateId(tlsCertificateId);
        bankHost.setGtIndex(gtIndex);
        bankHost.setPersonalizationId(persId);

        Log.i("TerminalID", "" + connectionLayer.getTerminalId());
        connectionLayer.dll(terminalID, bankHost, TRANSACTION_ID, sBillPayment);
        connectionLayer.addOnDllCompletedListener(this::onDllCompleted);
    }


    private void payment() {
        logger.logInfo(">>> payment()");

        // pauseUpdateBtn management
        pauseUpdateBtn.setVisibility(View.GONE);

        if (!amount_pay.getText().toString().isEmpty()) {
            logger.logInfo("payment amount == " + Integer.parseInt(amount_pay.getText().toString()));

            // get current TID
            String currentTid = connectionLayer.getTerminalId();
            logger.logInfo("currentTid == " + currentTid);

            /* Setting of paymentInputData */
            PaymentInputData paymentInputData = new PaymentInputData();
            paymentInputData.setTerminalId(currentTid);
            paymentInputData.setIdTrx("1234567");
            paymentInputData.setPaymentAmount(Integer.parseInt(amount_pay.getText().toString()));

            paymentInputData.setCurrency("978");
//            logger.logInfo("CashbackAmount == " + Integer.parseInt(amount_pay.getText().toString()));
//            paymentInputData.setCashbackAmount(Integer.parseInt(amount_pay.getText().toString()));
//            logger.logInfo("TipAmount == " + Integer.parseInt(amount_pay.getText().toString()));
//            paymentInputData.setTipAmount(Integer.parseInt(amount_pay.getText().toString()));
//            paymentInputData.setPayTechFilter("FC80");
//            paymentInputData.setAddDataField47("DF800102ABCD");
//            paymentInputData.setAddDataField48("DF8001021234");
            /*-*/

            connectionLayer.payment(paymentInputData);

            // set the listener on the completion event of the launched Operation
            connectionLayer.addOnPaymentCompletedListener(this::onPaymentCompleted);
        } else {
            Toast.makeText(this, "Amount is empty: cmd not executable!", Toast.LENGTH_LONG * 3).show();
        }
    }

    private void reversal() {
        logger.logInfo("reversal");

        connectionLayer.reversePayment("", TRANSACTION_ID);
        connectionLayer.addOnReversalCompletedListener(this::onReversalCompleted);
    }

    private void retroactiveReversal() {
        logger.logInfo("retroactiveReversal");

        RetroactiveReversalInputData retroactiveReversalInputData = new RetroactiveReversalInputData();
        retroactiveReversalInputData.setPan_abi("0808");
        retroactiveReversalInputData.setAiic("00000080007");
        retroactiveReversalInputData.setOp_amount("99");
        retroactiveReversalInputData.setOp_stan("000023");
        retroactiveReversalInputData.setOp_authorization_code("123456");
        retroactiveReversalInputData.setOp_date("230421");
        retroactiveReversalInputData.setOp_time("1916");
        //retroactiveReversalInputData.setPreauth_code("");

        connectionLayer.retroactiveReversePayment(TRANSACTION_ID, retroactiveReversalInputData, null, null);
        connectionLayer.addOnReversalCompletedListener(this::onReversalCompleted);
    }

    private void cancelTransaction() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("cancelTransaction");
        connectionLayer.cancelTransaction();
    }


    private void refund() {
        logger.logInfo(">>> refund()");

        // pauseUpdateBtn management
        pauseUpdateBtn.setVisibility(View.GONE);

        if (!amount_refund.getText().toString().isEmpty()) {
            logger.logInfo("refund amount == " + Integer.parseInt(amount_refund.getText().toString()));

            // get current TID
            String currentTid = connectionLayer.getTerminalId();
            logger.logInfo("currentTid == " + currentTid);

            connectionLayer.refund(currentTid, Integer.parseInt(amount_refund.getText().toString()), "");

            // set the listener on the completion event of the launched Operation
            connectionLayer.addOnRefundCompletedListener(this::onRefundCompleted);
        } else {
            Toast.makeText(this, "Amount is empty: cmd not executable!", Toast.LENGTH_LONG * 3).show();
        }

    }


    private void preAuth() {
        logger.logInfo(">>> preAuth()");

        // pauseUpdateBtn management
        pauseUpdateBtn.setVisibility(View.GONE);

        if (!amount_preAuth.getText().toString().isEmpty()) {
            logger.logInfo("preAuth amount == " + Integer.parseInt(amount_preAuth.getText().toString()));

            // get current TID
            String currentTid = connectionLayer.getTerminalId();
            logger.logInfo("currentTid == " + currentTid);
            String idTrx = "1";
            String addDataField47 = "DF8001029876";
            String addDataField48 = "DF800102ABCD";

            connectionLayer.preAuthorization(currentTid, Integer.parseInt(amount_preAuth.getText().toString()), idTrx, addDataField47, addDataField48);

            // set the listener on the completion event of the launched Operation
            connectionLayer.addOnPreAuthorizationCompletedListener(this::onPreAuthorizationCompleted);
        } else {
            Toast.makeText(this, "Amount is empty: cmd not executable!", Toast.LENGTH_LONG * 3).show();
        }

    }


    private void intPreAuth() {
        logger.logInfo(">>> intPreAuth()");

        // pauseUpdateBtn management
        pauseUpdateBtn.setVisibility(View.GONE);

        if (!amount_intPreAuth.getText().toString().isEmpty()) {
            logger.logInfo("intPreAuth amount == " + Integer.parseInt(amount_intPreAuth.getText().toString()));

            // get current TID
            String currentTid = connectionLayer.getTerminalId();
            logger.logInfo("currentTid == " + currentTid);

            /* Setting of preAuthInputDataCustom */
            PreAuthInputDataCustom preAuthInputDataCustom = new PreAuthInputDataCustom();
            preAuthInputDataCustom.setTerminalId(currentTid);
            preAuthInputDataCustom.setIdTrx("");
            preAuthInputDataCustom.setAmount(Integer.parseInt(amount_intPreAuth.getText().toString()));
            preAuthInputDataCustom.setCurrency("978");
            preAuthInputDataCustom.setPreAuthCode("000123456789");
            preAuthInputDataCustom.setAddDataField47("DF8001021234");
            preAuthInputDataCustom.setAddDataField48("DF800102abcd");

            preAuthInputDataCustom.setPan("6130");
            preAuthInputDataCustom.setAiic("00000080007");
            preAuthInputDataCustom.setServiceId("123");
            /*-*/

            connectionLayer.intPreAuthorizationCustom(preAuthInputDataCustom);

            // set the listener on the completion event of the launched Operation
            connectionLayer.addOnPreAuthorizationCompletedListener(this::onPreAuthorizationCompleted);
        } else {
            Toast.makeText(this, "Amount is empty: cmd not executable!", Toast.LENGTH_LONG * 3).show();
        }
    }


    private void closurePreAuth() {
        logger.logInfo(">>> closurePreAuth()");

        // pauseUpdateBtn management
        pauseUpdateBtn.setVisibility(View.GONE);

        if (!amount_closurePreAuth.getText().toString().isEmpty()) {
            logger.logInfo("closurePreAuth amount == " + Integer.parseInt(amount_closurePreAuth.getText().toString()));

            // get current TID
            String currentTid = connectionLayer.getTerminalId();
            logger.logInfo("currentTid == " + currentTid);

            /* Setting of preAuthInputDataCustom */
            PreAuthInputDataCustom preAuthInputDataCustom = new PreAuthInputDataCustom();
            preAuthInputDataCustom.setTerminalId(currentTid);
            preAuthInputDataCustom.setIdTrx("");
            preAuthInputDataCustom.setAmount(Integer.parseInt(amount_closurePreAuth.getText().toString()));
            preAuthInputDataCustom.setCurrency("978");
            preAuthInputDataCustom.setPreAuthCode("000123456789");
            preAuthInputDataCustom.setAddDataField47("DF8001021234");
            preAuthInputDataCustom.setAddDataField48("DF800102abcd");

            preAuthInputDataCustom.setPan("6130");
            preAuthInputDataCustom.setAiic("00000080007");
            preAuthInputDataCustom.setServiceId("123");
            /*-*/

            connectionLayer.closePreAuthorizationCustom(preAuthInputDataCustom);

            // set the listener on the completion event of the launched Operation
            connectionLayer.addOnPreAuthorizationCompletedListener(this::onPreAuthorizationCompleted);
        } else {
            Toast.makeText(this, "Amount is empty: cmd not executable!", Toast.LENGTH_LONG * 3).show();
        }
    }


    //region DataAcquisition
    private void dataAcquisitionPicture() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("dataAcquisitionPicture");
        connectionLayer.dataAcquisition(new DataAcquisitionRequest(DataAcquisitionActionValue.PICTURE, 60, true, true));
        connectionLayer.addOnDataAcquisitionCompletedListener((this::onDataAcquisitionCompleted));
    }

    private void dataAcquisitionQRCode() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("dataAcquisitionQRCode");
        connectionLayer.dataAcquisition(new DataAcquisitionRequest(DataAcquisitionActionValue.QR_CODE, 60, true, true));
        connectionLayer.addOnDataAcquisitionCompletedListener((this::onDataAcquisitionCompleted));
    }

    //endregion

    private void reboot() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("reboot");
        connectionLayer.reboot("80000000");
        connectionLayer.addOnRebootCompletedListener(this::onRebootCompleted);
    }

    //region - CardInteraction - Commands

    private void cardInteraction_ATCA() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("cardInteraction_ATCA");
        CardInteractionRequest cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SLAVE_ACTIVATION);
        connectionLayer.cardInteraction(cardInteractionRequest);
        connectionLayer.addOnCardInteractionCompletedListener((this::onCardInteractionCompleted));
    }

    int huntingTime = 20;                                                                          // Default value
    int magOption = 1;                                                                             // Default value (Mag reading Enabled)
    //int magOption = 0;                                                                           // Mag Disabled
    int iccOption = 0x0100;                                                                        // Default value (Icc Enabled)
    //int iccOption = 0x0000;                                                                      // ICC Disabled
    int ctlsOption = 0x4000;                                                                       // Default value (Cless enabled)
    //int ctlsOption = 0x0000;                                                                     // Cless disabled
    boolean bATBOparamsEntered = false;

    private void cardInteraction_ATBO() {
        logger.logInfo("cardInteraction_ATBO");
        if (notConnected()) {
            return;
        }

        //Re-init
        bATBOparamsEntered = false;

//        Dialog dDiATBOParamEnterDialog = new Dialog(this);
//        dDiATBOParamEnterDialog.setTitle("ATBO param.s");
//        dDiATBOParamEnterDialog.setCancelable(false);
//        dDiATBOParamEnterDialog.setContentView(R.layout.dialog_atbo_param);
//
//        dDiATBOParamEnterDialog.show();
//
//        huntingTime_EditText = findViewById(R.id.hunting_time_param_edit);
//        magOptions_EditText = findViewById(R.id.mag_opt_param_edit);
//        iccOptions_EditText = findViewById(R.id.icc_opt_param_edit);
//        ctlsOptions_EditText = findViewById(R.id.ctls_opt_param_edit);
//        bATBODialog_CancelButton = findViewById(R.id.atboDialog_cancelButton);
//        bATBODialog_ConfirmButton = findViewById(R.id.atboDialog_confirmButton);
//
//        huntingTime_EditText.setText(Integer.toString(huntingTime, 10));
//        magOptions_EditText.setText(Integer.toString(magOption, 10));
//        iccOptions_EditText.setText(Integer.toString(iccOption, 16));
//        ctlsOptions_EditText.setText(Integer.toString(ctlsOption, 16));
//
//        bATBODialog_CancelButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View arg0)
//            {
//                bATBOparamsEntered = false;
//                Toast.makeText(dDiATBOParamEnterDialog.getContext(), "ATBO param NOT defined!", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        bATBODialog_ConfirmButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View arg0)
//            {
//                huntingTime = Integer.parseInt(huntingTime_EditText.getText().toString(), 10);
//                magOption = Integer.parseInt(huntingTime_EditText.getText().toString(), 10);
//                iccOption = Integer.parseInt(huntingTime_EditText.getText().toString(), 16);
//                ctlsOption = Integer.parseInt(huntingTime_EditText.getText().toString(), 16);
//                bATBOparamsEntered = true;
//                Toast.makeText(dDiATBOParamEnterDialog.getContext(), "ATBO param defined!", Toast.LENGTH_LONG).show();
//            }
//        });
//
        logger.logInfo("cardInteraction_ATBO parameters entering - completed");
        logger.logInfo("huntingTime  ==  " + huntingTime);
        logger.logInfo("magOption  ==  " + magOption);
        logger.logInfo("iccOption  ==  " + iccOption);
        logger.logInfo("ctlsOption  ==  " + ctlsOption);
        logger.logInfo("bATBOparamsEntered  ==  " + bATBOparamsEntered);
//        if (bATBOparamsEntered == false)
//        {
//            logger.logInfo("No Action: ATBO Param.s NOT defined");
//        } else {
        //
        CardInteractionRequest cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.CARD_DETECT);
        cardInteractionRequest.setDetectionParameters(huntingTime, magOption, iccOption, ctlsOption);
        connectionLayer.cardInteraction(cardInteractionRequest);
        connectionLayer.addOnCardInteractionCompletedListener((this::onCardInteractionCompleted));
//        }
    }

//    private void cardInteraction_ATBO_Cancel() {
//        logger.logInfo("cardInteraction_ATBO_Cancel");
//
//        bATBOparamsEntered = false;
//        Toast.makeText(getApplicationContext(), "ATBO param NOT defined!", Toast.LENGTH_LONG).show();
//    }
//
//    private void cardInteraction_ATBO_Confirm() {
//        logger.logInfo("cardInteraction_ATBO_Confirm");
//
//
//        bATBOparamsEntered = true;
//        Toast.makeText(getApplicationContext(), "ATBO param defined!", Toast.LENGTH_LONG).show();
//    }

    private void cardInteraction_ATBS() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("cardInteraction_ATBS");
        CardInteractionRequest cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.CARD_STATE);
        connectionLayer.cardInteraction(cardInteractionRequest);
        connectionLayer.addOnCardInteractionCompletedListener((this::onCardInteractionCompleted));
    }

    private void cardInteraction_ATBB() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("cardInteraction_ATBB");
        CardInteractionRequest cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.CARD_EXTRACTION);
        connectionLayer.cardInteraction(cardInteractionRequest);
        connectionLayer.addOnCardInteractionCompletedListener((this::onCardInteractionCompleted));
    }

    private void cardInteraction_ATBP() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("cardInteraction_ATBP");
        CardInteractionRequest cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SEND_APDU);
        cardInteractionRequest.setCommandParameters(true, "00A40000023F00||00A40000021100||00A40000021102||00B000000000");
        connectionLayer.cardInteraction(cardInteractionRequest);
        connectionLayer.addOnCardInteractionCompletedListener((this::onCardInteractionCompleted));
    }

    private void cardInteraction_ATCT() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("cardInteraction_ATCT");
        CardInteractionRequest cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SEND_CTS);
        cardInteractionRequest.setCommandParameters(false, "00A40000023F00||00A40000021100||00A40000021102||00B000000000");
        connectionLayer.cardInteraction(cardInteractionRequest);
        connectionLayer.addOnCardInteractionCompletedListener((this::onCardInteractionCompleted));
    }

    private void cardInteraction_ATSO() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("cardInteraction_ATSO");
        CardInteractionRequest cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SAM_OPEN);
        cardInteractionRequest.setOpenSamParameters(1);
        connectionLayer.cardInteraction(cardInteractionRequest);
        connectionLayer.addOnCardInteractionCompletedListener((this::onCardInteractionCompleted));
    }

    private void cardInteraction_ATSC() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("cardInteraction_ATSC");
        CardInteractionRequest cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SAM_SEND_APDU);
        cardInteractionRequest.setOpenSamParameters(1, false);
        connectionLayer.cardInteraction(cardInteractionRequest);
        connectionLayer.addOnCardInteractionCompletedListener((this::onCardInteractionCompleted));
    }

    private void cardInteraction_ATMF() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("cardInteraction_ATMF");
        CardInteractionRequest cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SEND_MIFARE);
        cardInteractionRequest.setCommandParameters(false, "");//da definire parametro di input
        connectionLayer.cardInteraction(cardInteractionRequest);
        connectionLayer.addOnCardInteractionCompletedListener((this::onCardInteractionCompleted));
    }

    private void cardInteraction_ATSR() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("cardInteraction_ATSR");
        CardInteractionRequest cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SEND_STM_SRI_SRT);
        cardInteractionRequest.setCommandParameters(false, "");//da definire parametro di input
        connectionLayer.cardInteraction(cardInteractionRequest);
        connectionLayer.addOnCardInteractionCompletedListener((this::onCardInteractionCompleted));
    }

    private void cardInteraction_ATDA() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("cardInteraction_ATDA");
        CardInteractionRequest cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SLAVE_DEACTIVATION);
        connectionLayer.cardInteraction(cardInteractionRequest);
        connectionLayer.addOnCardInteractionCompletedListener((this::onCardInteractionCompleted));
    }

    private void cardInteraction_ATDH() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("cardInteraction_ATDH");
        CardInteractionRequest cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SLAVE_DEACTIVATION_HOLD_CARD);
        connectionLayer.cardInteraction(cardInteractionRequest);
        connectionLayer.addOnCardInteractionCompletedListener((this::onCardInteractionCompleted));
    }

    private void cardInteraction_ATZC() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("cardInteraction_ATZC");
        CardInteractionRequest cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.CANCEL_COMMAND);
        connectionLayer.cardInteraction(cardInteractionRequest);
        connectionLayer.addOnCardInteractionCompletedListener((this::onCardInteractionCompleted));

    }

    //endregion

    //region - Update FW -
    private void posSwUpdateCmd() {
        logger.logInfo("posSwUpdateCmd()");

        updateCloud();
        //updateLocal();
    }

    private void updateCloud() {
        tvUpdate.setText("Download FW: ");

        DownloadTask downloadTask = new DownloadTask();
        //downloadTask.execute("https://drive.google.com/u/0/uc?id=1wEmfdnq5mw6IPB2wHnMtDrC-SCrHZEX9&export=download");
        //downloadTask.execute("https://drive.google.com/u/0/uc?id=1VGk46-UtKtMzKaTo5hSd3rUAQwXaLAIn&export=download");
        //downloadTask.execute("https://drive.google.com/u/0/uc?id=11Y5cl9ibSgrfRxHXTP7b61A1Afbub3vf&export=download");
        downloadTask.execute("https://drive.google.com/u/0/uc?id=1yA6kCbwbl2PgkpFAeevf-AAa63MRUjlf&export=download");

        connectionLayer.addOnPosSoftwareUpdateCompletedListener(this::onPosSoftwareUpdateCompleted);
        connectionLayer.addOnPosSoftwareUpdateProgressListener(this::onPosSoftwareUpdateProgress);
    }

    private void updateLocal() {

        tvUpdate.setText("Sw Update Operation (from Local file) in progress ....");

        // Load Sw package from local allocation
        getFileFromAssets();

        /* Launch of Op. command */
        // 1° posSoftwareUpdate API implementation
        //connectionLayer.posSoftwareUpdate(nameFile);
        // 2° posSoftwareUpdate API implementation
        //long updateOpt = 0x00000000;                 // Sync Sw Update process
        long updateOpt = 0x80000000;              // Async Sw Update process
        //long updateOpt = 0x40000000;               // PDM Sw Update process
        connectionLayer.posSoftwareUpdate(nameFile, "Please, wait...", updateOpt);
        /*-*/

        // Op. completion Listener
        connectionLayer.addOnPosSoftwareUpdateCompletedListener((this::onPosSoftwareUpdateCompleted));
        connectionLayer.addOnPosSoftwareUpdateProgressListener((this::onPosSoftwareUpdateProgress));
    }


    private void isAsyncPosSoftwareUpdateSupportedReq() {
        logger.logInfo("isAsyncPosSoftwareUpdateSupportedReq()");

        boolean isAsyncPosSoftwareUpdateSupported;

        isAsyncPosSoftwareUpdateSupported = connectionLayer.isAsyncPosSoftwareUpdateSupported();
        logger.logDebug("connectionLayer.isAsyncPosSoftwareUpdateSupported()  -->  " + isAsyncPosSoftwareUpdateSupported);

        Toast.makeText(this, "isAsyncUpdateSupported()  -->  " + isAsyncPosSoftwareUpdateSupported, Toast.LENGTH_LONG * 3).show();
    }

    private void isPosSoftwareUpdateRunningReq() {
        logger.logInfo("isPosSoftwareUpdateRunningReq()");

        long isPosSoftwareUpdateRunning;

        isPosSoftwareUpdateRunning = connectionLayer.isPosSoftwareUpdateRunning();
        logger.logDebug("connectionLayer.isPosSoftwareUpdateRunning()  -->  " + isPosSoftwareUpdateRunning);

        Toast.makeText(this, "isPosSoftwareUpdateRunning()  -->  " + isPosSoftwareUpdateRunning, Toast.LENGTH_LONG * 3).show();
    }

    private void isPosSoftwareUpdatePausedReq() {
        logger.logInfo("isPosSoftwareUpdatePausedReq()");

        boolean isPosSoftwareUpdatePaused;

        isPosSoftwareUpdatePaused = connectionLayer.isPosSoftwareUpdatePaused();
        logger.logDebug("connectionLayer.isPosSoftwareUpdatePaused()  -->  " + isPosSoftwareUpdatePaused);

        Toast.makeText(this, "isPosSoftwareUpdatePaused()  -->  " + isPosSoftwareUpdatePaused, Toast.LENGTH_LONG * 3).show();
    }

    private void posSoftwareUpdatePercentageReq() {
        logger.logInfo("posSoftwareUpdatePercentageReq()");

        int posSoftwareUpdatePercentage;

        posSoftwareUpdatePercentage = connectionLayer.posSoftwareUpdatePercentage();
        logger.logDebug("connectionLayer.posSoftwareUpdatePercentage()  -->  " + posSoftwareUpdatePercentage);

        Toast.makeText(this, "posSoftwareUpdatePercentage()  -->  " + posSoftwareUpdatePercentage, Toast.LENGTH_LONG * 3).show();
    }

    private void pausePosSoftwareUpdateCmd() {
        logger.logInfo("pausePosSoftwareUpdateCmd()");

        int pausePosSoftwareUpdateResult;

        pausePosSoftwareUpdateResult = connectionLayer.pausePosSoftwareUpdate();
        logger.logDebug(" connectionLayer.pausePosSoftwareUpdate()  -->  " + pausePosSoftwareUpdateResult);

        Toast.makeText(this, "pausePosSoftwareUpdate()  -->  " + pausePosSoftwareUpdateResult, Toast.LENGTH_LONG * 3).show();
    }

    private void resumePosSoftwareUpdateCmd() {
        logger.logInfo("resumePosSoftwareUpdateCmd()");

        int resumePosSoftwareUpdateResult;

        resumePosSoftwareUpdateResult = connectionLayer.resumePosSoftwareUpdate();
        logger.logDebug(" connectionLayer.resumePosSoftwareUpdate()  -->  " + resumePosSoftwareUpdateResult);

        Toast.makeText(this, "resumePosSoftwareUpdate()  -->  " + resumePosSoftwareUpdateResult, Toast.LENGTH_LONG * 3).show();
    }
    //endregion

    private void dailyTotals() {
        if (cannotPerform()) {
            return;
        }

        // TODO: choose type of totals dialog

        connectionLayer.dailyTotals();
        connectionLayer.addOnTotalsCompletedListener((this::onTotalsCompleted));
    }

    private void closeSession() {
        if (cannotPerform()) {
            return;
        }
        connectionLayer.closeSession();
        connectionLayer.addOnCloseSessionCompletedListener((this::onCloseSessionCompleted));
    }

    private void pauseUpdate() {
        if (connectionLayer.isPosSoftwareUpdatePaused()) {
            connectionLayer.resumePosSoftwareUpdate();
            pauseUpdateBtn.setText(R.string.pause_update);
            tvUpdate.setText("Update");

        } else {
            connectionLayer.pausePosSoftwareUpdate();
            pauseUpdateBtn.setText(R.string.resume_update);
            tvUpdate.setText("Download FW: ");
        }
    }

    public void showPosMenu() {
        if (notConnected()) {
            return;
        }
        logger.logInfo("showPosMenu");
        //connectionLayer.showPosMenu();

        logger.logInfo("connectionLayer.getPosSoftwareVersion()  -->  " + connectionLayer.getPosSoftwareVersion());

        Toast.makeText(this, "Operation not enabled", Toast.LENGTH_LONG).show();
    }

    public void posWifiScanReq() {
        if (notConnected()) {
            logger.logInfo("POS not Connected");

            Toast.makeText(this, "Pos Wifi Scan Request NOT performed  (POS not connected)", Toast.LENGTH_LONG * 3).show();
            return;
        }

        // posWifiScanReq operation launching
        connectionLayer.posWifiScanReq();

        // listener of posWifiScanReq operation result
        connectionLayer.addOnPosWifiOpReqCompletedListener((this::onPosWifiOpReqCompleted));


        Toast.makeText(this, "Pos Wifi Scan Request Completed  -->  ESITO:   To be wait in the onPosWifiOpReqCompleted", Toast.LENGTH_LONG * 3).show();
    }

    public void posWifiConnReq() {
        if (notConnected()) {
            logger.logInfo("POS not Connected");
            return;
        }

        // set of necessary input parameter values for posWifiConnReq operation
        PosWifiAPconn posWifiAPconn = new PosWifiAPconn();
        //posWifiAPconn.setWifiSSID("");
        //posWifiAPconn.setWifiPwd("");
        //posWifiAPconn.setWifiSSID("PAXItalia");
        //posWifiAPconn.setWifiPwd("WRL$$2017@");
        //posWifiAPconn.setWifiSSID("PocketCube-96C3");
        //posWifiAPconn.setWifiPwd("H64MG8Q0");
        posWifiAPconn.setWifiSSID("WebCube-F4A0");
        posWifiAPconn.setWifiPwd("2V64BF82");
        int wifiConnOpt = 0x8000;       //with DHCP
        if ((wifiConnOpt & 0x4000) != 0) {
            // NO DHCP
            posWifiAPconn.setWifiConnOpt(0x4000);   // No DHCP
            posWifiAPconn.setWifiStaticIp("111.222.333.444");
            posWifiAPconn.setWifiGtw("111.222.333.444");
            posWifiAPconn.setWifiGtwBkp("111.222.333.444");
            posWifiAPconn.setWifiSubnetMask("111.222.333.444");
            posWifiAPconn.setWifiPrimaryDNS("111.222.333.444");
        } else {
            // DHCP
            posWifiAPconn.setWifiConnOpt(0x8000);   // DHCP
        }
        logger.logInfo("posWifiAPconn.getWifiConnOpt  -->  " + posWifiAPconn.getWifiConnOpt());

        // posWifiConnReq operation launching
        connectionLayer.posWifiConnReq(posWifiAPconn);

        // listener of posWifiConnReq operation result
        connectionLayer.addOnPosWifiOpReqCompletedListener((this::onPosWifiOpReqCompleted));

        Toast.makeText(this, "Pos Wifi Connection Request Completed  -->  Esito:  xxx", Toast.LENGTH_LONG * 3).show();
    }

    public void posWifiDisconnReq() {
        if (notConnected()) {
            logger.logInfo("POS not Connected");
            return;
        }

        // posWifiDisconnReq operation launching
        connectionLayer.posWifiDisconnReq();

        // listener of posWifiDisconnReq operation result
        connectionLayer.addOnPosWifiOpReqCompletedListener((this::onPosWifiOpReqCompleted));

        Toast.makeText(this, "Pos Wifi Disconnection Request Completed  -->  Esito:  xxx", Toast.LENGTH_LONG * 3).show();
    }

    public void posWifiStatusReq() {
        if (notConnected()) {
            logger.logInfo("POS not Connected");
            return;
        }

        // posWifiStatusReq operation launching
        connectionLayer.posWifiStatusReq();

        // listener of posWifiStatusReq operation result
        connectionLayer.addOnPosWifiOpReqCompletedListener((this::onPosWifiOpReqCompleted));

        Toast.makeText(this, "Pos Wifi Status Request Completed  -->  Esito:  xxx", Toast.LENGTH_LONG * 3).show();
    }

    public void posTmsTunnelSwUpdReq() {
        if (notConnected()) {
            logger.logInfo("POS not Connected");
            return;
        }

        int updOptions = 0x20000000;
        logger.logInfo("updOptions  ==  " + updOptions);
        TmsHost tmsHost = new TmsHost();
        tmsHost.setTmsIpAddress("192.168.1.xx");    // your IP
        tmsHost.setTmsIpPort(1234);                 // your port
        tmsHost.setTmsConnOpt(0x8000);              // proto TCP/IP
        //tmsHost.setTmsConnOpt(0x4000);            // proto TCP/IP + SSL
        //tmsHost.setTmsTlsCert(0x4507);            // specific TLS Certificate
        connectionLayer.posSoftwareUpdate("wait please...", updOptions, tmsHost);

        connectionLayer.addOnPosSoftwareUpdateCompletedListener((this::onPosSoftwareUpdateCompleted));

        Toast.makeText(this, "Pos Sw Update from TMS via Device-conn Completed  -->  Esito:  xxx", Toast.LENGTH_LONG * 3).show();
    }

    public void posTmsWifiSwUpdReq() {
        if (notConnected()) {
            logger.logInfo("POS not Connected");
            return;
        }

        // Definition of Sw Update Command
        int updOptions = 0x10000000;                    // Remote Sw Update from TMS via Wifi
        logger.logInfo("updOptions  ==  " + updOptions);

        // Definition of TmsHost connection data
        TmsHost tmsHost = new TmsHost();
        tmsHost.setTmsIpAddress("192.168.1.xx");    // your IP
        tmsHost.setTmsIpPort(1234);                 // your port
        tmsHost.setTmsConnOpt(0x8000);              // proto TCP/IP
        //tmsHost.setTmsConnOpt(0x4000);            // proto TCP/IP + SSL
        //tmsHost.setTmsTlsCert(0x4507);            // specific TLS Certificate

        // Procedure start
        connectionLayer.posSoftwareUpdate("wait please...", updOptions, tmsHost);

        connectionLayer.addOnPosSoftwareUpdateCompletedListener((this::onPosSoftwareUpdateCompleted));

        Toast.makeText(this, "Pos Sw Update from TMS via POS-wifi conn Completed --> Esito:  xxx", Toast.LENGTH_LONG * 3).show();
    }


    public void sendFileToTerminalCmd() {
        if (notConnected()) {
            logger.logInfo("POS not Connected");
            return;
        }

        File fileToSend = getFileFromAssets2();

        //Remote
        //DownloadTask downloadTask = new DownloadTask();
        //downloadTask.execute("https://drive.google.com/u/0/uc?id=1wEmfdnq5mw6IPB2wHnMtDrC-SCrHZEX9&export=download");
        //String fileName = receivedFile;

        FileType fileType = FileType.IDLE_LOGO;
        String fileOwner = "fileOwner";

        // Procedure start
        connectionLayer.sendFileToTerminal(fileToSend, fileType, fileOwner);

        // Define of result listener
        connectionLayer.addOnSendFileToTrmCompletedListener((this::onSendFileToTerminalCompleted));

        Toast.makeText(this, "Send File To Trm Request Completed --> Esito:  wait the relevant listener", Toast.LENGTH_LONG * 3).show();
    }


    public void reversalInfoRequest() {
        if (notConnected()) {
            logger.logInfo("POS not Connected");
            return;
        }

        // Definition of Command data parameters
        String terminalId = "00000037";

        // Procedure start
        connectionLayer.reversalInfoReq(terminalId);

        connectionLayer.addOnReversalInfoCompletedListener((this::onPosReversalInfoReqCompleted));

        Toast.makeText(this, "Reversal Info Request Completed --> Esito:  xxx", Toast.LENGTH_LONG * 3).show();
    }


    public void startupCmd() {
        logger.logInfo(">>> startupCmd()");

        connectionLayer.startup();

        Toast.makeText(this, "connectionLayer.startup() command performed", Toast.LENGTH_LONG * 3).show();
    }

    public void isStartedUpCmd() {
        logger.logInfo(">>> isStartedUpCmd()");

        // Procedure start
        boolean isStartedUp = connectionLayer.isStartedUp();

        Toast.makeText(this, "connectionLayer.isStartedUp() --> Esito:  " + isStartedUp, Toast.LENGTH_LONG * 3).show();
    }

    public void shutdownCmd() {
        logger.logInfo(">>> shutdownCmd()");

        // Procedure start
        connectionLayer.shutdown();

        Toast.makeText(this, "connectionLayer.shutdown() command performed", Toast.LENGTH_LONG * 3).show();
    }


    private void setCurrentOperationIdForNextOperation() {
        setCurrentOperationId(String.format("%08d", nextOperationId++));
    }

    private void resetCurrentOperationId() {
        setCurrentOperationId(null);
    }

    private void setCurrentOperationId(final String value) {
        currentOperationId = value;
        savePersistenceData();
    }

    private void savePersistenceData() {
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("currentOperationId", currentOperationId);
        editor.commit();
        logger.logInfo("saved currentOperationId=" + currentOperationId);
    }

    private void retrievePersistenceData() {
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        currentOperationId = settings.getString("currentOperationId", null);
        logger.logInfo("retrieved currentOperationId=" + currentOperationId);
    }

    private void updateStatusText(String text) {
        connectionStatus.setText(text);
    }

    private void updateConnectionErrorText(String text) {
        connectionStatus.setText(text);
    }

    private void enableConnectionSpinner(boolean enabled) {
        connectionTypeSpinner.setEnabled(enabled);
        connectionTypeSpinner.setClickable(enabled);
        connectSelectionSpinner.setEnabled(enabled);
        connectSelectionSpinner.setClickable(enabled);
    }

    @BluetoothDataReceived
    public void onBluetoothDataReceived() {
        logger.logInfo("onBluetoothDataReceived");
    }

    @BluetoothConnectionDropped
    public void onBluetoothConnectionDropped() {
        logger.logInfo("onBluetoothConnectionDropped");
    }

    @WlanDataReceived
    public void onWlanDataReceived() {
        logger.logInfo("onWlanDataReceived");
    }

    @WlanConnectionDropped
    public void onWlanConnectionDropped() {
        logger.logInfo("onWlanConnectionDropped");
    }

    @ConnectionLayerStarted
    public void onConnectionLayerStarted() {
        logger.logInfo("onConnectionLayerStarted");
        //connectionLayer.setUserID("PAX demo");
        //connectionLayer.setUserID("1234567890123456");
        connectionLayer.setUserID("12345678901234567");

//        // Cmd to connect automatically (at Connection Layer startUp) to the first "MPAY" POS found
//        connectionLayer.connectPos(deviceIdentifier, szSecToken);
    }

    @ConnectionLayerStopped
    public void onConnectionLayerStopped() {
        logger.logInfo("onConnectionLayerStopped");
        updateStatusText("Connection Layer stopped");
        enableConnectionSpinner(true);
    }

    @PosConnection
    public void onPosConnection() {
        logger.logInfo(">>>  onPosConnection");

        PosStatusCode posStatusCode = connectionLayer.getPosStatus().getPosStatusCode();
        switch (posStatusCode) {
            case POS_STATUS_DISCONNECTED:
                updateStatusText("POS non connesso");
                break;
            case POS_STATUS_UNAUTHORIZED:
                updateStatusText("POS Unauthorized");
                break;
            case POS_STATUS_OPERATIVE:
                updateStatusText("POS operativo");
                break;
            case POS_STATUS_TO_CONFIGURE:
                updateStatusText("POS da configurare");
                break;
            case POS_STATUS_TO_RECOVER:
                updateStatusText("POS da ripristinare");
                break;
            case POS_STATUS_TAMPERED:
                updateStatusText("POS manomesso");
                break;
            case POS_STATUS_OPERATION_IN_PROGRESS:
                updateStatusText("operazione in corso sul POS");
                break;
            default:
                updateStatusText("Undefined Pos Status Code");
                break;
        }

        PosInfo posInfo = connectionLayer.getPosInfo();
        if (posInfo != null) {
            logger.logInfo("PosInfo obj:" +
                    " softwareRelease=" + posInfo.getSoftwareRelease() +
                    " productCode=" + posInfo.getProductCode() +
                    " productRelease=" + posInfo.getProductRelease() +
                    " matricola=" + posInfo.getMatricola() +
                    " capabilitiesBitmap=" + posInfo.getCapabilitiesBitmap()
            );
        }

        logger.logInfo("posId: " + connectionLayer.getPosId());
        logger.logInfo("terminalId: " + connectionLayer.getTerminalId());
        logger.logInfo("pos Serial Number: " + connectionLayer.getPosSerialNumber());
        logger.logInfo("pos Status: " + connectionLayer.getPosStatus().toString());

        //fix 723
        if (posInfo != null) {
            /* PosInfo */
            logger.logInfo("pos Info Matricola: " + posInfo.getMatricola());
            logger.logInfo("pos Info ProductCode: " + posInfo.getProductCode());
            logger.logInfo("-->pos product release: " + posInfo.getProductRelease());
            logger.logInfo("-->pos software version: " + posInfo.getSoftwareRelease());
            logger.logInfo("posInfo().getCapabilitiesBitmap()  -->  " + posInfo.getCapabilitiesBitmap());

            LinkedList<TIDInfo> posInfoTidList = posInfo.getTidInfoList();
            if (posInfoTidList != null) {
                logger.logInfo("posInfoTidList.size()  ==  " + posInfoTidList.size());
                for (int i = 0; i < posInfoTidList.size(); i++) {
                    logger.logInfo("posInfoTidList.get(" + (i + 1) + ").getTID()  ==  " + posInfoTidList.get(i).getTID());
                    logger.logInfo("posInfoTidList.get(" + (i + 1) + ").getStatus()  ==  " + posInfoTidList.get(i).getStatus());
                    logger.logInfo("posInfoTidList.get(" + (i + 1) + ").getOriginalIndex()  ==  " + posInfoTidList.get(i).getOriginalIndex());
                    logger.logInfo("posInfoTidList.get(" + (i + 1) + ").getTidAbilitation()  ==  " + posInfoTidList.get(i).getTidAbilitation());
                }
            } else {
                logger.logInfo("connectionLayer.getPosInfo().getTidInfoList()  -->  posInfoTidList == null");
            }
        }

        long tidAbilitation = connectionLayer.getTIDAbilitation(0);
        if ((tidAbilitation & 0x8000) != 0) {
            logger.logInfo("CLESS enabled: ");
        }
        if ((tidAbilitation & 0x4000) != 0) {
            logger.logInfo("PreAuthorization enabled: ");
        }
        if ((tidAbilitation & 0x2000) != 0) {
            logger.logInfo("PreAuthorization Closure enabled: ");
        }
        if ((tidAbilitation & 0x1000) != 0) {
            logger.logInfo("Refund enabled: ");
        }
        if ((tidAbilitation & 0x0800) != 0) {
            logger.logInfo("Purchase Closure enabled: ");
        }

        recoveryTransaction();
    }

    @PosConnectionError
    public void onPosConnectionError(ConnectionError connectionError) {
        logger.logInfo(">>>  onPosConnectionError(...)");
        if (connectionError != null) {
            logger.logInfo("onPosConnectionError: " + connectionError.getConnectionErrorCode());
        }

        PosStatusCode posStatusCode = connectionLayer.getPosStatus().getPosStatusCode();
        switch (posStatusCode) {
            case POS_STATUS_DISCONNECTED:
                updateStatusText("POS non connesso");
                break;
            case POS_STATUS_UNAUTHORIZED:
                updateStatusText("POS Unauthorized");
                break;
            case POS_STATUS_OPERATIVE:
                updateStatusText("POS operativo");
                break;
            case POS_STATUS_TO_CONFIGURE:
                updateStatusText("POS da configurare");
                break;
            case POS_STATUS_TO_RECOVER:
                updateStatusText("POS da ripristinare");
                break;
            case POS_STATUS_TAMPERED:
                updateStatusText("POS manomesso");
                break;
            case POS_STATUS_OPERATION_IN_PROGRESS:
                updateStatusText("operazione in corso sul POS");
                break;
            default:
                updateStatusText("Undefined Pos Status Code");
                break;
        }

        if (connectionError != null) {
            final ConnectionErrorCode connectionErrorCode = connectionError.getConnectionErrorCode();
            switch (connectionErrorCode) {
                case CONNECTION_ERROR_INTERFACE_NOT_ENABLED:
                    updateConnectionErrorText("Connection Error:  Bluetooth non abilitato");
                    break;

                case CONNECTION_ERROR_DEVICE_NOT_PAIRED:
                    updateConnectionErrorText("Connection Error:  pairing non eseguito");
                    break;

                case CONNECTION_ERROR_SERVICE_NOT_FOUND:
                    updateConnectionErrorText("Connection Error:  servizio non attivo sul dispositivo");
                    break;
                case CONNECTION_ERROR_DEVICE_NOT_FOUND:
                    updateConnectionErrorText("Connection Error:  Device not found");
                    break;
                case CONNECTION_ERROR_FAILED:
                    updateConnectionErrorText("Connection Error:  Failed");
                    break;
                default:
                    updateConnectionErrorText("(Connection Error:  Undefined Result)");
                    break;
            }
        }
    }

    @PosStatusChanged
    public void onPosStatusChanged(PosStatus posStatus) {
        logger.logInfo(">>> onPosStatusChanged ( " + posStatus.getPosStatusCode().name() + " ) ");

        final PosStatusCode posStatusCode = posStatus.getPosStatusCode();
        if (posStatusCode != null) {
            posStatusTxt.setText("POS Status:  " + posStatusCode.name());
        } else {
            posStatusTxt.setText("POS Status:  null");
        }

        if (posStatusCode == PosStatusCode.POS_STATUS_DISCONNECTED) {
            if (updateRunning != 0)
                pauseUpdateBtn.setVisibility(View.VISIBLE);
        } else if (posStatusCode == PosStatusCode.POS_STATUS_UNAUTHORIZED) {
            if (updateRunning != 0)
                pauseUpdateBtn.setVisibility(View.VISIBLE);
        } else {
            updateStatusText(getString(R.string.status_connected));
            pauseUpdateBtn.setVisibility(View.GONE);
        }

        switch (posStatus.getPosStatusCode()) {
            case POS_STATUS_DISCONNECTED:
                updateStatusText("POS non connesso");
                break;
            case POS_STATUS_UNAUTHORIZED:
                updateStatusText("POS Unauthorized");
                connectToPos();
                break;
            case POS_STATUS_OPERATIVE:
                updateStatusText("POS operativo");
                break;
            case POS_STATUS_TO_CONFIGURE:
                updateStatusText("POS da configurare");
                break;
            case POS_STATUS_TO_RECOVER:
                updateStatusText("POS da ripristinare");
                break;
            case POS_STATUS_TAMPERED:
                updateStatusText("POS manomesso");
                break;
            case POS_STATUS_OPERATION_IN_PROGRESS:
                updateStatusText("operazione in corso sul POS");
                break;
            default:
                updateStatusText("Undefined Pos Status Code");
                break;
        }

        if ((posStatus.getPosStatusCode() != PosStatusCode.POS_STATUS_OPERATION_IN_PROGRESS) &&
                (posStatus.getPosStatusCode() != PosStatusCode.POS_STATUS_DISCONNECTED)) {

            // DEBUG
            PosInfo posInfo = connectionLayer.getPosInfo();
            if (posInfo != null) {
                logger.logInfo("posInfo.getSoftwareRelease()  -->  " + posInfo.getSoftwareRelease());
                logger.logInfo("posInfo.getProductCode()  -->  " + posInfo.getProductCode());
                logger.logInfo("posInfo.getProductRelease()  -->  " + posInfo.getProductRelease());
                logger.logInfo("posInfo.getMatricola()  -->  " + posInfo.getMatricola());
                logger.logInfo("posInfo.getCapabilitiesBitmap()  -->  " + posInfo.getCapabilitiesBitmap());

                if (posInfo.getTidInfoList() != null) {
                    logger.logInfo("posInfo.getTidInfoList().size()  -->  " + posInfo.getTidInfoList().size());

                    for (int i = 0; i < posInfo.getTidInfoList().size(); i++) {
                        logger.logInfo("posInfo.getTidInfoList().get(" + i + ").getTID()  -->  " + posInfo.getTidInfoList().get(i).getTID());
                        logger.logInfo("posInfo.getTidInfoList().get(" + i + ").getStatus()  -->  " + posInfo.getTidInfoList().get(i).getStatus());
                        logger.logInfo("posInfo.getTidInfoList().get(" + i + ").getOriginalIndex()  -->  " + posInfo.getTidInfoList().get(i).getOriginalIndex());
                        logger.logInfo("posInfo.getTidInfoList().get(" + i + ").getTidAbilitation()  -->  " + posInfo.getTidInfoList().get(i).getTidAbilitation());
                        logger.logInfo("posInfo.getTidInfoList().get(" + i + ").getCurrencyCode()  -->  " + posInfo.getTidInfoList().get(i).getCurrencyCode());
                        logger.logInfo("posInfo.getTidInfoList().get(" + i + ").getCountryCode()  -->  " + posInfo.getTidInfoList().get(i).getCountryCode());
                    }
                } else {
                    logger.logInfo("posInfo.getTidInfoList()  -->  null");
                }

            } else {
                logger.logInfo("connectionLayer.getPosInfo()  -->  null");
            }
        }
    }

    @ReceiptReceived
    public void onServiceOperationReceiptReceived(ServiceOperationReceipt serviceOperationReceipt) {
        logger.logInfo("onServiceOperationReceiptReceived");

        if (serviceOperationReceipt != null) {
            logger.logInfo("onServiceOperationReceiptReceived:  serviceOperationReceipt != null");
            logger.logInfo("onServiceOperationReceiptReception:  serviceOperationReceipt.getOperationType() -->  " + serviceOperationReceipt.getOperationType());
            logger.logInfo("onServiceOperationReceiptReception:  serviceOperationReceipt.getNumReceiptRows() --> " + serviceOperationReceipt.getNumReceiptRows());
            logger.logInfo("onServiceOperationReceiptReception:  serviceOperationReceipt: ");
            String receiptText = "";

            for (int i = 1; i <= serviceOperationReceipt.getNumReceiptRows(); i++) {
                ReceiptRow receiptRow = serviceOperationReceipt.getReceiptRowByRowNumber(i);
                receiptText += "[";
                if ((receiptRow.getAttribute() & ReceiptRow.ROW_ATTRIBUTE_DOUBLE_HEIGHT) != 0)
                    receiptText += "D";
                if ((receiptRow.getAttribute() & ReceiptRow.ROW_ATTRIBUTE_COMPRESSED) != 0)
                    receiptText += "C";
                if ((receiptRow.getAttribute() & ReceiptRow.ROW_ATTRIBUTE_SIGNATURE) != 0)
                    receiptText += "S";
                receiptText += "]";
                receiptText += " <";
                receiptText += receiptRow.getText();
                receiptText += ">";
                receiptText += "\n";
            }

            logger.logDebug("receipt onServiceOperationReceiptReceived:\n" + receiptText);
            showResultDialog("Scontrino di servizio", serviceOperationReceipt.toString(), serviceOperationReceipt, ReceiptType.SERVICE);
        } else {
            logger.logInfo("onServiceOperationReceiptReceived:  serviceOperationReceipt == null");
            showResultDialog("Scontrino di servizio", "");
        }
    }

    @DllCompleted
    public void onDllCompleted(DllResult dllResult) {
        logger.logInfo("onDllCompleted");

        logger.logInfo("onDllCompleted" + " terminalId: " + connectionLayer.getTerminalId() + " transactionId: " + dllResult.getTransactionId());

        logger.logInfo("dllResult.getResultMessage()  -->  " + dllResult.getResultMessage());
        logger.logInfo("dllResult.getFunctionCode()  -->  " + dllResult.getFunctionCode());
        if (dllResult.getAquirerList() != null) {
            logger.logInfo("dllResult.getAquirerList().size()  -->  " + dllResult.getAquirerList().size());
            if (dllResult.getAquirerList().size() > 0) {
                for (int i = 0; i < dllResult.getAquirerList().size(); i++) {
                    logger.logInfo("dllResult.getAquirerList().get(" + i + ").getId()  -->  " + dllResult.getAquirerList().get(i).getId());
                    logger.logInfo("dllResult.getAquirerList().get(" + i + ").getName()  -->  " + dllResult.getAquirerList().get(i).getName());
                    logger.logInfo("dllResult.getAquirerList().get(" + i + ").getResponse()  -->  " + dllResult.getAquirerList().get(i).getResponse());
                }
            }
        } else {
            logger.logInfo("dllResult.getAquirerList()  -->  null");
        }

        showDllResult(dllResult);
        resetCurrentOperationId();

        Receipt receipt = dllResult.getReceipt();
        if (receipt != null) {
            logger.logInfo("dllResult.getReceipt().getNumReceiptRows()  -->  " + dllResult.getReceipt().getNumReceiptRows());
            StringBuilder receiptText = new StringBuilder();

            for (int i = 1; i <= dllResult.getReceipt().getNumReceiptRows(); i++) {
                ReceiptRow receiptRow = dllResult.getReceipt().getReceiptRowByRowNumber(i);
                receiptText.append("[");
                if ((receiptRow.getAttribute() & ReceiptRow.ROW_ATTRIBUTE_DOUBLE_HEIGHT) != 0)
                    receiptText.append("D");
                if ((receiptRow.getAttribute() & ReceiptRow.ROW_ATTRIBUTE_COMPRESSED) != 0)
                    receiptText.append("C");
                if ((receiptRow.getAttribute() & ReceiptRow.ROW_ATTRIBUTE_SIGNATURE) != 0)
                    receiptText.append("S");
                receiptText.append("]");
                receiptText.append(" <");
                receiptText.append(receiptRow.getText());
                receiptText.append(">");
                receiptText.append("\n");
            }

            logger.logDebug("receipt (at Demo level:\n" + receiptText);
        }

        showResultDialog("Dll completato", "Result DLL: " + dllResult.getTransactionResult().name(), receipt, ReceiptType.SERVICE);
    }

    @StatusMessageEvent
    public void onStatusMessageReceivingEvent(StatusMessageResult statusMessageResult) {
        logger.logInfo(">>>  onStatusMessageReceivingEvent");

        logger.logInfo("statusMessageResult.getCode()  ==  " + statusMessageResult.getCode());
        logger.logInfo("statusMessageResult.getDescription()  ==  " + statusMessageResult.getDescription());
    }

    @PosWifiOpReqCompleted
    public void onPosWifiOpReqCompleted(PosWifiOpReqResult posWifiOpReqResult) {
        //TODO:
        TransactionResultCode posWifiOpReqResultCode_Demo;
        PosWifiOpReqResult posWifiOpReqResult_Demo = connectionLayer.getPosWifiOpReqResult();
        if (posWifiOpReqResult_Demo == null) {
            logger.logInfo("posWifiOpReqResult_Demo == null");
            posWifiOpReqResultCode_Demo = TransactionResultCode.RESULT_POS_ERROR;
        } else {
            logger.logInfo("posWifiOpReqResult_Demo != null");

            //int posWifiOpReqCode;
            PosWifiOpReqCode posWifiOpReqCode;
            ArrayList<PosWifiAPdata> posWifiAPList;
            PosWifiAPdata posWifiAPcurr;

            posWifiOpReqResultCode_Demo = posWifiOpReqResult_Demo.getResult();
            logger.logInfo("posWifiOpReqResult_Demo.getResult()  -->  " + posWifiOpReqResultCode_Demo);

            posWifiOpReqCode = posWifiOpReqResult_Demo.getPosWifiOpReqCode();
            logger.logInfo("posWifiOpReqResult.getPosWifiOpReqCode()  -->  " + posWifiOpReqCode.name());

            if (posWifiOpReqCode.equals(PosWifiOpReqCode.POS_WIFI_SCAN_OP_REQ)) {
                posWifiAPList = posWifiOpReqResult_Demo.getPosWifiAPList();
                if (posWifiAPList == null) {
                    logger.logInfo("posWifiOpReqResult.getPosWifiAPList()  -->  null");
                } else {
                    int posWifiAPList_size = posWifiAPList.size();
                    logger.logInfo("posWifiAPList.size()  -->  " + posWifiAPList_size);

                    for (int ii = 0; ii < posWifiAPList_size; ii++) {
                        logger.logInfo("posWifiAPList.get(ii).getEssid()  -->  " + posWifiAPList.get(ii).getEssid());
                        logger.logInfo("posWifiAPList.get(ii).getRssid()  -->  " + posWifiAPList.get(ii).getRssi());
                    }
                }
            }

            PosWifiOpReqResultStatusCode posWifiOpReqResultStatusCode = posWifiOpReqResult_Demo.getPosWifiOpReqResultStatusCode();
            if (posWifiOpReqResultStatusCode != null) {
                logger.logInfo("posWifiOpReqResultStatusCode  -->  " + posWifiOpReqResultStatusCode.name());

                if (posWifiOpReqResultStatusCode.equals(PosWifiOpReqResultStatusCode.POS_WIFI_OP_RESULT_STATUS_CONNECTED)) {
                    posWifiAPcurr = posWifiOpReqResult_Demo.getPosWifiAPcurr();
                    if (posWifiAPcurr == null) {
                        logger.logInfo("posWifiOpReqResult.getPosWifiAPcurr()  -->  null");
                    } else {
                        logger.logInfo("posWifiAPcurr.getEssid()  -->  " + posWifiAPcurr.getEssid());
                        logger.logInfo("posWifiAPcurr.getRssi()  -->  " + posWifiAPcurr.getRssi());
                    }
                }
            } else {
                logger.logInfo("posWifiOpReqResultStatusCode  ==  null");
            }
        }

        Toast.makeText(this, "PosWifiOpReq Result: " + posWifiOpReqResult.getResult().name(), Toast.LENGTH_LONG * 3).show();
    }

    @PaymentCompleted
    public void onPaymentCompleted(PaymentResult paymentResult) {
        logger.logInfo(">>> onPaymentCompleted");

        logger.logInfo("paymentResult.getPosId()  -->  " + paymentResult.getPosId());
        logger.logInfo("paymentResult.getTerminalId()  -->  " + paymentResult.getTerminalId());
        logger.logInfo("paymentResult.getTransactionId()  -->  " + paymentResult.getTransactionId());
        logger.logInfo("paymentResult.getTransactionResult()  -->  " + paymentResult.getTransactionResult().name());
        logger.logInfo("paymentResult.getTransactionDate()  -->  " + paymentResult.getTransactionDate());
        logger.logInfo("paymentResult.getTransactionTime()  -->  " + paymentResult.getTransactionTime());
        logger.logInfo("paymentResult.getStan()  -->  " + paymentResult.getStan());
        logger.logInfo("paymentResult.getIdOnline()  -->  " + paymentResult.getIdOnline());
        logger.logInfo("paymentResult.getPan()  -->  " + paymentResult.getPan());
        logger.logInfo("paymentResult.getBin()  -->  " + paymentResult.getBin());
        logger.logInfo("paymentResult.getPanBin()  -->  " + paymentResult.getPanBin());
        logger.logInfo("paymentResult.getPanSeqNumber()  -->  " + paymentResult.getPanSeqNumber());
        logger.logInfo("paymentResult.getEmvApplicationId()  -->  " + paymentResult.getEmvApplicationId());
        logger.logInfo("paymentResult.getActionCode()  -->  " + paymentResult.getActionCode());

        logger.logInfo("paymentResult.getAmount()  -->  " + paymentResult.getAmount());
        logger.logInfo("paymentResult.getCashbackAmount()  -->  " + paymentResult.getCashbackAmount());
        logger.logInfo("paymentResult.getTipAmount()  -->  " + paymentResult.getTipAmount());
        logger.logInfo("paymentResult.getCardType()  -->  " + paymentResult.getCardType());
        logger.logInfo("paymentResult.getTechnologyType()  -->  " + paymentResult.getTechnologyType());
        logger.logInfo("paymentResult.getAcquirerId()  -->  " + paymentResult.getAcquirerId());
        logger.logInfo("paymentResult.getAcquirerName()  -->  " + paymentResult.getAcquirerName());
        logger.logInfo("paymentResult.getMerchantId()  -->  " + paymentResult.getMerchantId());
        logger.logInfo("paymentResult.getCurrency()  -->  " + paymentResult.getCurrency());
        logger.logInfo("paymentResult.getApprovalCode()  -->  " + paymentResult.getApprovalCode());
        logger.logInfo("paymentResult.getNumReceiptCopies()  -->  " + paymentResult.getNumReceiptCopies());
        if (paymentResult.getAddData() != null) {
            logger.logInfo("paymentResult.getAddData().length  -->  " + paymentResult.getAddData().length);
            logger.logInfo("Utility.byteArrayToAsciiHex(paymentResult.getAddData())  -->  " + Utility.byteArrayToAsciiHex(paymentResult.getAddData()));
        } else {
            logger.logInfo("paymentResult.getAddData()  -->  null");
        }
        if (paymentResult.getAddDataField48() != null) {
            logger.logInfo("paymentResult.getAddDataField48().length  -->  " + paymentResult.getAddDataField48().length);
            logger.logInfo("Utility.byteArrayToAsciiHex(paymentResult.getAddDataField48())  -->  " + Utility.byteArrayToAsciiHex(paymentResult.getAddDataField48()));
        } else {
            logger.logInfo("paymentResult.getAddDataField48()  -->  null");
        }
        if (paymentResult.getAddDataField62() != null) {
            logger.logInfo("paymentResult.getAddDataField62().length  -->  " + paymentResult.getAddDataField62().length);
            logger.logInfo("Utility.byteArrayToAsciiHex(paymentResult.getAddDataField62())  -->  " + Utility.byteArrayToAsciiHex(paymentResult.getAddDataField62()));
        } else {
            logger.logInfo("paymentResult.getAddDataField62()  -->  null");
        }
        logger.logInfo("paymentResult.getDccInfo_TransactionOption()  -->  " + paymentResult.getDccInfo_TransactionOption());
        logger.logInfo("paymentResult.getDccInfo_IsoCode()  -->  " + paymentResult.getDccInfo_IsoCode());
        logger.logInfo("paymentResult.getDccInfo_ExchangeRate()  -->  " + paymentResult.getDccInfo_ExchangeRate());
        logger.logInfo("paymentResult.getDccInfo_AmountInCardCurr()  -->  " + paymentResult.getDccInfo_AmountInCardCurr());
        logger.logInfo("paymentResult.getDccInfo_CardCurrPrecision()  -->  " + paymentResult.getDccInfo_CardCurrPrecision());
        logger.logInfo("paymentResult.getDccInfo_Markup()  -->  " + paymentResult.getDccInfo_Markup());

        logger.logInfo("paymentResult.getPosMessage()  -->  " + paymentResult.getPosMessage());

        if (paymentResult.isReceiptPresent()) {
            logger.logDebug("Receipt is present!");
            logReceipt(paymentResult.getReceipt());
        } else {
            logger.logDebug("Receipt not present!");
        }

        showPaymentResult(paymentResult);

        resetCurrentOperationId();
    }

    @RefundCompleted
    public void onRefundCompleted(RefundResult refundResult) {
        logger.logInfo("onRefundCompleted");
        if (refundResult != null) {
            showRefundResult(refundResult);
        }
        resetCurrentOperationId();
    }

    @CustomCardReadingCompleted
    public void onCustomCardReadingCompleted(CustomCardReadingResult customCardReadingResult) {
        logger.logInfo("onCustomCardReadingCompleted");
        resetCurrentOperationId();
    }

    @PreAuthorizationCompleted
    public void onPreAuthorizationCompleted(PreAuthorizationResult preAuthorizationResult) {
        logger.logInfo(">>> onPreAuthorizationCompleted");

        logger.logInfo("/*** preAuthorizationResult ***/");
        logger.logInfo("preAuthorizationResult.getPosId()  -->  " + preAuthorizationResult.getPosId());
        logger.logInfo("preAuthorizationResult.getTerminalId()  -->  " + preAuthorizationResult.getTerminalId());
        logger.logInfo("preAuthorizationResult.getTransactionId()  -->  " + preAuthorizationResult.getTransactionId());
        logger.logInfo("preAuthorizationResult.getTransactionResult()  -->  " + preAuthorizationResult.getTransactionResult().name());
        logger.logInfo("preAuthorizationResult.getTransactionDate()  -->  " + preAuthorizationResult.getTransactionDate());
        logger.logInfo("preAuthorizationResult.getTransactionTime()  -->  " + preAuthorizationResult.getTransactionTime());
        logger.logInfo("preAuthorizationResult.getStan()  -->  " + preAuthorizationResult.getStan());
        logger.logInfo("preAuthorizationResult.getIdOnline()  -->  " + preAuthorizationResult.getIdOnline());
        logger.logInfo("preAuthorizationResult.getPan()  -->  " + preAuthorizationResult.getPan());
        logger.logInfo("preAuthorizationResult.getBin()  -->  " + preAuthorizationResult.getBin());
        logger.logInfo("preAuthorizationResult.getPanBin()  -->  " + preAuthorizationResult.getPanBin());
        logger.logInfo("preAuthorizationResult.getPanSeqNumber()  -->  " + preAuthorizationResult.getPanSeqNumber());
        logger.logInfo("preAuthorizationResult.getEmvApplicationId()  -->  " + preAuthorizationResult.getEmvApplicationId());
        logger.logInfo("preAuthorizationResult.getActionCode()  -->  " + preAuthorizationResult.getActionCode());

        logger.logInfo("preAuthorizationResult.getAmount()  -->  " + preAuthorizationResult.getAmount());
        logger.logInfo("preAuthorizationResult.getCardType()  -->  " + preAuthorizationResult.getCardType());
        logger.logInfo("preAuthorizationResult.getTechnologyType()  -->  " + preAuthorizationResult.getTechnologyType());
        logger.logInfo("preAuthorizationResult.getAcquirerId()  -->  " + preAuthorizationResult.getAcquirerId());
        logger.logInfo("preAuthorizationResult.getAcquirerName()  -->  " + preAuthorizationResult.getAcquirerName());
        logger.logInfo("preAuthorizationResult.getMerchantId()  -->  " + preAuthorizationResult.getMerchantId());
        logger.logInfo("preAuthorizationResult.getCurrency()  -->  " + preAuthorizationResult.getCurrency());
        logger.logInfo("preAuthorizationResult.getApprovalCode()  -->  " + preAuthorizationResult.getApprovalCode());
        logger.logInfo("preAuthorizationResult.getNumReceiptCopies()  -->  " + preAuthorizationResult.getNumReceiptCopies());
        if (preAuthorizationResult.getAddData() != null) {
            logger.logInfo("preAuthorizationResult.getAddData().length  -->  " + preAuthorizationResult.getAddData().length());
            logger.logInfo("Utility.byteArrayToAsciiHex(preAuthorizationResult.getAddData())  -->  " + preAuthorizationResult.getAddData());
        } else {
            logger.logInfo("preAuthorizationResult.getAddData()  -->  null");
        }
        if (preAuthorizationResult.getAddDataField62() != null) {
            logger.logInfo("preAuthorizationResult.getAddDataField62().length  -->  " + preAuthorizationResult.getAddDataField62().length);
            logger.logInfo("Utility.byteArrayToAsciiHex(preAuthorizationResult.getAddDataField62())  -->  " + Utility.byteArrayToAsciiHex(preAuthorizationResult.getAddDataField62()));
        } else {
            logger.logInfo("preAuthorizationResult.getAddDataField62()  -->  null");
        }
        logger.logInfo("preAuthorizationResult.getPreauth_code()  -->  " + preAuthorizationResult.getPreauth_code());
        logger.logInfo("preAuthorizationResult.getServiceId()  -->  " + preAuthorizationResult.getServiceId());
        logger.logInfo("preAuthorizationResult.getDccInfo_TransactionOption()  -->  " + preAuthorizationResult.getDccInfo_TransactionOption());
        logger.logInfo("preAuthorizationResult.getDccInfo_IsoCode()  -->  " + preAuthorizationResult.getDccInfo_IsoCode());
        logger.logInfo("preAuthorizationResult.getDccInfo_ExchangeRate()  -->  " + preAuthorizationResult.getDccInfo_ExchangeRate());
        logger.logInfo("preAuthorizationResult.getDccInfo_AmountInCardCurr()  -->  " + preAuthorizationResult.getDccInfo_AmountInCardCurr());
        logger.logInfo("preAuthorizationResult.getDccInfo_CardCurrPrecision()  -->  " + preAuthorizationResult.getDccInfo_CardCurrPrecision());
        logger.logInfo("preAuthorizationResult.getDccInfo_Markup()  -->  " + preAuthorizationResult.getDccInfo_Markup());

        logger.logInfo("preAuthorizationResult.getOptype()  -->  " + preAuthorizationResult.getOptype());
        logger.logInfo("preAuthorizationResult.getPosMessage()  -->  " + preAuthorizationResult.getPosMessage());
        logger.logInfo("/***------------------------***/");

        showResultDialog("Preauth", preAuthorizationResult.getTransactionResult().name(), preAuthorizationResult.getReceipt(), ReceiptType.PAYMENT);

        resetCurrentOperationId();
    }

    @TotalsCompleted
    public void onTotalsCompleted(TotalsResult totalsResult) {
        logger.logInfo("onTotalsCompleted: " + totalsResult);

        String totalsType = null;
        switch (totalsResult.getOptype()) {
            case TotalsResult.LOCAL_TOTALS:
                totalsType = "Locali";
                break;
            case TotalsResult.DAILY_TOTALS:
                totalsType = "Locali Giorno";
                break;
            case TotalsResult.HOST_TOTALS:
                totalsType = "Host";
                break;
        }
        showResultDialog("Totali " + totalsType, totalsResult.getTransactionResult().name(), totalsResult.getReceipt(), ReceiptType.SERVICE);

        resetCurrentOperationId();
    }

    @RebootCompleted
    public void onRebootCompleted(RebootResult rebootResult) {
        logger.logInfo("onRebootCompleted");
    }

    @ReversalCompleted
    public void onReversalCompleted(ReversalResult reversalResult) {
        logger.logInfo("reversalResult.getApprovalCode()  ==  " + reversalResult.getApprovalCode());

        showReversalResult(reversalResult);

        if (updateRunning != 0) {
            runOnUiThread(() -> pauseUpdateBtn.setVisibility(View.VISIBLE));
        }
    }

    @ReversalInfoCompleted
    public void onPosReversalInfoReqCompleted(ReversalInfoResult reversalInfoResult) {
        ReversalInfoResult reversalInfoResult_Demo = connectionLayer.getReversalInfoResult();
        if (reversalInfoResult_Demo != null) {
            logger.logInfo("reversalInfoResult_Demo:" + "\n" +
                    // Result of the ReversalInfoReq operation: se 0x00 [RESULT_TRANSACTION_PERFORMED]
                    // significa anche che la LastTransaction (i cui dati completi sono reperibili
                    // dall'omonima API) è stornabile, altrimenti viene ritornato un qualche valore di errore
                    // (tipo RESULT_TRANSACTION_DENIED).
                    " Op Result  ==  " + reversalInfoResult_Demo.getTransactionResult().name() + "\n" +
                    " TID  ==  " + reversalInfoResult_Demo.getTerminalId() + "\n" +
                    " Trx Date  ==  " + reversalInfoResult_Demo.getTransactionDate() + "\n" +
                    " Trx Time  ==  " + reversalInfoResult_Demo.getTransactionTime() + "\n" +
                    " Trx Amn  ==  " + reversalInfoResult_Demo.getAmount() + "\n" +
                    " Appl Id  ==  " + reversalInfoResult_Demo.getEmvApplicationId() + "\n" +
                    " Stan  ==  " + reversalInfoResult_Demo.getStan()
            );
        } else {
            logger.logInfo("reversalInfoResult_Demo  ==  null");
        }

        Toast.makeText(this, "ReversalInfoReq Result:  Reversal Info Received!", Toast.LENGTH_LONG * 3).show();
    }


    @DataAcquisitionCompleted
    public void onDataAcquisitionCompleted(DataAcquisitionResponse dataAcquisitionResponse) {
        DataAcquisitionRequest dataAcquisitionRequest = null;

        if (DataAcquisitionActionValue.QR_CODE == dataAcquisitionResponse.getActionValue()) {
            logger.logDebug("data: " + dataAcquisitionResponse.getData());
            logger.logDebug("result: " + DataAcquisitionResultCode.getValue(dataAcquisitionResponse.getResult()));

            showResultDialog(
                    "Data Acquisition Completed",
                    "data: " + dataAcquisitionResponse.getData() + "\n" +
                            "result: " + DataAcquisitionResultCode.getValue(dataAcquisitionResponse.getResult())
            );

            /*
            Toast.makeText(this, "DATA ACQUISITION COMPLETED \n" +
                    "data: " + dataAcquisitionResponse.getData() + "\n" +
                    "result: " + DataAcquisitionResultCode.getValue(dataAcquisitionResponse.getResult()),
                    Toast.LENGTH_LONG).show();
                    */
        } else if (DataAcquisitionActionValue.PICTURE == dataAcquisitionResponse.getActionValue()) {
            logger.logDebug("data: " + dataAcquisitionResponse.getData());
            logger.logDebug("dataFormat: " + dataAcquisitionResponse.getDataFormat());
            logger.logDebug("result: " + DataAcquisitionResultCode.getValue(dataAcquisitionResponse.getResult()));

            showResultDialog(
                    "Data Acquisition Completed",
                    "data: " + dataAcquisitionResponse.getData() + "\n" +
                            "result: " + DataAcquisitionResultCode.getValue(dataAcquisitionResponse.getResult()) + "\n" +
                            "dataFormat: " + dataAcquisitionResponse.getDataFormat()
            );

            /*
            Toast.makeText(this, "DATA ACQUISITION COMPLETED \n" +
                            "data: " + dataAcquisitionResponse.getData() + "\n" +
                            "result: " + DataAcquisitionResultCode.getValue(dataAcquisitionResponse.getResult()) + "\n" +
                            "dataFormat: " +  dataAcquisitionResponse.getDataFormat(),
                    Toast.LENGTH_LONG).show();
                    */
        }

        if (dataAcquisitionRequest != null) {
            connectionLayer.dataAcquisition(dataAcquisitionRequest);
        }
        resetCurrentOperationId();
    }

    @CardInteractionCompleted
    public void onCardInteractionCompleted(CardInteractionResult cardInteractionResult) {
        logger.logDebug(">>>  onCardInteractionCompleted(CardInteractionResult cardInteractionResult)");

        CardInteractionRequest cardInteractionRequest = null;

        //LETTURA CARTA REGIONALE SERVIZI
        if (CardInteractionCommand.SLAVE_ACTIVATION.equals(cardInteractionResult.getCardInteractionCommand())) {
            logger.logDebug("(CardInteractionCommand.SLAVE_ACTIVATION.equals(cardInteractionResult.getCardInteractionCommand()))");
            logger.logDebug("cardInteractionResult.getCardInteractionCommand()  ==  " + cardInteractionResult.getCardInteractionCommand());
            logger.logDebug("Response: " + cardInteractionResult.getResponse());
            logger.logDebug("Response (Legacy): " + cardInteractionResult.getResponseLegacy());

            showResultDialog("Card Interaction Completed", "Response: " + cardInteractionResult.getResponse() + "legacy: " + cardInteractionResult.getResponseLegacy());
            //Toast.makeText(this, "CARD INTERACTION COMPLETED \n" + "Response: "+ cardInteractionResult.getResponse(), Toast.LENGTH_LONG).show();
        } else if (CardInteractionCommand.CARD_DETECT.equals(cardInteractionResult.getCardInteractionCommand())) {
            logger.logDebug("AtrLength: " + cardInteractionResult.getAtrLength());

            logger.logDebug("Track1: " + cardInteractionResult.getTrack1Lenght() + " Data: " + cardInteractionResult.getTrack1Data());
            logger.logDebug("Track2: " + cardInteractionResult.getTrack2Lenght() + " Data: " + cardInteractionResult.getTrack2Data());
            logger.logDebug("Track3: " + cardInteractionResult.getTrack3Lenght() + " Data: " + cardInteractionResult.getTrack3Data());

            logger.logDebug("IccCardTyp: " + cardInteractionResult.getIccCardType());
            logger.logDebug("ClessCardType: " + cardInteractionResult.getClessCardType());

//            if(cardInteractionResult.getIccCardType() == '1') {
//                cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SEND_APDU);
//                cardInteractionRequest.setCommandParameters(true,"00A40000023F00||00A40000021100||00A40000021102||00B000000000");
//            }
        } else if (CardInteractionCommand.SEND_APDU.equals(cardInteractionResult.getCardInteractionCommand())) {
            logger.logDebug("(CardInteractionCommand.SEND_APDU.equals(cardInteractionResult.getCardInteractionCommand()))");
            logger.logDebug("cardInteractionResult.getCardInteractionCommand()  ==  " + cardInteractionResult.getCardInteractionCommand());
//            cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SLAVE_DEACTIVATION);
            logger.logDebug("Response: " + cardInteractionResult.getResponse());
            logger.logDebug("Response (Legacy): " + cardInteractionResult.getResponseLegacy());
        }

            /*
            else if(CardInteractionCommand.SEND_CTS.equals(cardInteractionResult.getCardInteractionCommand())) {
                cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SLAVE_ACTIVATION);
            }
            else if(CardInteractionCommand.SAM_OPEN.equals(cardInteractionResult.getCardInteractionCommand())) {
                cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SLAVE_ACTIVATION);
            }
            else if(CardInteractionCommand.SAM_SEND_APDU.equals(cardInteractionResult.getCardInteractionCommand())) {
                cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SLAVE_ACTIVATION);
            }
            else if(CardInteractionCommand.SEND_MIFARE.equals(cardInteractionResult.getCardInteractionCommand())) {
                cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SLAVE_ACTIVATION);
            }
            else if(CardInteractionCommand.SEND_STM_SRI_SRT.equals(cardInteractionResult.getCardInteractionCommand())) {
                cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SLAVE_ACTIVATION);
            }
            else if(CardInteractionCommand.SLAVE_DEACTIVATION.equals(cardInteractionResult.getCardInteractionCommand())) {
                cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SLAVE_ACTIVATION);
            }
            else if(CardInteractionCommand.SLAVE_DEACTIVATION_HOLD_CARD.equals(cardInteractionResult.getCardInteractionCommand())) {
                cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.SLAVE_ACTIVATION);
            }
            else if(CardInteractionCommand.CARD_STATE.equals(cardInteractionResult.getCardInteractionCommand())) {
                cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.CARD_STATE);
                cardInteractionRequest.setDetectionParameters(0,0,0,0);
            }
            else if(CardInteractionCommand.CARD_EXTRACTION.equals(cardInteractionResult.getCardInteractionCommand())) {
                cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.CARD_EXTRACTION);
                cardInteractionRequest.setDetectionParameters(0,0,0,0);
            }
            else if(CardInteractionCommand.CANCEL_COMMAND.equals(cardInteractionResult.getCardInteractionCommand())) {
                cardInteractionRequest = new CardInteractionRequest(CardInteractionCommand.CANCEL_COMMAND);
                cardInteractionRequest.setDetectionParameters(0, 0, 0, 0);
            }

             */
// AU - 2020/11/16 - Removed Automatic command flow management on onCardInteractionCompleted - start
        //NOTE: It could cause some disalignment in the case of more cmd selected simultaneously by hand,
        // because here the command is sent before 0017 (End Session) of previous command.
//        if(cardInteractionRequest != null) {
//            logger.logDebug("(cardInteractionRequest != null)");
//            logger.logDebug("AtrLength: "+ cardInteractionResult.getAtrLength());
//            connectionLayer.cardInteraction(cardInteractionRequest);
//        }
// AU - 2020/11/16 - Removed Automatic command flow management on onCardInteractionCompleted - end
    }

    @CloseSessionCompleted
    public void onCloseSessionCompleted(CloseSessionResult closeSessionResult) {
        logger.logInfo("onCloseSessionCompleted: " + closeSessionResult);

        showResultDialog("Chiusura", closeSessionResult.getTransactionResult().name(), closeSessionResult.getReceipt(), ReceiptType.SERVICE);

        resetCurrentOperationId();
    }

    @PosSoftwareUpdateCompleted
    public void onPosSoftwareUpdateCompleted(PosSoftwareUpdateResult posSoftwareUpdateResult) {
        logger.logInfo("onPosSoftwareUpdateCompleted:  result --> " + posSoftwareUpdateResult.getResult());
        updateStatus.setVisibility(View.GONE);
        pauseUpdateBtn.setVisibility(View.GONE);
        updateRunning = 0;
        tvUpdate.setText("Update Result:  " + posSoftwareUpdateResult.getResult().name());

        Toast.makeText(this, "onPosSoftwareUpdateCompleted  -->  " + posSoftwareUpdateResult.getResult().name(), Toast.LENGTH_LONG * 3).show();

    }

    @PosSoftwareUpdateProgress
    public void onPosSoftwareUpdateProgress(PosSoftwareUpdateProgressValue posSoftwareUpdateProgressValue) {
        logger.logInfo("onPosSoftwareUpdateProgress:  posSoftwareUpdateProgressValue == " + posSoftwareUpdateProgressValue.getValue());
        //updateStatus.setVisibility(View.GONE);
        //pauseUpdateBtn.setVisibility(View.GONE);
        //updateRunning = false;
        //tvUpdate.setText("Update Progress:  " + posSoftwareUpdateProgressValue.getValue() + " %");

        Toast.makeText(this, "onPosSoftwareUpdateProgress:  Percentage ==  " + posSoftwareUpdateProgressValue.getValue(), Toast.LENGTH_LONG * 3).show();
    }


    @SendFileToTerminalCompleted
    public void onSendFileToTerminalCompleted(SendFileToTerminalResult sendFileToTerminalResult) {
        logger.logInfo("onSendFileToTerminalCompleted:  result --> " + sendFileToTerminalResult.name());

        Toast.makeText(this, "onSendFileToTerminalCompleted  -->  " + sendFileToTerminalResult.name(), Toast.LENGTH_LONG * 3).show();

    }

    @ImgShowOnPosCompleted
    public void onImgShowOnPosCompleted(ImgShowOnPosResult imgShowOnPosResult) {
        logger.logInfo("onImgShowOnPosCompleted: result=" + imgShowOnPosResult.getResult());
    }

    private void showDllResult(DllResult dllResult) {
        updateTransactionResult(dllResult);
    }

    private void updateTransactionResult(TransactionResult transactionResult) {
        String transactionResultText = "";

        transactionResultText += "operationType: " + transactionResult.getOperationType() + "\n";
        transactionResultText += "posId: " + transactionResult.getPosId() + "\n";
        transactionResultText += "terminalId: " + transactionResult.getTerminalId() + "\n";
        transactionResultText += "transactionId: " + transactionResult.getTransactionId() + "\n";
        transactionResultText += "stan: " + transactionResult.getStan() + "\n";
        transactionResultText += "transactionDate: " + transactionResult.getTransactionDate() + "\n";
        transactionResultText += "transactionTime: " + transactionResult.getTransactionTime() + "\n";

        if (transactionResult.getOperationType() == OperationType.OPERATION_TYPE_PAYMENT) {
            PaymentResult paymentTransactionResult = (PaymentResult) transactionResult;

            transactionResultText += "amount: " + paymentTransactionResult.getAmount() + "\n";
            transactionResultText += "pan: " + paymentTransactionResult.getPan() + "\n";
            transactionResultText += "cardType: " + paymentTransactionResult.getCardType() + "\n";
            transactionResultText += "technologyType: " + paymentTransactionResult.getTechnologyType() + "\n";
            transactionResultText += "acquirerId: " + paymentTransactionResult.getAcquirerId() + "\n";
            transactionResultText += "acquirerName: " + paymentTransactionResult.getAcquirerName() + "\n";
            transactionResultText += "approvalCode: " + paymentTransactionResult.getApprovalCode() + "\n";
            transactionResultText += "merchantId: " + paymentTransactionResult.getMerchantId() + "\n";
            transactionResultText += "actionCode: " + paymentTransactionResult.getActionCode() + "\n";
            transactionResultText += "posMessage: " + paymentTransactionResult.getPosMessage() + "\n";
            transactionResultText += "emvApplicationId: " + paymentTransactionResult.getEmvApplicationId() + "\n";
            transactionResultText += "Additional data: " + paymentTransactionResult.getAddData() + "";
        } else if (transactionResult.getOperationType() == OperationType.OPERATION_TYPE_REVERSAL) {
            ReversalResult reversalTransactionResult = (ReversalResult) transactionResult;

            transactionResultText += "amount: " + reversalTransactionResult.getAmount() + "\n";
            transactionResultText += "pan: " + reversalTransactionResult.getPan() + "\n";
            transactionResultText += "cardType: " + reversalTransactionResult.getCardType() + "\n";
            transactionResultText += "technologyType: " + reversalTransactionResult.getTechnologyType() + "\n";
            transactionResultText += "acquirerId: " + reversalTransactionResult.getAcquirerId() + "\n";
            transactionResultText += "acquirerName: " + reversalTransactionResult.getAcquirerName() + "\n";
            transactionResultText += "merchantId: " + reversalTransactionResult.getMerchantId() + "\n";
            transactionResultText += "actionCode: " + reversalTransactionResult.getActionCode() + "\n";
        } else if (transactionResult.getOperationType() == OperationType.OPERATION_TYPE_PREAUTH) {
            PreAuthorizationResult preAuthorizationResult = (PreAuthorizationResult) transactionResult;

            transactionResultText += "amount: " + preAuthorizationResult.getAmount() + "\n";
            transactionResultText += "pan: " + preAuthorizationResult.getPan() + "\n";
            transactionResultText += "cardType: " + preAuthorizationResult.getCardType() + "\n";
            transactionResultText += "technologyType: " + preAuthorizationResult.getTechnologyType() + "\n";
            transactionResultText += "acquirerId: " + preAuthorizationResult.getAcquirerId() + "\n";
            transactionResultText += "acquirerName: " + preAuthorizationResult.getAcquirerName() + "\n";
            transactionResultText += "approvalCode: " + preAuthorizationResult.getApprovalCode() + "\n";
            transactionResultText += "merchantId: " + preAuthorizationResult.getMerchantId() + "\n";
            transactionResultText += "actionCode: " + preAuthorizationResult.getActionCode() + "\n";
            transactionResultText += "posMessage: " + preAuthorizationResult.getPosMessage() + "\n";
            transactionResultText += "emvApplicationId: " + preAuthorizationResult.getEmvApplicationId() + "\n";
            transactionResultText += "Operation Type: " + preAuthorizationResult.getOptype() + "\n";
            if (preAuthorizationResult.getOptype() == 00) {
                transactionResultText += "PreAutorization Code: " + preAuthorizationResult.getPreauth_code() + "\n";
            } else if (preAuthorizationResult.getOptype() == 01) {
                transactionResultText += "Integrative PreAutorization Code: " + preAuthorizationResult.getPreauth_code() + "\n";
            } else {
                transactionResultText += "Closure PreAutorization Code: " + preAuthorizationResult.getPreauth_code() + "\n";
            }
            transactionResultText += "Add Data: " + preAuthorizationResult.getAddData() + "\n";
        }
        if (transactionResult.getOperationType() == OperationType.OPERATION_TYPE_REFUND) {
            RefundResult refundTransactionResult = (RefundResult) transactionResult;

            transactionResultText += "amount: " + refundTransactionResult.getAmount() + "\n";
            transactionResultText += "pan: " + refundTransactionResult.getPan() + "\n";
            transactionResultText += "cardType: " + refundTransactionResult.getCardType() + "\n";
            transactionResultText += "technologyType: " + refundTransactionResult.getTechnologyType() + "\n";
            transactionResultText += "acquirerId: " + refundTransactionResult.getAcquirerId() + "\n";
            transactionResultText += "acquirerName: " + refundTransactionResult.getAcquirerName() + "\n";
            transactionResultText += "approvalCode: " + refundTransactionResult.getApprovalCode() + "\n";
            transactionResultText += "merchantId: " + refundTransactionResult.getMerchantId() + "\n";
            transactionResultText += "actionCode: " + refundTransactionResult.getActionCode() + "\n";
            transactionResultText += "posMessage: " + refundTransactionResult.getPosMessage() + "\n";
            transactionResultText += "emvApplicationId: " + refundTransactionResult.getEmvApplicationId() + "\n";
        } else if (transactionResult.getOperationType() == OperationType.OPERATION_TYPE_TOTALS) {
            TotalsResult totalsResult = (TotalsResult) transactionResult;
            if (totalsResult.getOptype() == TotalsResult.LOCAL_TOTALS) {
                transactionResultText += "Local Totals \n";
            }
            if (totalsResult.getOptype() == TotalsResult.DAILY_TOTALS) {
                transactionResultText += "Daily Totals \n";
            }
            if (totalsResult.getOptype() == TotalsResult.HOST_TOTALS) {
                transactionResultText += "Host Totals \n";
            }
            transactionResultText += "Acquirer Number: " + String.valueOf(totalsResult.getAcquirerNumber()) + "\n";
            transactionResultText += "GlobalTotalSignAcquirers: " + totalsResult.getGlobalTotalSignAcquirers() + "\n";
            transactionResultText += "GlobalTotalAcquirers: " + totalsResult.getGlobalTotalAcquirers() + "\n";
            transactionResultText += "GlobalSignToNotify: " + totalsResult.getGlobalSignToNotify() + "\n";
            transactionResultText += "GlobalToNotify: " + totalsResult.getGlobalToNotify() + "\n";
            for (int i = 0; i < totalsResult.getAcquirerNumber(); i++) {
                transactionResultText += "Acquirer ID: " + totalsResult.getAcquirerIDInStruct(i) + "\n";
                transactionResultText += "Acquirer Name: " + totalsResult.getAcquirerNameInStruct(i) + "\n";
            }
        }
    }

    private void lastTransactionResult() {
        logger.logInfo("lastTransactionResult");

        TransactionResult lastTransactionResult = connectionLayer.getLastTransactionResult();

        logger.logInfo("lastTransactionResult.getPosId()  =  " + lastTransactionResult.getPosId());
        logger.logInfo("lastTransactionResult.getTerminalId()  =  " + lastTransactionResult.getTerminalId());
        logger.logInfo("lastTransactionResult.getTransactionId()  =  " + lastTransactionResult.getTransactionId());
        logger.logInfo("lastTransactionResult.getTransactionDate()  =  " + lastTransactionResult.getTransactionDate());
        logger.logInfo("lastTransactionResult.getTransactionTime()  =  " + lastTransactionResult.getTransactionTime());
        logger.logInfo("lastTransactionResult.getEmvApplicationId()  =  " + lastTransactionResult.getEmvApplicationId());
        logger.logInfo("lastTransactionResult.getBin()  =  " + lastTransactionResult.getBin());
        logger.logInfo("lastTransactionResult.getPan()  =  " + lastTransactionResult.getPan());
        logger.logInfo("lastTransactionResult.getPanBin()  =  " + lastTransactionResult.getPanBin());
        logger.logInfo("lastTransactionResult.getIdOnline()  =  " + lastTransactionResult.getIdOnline());
        logger.logInfo("lastTransactionResult.getStan()  =  " + lastTransactionResult.getStan());
        logger.logInfo("lastTransactionResult.getOperationType()  =  " + lastTransactionResult.getOperationType());
        logger.logInfo("lastTransactionResult.getActionCode()  =  " + lastTransactionResult.getActionCode());
        logger.logInfo("lastTransactionResult.getTransactionResult()  =  " + lastTransactionResult.getTransactionResult());

        showResultDialog("Ultima transazione", lastTransactionResult.getTransactionResult().name(), lastTransactionResult.getReceipt(), ReceiptType.PAYMENT);
    }

    private void recoveryTransaction() {
        if (currentOperationId != null) {
            logger.logInfo("trovata operazione in sospeso: idtrx=" + currentOperationId);

            PosStatusCode posStatusCode = connectionLayer.getPosStatus().getPosStatusCode();

            if (posStatusCode == PosStatusCode.POS_STATUS_OPERATIVE) {
                TransactionResult lastTransactionResult = connectionLayer.getLastTransactionResult();

                if (currentOperationId.equals(lastTransactionResult.getTransactionId())) {
                    // l'operazione è stata completata, procede
                    // con la lettura dell'esito e dei dati
                    if (lastTransactionResult.getOperationType() == OperationType.OPERATION_TYPE_PAYMENT) {
                        logger.logInfo("operazione di pagamento idtrx=" + currentOperationId + " recuperata");

                        PaymentResult paymentResult = (PaymentResult) lastTransactionResult;

                        showPaymentResult(paymentResult);

                        resetCurrentOperationId();
                    }
                } else {
                    // l'operazione
                    Toast.makeText(getApplicationContext(), "operazione idtrx=" + currentOperationId + " non eseguita sul POS", Toast.LENGTH_LONG).show();
                    resetCurrentOperationId();
                }
            } else if (posStatusCode == PosStatusCode.POS_STATUS_OPERATION_IN_PROGRESS) {
                // l'operazione è ancora in corso; al completamento
                // della medesima l'app riceverà la notifica tramite il
                // metodo di callback

                logger.logInfo("operazione di pagamento idtrx=" + currentOperationId + " non completa: sarà inviata notifica al termine");
            }
        } else
            logger.logInfo("nessuna transazione in sospeso rilevata in fase di avvio");
    }

    private void showPaymentResult(PaymentResult paymentResult) {
        String msg = "";
        if (paymentResult != null) {
            msg = "pagamento completato: esito " + paymentResult.getTransactionResult() +
                    ", Transactio ID " + paymentResult.getTransactionId() +
                    ", Approval Code " + paymentResult.getApprovalCode() +
                    ", A.C. " + paymentResult.getActionCode();
            showResultDialog("Pagamento completato", msg, paymentResult.getReceipt(), ReceiptType.PAYMENT);
        } else {
            msg = "(paymentResult == null)";
            showResultDialog("Op. Pagamento completato:  ", msg);
        }

    }

    private void showRefundResult(RefundResult refundResult) {
        String msg = "";
        if (refundResult != null) {
            msg = "refund completato: esito " + refundResult.getTransactionResult() +
                    ", Transaction ID " + refundResult.getTransactionId() +
                    ", Approval Code " + refundResult.getApprovalCode() +
                    ", A.C. " + refundResult.getActionCode();
            showResultDialog("refund completato", msg, refundResult.getReceipt(), ReceiptType.PAYMENT);
        } else {
            msg = "(refundResult == null)";
            showResultDialog("Op. refund completato:  ", msg);
        }
    }

    private void showReversalResult(ReversalResult reversalResult) {
        String msg = "";
        if (reversalResult != null) {
            logger.logInfo("reversalResult.getEmvApplicationId()  ==  " + reversalResult.getEmvApplicationId());
            logger.logInfo("reversalResult.getPan()  ==  " + reversalResult.getPan());
            logger.logInfo("reversalResult.getPanBin()  ==  " + reversalResult.getPanBin());
            logger.logInfo("reversalResult.getBin()  ==  " + reversalResult.getBin());

            msg = "Storno completato: esito " + reversalResult.getTransactionResult() +
                    ", Transactio ID " + reversalResult.getTransactionId() + /*" Echo Data " + output.toString() +*/
//                        ", Approval Code " + reversalResult.getApprovalCode() +
                    ", A.C. " + reversalResult.getActionCode();
            showResultDialog("Storno completato", msg, reversalResult.getReceipt(), ReceiptType.PAYMENT);
        } else {
            showResultDialog("Op. Storno completato:  ", msg);
        }

    }

    protected boolean notConnected() {
        if (connectionLayer.getPosStatus().getPosStatusCode() == PosStatusCode.POS_STATUS_DISCONNECTED) {
            Toast.makeText(getApplicationContext(), "POS disconnected", Toast.LENGTH_LONG).show();
            return true;
        } else if (connectionLayer.getPosStatus().getPosStatusCode() == PosStatusCode.POS_STATUS_UNAUTHORIZED) {
            Toast.makeText(getApplicationContext(), "POS Unauthorized", Toast.LENGTH_LONG).show();
            return true;
        } else {
            //TODO:
        }

        return false;
    }

    private boolean cannotPerform() {
        if (connectionLayer.getPosStatus().getPosStatusCode() != PosStatusCode.POS_STATUS_OPERATIVE && (updateRunning == 0)) {
            Toast.makeText(getApplicationContext(), "POS not operative", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private void mPayDevices() {
        logger.logInfo("mPayDevices");

        StringBuilder listDevicesAddress = new StringBuilder();

        // check BLUETOOTH_CONNECT permission
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            List<BluetoothDevice> bluetoothDeviceList = connectionLayer.getMpayBtBoundedDevices();

            // show result on display
            if (bluetoothDeviceList != null) {
                logger.logInfo("bluetoothDeviceList.size()  ==  " + bluetoothDeviceList.size());
                for (int i = 0; i < bluetoothDeviceList.size(); i++) {
                    logger.logInfo("bluetoothDeviceList.get(" + i + ").getName()  ==  " + bluetoothDeviceList.get(i).getName());
                    logger.logInfo("bluetoothDeviceList.get(" + i + ").getAddress()  ==  " + bluetoothDeviceList.get(i).getAddress());

                    listDevicesAddress
                            .append((i+1) + ". ")
                            .append("Name: ")
                            .append(bluetoothDeviceList.get(i).getName())
                            .append(" - ")
                            .append("Address: ")
                            .append(bluetoothDeviceList.get(i).getAddress())
                            .append(";\n");
                }

                if (bluetoothDeviceList.size() > 0) {
                    showResultDialog("Lista devices recuperati:", listDevicesAddress.toString());
                } else {
                    showResultDialog("Lista devices recuperati:", "Nessun device rilevato [bluetoothDeviceList.size() == 0]");
                }
            } else {
                showResultDialog("Lista devices recuperati:", "ERROR: bluetoothDeviceList == null");
            }
        }
    }

    //POS Update
    private class DownloadTask extends AsyncTask<String, Integer, Integer> {
        @SuppressLint("WrongThread")
        @Override
        protected Integer doInBackground(String... sUrl) {
            logger.logInfo("DownloadTask: doInBackground");
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    logger.logError("DownloadTask: server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage());
                    return -1;
                }
                // download the file
                input = connection.getInputStream();
                file = getDownloadFile();
                logger.logInfo("DownloadTask: writing file: " + file.getAbsolutePath());
                output = new FileOutputStream(file);
                byte[] data = new byte[4096];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                logger.logError("DownloadTask: " + e.getMessage());
                //tvUpdate.setText("Download Error");
                return -1;
            } finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return 0;
        }

        @PosConnectionError
        public void onPosConnectionError(ConnectionError connectionError) {
            logger.logInfo(">>>  onPosConnectionError [internal to DownloadTask]");
            logger.logInfo("onPosConnectionError: " + connectionError.getConnectionErrorCode());

            final ConnectionErrorCode connectionErrorCode = connectionError.getConnectionErrorCode();

            switch (connectionErrorCode) {
                case CONNECTION_ERROR_INTERFACE_NOT_ENABLED:
                    updateConnectionErrorText("(Bluetooth non abilitato)");
                    break;

                case CONNECTION_ERROR_DEVICE_NOT_PAIRED:
                    updateConnectionErrorText("(pairing non eseguito)");
                    break;

                case CONNECTION_ERROR_SERVICE_NOT_FOUND:
                    updateConnectionErrorText("(servizio non attivo sul dispositivo)");
                    break;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            tvUpdate.setText("Update in progress ....");
            updateStatus.setVisibility(View.GONE);
            updateRunning = 0x80000000;
            logger.logInfo("onPostExecute result=" + result);
            String downloadedFilePathname = getDownloadFile().getAbsolutePath();
            logger.logInfo("DownloadTask: readin file: " + downloadedFilePathname);
            //updateStatus.setVisibility(View.VISIBLE);
            pauseUpdateBtn.setVisibility(View.VISIBLE);
            if (!connectionLayer.isAsyncPosSoftwareUpdateSupported()) {
                pauseUpdateBtn.setEnabled(false);
            }
            connectionLayer.posSoftwareUpdate(downloadedFilePathname);
        }

        private File getDownloadFile() {
            return new File(getFilesDir(), "/" + "d200.upd");
        }

    }

    //region For_Update_Firmware

    private void getFileFromAssets() {
        logger.logInfo("getFileFromAssets()");
        File file = null;
        try (InputStream input = this.getAssets().open(nameFileToUpdate)) {
            copyFileFromAssetToDirectory(this, nameFileToUpdate, this.getCacheDir().getAbsolutePath());
            file = new File(this.getCacheDir() + "/" + nameFileToUpdate);
            long fileSize = file.length();
            logger.logInfo("file.length()  -->  " + fileSize);
            try (OutputStream output = new FileOutputStream(file)) {
                //byte[] buffer = new byte[4 * 1024];
                byte[] buffer = new byte[9 * 1024];
                int read;
                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            }
        } catch (IOException e) {
            logger.logError("getFileFromAssets():  IOException e");
            e.printStackTrace();
        }
        nameFile = file.getAbsolutePath();
        logger.logInfo("file.getAbsolutePath()  -->  " + nameFile);
    }

    private File getFileFromAssets2() {
        File file = null;
        try (InputStream input = this.getAssets().open(nameFileToSend)) {
            copyFileFromAssetToDirectory(this, nameFileToSend, this.getCacheDir().getAbsolutePath());
            file = new File(this.getCacheDir() + "/" + nameFileToSend);
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void copyFileFromAssetToDirectory(Context context, String fileName, String directory) {
        try {
            InputStream in = context.getAssets().open(fileName);
            File file = new File(directory + fileName);
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch(Exception ex) {
            Log.e("ErrorLocalUpdate", String.format("ECCEZIONE GENERATA: %s", ex.getLocalizedMessage()));
        }
    }

    //endregion

    /**
     * Visualizza una dialog con il titolo e esito del test
     * @param title titolo del test effettuato
     * @param message esito del test effettuato
     */
    private void showResultDialog(String title, String message) {
        showResultDialog(title, message, null, null);
    }

    private void showResultDialog(String title, String message, @Nullable Receipt receipt, @Nullable ReceiptType receiptType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Chiudi", (dialog, id) -> {
                    dialog.cancel();
                });

        if (receipt != null && receiptType != null) {
            builder.setNeutralButton("Stampa", (dialog, id) -> {
                ReceiptToPrint receiptToPrint = new ReceiptToPrint(receiptType);
                merchantReceiptToPrint(receipt, receiptToPrint);
                printReceipt(receiptToPrint);
            });
        }

        AlertDialog alert = builder.create();
        alert.setTitle(title);
        if (!isFinishing()) {
            alert.show();
        }
    }


    private void logReceipt(Receipt receipt) {
        String receiptText = "";

        for (int i = 1; i <= receipt.getNumReceiptRows(); i++) {
            ReceiptRow receiptRow = receipt.getReceiptRowByRowNumber(i);
            receiptText += "[";
            if ((receiptRow.getAttribute() & ReceiptRow.ROW_ATTRIBUTE_DOUBLE_HEIGHT) != 0)
                receiptText += "D";
            if ((receiptRow.getAttribute() & ReceiptRow.ROW_ATTRIBUTE_COMPRESSED) != 0)
                receiptText += "C";
            if ((receiptRow.getAttribute() & ReceiptRow.ROW_ATTRIBUTE_SIGNATURE) != 0)
                receiptText += "S";
            receiptText += "]";
            receiptText += " <";
            receiptText += receiptRow.getText();
            receiptText += ">";
            receiptText += "\n";
        }

        logger.logDebug("receipt:\n" + receiptText);
    }

    private void printReceipt(ReceiptToPrint receiptToPrint) {
        if (receiptToPrint != null) {
            ErrorCodes res = receiptToPrint.print(0);
            if (res.equals(ErrorCodes.OUT_OF_PAPER)) {
                showResultDialog("PRINTER ERROR", "Paper is missing. Please check.");
            } else if (res.equals(ErrorCodes.ERR_PRINTER)) {
                showResultDialog("PRINTER ERROR", "Print Error. Please check.");
            } else {
                logger.logInfo("ErrorCodes res  ==  " + res);
            }
        } else {
            logger.logInfo("receiptToPrint == null");
        }
    }

    private void merchantReceiptToPrint(Receipt receipt, ReceiptToPrint toPrint) {
        for (int rowNumber = 1; rowNumber <= receipt.getNumReceiptRows(); rowNumber++) {
            ReceiptRow receiptRow = receipt.getReceiptRowByRowNumber(rowNumber);

            if ((receiptRow.getAttribute() & ReceiptRow.ROW_ATTRIBUTE_MERCHANT) != 0) {

                if (TextUtils.isEmpty(receiptRow.getText())) {
                    continue;
                }

                if ((receiptRow.getAttribute() & ReceiptRow.ROW_ATTRIBUTE_DOUBLE_HEIGHT) != 0) {
                    toPrint.addTitle(receiptRow.getText());
                    //receiptText += "D";
                } else if ((receiptRow.getAttribute() & ReceiptRow.ROW_ATTRIBUTE_COMPRESSED) != 0) {
                    toPrint.addCompressedLine(receiptRow.getText());
                    //receiptText += "C";
                } else if ((receiptRow.getAttribute() & ReceiptRow.ROW_ATTRIBUTE_SIGNATURE) != 0) {
                    toPrint.addLine(receiptRow.getText());
                    //receiptText += "S";
                } else {
                    toPrint.addLine(receiptRow.getText());
                }
            }
        }
    }

}