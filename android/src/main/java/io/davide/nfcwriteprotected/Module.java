package io.davide.nfcwriteprotected;

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


public class Module extends ReactContextBaseJavaModule {

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  private NxpNfcLib nxpLib = RNProtectedWritePackage.getLibInstance();


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


  @ReactMethod
  public void connect() {

    try{
      INTag213215216 objNtag = NTagFactory.getInstance().getNTAG213(RNProtectedWritePackage.getLibInstance().getCustomModules());
      objNtag.getReader().connect();

    } catch (Exception e) {

      Toast.makeText(getReactApplicationContext(), "NFC connect error", Toast.LENGTH_LONG).show();
    }


    Toast.makeText(getReactApplicationContext(), "NFC connected", Toast.LENGTH_LONG).show();
  }
}