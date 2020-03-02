package com.noform.diaryofsuccess.Util;
import com.google.gson.Gson;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

public class GsonUtil
{
   public static String arr2json(HashMap map){
        Gson gson=new Gson();
		String senddatastr=gson.toJson(map);
        Log.d("GsonUtil", senddatastr);
        return senddatastr;  
        
    }
   public static Object json2arr(String str,Object custom,Class cla){
        Gson gson = new Gson();
         custom = gson.fromJson(str, cla);
        Log.d("GsonUtil", custom.toString());
        return custom;
    }
    static HashMap<Integer,Object> $_(Object... params) {
        // 和
        HashMap<Integer,Object> obj = new HashMap<Integer,Object>();
        int sum = 0;
        for (Object i : params) {
            obj.put(sum,i);
            sum++;
           // Log.v("A-Test",i.toString());
        }
        return obj;
    }
}
