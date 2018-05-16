package jaykay12.nithoppnet.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jaykay12.nithoppnet.database.DatabaseOperations;
import jaykay12.nithoppnet.fragments.PeerListFragment;
import jaykay12.nithoppnet.R;
import jaykay12.nithoppnet.database.DBAdapter;
import jaykay12.nithoppnet.model.ChatDTO;
import jaykay12.nithoppnet.model.DeviceDTO;
import jaykay12.nithoppnet.model.MessageDTO;
import jaykay12.nithoppnet.transfer.DataHandler;
import jaykay12.nithoppnet.transfer.DataSender;
import jaykay12.nithoppnet.transfer.TransferConstants;
import jaykay12.nithoppnet.utils.AppController;
import jaykay12.nithoppnet.utils.ConnectionUtils;
import jaykay12.nithoppnet.utils.DialogUtils;
import jaykay12.nithoppnet.utils.SharedPref;
import jaykay12.nithoppnet.utils.Utility;
import jaykay12.nithoppnet.utils.WifiDirectBroadcastReceiver;

public class FindPeerActivity extends AppCompatActivity implements PeerListFragment.OnListFragmentInteractionListener
        , WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener {

    public static final String ACTION_CHAT_RECEIVED = "jaykay12.nithoppnet.chatreceived";
    public static final String KEY_CHAT_DATA = "chat_data_key";

    public static final String FIRST_DEVICE_CONNECTED = "first_device_connected";
    public static final String KEY_FIRST_DEVICE_IP = "first_device_ip";

    private static final String WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int WRITE_PERM_REQ_CODE = 19;

    PeerListFragment deviceListFragment;
    View progressBarLocalDash;

    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel wifip2pChannel;
    DatabaseOperations dbobj;
    SharedPref sharedPref;
    WifiDirectBroadcastReceiver wiFiDirectBroadcastReceiver;
    private boolean isWifiP2pEnabled = false;

    private boolean isWDConnected = false;

    private AppController appController;
//    private ConnectionListener connListener;

    /**
     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
     */
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_peer);

        initialize();
    }

    private void initialize() {

        progressBarLocalDash = findViewById(R.id.pbFindingPeer);

        String myIP = Utility.getWiFiIPAddress(FindPeerActivity.this);
        Utility.saveString(FindPeerActivity.this, TransferConstants.KEY_MY_IP, myIP);

//        Starting connection listener with default for now
//        connListener = new ConnectionListener(LocalDashWiFiDirect.this, TransferConstants.INITIAL_DEFAULT_PORT);
//        connListener.start();

        setToolBarTitle(0);

        wifiP2pManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        wifip2pChannel = wifiP2pManager.initialize(this, getMainLooper(), null);

        // Starting connection listener with default port for now
        appController = (AppController) getApplicationContext();
        appController.startConnectionListener(TransferConstants.INITIAL_DEFAULT_PORT);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CHAT_RECEIVED);
        LocalBroadcastManager.getInstance(this).registerReceiver(chatReceiver, filter);

        checkWritePermission();
    }

    public void FindPeers(View v) {

        if (!isWDConnected) {
            Snackbar.make(v, "Finding peers", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            try {
                wifiP2pManager.discoverPeers(wifip2pChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(FindPeerActivity.this, "Peer discovery started", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(FindPeerActivity.this, "Peer discovery failure: " + reasonCode, Toast.LENGTH_LONG).show();
                    }
                });
            }
            catch (NullPointerException exp){
                System.out.println("Error " + exp.getMessage());
            }

        }
    }

    @Override
    protected void onPause() {
//        if (mNsdHelper != null) {
//            mNsdHelper.stopDiscovery();
//        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localDashReceiver);
        unregisterReceiver(wiFiDirectBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter localFilter = new IntentFilter();
        localFilter.addAction(DataHandler.DEVICE_LIST_CHANGED);
        localFilter.addAction(FIRST_DEVICE_CONNECTED);
        localFilter.addAction(DataHandler.CHAT_REQUEST_RECEIVED);
        localFilter.addAction(DataHandler.CHAT_RESPONSE_RECEIVED);
        LocalBroadcastManager.getInstance(FindPeerActivity.this).registerReceiver(localDashReceiver,
                localFilter);

        IntentFilter wifip2pFilter = new IntentFilter();
        wifip2pFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifip2pFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifip2pFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifip2pFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wiFiDirectBroadcastReceiver = new WifiDirectBroadcastReceiver(wifiP2pManager, wifip2pChannel, this);
        registerReceiver(wiFiDirectBroadcastReceiver, wifip2pFilter);

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(DataHandler.DEVICE_LIST_CHANGED));
    }

    @Override
    protected void onDestroy() {
//        mNsdHelper.tearDown();
//        connListener.tearDown();
        appController.stopConnectionListener();
        Utility.clearPreferences(FindPeerActivity.this);
        Utility.deletePersistentGroups(wifiP2pManager, wifip2pChannel);
        DBAdapter.getInstance(FindPeerActivity.this).clearDatabase();
        wifiP2pManager.removeGroup(wifip2pChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {

            }
        });

        super.onDestroy();
    }

    private BroadcastReceiver localDashReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case FIRST_DEVICE_CONNECTED:
                    appController.restartConnectionListenerWith(ConnectionUtils.getPort(FindPeerActivity.this));

                    String senderIP = intent.getStringExtra(KEY_FIRST_DEVICE_IP);
                    int port = DBAdapter.getInstance(FindPeerActivity.this).getDevice
                            (senderIP).getPort();
                    DataSender.sendCurrentDeviceData(FindPeerActivity.this, senderIP, port, true);
                    isWDConnected = true;
                    break;
                case DataHandler.DEVICE_LIST_CHANGED:
                    ArrayList<DeviceDTO> devices = DBAdapter.getInstance(FindPeerActivity.this)
                            .getDeviceList();
                    int peerCount = (devices == null) ? 0 : devices.size();
                    if (peerCount > 0) {
                        progressBarLocalDash.setVisibility(View.GONE);
                        deviceListFragment = new PeerListFragment();
                        Bundle args = new Bundle();
                        args.putSerializable(PeerListFragment.ARG_DEVICE_LIST, devices);
                        deviceListFragment.setArguments(args);

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.deviceListHolder, deviceListFragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
                        ft.commit();
                    }
                    setToolBarTitle(peerCount);
                    break;
                case DataHandler.CHAT_REQUEST_RECEIVED:
                    DeviceDTO chatRequesterDevice = (DeviceDTO) intent.getSerializableExtra(DataHandler.KEY_CHAT_REQUEST);
                    DialogUtils.getChatRequestDialog(FindPeerActivity.this, chatRequesterDevice).show();
                    break;
                case DataHandler.CHAT_RESPONSE_RECEIVED:
                    boolean isChatRequestAccepted = intent.getBooleanExtra(DataHandler.KEY_IS_CHAT_REQUEST_ACCEPTED, false);
                    if (!isChatRequestAccepted) {
                        Toast.makeText(FindPeerActivity.this, "Chat request " + "rejected",Toast.LENGTH_LONG).show();
                    } else {
                        DeviceDTO chatDevice = (DeviceDTO) intent.getSerializableExtra(DataHandler.KEY_CHAT_REQUEST);
                        DialogUtils.openChatActivity(FindPeerActivity.this, chatDevice);
                        Toast.makeText(FindPeerActivity.this, chatDevice.getPlayerName() + "Accepted Chat request",Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private DeviceDTO selectedDevice;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case DialogUtils.CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    DataSender.sendFile(FindPeerActivity.this, selectedDevice.getIp(),
                            selectedDevice.getPort(), imageUri);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            finish();
        }
    }

    private void checkWritePermission() {
        boolean isGranted = Utility.checkPermission(WRITE_PERMISSION, this);
        if (!isGranted) {
            Utility.requestPermission(WRITE_PERMISSION, WRITE_PERM_REQ_CODE, this);
        }
    }

    boolean isConnectionInfoSent = false;

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {

        if (wifiP2pInfo.groupFormed && !wifiP2pInfo.isGroupOwner && !isConnectionInfoSent) {

            isWDConnected = true;

            appController.restartConnectionListenerWith(ConnectionUtils.getPort(FindPeerActivity.this));

            String groupOwnerAddress = wifiP2pInfo.groupOwnerAddress.getHostAddress();
            DataSender.sendCurrentDeviceDataWD(FindPeerActivity.this, groupOwnerAddress, TransferConstants.INITIAL_DEFAULT_PORT, true);
            isConnectionInfoSent = true;
        }
    }

    private BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("### Recevied something","yy");
            switch (intent.getAction()) {
                case ACTION_CHAT_RECEIVED:
                    ChatDTO chat = (ChatDTO) intent.getSerializableExtra(KEY_CHAT_DATA);
                    chat.setMyChat(false);
                    Log.v("### Recevied",chat.getMessages()+"");

                    List<MessageDTO> messageRecieved = chat.getMessages();
                    String messageid="", senderid="", sendername="", messagecontents="";
                    dbobj = new DatabaseOperations(getApplicationContext());
                    sharedPref = new SharedPref(getApplicationContext());
                    long result=0;
                    int i;
                    int countMSG = 0;
                    try {
                        for (i = 0; i < messageRecieved.size(); i++) {
                            MessageDTO messageDTO = messageRecieved.get(i);
                            messageid = messageDTO.getMessage_id();
                            senderid = messageDTO.getSender_id();
                            sendername = messageDTO.getSender_name();
                            messagecontents = messageDTO.getContent();
                            try {
                                result = dbobj.Add_Message(messageid, senderid, sendername, messagecontents);
                                countMSG++;
                            } catch (SQLiteConstraintException exp) {
                                Log.v("##", exp + "");
                            }

                        }
                    }
                    catch(NullPointerException exp){
                        Log.v("##","Lag gye!");
                    }

                    if(countMSG>0)
                        Toast.makeText(getApplicationContext(),"Total "+countMSG+" Messages Recieved from "+sendername+"!",Toast.LENGTH_SHORT).show();


                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {

        ArrayList<DeviceDTO> deviceDTOs = new ArrayList<>();

        List<WifiP2pDevice> devices = (new ArrayList<>());
        devices.addAll(peerList.getDeviceList());
        for (WifiP2pDevice device : devices) {
            DeviceDTO deviceDTO = new DeviceDTO();
            deviceDTO.setIp(device.deviceAddress);
            deviceDTO.setPlayerName(device.deviceName);
            deviceDTO.setDeviceName(new String());
            deviceDTO.setOsVersion(new String());
            deviceDTO.setPort(-1);
            deviceDTOs.add(deviceDTO);
        }


        progressBarLocalDash.setVisibility(View.GONE);
        deviceListFragment = new PeerListFragment();
        Bundle args = new Bundle();
        args.putSerializable(PeerListFragment.ARG_DEVICE_LIST, deviceDTOs);
        deviceListFragment.setArguments(args);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.deviceListHolder, deviceListFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        ft.commit();
    }

    @Override
    public void onListFragmentInteraction(DeviceDTO deviceDTO) {
        if (!isWDConnected) {
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = deviceDTO.getIp();
            config.wps.setup = WpsInfo.PBC;
            config.groupOwnerIntent = 4;
            wifiP2pManager.connect(wifip2pChannel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    // Connection request succeeded. No code needed here
                }

                @Override
                public void onFailure(int reasonCode) {
                    Toast.makeText(FindPeerActivity.this, "Connection failed. try" + " again: reason: " + reasonCode,Toast.LENGTH_LONG);
                }
            });
        } else {
            selectedDevice = deviceDTO;
//            showServiceSelectionDialog();

            DialogUtils.getServiceSelectionDialog(FindPeerActivity.this, deviceDTO).show();
        }
    }

    private void setToolBarTitle(int peerCount) {
        if (getSupportActionBar() != null) {
            String title = String.format(getString(R.string.wd_title_with_count), String.valueOf(peerCount));
            getSupportActionBar().setTitle(title);

        }
    }

}
