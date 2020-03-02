package com.noform.diaryofsuccess.CustomView;
import android.widget.ListView;
import android.content.Context;
import android.util.AttributeSet;

public class NestListView extends ListView {
    public NestListView(Context context) {
        super(context);
    }

    public NestListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                                                     MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
