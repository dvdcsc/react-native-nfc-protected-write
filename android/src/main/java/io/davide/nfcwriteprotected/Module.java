package io.davide.nfcwriteprotected;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.util.Log;
import android.widget.Toast;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.HashMap;
import java.util.Map;;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.facebook.react.bridge.*;
import android.nfc.Tag;
import java.util.*;
import com.nxp.nfclib.ntag.INTag213215216;
import com.nxp.nfclib.ntag.NTagFactory;
import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.utils.Utilities;

public class Module extends ReactContextBaseJavaModule  implements ActivityEventListener, LifecycleEventListener {

  /*private final List<IntentFilter> intentFilters = new ArrayList<IntentFilter>();
  private final ArrayList<String[]> techLists = new ArrayList<String[]>();
  private Context context;
  private ReactApplicationContext reactContext;
  private Boolean isForegroundEnabled = false;
  private Boolean isResumed = false;*/

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";
  private NxpNfcLib nxpLib = null;
  private String nxpLibKey = "0235a960fcb9a265be2ffe54da8288bd";
  final byte[] byPassword = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
  final byte[] byAcknowg = new byte[] {0x00,0x00};
  final byte[] byToWrite = new byte[] {0x00,0x00,0x00,0x00};

  public Module(ReactApplicationContext reactContext) {


    super(reactContext);
    reactContext.addActivityEventListener(this);
  }

  @Override
  public void onNewIntent(Intent intent) {
    try {

      Toast.makeText(getReactApplicationContext(), "onNewIntent: ", Toast.LENGTH_LONG).show();

      connect(intent);
    } catch (Exception e) {

        Toast.makeText(getReactApplicationContext(), "NFC connect error: "+e.getMessage(), Toast.LENGTH_LONG).show();

      }
  }
  @Override
  public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

  }

  @Override
  public String getName() {
    return "NfcProtectedWrite";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
    constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
    return constants;
  }

  @Override
  public void onHostResume() {
    Toast.makeText(getReactApplicationContext(), "startForeGroundDispatch: ", Toast.LENGTH_LONG).show();

    nxpLib.startForeGroundDispatch();
  }

  @Override
  public void onHostPause() {
    nxpLib.stopForeGroundDispatch();
  }

  @Override
  public void onHostDestroy() {
    nxpLib.stopForeGroundDispatch();
  }


  @ReactMethod
  public void show(String message) {

    Toast.makeText(getReactApplicationContext(), message, Toast.LENGTH_SHORT).show();
  }

  private void initializeLibrary()
  {
    // Initialize the TapLinx library

    nxpLib = NxpNfcLib.getInstance();
    nxpLib.registerActivity(getCurrentActivity(), nxpLibKey);
    nxpLib.startForeGroundDispatch();
    Toast.makeText(getReactApplicationContext(), "initializeLibrary", Toast.LENGTH_LONG).show();


  }

  @ReactMethod
  public void start() {
    initializeLibrary();
  }

  @ReactMethod
  public void connect(Intent intent) {

    try{

      CardType m_cardType = nxpLib.getCardType( intent );
      Toast.makeText(getReactApplicationContext(), "m_cardType: "+m_cardType.getTagName(), Toast.LENGTH_LONG).show();

      Tag tag =  getCurrentActivity().getIntent().getParcelableExtra( NfcAdapter.EXTRA_TAG );
      Toast.makeText(getReactApplicationContext(), "tag: "+tag, Toast.LENGTH_LONG).show();


      INTag213215216 objNtag = NTagFactory.getInstance().getNTAG216(nxpLib.getCustomModules());
      Toast.makeText(getReactApplicationContext(), "objNtag: "+objNtag, Toast.LENGTH_LONG).show();

      objNtag.getReader().connect();
      Toast.makeText(getReactApplicationContext(), "__CONNECTED__", Toast.LENGTH_LONG).show();

      objNtag.enablePasswordProtection(true,0x10);
      Toast.makeText(getReactApplicationContext(), "AUTHENTICATE_1", Toast.LENGTH_LONG).show();

      objNtag.authenticatePwd(byPassword,byAcknowg);
      Toast.makeText(getReactApplicationContext(), "AUTHENTICATE_2", Toast.LENGTH_LONG).show();


      byte[] data = objNtag.read(0x0f);
      Toast.makeText(getReactApplicationContext(), "read page 0x0f: " + Utilities.byteToHexString(data) , Toast.LENGTH_LONG).show();
      objNtag.write(0x0f, byToWrite);
      data = objNtag.read(0x10);
      Toast.makeText(getReactApplicationContext(), "read page 0x10: " + Utilities.byteToHexString(data) , Toast.LENGTH_LONG).show();
      objNtag.write(0x10, byToWrite);



    } catch (Exception e) {

      Toast.makeText(getReactApplicationContext(), "NFC connect error: "+e.getMessage(), Toast.LENGTH_LONG).show();

    }

  }
}