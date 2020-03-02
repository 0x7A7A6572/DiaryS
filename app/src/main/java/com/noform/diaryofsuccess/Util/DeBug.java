package com.noform.diaryofsuccess.Util;
import android.view.View;
import android.widget.Toast;
import com.noform.diaryofsuccess.MainActivity;
import java.util.Arrays;
import android.util.DebugUtils;
import java.util.ArrayList;

public class DeBug
{
    private View view = null;

    public void setView(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }
    
    public static String dumpArrayList(ArrayList arrays){
        
        StringBuilder sb = new StringBuilder();
        String split = null;
        for(int i = 0; i < arrays.size();i++){
            if(arrays.get(i).getClass().isArray()){
                arrays.set(i,dumpArray((Object[])arrays.get(i)));
            }
            if(i==arrays.size()-1){split = "";}else{split = ",\n";}   
            sb.append("[" + i + "] = " + arrays.get(i).toString() + split);
        }

        return "[\n " + sb.toString() + " \n]";
    }
    
    public static String dumpArray(Object[] obja){
       StringBuilder sb = new StringBuilder();
        String split = null;
       for(int i = 0; i < obja.length;i++){
           if(obja[i].getClass().isArray()){
              obja[i]=dumpArray((Object[])obja[i]);
           }
           if(i==obja.length-1){split = "";}else{split = ",\n";}   
           sb.append("[" + i + "] = " + obja[i] + split);
       }
       
       return "[\n " + sb.toString() + " \n]";
    }

   /* public static String dumpArray(Object obja) {
       return dumpArray((Object[])obja);
    }*/
   public static void print(Object...paramet) {
       int count = paramet.length;    // 获取总个数
       StringBuilder sb = new StringBuilder();
       
       for(int i = 0;i < count;i++) {
           if(paramet[i] instanceof Object[]){
               paramet[i] =  dumpArray((Object[])paramet[i]);
              // for(int m = 0;m < (Object[])paramet[i].toString();m++){
                   
              // }
           }else if("ArrayList".equals(paramet[i].getClass().getSimpleName())){
               
               paramet[i] = dumpArrayList((ArrayList)paramet[i]);
           }
           sb.append(paramet[i]+"\n");
        }
       Toast.makeText(MainActivity.CONTEXT, sb, Toast.LENGTH_LONG).show();
  
   }

   
}
