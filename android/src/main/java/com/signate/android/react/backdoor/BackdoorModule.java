package com.signate.android.react.backdoor;

import javax.annotation.Nullable;

import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;

class BackdoorModule extends ReactContextBaseJavaModule {
    private Backdoor mBackdoor;

    private static final String NAME = "Backdoor";

    BackdoorModule(ReactApplicationContext reactContext, Backdoor backdoor) {
        super(reactContext);

        mBackdoor = backdoor;
        mBackdoor.setReactContext(reactContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    @SuppressWarnings("unused")
    public void resolve(int id, @Nullable ReadableArray arg) {
        Object value = null;

        if (arg != null) {
            value = arg.toArrayList().get(0);
        }

        mBackdoor.resolve(id, value);
    }
}
