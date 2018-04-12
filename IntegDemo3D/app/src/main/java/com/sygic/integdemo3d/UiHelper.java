package com.sygic.integdemo3d;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

public class UiHelper {

    private UiHelper() {}

    public static int getIntFromEdit(Context context, EditText edit) {
        try {
            return Integer.parseInt(edit.getText().toString());
        }
        catch (NumberFormatException e) {
            Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
            edit.startAnimation(shake);
            throw e;
        }
    }

}
