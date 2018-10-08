package io.davide.nfcwriteprotected;

import android.app.Activity;
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

public class Module extends ReactContextBaseJavaModule {

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  private NxpNfcLib nxpLib = null;
  private String nxpLibKey = "ca5b8b55afc1d1267faf36fc14bc9ac3";

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
  }

  @ReactMethod
  public void connect() {

    try{

      initializeLibrary();

      INTag213215216 objNtag = NTagFactory.getInstance().getNTAG213(nxpLib.getCustomModules());
      Toast.makeText(getReactApplicationContext(), "objNtag: "+objNtag, Toast.LENGTH_LONG).show();

      byte[] data = objNtag.read(0x0f);
      Toast.makeText(getReactApplicationContext(), "data: "+data, Toast.LENGTH_LONG).show();

      byte[] byteToWrite = new byte[] {0x00,0x00,0x00,0x00};

      String hexString = Utilities.byteToHexString(byteToWrite);

      Toast.makeText(getReactApplicationContext(), "hexString: "+hexString, Toast.LENGTH_LONG).show();

      objNtag.write(0x0f,byteToWrite);

      Toast.makeText(getReactApplicationContext(), "NFC writed", Toast.LENGTH_LONG).show();



    } catch (Exception e) {

      Toast.makeText(getReactApplicationContext(), "NFC connect error: "+e.getMessage(), Toast.LENGTH_LONG).show();
    }


  }
}