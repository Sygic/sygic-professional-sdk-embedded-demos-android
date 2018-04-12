package com.sygic.integdemo3d.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sygic.integdemo3d.MainActivity;
import com.sygic.integdemo3d.R;
import com.sygic.integdemo3d.UiHelper;
import com.sygic.sdk.api.ApiItinerary;
import com.sygic.sdk.api.exception.GeneralException;
import com.sygic.sdk.api.exception.NavigationException;
import com.sygic.sdk.api.model.StopOffPoint;

import java.util.ArrayList;
import java.util.List;

import com.sygic.integdemo3d.SdkThread;
import com.sygic.sdk.api.model.WayPoint;

public class ItineraryFragment extends Fragment {

    private static final String ITINERARY_TEST = "TestItinerary";

    private ArrayAdapter<StopOffPoint> mListAdapter;
    private ArrayList<StopOffPoint> mItinerary;
    private Handler mHandler;

    private class ViewHolder {
        int pos;
        TextView tvItem;
        Button btnDel;
        StopOffPoint wp;
    };

    public ItineraryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mHandler = new Handler();
        View rootView = inflater.inflate(R.layout.fragment_itin, container, false);

        registerButtons(rootView);
        registerListView(rootView);

        return rootView;
    }

    private void registerButtons(View rootView) {

        rootView.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SdkThread(getActivity(), mHandler) {
                    @Override
                    public void execSdkCommand() {
                        try {
                            ApiItinerary.setRoute(ITINERARY_TEST, 0, 0);
                        } catch (NavigationException e) {
                            Log.e(MainActivity.LOG_TAG, "No itinerary added!");
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        rootView.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showItineraryDialog();
            }
        });

        rootView.findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SdkThread(getActivity(), mHandler) {
                    @Override
                    public void execSdkCommand() {
                        try {
                            ApiItinerary.deleteItinerary(ITINERARY_TEST, MainActivity.API_CALL_TIMEOUT);
                        } catch (GeneralException e) {
                            Log.e(MainActivity.LOG_TAG, "No itinerary added!");
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void postExecute() {
                        refreshList(ITINERARY_TEST);
                    }
                }.start();
            }
        });

        rootView.findViewById(R.id.btn_add_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showViapointDialog();
            }

        });
    }

    public void addItinerary(final int startLong, final int startLat, final int stopLong, final int stopLat) {
        new SdkThread(getActivity(), mHandler) {
            @Override
            public void execSdkCommand() {
                ArrayList<StopOffPoint> list = new ArrayList<>();
                list.add(new StopOffPoint(true, false, StopOffPoint.PointType.START, startLong, startLat, -1, 0, "SVK", "start", ""));
                list.add(new StopOffPoint(true, false, StopOffPoint.PointType.FINISH, stopLong, stopLat, -1, 1, "SVK", "stop", ""));
                try {
                    ApiItinerary.addItinerary(list, ITINERARY_TEST, 0);
                } catch (GeneralException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void postExecute() {
                refreshList(ITINERARY_TEST);
            }
        }.start();
    }

    public void addViapoint(final int viaLong, final int viaLat, final boolean visible) {
        new SdkThread(getActivity(), mHandler) {
            @Override
            public void execSdkCommand() {
                try {
                    StopOffPoint sop = new StopOffPoint(false, false, visible ? StopOffPoint.PointType.VIAPOINT : StopOffPoint.PointType.INVISIBLE, viaLong, viaLat, -1, 0, "", "", "");
                    ApiItinerary.addEntryToItinerary(ITINERARY_TEST, sop, 1, MainActivity.API_CALL_TIMEOUT);
                } catch (GeneralException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void postExecute() {
                refreshList(ITINERARY_TEST);
            }
        }.start();
    }

    private void registerListView(View rootView) {
        ArrayList<StopOffPoint> arrWp = new ArrayList<>();
        mListAdapter = new ItineraryListAdapter(getActivity(), arrWp);
        ListView listItin = (ListView) rootView.findViewById(R.id.itin_list);
        listItin.setAdapter(mListAdapter);
        refreshList(ITINERARY_TEST);
    }

    private void refreshList(final String name) {
        mListAdapter.clear();
        new SdkThread(getActivity(), mHandler) {
            @Override
            public void execSdkCommand() {
                try {
                    mItinerary = ApiItinerary.getItineraryList(name, MainActivity.API_CALL_TIMEOUT);
                } catch (GeneralException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void postExecute() {
                if (mItinerary != null) {
                    for (StopOffPoint p : mItinerary) {
                        mListAdapter.add(p);
                    }
                }
                mListAdapter.notifyDataSetChanged();
            }
        }.start();
    }



    public void removeEntry(final View v) {
        new SdkThread(getActivity(), mHandler) {
            ViewHolder holder = (ViewHolder)v.getTag();
            @Override
            public void execSdkCommand() {
                try {
                    ApiItinerary.deleteEntryInItinerary(ITINERARY_TEST, holder.pos, MainActivity.API_CALL_TIMEOUT);
                } catch (GeneralException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void postExecute() {
                mListAdapter.remove(holder.wp);
                mListAdapter.notifyDataSetChanged();
            }
        }.start();
    }


    private void showItineraryDialog() {
        final ViewGroup viewGroup = (ViewGroup)LayoutInflater.from(getContext()).inflate(R.layout.dialog_itin, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog dialog = builder.setTitle("Add itinerary")
                .setView(viewGroup)
                .setPositiveButton("Add", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int startLong = UiHelper.getIntFromEdit(getContext(), (EditText) viewGroup.findViewById(R.id.etLongStart));
                            int startLat = UiHelper.getIntFromEdit(getContext(), (EditText) viewGroup.findViewById(R.id.etLatStart));
                            int stopLong = UiHelper.getIntFromEdit(getContext(), (EditText) viewGroup.findViewById(R.id.etLongStop));
                            int stopLat = UiHelper.getIntFromEdit(getContext(), (EditText) viewGroup.findViewById(R.id.etLatStop));
                            addItinerary(startLong, startLat, stopLong, stopLat);
                            dialog.dismiss();
                        }
                        catch (NumberFormatException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        dialog.show();
    }

    private void showViapointDialog() {
        final ViewGroup viewGroup = (ViewGroup)LayoutInflater.from(getContext()).inflate(R.layout.dialog_viapoint, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog dialog = builder.setTitle("Add viapoint")
                .setView(viewGroup)
                .setPositiveButton("Add", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int viaLong = UiHelper.getIntFromEdit(getContext(), (EditText) viewGroup.findViewById(R.id.etLongVia));
                            int viaLat = UiHelper.getIntFromEdit(getContext(), (EditText) viewGroup.findViewById(R.id.etLatVia));
                            boolean visible = ((CheckBox)viewGroup.findViewById(R.id.cbVisible)).isChecked();
                            addViapoint(viaLong, viaLat, visible);
                            dialog.dismiss();
                        }
                        catch (NumberFormatException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    private class ItineraryListAdapter extends ArrayAdapter<StopOffPoint> {
        private final Context context;
        private final List<StopOffPoint> items;
        ViewHolder holder = null;

        public ItineraryListAdapter(Context context, List<StopOffPoint> items) {
            super(context, R.layout.itin_list_item, items);
            this.context = context;
            this.items = items;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final StopOffPoint wp = items.get(position);
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.itin_list_item, null);

                holder.tvItem = (TextView) convertView.findViewById(R.id.tv_item);
                holder.tvItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PointInfoDialog dlg = new PointInfoDialog(getContext(), "Waypoint info");
                        dlg.addInfo("Caption", wp.getCaption())
                                .addInfo("Location", wp.getLocation())
                                .addInfo("Address", wp.getAddress())
                                .addInfo("Type", wp.getPointType() + "")
                                .addInfo("Visited", wp.isVisited() ? "yes" : "no");
                        dlg.show();
                    }
                });

                holder.wp = wp;

                holder.btnDel = (Button) convertView.findViewById(R.id.btn_del);
                holder.btnDel.setTag(holder);
                holder.btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeEntry(view);
                    }
                });

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String title = wp.getCaption() != null && !wp.getCaption().isEmpty() ? wp.getCaption() : wp.getAddress();
            holder.tvItem.setText(title);
            holder.pos = position;
            if (position == 0 || position == (items.size() - 1)) {
                holder.btnDel.setVisibility(View.INVISIBLE);
            } else {
                holder.btnDel.setVisibility(View.VISIBLE);
            }

            return convertView;
        }
    }
}
