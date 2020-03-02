package com.noform.diaryofsuccess;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.noform.diaryofsuccess.Util.ImageUtil;
import android.view.Window;
import android.view.WindowManager;
import android.content.Intent;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.util.Log;

public class ImageShowActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags
        (
            WindowManager.LayoutParams.FLAG_FULLSCREEN,  
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.image_show_acticity);
        Intent it=this.getIntent();
        String thisPath=it.getStringExtra("image_path");
        if(thisPath==null)
        {
            Toast.makeText(ImageShowActivity.this,"打开图片预览失败",Toast.LENGTH_SHORT).show();
        }
        
       ImageView image =  findViewById(R.id.image_show_activity_view);
       image.setImageBitmap(ImageUtil.getLoacalBitmap(thisPath));
       
       findViewById(R.id.click_image_show_close).setOnClickListener(new OnClickListener(){
           @Override
           public void onClick(View v){
               finish();
           }
           
       });
       
       /*
       *  双手缩放
       *
        //绑定控件
        ImageView imgfire=findViewById(R.id.click_image_show_close);

//触摸监听事件
        imgfire.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.e("xxx","按下");
                            break;
                        case MotionEvent.ACTION_UP:
                            Log.e("xxx","抬起");
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.e("xxx","移动");
                            break;
                    }
                    return true;
                }
            });
        */
            
    }
    
   
    
    
}
