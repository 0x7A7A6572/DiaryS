package com.noform.diaryofsuccess;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.noform.diaryofsuccess.Adapter.DiaryAdapter;
import com.noform.diaryofsuccess.Adapter.MyPagerAdapter;
import com.noform.diaryofsuccess.Adapter.NoteAdapter;
import com.noform.diaryofsuccess.CustomView.NFDialog;
import com.noform.diaryofsuccess.CustomView.RoundCornersImageView;
import com.noform.diaryofsuccess.Object.Note;
import com.noform.diaryofsuccess.Util.ImageUtil;
import com.noform.diaryofsuccess.Util.MD5;
import com.noform.diaryofsuccess.Util.io;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static android.provider.UserDictionary.Words.APP_ID;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_IMAGE = 0x7A7A ;
    private NFDialog diary_dialog;
    private static String SystemReturnPath;
    String diary_dialog_image_path;

    /*
     *   @listItem 日记数据
     *   @diary_dialog_show 
     */
   
    private View NoteView,DiaryView,LedgerView;
    private MyPagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private List<View> viewList;//view数组  
    public static ListView list;
    public static Context CONTEXT;
    public static  String userJson; 
    public static ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();  
    public String filePath;
    public static boolean isMultipleSelectionMode;
    static TextView textViewOfClickItemCount;
    private DiaryAdapter listItemAdapter;
    private SimpleDateFormat format=new SimpleDateFormat("MM/dd/yy HH:mm");
    private SimpleDateFormat note_format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private SimpleDateFormat simple_format=new SimpleDateFormat("yyyyMMddHHmm");
    private List<Note> NoteList = new ArrayList<>();

    private Intent intent;


    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // startActivity(new Intent(MainActivity.this, StartActivity.class));
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);
        CONTEXT = this.getApplicationContext();
        textViewOfClickItemCount = findViewById(R.id.textViewOfclickItemCount);
        mViewPager = findViewById(R.id.activityViewPager);

       // Toolbar myToolBar = findViewById(R.id.toolbar);
        // myToolBar.setVisibility(View.GONE);

        /*  @loadUserData();  加载用户本地数据
         *  @setListViewListen(); 设置listView的监听
         *  @setButtonsListener(); 设置所有按钮的监听
         *  @loadViewPager();  加载ViewPager
         */
         
         //setTitle("标签🏷");

        RegistrationPermissions();
        createFile();
        loadViewPager();
        loadMemorandum();
        loadUserData(); 
         setListViewListen();
        setButtonsListener();

    }
    
   

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }


    public void RegistrationPermissions(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }


        PowerManager powerManager =(PowerManager) this.getSystemService(Service.POWER_SERVICE);
        //DeBug.print(powerManager,this.getPackageName());
        boolean hasIgnored = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasIgnored = powerManager.isIgnoringBatteryOptimizations(this.getPackageName());
        }
        // 判断如果没有加入电池优化的白名单,则弹出加入电池优化的白名单的设置对话
        if (! hasIgnored){
            // print("没有加入白名单")
            intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).setData(Uri.parse("package:" + this.getPackageName()));
            this.startActivityForResult(intent, 100);
        }else{
            // this.moveTaskToBack(true);
        }

       // this.startActivityForResult( getAppDetailSettingIntent(CONTEXT).setData(Uri.parse("package:" + this.getPackageName())),100);

    }
    public Intent getAppDetailSettingIntent(Context context){
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (android.os.Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(),null));
        } else if (android.os.Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
         return localIntent;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //outState.putString("anAnt","Android");
        //DeBug.print("我未经许可关闭了该Activity");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null,null);
            if (cursor != null && cursor.moveToFirst()) {
                SystemReturnPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
               ImageView dialog_image = diary_dialog.findViewById(R.id.dialog_image);
               dialog_image.setImageBitmap(ImageUtil.getLoacalBitmap(SystemReturnPath));
               dialog_image.setVisibility(View.VISIBLE);
                Log.d("TAG",SystemReturnPath);
            }
        }
    }

       
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        if (!isAppOnForeground()) {
            //app 进入后台
          //  DeBug.print("进入后台");

            //全局变量isActive = false 记录当前已经进入后台
        }
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
       // DeBug.print("回来");
       // Log.i(TAG, "BaseActivity - onResume");
       /* if (!ComConstant.isActive()) {
            Log.i(TAG, "从后台返回到前台");
            ComConstant.setIsActive(true);
        }*/
    }
    
    
    public void createFile(){
        this.getDir("diaryImage",MODE_PRIVATE);
    }
    
    

    public boolean isAppOnForeground() {

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
            .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }
    

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
            if (isMultipleSelectionMode) {

                showMultipleSelectionMode(false);
                list.clearChoices();//取消选中状态
                textViewOfClickItemCount.setText("0");
                DiaryView.findViewById(R.id.listView_father).setBackgroundResource(R.color.white);
                listItemAdapter.notifyDataSetChanged();
                return true;
            }
            return super.onKeyDown(keyCode, event);
        } 
        return super.onKeyDown(keyCode, event); 
    }
    

 

    public void loadViewPager() {
        LayoutInflater inflater=getLayoutInflater();  
        DiaryView = (RelativeLayout)inflater.inflate(R.layout.diary, null);
        NoteView = inflater.inflate(R.layout.note, null);
        LedgerView = inflater.inflate(R.layout.ledger, null);
        list = DiaryView.findViewById(R.id.NoteList);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中  
        viewList.add(NoteView);  
        viewList.add(DiaryView);  
        viewList.add(LedgerView);  
        pagerAdapter = new MyPagerAdapter(viewList);
        mViewPager.setAdapter(pagerAdapter);  

        /*
         @mViewPager.setCurrentItem(1);
         初始显示第二个视图
         使得主视图可左右滑动到另外两个视图，
         而不是滑两次.
         */
        mViewPager.setCurrentItem(1);
    }

    private String getRandomLengtName(String content){
        Random random = new Random();
        int length = random.nextInt(2) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++){
            builder.append(content);
        }
        return builder.toString();
    }

    public void  loadMemorandum(){

        String time =  note_format.format(new Date());
        for (int i = 0;i < 2;i++){
            Note note0 = new Note(time,getRandomLengtName("hahahahahahahahahahahahahah"));
            Note note1 = new Note(time,getRandomLengtName("BBBBBBBBBBBBBBBBBBBBBBB"));
            Note note2 = new Note(time,getRandomLengtName("hCCCCCCCCCCCCCCCCahah"),true);
            Note note3 = new Note(time,getRandomLengtName("haDDDDDDDDDDDDDDahahahahah"));
            NoteList.add(note0);
            NoteList.add(note1);
            NoteList.add(note2);
            NoteList.add(note3);
        }
        RecyclerView recyclerView = NoteView.findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this); //纵向布局
        recyclerView.setLayoutManager(layoutManager);
        NoteAdapter noteAdapter = new NoteAdapter(NoteList);
        recyclerView.setAdapter(noteAdapter);

    }

    public void loadUserData() {
        filePath = getFilesDir() + "/diary_data.json";
        Log.v("#debug", filePath);
        userJson = new io(filePath).read();
        if ("".equals(userJson)) {//数据空白
            userJson = "[{\"diary_date\":\"00/00\",\"diary_time\":\"00:00\",\"diary_DiaryContent\":\"想一想你今天做的事，编写你的成功日记吧！\\n *长按我可以删除和收藏哦\",\"diary_address\":\"null\",\"diary_numberOf\":1}]";
            new io(filePath).writer(userJson);
        } else {//存在数据

        }

    }

    public void setListViewListen() {

        //userJson =  "[{\"notelist_date\":\"02/08\",\"notelist_time\":\"23:59\",\"notelist_year\":\"20\",\"notelist_DiaryContent\":\"这个是日记内容开局痛快淋漓\",\"notelist_address\":\"地址在哪我哪知道\",\"notelist_numberOf\":1}]";
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<HashMap<String, String>>>(){
        }.getType();
        ArrayList<HashMap<String,String>> diatyJson = gson.fromJson(userJson, type);
        Log.v("#debug", diatyJson.toString());
        if (true) {
            listItem = diatyJson;
        }

        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listItemAdapter = new DiaryAdapter(listItem);
        list.setAdapter(listItemAdapter);



        list.setOnItemClickListener(new OnItemClickListener() {  

                @Override  
                public void onItemClick(AdapterView<?> p, View v, int index,  
                                        long arg3) {

                    if (isMultipleSelectionMode) {
                        textViewOfClickItemCount.setText(String.valueOf(list.getCheckedItemCount()));
                    }
                    listItemAdapter.notifyDataSetChanged();
                    scale(v.findViewById(R.id.diary_content_view));
                }  
            });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long itemId) {
                    //无论进入多选状态还是退出多选状态，都清理选中的
                    list.clearChoices();//取消选中状态
                    if (isMultipleSelectionMode) {
                        showMultipleSelectionMode(false);
                        list.clearChoices();//取消选中状态
                        textViewOfClickItemCount.setText("0");
                        DiaryView.findViewById(R.id.listView_father).setBackgroundResource(R.color.white);

                    } else {
                        // listItemAdapter.balistItem.clear();


                        showMultipleSelectionMode(true);
                        listItemAdapter.notifyDataSetChanged();
                        DiaryView.findViewById(R.id.listView_father).setBackgroundResource(R.color.deepgrey);

                        //多选模式
                        return true;
                        //监听模式状态
                        /* if(mCallback==null) mCallback = new ModeCallback();
                         loglist.setMultiChoiceModeListener(mCallback);  */
                        //清除所有选中
                        //listItemAdapter.clearChoices();
                    }

                    listItemAdapter.notifyDataSetChanged();

                    return false;
                }
            });




        list.setOnScrollListener(new OnScrollListener(){
                boolean scoll_bottom = false;
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {            
                    /*toast("dc"+"f：：" + firstVisibleItem + ":v:" + visibleItemCount + ":t:" + totalItemCount,getContext());
                     if (scrollFlag) {
                     if (firstVisibleItem > lastVisibleItemPosition) {
                     //toast("dc"+"上滑"+firstVisibleItem,getContext());
                     scoll_flag = 0;
                     //底部再次上滑
                     //  toast("底部再次上滑",getContext());

                     }
                     if (firstVisibleItem < lastVisibleItemPosition) {
                     // toast("dc"+"下滑",getContext());
                     scoll_flag = 1;
                     }
                     if (firstVisibleItem == lastVisibleItemPosition) {
                     return;
                     }
                     lastVisibleItemPosition = firstVisibleItem;
                     }*/
                }

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    // TODO Auto-generated method stub


                    if (scrollState == OnScrollListener.SCROLL_STATE_IDLE // 滑到底时自动加载数据
                        && view.getLastVisiblePosition() == view.getCount() - 1  &&  !scoll_bottom && !isMultipleSelectionMode) {
                        //toast("xxxxxx" + "到底", getContext());       //在此处理加载事件
                        scoll_bottom = true;
                        ShowQucklyToolBar(false);

                    } else if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && 
                               view.getLastVisiblePosition() != view.getCount() - 1) {//}SCROLL_STATE_TOUCH_SCROLL) {
                        if (findViewById(R.id.quckly_tool_bar).getVisibility() == View.GONE && !isMultipleSelectionMode) {
                            ShowQucklyToolBar(true);
                        }

                        scoll_bottom = false;
                        // first_y = view.getY();
                    } /*else if(scrollState == OnScrollListener.SCROLL_STATE_FLING){//正在滑动
                     if(view.getY()<first_y){
                     first_y = view.getY();
                     toast(Float.toString(first_y),getContext());                       
                     }
                     }*/
                }


            });

    }

    public  void setButtonsListener() {
        ImageView activity_addDiaryButton = findViewById(R.id.activity_addDiaryButton);
        ImageView activity_delItem = findViewById(R.id.activity_delItem);
        ImageView activity_allselect = findViewById(R.id.activity_allselect);
        ImageView activity_reverseSelect = findViewById(R.id.activity_reverseSelect);
        ImageButton set_main = findViewById(R.id.set_main);
        RoundCornersImageView login_activity = findViewById(R.id.login);
        // AllViewListener.setOnClick(activity_addDiaryButton, "activity_addDiaryButton");
        // AllViewListener.setOnClick(activity_delItem,"activity_delItem");


        activity_delItem.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View view) {
                    removeItemTo();
                    list.clearChoices();//取消选中状态
                    listItemAdapter.notifyDataSetChanged();
                }
            });

        activity_allselect.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View view) {
                    //StringBuilder sb = new StringBuilder();
                    for (int i = 0;i < listItemAdapter.balistItem.size();i++) {
                        //设置list的点击状态为true i大小为balistItem的size
                        list.setItemChecked(i, true);
                       // sb.append(i + " : " + list.isItemChecked(i) + "\n");
                    }
                    textViewOfClickItemCount.setText(String.valueOf(list.getCheckedItemCount()));
                    listItemAdapter.notifyDataSetChanged();
                    //DeBug.print("选中:"+list.getCheckedItemCount(),"大小:" + listItemAdapter.convertViewList.size());
                }
            });

        activity_addDiaryButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View view) {
                  //  TextView  textview = listItemAdapter.convertViewList.get(0).findViewById(R.id.notelist_DiaryContent);
                   // DeBug.print(listItemAdapter.convertViewList);
               for (int i = 0;i<200;i++){
                   addItemsTo("测试便签" + i,null);
               }
                    
                }
            });
            
        activity_reverseSelect.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    for (int i = 0;i < listItemAdapter.balistItem.size();i++) {
                        if(list.isItemChecked(i)){
                            list.setItemChecked(i, false); 
                            //listItemAdapter.setChoiceBackground(false, i);
                        }else{
                            list.setItemChecked(i, true); 
                           // listItemAdapter.setChoiceBackground(true, i);
                        }                                                    
                    }
                    textViewOfClickItemCount.setText(String.valueOf(list.getCheckedItemCount()));
                    listItemAdapter.notifyDataSetChanged();
                }                         
        });

        set_main.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SetActivity.class));
            }
        });

        login_activity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));


            }
        });

    }




    public void showMultipleSelectionMode(boolean is) {
        LinearLayout MultipleSelectionMode_bar = findViewById(R.id.MultipleSelectionMode_bar);
        LinearLayout quckly_tool_bar = findViewById(R.id.quckly_tool_bar);
        if (is) {
            ShowQucklyToolBar(false);
            quckly_tool_bar.setVisibility(View.GONE);
            MultipleSelectionMode_bar.setVisibility(View.VISIBLE);
            isMultipleSelectionMode = true;
            //ShowQucklyToolBar(true);
        } else if (!is) {
            // ShowQucklyToolBar(false);
            quckly_tool_bar.setVisibility(View.VISIBLE);
            MultipleSelectionMode_bar.setVisibility(View.GONE);
            isMultipleSelectionMode = false;
            ShowQucklyToolBar(true);
        } else {
            // Log.v("#error","发生了错误呢! is的值为:" + is);
        }
    }

    public  void ShowQucklyToolBar(boolean is) {
        // MainActivity mainActivity = new MainActivity();

        LinearLayout quckly_tool_bar = findViewById(R.id.quckly_tool_bar);
        //LinearLayout  quckly_tool_bar2 = findViewById(R.id.quckly_tool_bar_right);
        /***
         * 平移动画TranslateAnimation 从x,y 轴 从（0,0）平移到（0,500） *
         **/
        if (!is) {
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 500.f);
            translateAnimation.setDuration(500);
            // translateAnimation.setRepeatCount(-1);
            //translateAnimation.setRepeatMode(Animation.RESTART);
            quckly_tool_bar.startAnimation(translateAnimation);
            quckly_tool_bar.setVisibility(View.GONE);
            //  quckly_tool_bar2.setVisibility(View.GONE);
        } else {
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 500.f, 0);
            translateAnimation.setDuration(500);
            // translateAnimation.setRepeatCount(-1);
            //translateAnimation.setRepeatMode(Animation.REVERSE); 
            quckly_tool_bar.startAnimation(translateAnimation);
            quckly_tool_bar.setVisibility(View.VISIBLE);
            //quckly_tool_bar2.setVisibility(View.VISIBLE);
        }
    }

    public void scale(View v) {

        Animation animation = new ScaleAnimation(
            1.0f, 0.98f, 1.0f, 0.98f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        animation.setDuration(200);
        v.clearAnimation();
        v.startAnimation(animation);
    }


    public Context getContext() {
        Context context = this.getApplicationContext();
        return context;
    }

    public void addItemsTo(String diary_text,String diary_image_url) {
        //绑定Layout里面的ListView  
        // ListView list = findViewById(R.id.NoteList);  
        HashMap<String, String> map = new HashMap<String, String>();  
        map.put("diary_DiaryContent", diary_text );

        //使用Date创建日期对象
        String[] date =  format.format(new Date()).split(" ");
        String diary_date = date[0];//.substring(5, 10);
        String diary_time = date[1];//.substring(0, 5);
        String diary_address = " - ";
        map.put("diary_time", diary_time);
        map.put("diary_date", diary_date);
        map.put("diary_image", diary_image_url);
        map.put("diary_address", diary_address);
        listItemAdapter.balistItem.add(0, map);  
        list.invalidateViews(); //listView刷新
        Gson gson = new GsonBuilder().create();
        String content = gson.toJson(listItemAdapter.balistItem);
        new io(filePath).writer(content);
       // DeBug.print(date);
       listItemAdapter.notifyDataSetChanged();
    }

    public void removeItemTo() {
        
        ArrayList< HashMap<String,String>> mapStorage = new ArrayList<HashMap<String,String>>();
        for (int i = 0;i < listItemAdapter.balistItem.size();i++) {
            if(list.isItemChecked(i)){
                //是选中状态时
                mapStorage.add(listItemAdapter.balistItem.get(i));
                if(listItemAdapter.balistItem.get(i).get("diary_image") != null){
                io.del(listItemAdapter.balistItem.get(i).get("diary_image"));
                }
            }
        }
        listItemAdapter.balistItem.removeAll(mapStorage);
        
        listItemAdapter.notifyDataSetChanged();
        list.invalidateViews(); //listView刷新
        textViewOfClickItemCount.setText(String.valueOf(list.getCheckedItemCount()));
        showMultipleSelectionMode(false);
        Gson gson = new GsonBuilder().create();
        String content = gson.toJson(listItemAdapter.balistItem);
        new io(filePath).writer(content);   
        DiaryView.findViewById(R.id.listView_father).setBackgroundResource(R.color.white);


    }

    public void voice(View view) {
          diary_dialog = new NFDialog(
            this, 
            -1, -1, 
            R.layout.diary_dialog,
            R.style.noform_dialog);

        diary_dialog.show();

        //如果要修改Dialog中的某个View,比如把"正在删除..."改为"加载中..."
        //TextView mMessage =  dialog1.findViewById(R.id.message);
        //mMessage.setText("加载中...");

        TextView add_diary_button = diary_dialog.findViewById(R.id.add_diary_button);

        add_diary_button.setOnClickListener(
            new OnClickListener(){
                @Override
                public void onClick(View view) {
                    //toast("???", getContext());
                    EditText  dialog_editText = diary_dialog.findViewById(R.id.dialog_editText);
                    String dairy_text = dialog_editText.getText().toString();
                    /*ImageView dialog_edit_image = diary_dialog.findViewById(R.id.dialog_image);
                    dialog_edit_image.getDrawable();*/
                    if(SystemReturnPath != null){
                        diary_dialog_image_path = getDir("diaryImage",MODE_PRIVATE) + "/" + MD5.MD51(SystemReturnPath) + simple_format.format(new Date()) + ".jpg";
                    //Log.d("#debug",diary_dialog_image_path);
                    ImageUtil.compress(SystemReturnPath,diary_dialog_image_path,10);
                        SystemReturnPath = null;//重置记录返回值
                    }
                    addItemsTo(dairy_text,diary_dialog_image_path);
                    diary_dialog.hide();
                    
                }
            });

        ImageButton diary_add_image_for_system = diary_dialog.findViewById(R.id.diary_add_image_for_system);

        diary_add_image_for_system.setOnClickListener(
                new OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        //调用系统图库：
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_CODE_IMAGE);


                    }
                });
       final TextView diary_dialog_text_count = diary_dialog.findViewById(R.id.dialog_text_count);
       final EditText diary_dialog_edit = diary_dialog.findViewById(R.id.dialog_editText);
        diary_dialog_edit.addTextChangedListener(new TextWatcher() {
                    @Override
                    //文本输入前的状态
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                           
                    }

                    @Override
                    //文本改变时的状态
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        
                    }

                    @Override
                    //文本改变之后的状态
                    public void afterTextChanged(Editable s) {
                        diary_dialog_text_count.setText(diary_dialog_edit.getText().length() + "/249");
                    }
                });
            
            
        }
   



} 

