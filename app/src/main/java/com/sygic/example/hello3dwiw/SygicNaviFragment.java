package com.sygic.example.hello3dwiw;

import com.sygic.aura.embedded.SygicFragmentSupportV4;

public class SygicNaviFragment extends SygicFragmentSupportV4 {

    @Override
    public void onResume() {
        startNavi();
        setCallback(new SygicNaviCallback(getActivity()));
        super.onResume();
    }
}