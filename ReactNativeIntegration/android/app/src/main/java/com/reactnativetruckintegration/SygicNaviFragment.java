package com.reactnativetruckintegration;

import com.sygic.aura.embedded.IApiCallback;
import com.sygic.aura.embedded.SygicFragmentSupportV4;
import com.sygic.sdk.api.events.ApiEvents;

public class SygicNaviFragment extends SygicFragmentSupportV4 {

    IApiCallback apiCallback = new ApiCallback();

    @Override
    public void onResume() {
        setCallback(apiCallback);
        startNavi();
        super.onResume();
    }

    private class ApiCallback implements IApiCallback{

        @Override
        public void onEvent(int event, String data) {
            if(event == ApiEvents.EVENT_APP_EXIT) {
                getActivity().finish();
            }
        }

        @Override
        public void onServiceConnected() {

        }

        @Override
        public void onServiceDisconnected() {

        }
    }
}
