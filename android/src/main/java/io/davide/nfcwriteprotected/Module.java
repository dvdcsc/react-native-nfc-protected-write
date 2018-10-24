package io.davide.nfcwriteprotected;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.HashMap;
import java.util.Map;

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

public class Module extends ReactContextBaseJavaModule {

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  private NxpNfcLib nxpLib = null;
  private String nxpLibKey = "0235a960fcb9a265be2ffe54da8288bd";

  public Module(ReactApplicationContext reactContext) {
    super(reactContext);
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
  public void connect() {

    try{

      initializeLibrary();
      Toast.makeText(getReactApplicationContext(), "Intent: "+getCurrentActivity().getIntent(), Toast.LENGTH_LONG).show();


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