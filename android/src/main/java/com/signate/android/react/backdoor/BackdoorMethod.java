package com.signate.android.react.backdoor;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;

class BackdoorMethod {
    private static int nextId = 0;

    private int mId;
    private String mMethodName;
    private Object mArg;
    private boolean mInvoked = false;
    private boolean mFulfilled = false;
    private Object mValue;

    BackdoorMethod(String methodName, Object arg) {
        mMethodName = methodName;
        mArg = arg;
        mId = nextId++;
    }

    int getId() {
        return mId;
    }

    void invoke(ReactContext context) {
        if (context == null || !context.hasActiveCatalystInstance()) {
            return;
        }

        if (mInvoked) {
            return;
        }

        mInvoked = true;

        context
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit("RNBackdoor/invoke", createEventData());
    }

    private WritableArray createEventData() {
        WritableArray data = Arguments.createArray();

        data.pushInt(mId);
        data.pushString(mMethodName);

        if (mArg instanceof String) {
            data.pushString((String) mArg);
        }

        if (mArg instanceof Integer) {
            data.pushInt((Integer) mArg);
        }

        if (mArg instanceof Double) {
            data.pushDouble((Double) mArg);
        }

        if (mArg instanceof Boolean) {
            data.pushBoolean((Boolean) mArg);
        }

        return data;
    }

    void resolve(Object value) {
        mFulfilled = true;
        mValue = value;
    }

    String getName() {
        return mMethodName;
    }

    boolean isFulfilled() { return mFulfilled; }

    boolean isInvoked() {
        return mInvoked;
    }

    Object getReturnValue() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("fulfilled", mFulfilled);
        result.put("value", mValue);

        return result;
    }
}
