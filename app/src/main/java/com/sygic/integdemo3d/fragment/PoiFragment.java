package com.sygic.integdemo3d.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sygic.integdemo3d.MainActivity;
import com.sygic.integdemo3d.R;
import com.sygic.integdemo3d.UiHelper;
import com.sygic.sdk.api.ApiPoi;
import com.sygic.sdk.api.exception.GeneralException;
import com.sygic.sdk.api.model.Poi;
import com.sygic.sdk.api.model.PoiCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.sygic.integdemo3d.SdkThread;

public class PoiFragment extends Fragment {

    private MainActivity mActivity;
    private ArrayAdapter<String> mListAdapter, mSpinnerAdapter;
    private Spinner mSpin;
    private ArrayList<Poi> mPois;
    private Handler mHandler;

    public PoiFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1);
        mSpinnerAdapter = new ArrayAdapter<String>(mActivity, R.layout.spinner_item);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pois, container, false);

        registerButtons(rootView);
        registerListView(rootView);
        registerSpinner(rootView);
        populateSpinner();
        return rootView;
    }

    private void populateSpinner() {
        mSpinnerAdapter.clear();
        new SdkThread(getActivity(), mHandler) {
            ArrayList<PoiCategory> list = null;
            @Override
            public void execSdkCommand() {
                try {
                    list = ApiPoi.getPoiCategoryList(MainActivity.API_CALL_TIMEOUT);
                } catch (GeneralException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void postExecute() {
                if (list != null) {
                    Collections.sort(list, new Comparator<PoiCategory>() {

                        @Override
                        public int compare(PoiCategory arg0, PoiCategory arg1) {
                            Integer defCatA = null;
                            Integer defCatB = null;
                            try {
                                defCatA = Integer.parseInt(arg0.getName());
                                defCatB = Integer.parseInt(arg1.getName());
                                return defCatA.compareTo(defCatB);
                            } catch (NumberFormatException e) {
                                return arg0.getName().compareToIgnoreCase(arg1.getName());
                            }
                        }

                    });
                    if (list != null && !list.isEmpty()) {
                        for (PoiCategory cat : list) {
                            mSpinnerAdapter.add(cat.getName());
                        }
                    }
                    mSpinnerAdapter.notifyDataSetChanged();
                }
            }
        }.start();
    }

    private void registerSpinner(final View v) {
        mSpin = (Spinner) v.findViewById(R.id.spin1);
        mSpin.setAdapter(mSpinnerAdapter);
        mSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refreshList(false);
                ((TextView) v.findViewById(R.id.tv_category)).setText("Category: " + (String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void registerButtons(View rootView) {

        rootView.findViewById(R.id.btn_nearby).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View content = LayoutInflater.from(getContext()).inflate(R.layout.dialog_nearby_pois, null);

                final AlertDialog dialog = new AlertDialog.Builder(mActivity)
                        .setTitle("Nearby pois")
                        .setView(content)
                        .setPositiveButton("OK", null)
                        .create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                ViewGroup viewGroup = (ViewGroup)content;
                                try{
                                    final int count = UiHelper.getIntFromEdit(getContext(), (EditText)viewGroup.findViewById(R.id.etCount));
                                    final int lat = UiHelper.getIntFromEdit(getContext(), (EditText)viewGroup.findViewById(R.id.etLat));
                                    final int lon = UiHelper.getIntFromEdit(getContext(), (EditText)viewGroup.findViewById(R.id.etLong));

                                    int nsel;
                                    final String sel = (String) mSpin.getSelectedItem();
                                    try {
                                        nsel = Integer.parseInt(sel);
                                    } catch (NumberFormatException e) {
                                        nsel = ApiPoi.USERDEFINE;
                                    }
                                    final int id = nsel;

                                    new SdkThread(getActivity(), mHandler) {
                                        boolean dismiss = true;

                                        @Override
                                        public void execSdkCommand() {
                                            try {
                                                mPois = ApiPoi.findNearbyPoiList(id, sel, lon, lat, count, MainActivity.API_CALL_TIMEOUT);
                                            } catch (GeneralException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void postExecute() {
                                            if (dismiss) {
                                                dialog.dismiss();
                                                refreshList(true);
                                            }
                                        }
                                    }.start();

                                }
                                catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
                dialog.show();
            }
        });
    }

    private void registerListView(View rootView) {
        ListView list = (ListView) rootView.findViewById(R.id.pois_list);
        list.setAdapter(mListAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Poi poi = mPois.get(i);
                PointInfoDialog dlg = new PointInfoDialog(getContext(), "Poi details");
                dlg.addInfo("Caption", poi.getName())
                        .addInfo("Category", poi.getCategory())
                        .addInfo("Address", poi.getAddress())
                        .addInfo("Location", poi.getLocation());
                dlg.show();
            }
        });
    }

    private void refreshList(final boolean nearby) {
        new SdkThread(getActivity(), mHandler) {
            @Override
            public void execSdkCommand() {
                if (!nearby) {
                    try {
                        mPois = ApiPoi.getPoiList((String) mSpin.getSelectedItem(), true, MainActivity.API_CALL_TIMEOUT);
                    } catch (GeneralException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void postExecute() {
                mListAdapter.clear();
                if (mPois != null) {
                    for (Poi p : mPois) {
                        mListAdapter.add(p.getName());
                    }
                }
                mListAdapter.notifyDataSetChanged();
            }
        }.start();
    }
}
