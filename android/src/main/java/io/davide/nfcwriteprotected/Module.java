package io.davide.nfcwriteprotected;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.HashMap;
import java.util.Map;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.provider.Settings;
import com.facebook.react.bridge.*;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;

import android.app.PendingIntent;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.TagTechnology;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.*;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

import com.nxp.nfclib.desfire.IDESFireEV1;
import com.nxp.nfclib.icode.ICodeFactory;
import com.nxp.nfclib.icode.IICodeSLIXS;
import com.nxp.nfclib.ntag.INTag213215216;
import com.nxp.nfclib.ntag.NTagFactory;
import com.nxp.nfclib.utils.Utilities;
import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.interfaces.IReader;
import android.nfc.Tag;
import com.nxp.nfclib.CardType;

import android.app.Activity;
import android.content.Intent;

public class Module extends ReactContextBaseJavaModule  implements ActivityEventListener, LifecycleEventListener {

  private static final String LOG_TAG = "NfcProtectedWrite";
  private final List<IntentFilter> intentFilters = new ArrayList<IntentFilter>();
  private final ArrayList<String[]> techLists = new ArrayList<String[]>();
  private Context context;
  private ReactApplicationContext reactContext;
  private Boolean isForegroundEnabled = false;
  private Boolean isResumed = false;

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";
  private NxpNfcLib nxpLib = null;
  private String nxpLibKey = "0235a960fcb9a265be2ffe54da8288bd";


  public Module(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public void onNewIntent(Intent intent) {
    Log.d(LOG_TAG, "onNewIntent " + intent);
    connect();
  }
  @Override
  public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    Log.d(LOG_TAG, "onActivityResult");
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
    Log.d(LOG_TAG, "onResume");
    initializeLibrary();                  // Initialize library
    nxpLib.startForeGroundDispatch();
  }

  @Override
  public void onHostPause() {
    Log.d(LOG_TAG, "onPause");
    isResumed = false;
    nxpLib.stopForeGroundDispatch();
  }

  @Override
  public void onHostDestroy() {
    Log.d(LOG_TAG, "onDestroy");
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

  }

  @ReactMethod
  public void start() {
    initializeLibrary();
  }

  @ReactMethod
  public void connect() {

    try{

      CardType m_cardType = nxpLib.getCardType( getCurrentActivity().getIntent() );
      Toast.makeText(getReactApplicationContext(), "m_cardType: "+m_cardType.getTagName(), Toast.LENGTH_LONG).show();

      Tag tag =  getCurrentActivity().getIntent().getParcelableExtra( NfcAdapter.EXTRA_TAG );
      Toast.makeText(getReactApplicationContext(), "tag: "+tag, Toast.LENGTH_LONG).show();


      INTag213215216 objNtag = NTagFactory.getInstance().getNTAG213(nxpLib.getCustomModules());
      Toast.makeText(getReactApplicationContext(), "objNtag: "+objNtag, Toast.LENGTH_LONG).show();

      objNtag.getReader().connect();
      Toast.makeText(getReactApplicationContext(), "__CONNECTED__", Toast.LENGTH_LONG).show();


    } catch (Exception e) {

      Toast.makeText(getReactApplicationContext(), "NFC connect error: "+e.getMessage(), Toast.LENGTH_LONG).show();

    } finally {
      nxpLib.stopForeGroundDispatch();

    }

  }
}