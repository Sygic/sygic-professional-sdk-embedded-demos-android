package com.sygic.integdemo3d.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sygic.integdemo3d.R;
import com.sygic.sdk.api.model.Position;

import java.util.ArrayList;

public class PointInfoDialog {

    public static class Info {
        String name, value;

        public Info(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    private ArrayList<Info> mListInfos = new ArrayList<>();
    private Context mContext;
    private String mTitle;

    public PointInfoDialog(Context context, String title) {
        mContext = context;
        mTitle = title;
    }

    public PointInfoDialog addInfo(String name, String value) {
        mListInfos.add(new Info(name, value));
        return this;
    }

    public PointInfoDialog addInfo(String name, Position position) {
        mListInfos.add(new Info(name, position.getX() + ", " + position.getY()));
        return this;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mTitle)
                .setView(getContentView())
                .setPositiveButton("OK", null);

        builder.create().show();
    }

    private View getContentView() {
        TableLayout table = new TableLayout(mContext);

        for(Info info : mListInfos) {
            TableRow row = (TableRow)LayoutInflater.from(mContext).inflate(R.layout.row_point_info, null);

            ((TextView)row.findViewById(R.id.tvName)).setText(info.name);
            ((TextView)row.findViewById(R.id.tvValue)).setText(info.value);

            table.addView(row);
        }

        return table;
    }

}
