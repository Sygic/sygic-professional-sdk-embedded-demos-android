package com.sygic.integdemo3d.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sygic.integdemo3d.MainActivity;
import com.sygic.integdemo3d.R;
import com.sygic.sdk.api.ApiLocation;
import com.sygic.sdk.api.ApiNavigation;
import com.sygic.sdk.api.exception.GeneralException;
import com.sygic.sdk.api.exception.InvalidLocationException;
import com.sygic.sdk.api.exception.NavigationException;
import com.sygic.sdk.api.model.Position;
import com.sygic.sdk.api.model.RoadInfo;
import com.sygic.sdk.api.model.WayPoint;

import java.util.ArrayList;
import java.util.Arrays;

import com.sygic.integdemo3d.SdkThread;


public class LocationFragment extends Fragment {

    private EditText mPosX, mPosY, mAddress;
    private TextView mText;
    private Handler mHandler;


    public LocationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location, container, false);

        registerButtons(root);
        registerFields(root);
        mHandler = new Handler();
        return root;
    }

    private void registerFields(View view) {
        mPosX = (EditText) view.findViewById(R.id.et1);
        mPosY = (EditText) view.findViewById(R.id.et2);
        mAddress = (EditText) view.findViewById(R.id.et3);
        mText = (TextView) view.findViewById(R.id.tv1);
    }

    private void registerButtons(View rootView) {
        final Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        Button btn = (Button) rootView.findViewById(R.id.btn_loc_address);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Position pos = new Position();
                if (mAddress.getText().length() == 0) {
                    mAddress.startAnimation(shake);
                }
                new SdkThread(getActivity(), mHandler) {
                    Position pos = new Position();
                    @Override
                    public void execSdkCommand() {
                        try {
                            pos = ApiLocation.locationFromAddress(mAddress.getText().toString(), false, true, MainActivity.API_CALL_TIMEOUT);
                        } catch (GeneralException e) {
                            e.printStackTrace();
                            Log.e(MainActivity.LOG_TAG, "Error: " + e.getCode() + ", " + e.getMessage());
                        }
                    }
                    @Override
                    public void postExecute() {
                        mText.setText(pos.toString());
                        mPosX.setText(String.valueOf(pos.getX()));
                        mPosY.setText(String.valueOf(pos.getY()));
                    }
                }.start();
            }
        });

        btn = (Button) rootView.findViewById(R.id.btn_loc_address_ex);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mAddress.getText().length() == 0) {
                    mAddress.startAnimation(shake);
                }
                new SdkThread(getActivity(), mHandler) {
                    ArrayList<WayPoint> wp = new ArrayList<WayPoint>();
                    @Override
                    public void execSdkCommand() {
                        try {
                            wp = ApiLocation.locationFromAddressEx(mAddress.getText().toString(), false, MainActivity.API_CALL_TIMEOUT);
                        } catch (GeneralException e) {
                            e.printStackTrace();
                            Log.e(MainActivity.LOG_TAG, "Error: " + e.getCode() + ", " + e.getMessage());
                        }
                    }
                    @Override
                    public void postExecute() {
                        if (wp != null) {
                            mText.setText(Arrays.toString(wp.toArray()));
                        }
                    }
                }.start();
            }
        });

        btn = (Button) rootView.findViewById(R.id.btn_loc_address_fuzzy);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAddress.getText().length() == 0) {
                    mAddress.startAnimation(shake);
                }
                new SdkThread(getActivity(), mHandler) {
                    ArrayList<WayPoint> wp = new ArrayList<WayPoint>();
                    @Override
                    public void execSdkCommand() {
                        try {
                            wp = ApiLocation.locationFromAddressEx(mAddress.getText().toString(), false, true, MainActivity.API_CALL_TIMEOUT);
                        } catch (GeneralException e) {
                            e.printStackTrace();
                            Log.e(MainActivity.LOG_TAG, "Error: " + e.getCode() + ", " + e.getMessage());
                        }
                    }
                    @Override
                    public void postExecute() {
                        if (wp != null) {
                            mText.setText(Arrays.toString(wp.toArray()));
                        }
                    }
                }.start();
            }
        });

        btn = (Button) rootView.findViewById(R.id.btn_loc_addresses);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAddress.getText().length() == 0) {
                    mAddress.startAnimation(shake);
                }
                new SdkThread(getActivity(), mHandler) {
                    ArrayList<WayPoint> wp = new ArrayList<WayPoint>();
                    @Override
                    public void execSdkCommand() {
                        try {
                            wp = ApiLocation.getAddressList(mAddress.getText().toString(), false, 50, MainActivity.API_CALL_TIMEOUT);
                        } catch (GeneralException e) {
                            e.printStackTrace();
                            Log.e(MainActivity.LOG_TAG, "Error: " + e.getCode() + ", " + e.getMessage());
                        }
                    }
                    @Override
                    public void postExecute() {
                        if (wp != null) {
                            mText.setText(Arrays.toString(wp.toArray()));
                        }
                    }
                }.start();
            }
        });

        btn = (Button) rootView.findViewById(R.id.btn_loc_address_info);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = 0, y = 0;
                if (!mPosX.getText().toString().equals("")) {
                    x = Integer.parseInt(mPosX.getText().toString());
                } else {
                    mPosX.startAnimation(shake);
                }
                if (!mPosY.getText().toString().equals("")) {
                    y = Integer.parseInt(mPosY.getText().toString());
                } else {
                    mPosY.startAnimation(shake);
                }
                final int finalX = x;
                final int finalY = y;
                new SdkThread(getActivity(), mHandler) {
                    String str;
                    @Override
                    public void execSdkCommand() {
                        try {
                            str = ApiLocation.getLocationAddressInfo(new Position(finalX, finalY), MainActivity.API_CALL_TIMEOUT);
                        } catch (InvalidLocationException e) {
                            e.printStackTrace();
                            Log.e(MainActivity.LOG_TAG, "Error: " + e.getCode() + ", " + e.getMessage());
                        }
                    }
                    @Override
                    public void postExecute() {
                        if (str != null) {
                            mText.setText(str);
                        }
                    }
                }.start();
            }
        });

        btn = (Button) rootView.findViewById(R.id.btn_loc_road_info);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = 0, y = 0;
                if (!mPosX.getText().toString().equals("")) {
                    x = Integer.parseInt(mPosX.getText().toString());
                } else {
                    mPosX.startAnimation(shake);
                }
                if (!mPosY.getText().toString().equals("")) {
                    y = Integer.parseInt(mPosY.getText().toString());
                } else {
                    mPosY.startAnimation(shake);
                }
                final int finalX = x;
                final int finalY = y;
                new SdkThread(getActivity(), mHandler) {
                    RoadInfo info;
                    @Override
                    public void execSdkCommand() {
                        try {
                            info = ApiLocation.getLocationRoadInfo(new Position(finalX, finalY), MainActivity.API_CALL_TIMEOUT);
                        } catch (InvalidLocationException e) {
                            e.printStackTrace();
                            Log.e(MainActivity.LOG_TAG, "Error: " + e.getCode() + ", " + e.getMessage());
                        }
                    }
                    @Override
                    public void postExecute() {
                        if (info != null) {
                            mText.setText(info.toString());
                        }
                    }
                }.start();
            }
        });

        btn = (Button) rootView.findViewById(R.id.btn_loc_navi_point);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = 0, y = 0;
                if (!mPosX.getText().toString().equals("")) {
                    x = Integer.parseInt(mPosX.getText().toString());
                } else {
                    mPosX.startAnimation(shake);
                }
                if (!mPosY.getText().toString().equals("")) {
                    y = Integer.parseInt(mPosY.getText().toString());
                    final int finalX = x;
                    final int finalY = y;
                    new SdkThread(getActivity(), mHandler) {
                        @Override
                        public void execSdkCommand() {
                            String address = null;
                            try {
                                address = ApiLocation.getLocationAddressInfo(new Position(finalX, finalY), MainActivity.API_CALL_TIMEOUT);
                                ApiNavigation.startNavigation(new WayPoint(address, finalX, finalY), 0, false, MainActivity.API_CALL_TIMEOUT);
                            } catch (InvalidLocationException e) {
                                e.printStackTrace();
                                Log.e(MainActivity.LOG_TAG, "Error: " + e.getCode() + ", " + e.getMessage());
                            } catch (NavigationException e) {
                                e.printStackTrace();
                                Log.e(MainActivity.LOG_TAG, "Error: " + e.getCode() + ", " + e.getMessage());
                            }
                        }
                    }.start();
                } else {
                    mPosY.startAnimation(shake);
                }
            }
        });

        btn = (Button) rootView.findViewById(R.id.btn_loc_navi_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAddress.getText().length() == 0) {
                    mAddress.startAnimation(shake);
                } else {
                    new SdkThread(getActivity(), mHandler) {
                        @Override
                        public void execSdkCommand() {
                            Position pos = null;
                            try {
                                pos = ApiLocation.locationFromAddress(mAddress.getText().toString(), false, true, MainActivity.API_CALL_TIMEOUT);
                                ApiNavigation.startNavigation(new WayPoint(mAddress.getText().toString(), pos.getX(), pos.getY()), 0, false, MainActivity.API_CALL_TIMEOUT);
                            } catch (GeneralException e) {
                                Log.e(MainActivity.LOG_TAG, "Error: " + e.getCode() + ", " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });
    }
}
