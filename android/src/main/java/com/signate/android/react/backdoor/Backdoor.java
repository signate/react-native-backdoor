package com.signate.android.react.backdoor;

import android.util.SparseArray;
import com.facebook.react.bridge.ReactContext;
import javax.annotation.Nullable;

public class Backdoor {
    private SparseArray<BackdoorMethod> mMethods = new SparseArray<>();
    private ReactContext mReactContext = null;

    void setReactContext(ReactContext reactContext) {
        mReactContext = reactContext;
    }

    @SuppressWarnings("unused")
    public int invokeMethod(String methodName, @Nullable Object arg) {
        BackdoorMethod method = new BackdoorMethod(methodName, arg);
        mMethods.put(method.getId(), method);

        if (mReactContext != null) {
            method.invoke(mReactContext);
        }

        return method.getId();
    }

    @SuppressWarnings("unused")
    public Object getResult(int id) {
        BackdoorMethod method = mMethods.get(id);

        if (method == null) {
            return null;
        }

        if (mReactContext != null && !method.isInvoked()) {
            method.invoke(mReactContext);
        }

        Object returnValue = method.getReturnValue();

        if (method.isFulfilled()) {
            mMethods.remove(id);
        }

        return returnValue;
    }

    void resolve(int id, Object result) {
        BackdoorMethod method = mMethods.get(id);

        if (method != null) {
            method.resolve(result);
        }
    }
}
