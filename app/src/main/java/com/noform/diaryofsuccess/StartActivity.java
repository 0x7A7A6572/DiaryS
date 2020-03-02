package com.noform.diaryofsuccess;

    import android.app.ActionBar;
    import android.app.Activity;
    import android.content.Intent;
    import android.graphics.Color;
    import android.os.Build;
    import android.os.Bundle;
    import android.os.Handler;
    import android.os.Message;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.Window;
    import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

    /**
     * 启动页面
     */
public class StartActivity extends AppCompatActivity {

        private static final int WHAT_DELAY = 0x11;// 启动页的延时跳转
        private static final int DELAY_TIME = 200;// 延时时间

        // 创建Handler对象，处理接收的消息
        private Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case WHAT_DELAY:// 延时1秒跳转
                        goHome();
                        break;
                }
            }
        };
    

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start);
            // 调用handler的sendEmptyMessageDelayed方法
          handler.sendEmptyMessageDelayed(WHAT_DELAY, DELAY_TIME);         
        }

        /**
         * 跳转到主页面
         */
        private void goHome() {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();// 销毁当前活动界面
        }
}
