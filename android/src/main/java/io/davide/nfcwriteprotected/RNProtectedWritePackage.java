package io.davide.nfcwriteprotected;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.nxp.nfclib.NxpNfcLib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;

public class RNProtectedWritePackage implements ReactPackage {

    private Activity mActivity = null;

    private static NxpNfcLib nxpLib = null;
    private String nxpLibKey = "ca5b8b55afc1d1267faf36fc14bc9ac3";


    public RNProtectedWritePackage(Activity activity) {
        mActivity = activity;
    }

    private void initializeLibrary()
    {                                        // Initialize the TapLinx library
        nxpLib = NxpNfcLib.getInstance();
        nxpLib.registerActivity(mActivity, nxpLibKey);
    }

    public static NxpNfcLib getLibInstance() {

        return nxpLib;
    }
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    @Override
    public List<NativeModule> createNativeModules(
            ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();

        modules.add(new Module(reactContext));

        return modules;
    }
}
