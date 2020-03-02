package com.noform.diaryofsuccess.Object;
import android.view.View;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import com.noform.diaryofsuccess.MainActivity;
import android.content.Context;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.noform.diaryofsuccess.R;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

public class AllViewListener {
    View[] views;
    public void AllViewListener() {
        //this();
    }
    public void bind(String funName, View view) {
        String viewObj = "android.widget." + view.getClass().getSimpleName();

        try {
            Class clazz = Class.forName("com.noform.diaryofsuccess.Object.AllViewListener");
            switch (funName) {
                case "textButton":

                    try {
                        Method method=clazz.getMethod(funName, Class.forName(viewObj));
                        try {
                            method.invoke(clazz.newInstance(), view);
                        } catch (InvocationTargetException e) {} catch (IllegalArgumentException e) {} catch (InstantiationException e) {} catch (IllegalAccessException e) {}
                    } catch (NoSuchMethodException e) {} catch (SecurityException e) {}                                    
                    break;      
            }         
        } catch (ClassNotFoundException e) {}    
    }


    public static void setOnClick(View view, final String funName) {

        view.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    // String fun = funName;
                    //Toast.makeText(getContext(), "你妹的", Toast.LENGTH_LONG).show();
                    try {                     
                        new AllViewListener().bind(funName, p1);
                    } catch (SecurityException e) {} catch (IllegalArgumentException e) {}    

                }
            });
    }

    public static void setOnLongClick(View view,final String funName) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    try {                     
                        new AllViewListener().bind(funName, view);
                    } catch (SecurityException e) {} catch (IllegalArgumentException e) {}    
             
                    return false;
                }
            });
    }


    public static void setOnTouch(View view){
        view.setOnTouchListener(new OnTouchListener(){

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    int action=event.getAction();
                    if (action == MotionEvent.ACTION_DOWN) {
                        //点击
                    } else if (action == MotionEvent.ACTION_UP) {
                        //松开  
                    } else if (action == MotionEvent.ACTION_MOVE) {
                        //移动
                    }
                    return true;
                }
            });
    }
    
    

    
    
}
