package com.noform.diaryofsuccess.CustomView;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.noform.diaryofsuccess.R;

public class NFDialog extends Dialog {

    private static int default_width =  -1; //默认宽度
    private static int default_height = -1;//默认高度
    private static int Layout_LoadView = R.layout.diary_dialog;
    private static int style_LoadView = R.style.noform_dialog;
    private static boolean keyboard = true;
    public NFDialog(Context context) {
        this(context, default_width, default_height, Layout_LoadView, style_LoadView,keyboard);
    }
    public NFDialog(Context context, int layout, int style) {
        this(context, default_width, default_height, layout, style,true);
    }

    public NFDialog(Context context, int width, int height, int layout, int style) {
        this(context, width, height, layout, style,keyboard);
    }

    public NFDialog(Context context, int width, int height, int layout, int style,boolean keyboard) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        float density = getDensity(context);
        if (width == -1) {//满宽度
            width = (int) (window.getWindowManager().getDefaultDisplay().getWidth() / density);
        }
        params.width = (int) (width * density);
        params.height = (int) (height * density);
        params.gravity = Gravity.BOTTOM;
        if(keyboard) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        window.setAttributes(params);
    }
    private float getDensity(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.density;
    }

}
