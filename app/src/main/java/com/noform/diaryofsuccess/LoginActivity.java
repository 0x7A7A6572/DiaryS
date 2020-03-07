package com.noform.diaryofsuccess;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.noform.diaryofsuccess.CustomView.NFDialog;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import android.widget.EditText;
import android.graphics.drawable.GradientDrawable;

public class LoginActivity extends AppCompatActivity {
    Context CONTEXT;
    private Tencent mTencent;
    NFDialog userAgreement_dialog;

    String token;
    String expires_in;
    String uniqueCode;
    //授权登录监听（最下面是返回结果）
    private IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            uniqueCode = ((JSONObject) o).optString("openid"); //QQ的openid
            try {
                token = ((JSONObject) o).getString("access_token");
                expires_in = ((JSONObject) o).getString("expires_in");

                //在这里直接可以处理登录
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
        }

        @Override
        public void onCancel() {
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();

        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        //设置状态栏背景色
        window.setStatusBarColor(0xffFFA500);
        CONTEXT = this.getApplicationContext();
        inti();
        mTencent = Tencent.createInstance("101854186", CONTEXT);
        ImageButton im_qq = findViewById(R.id.im_qq);
        im_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //QQ登录
                if (null == mTencent) {
                    mTencent = Tencent.createInstance("101854186", CONTEXT);

                }
                if (!mTencent.isSessionValid()) {
                    mTencent.login(LoginActivity.this, "all", loginListener);
                }
            }
        });

    }

    private void inti() {
        final RelativeLayout login_bg = findViewById(R.id.login_background);
        EditText et_name = findViewById(R.id.et_username);
        EditText et_psw = findViewById(R.id.et_password);
        Button btn_login = findViewById(R.id.btn_login);
        //初始化布局
        GradientDrawable loginShape = (GradientDrawable)btn_login.getBackground();
        //GradientDrawable nameShape = (GradientDrawable)et_name.getBackground();
        //GradientDrawable pswShape = (GradientDrawable)et_psw.getBackground();
        //nameShape.setColor(0x99cc7623);
        //pswShape.setColor(0x7E0377cc);
        loginShape.setColor(0xfffcfcfc);
        
       final TextView userAgreement = findViewById(R.id.UserAgreement);
        userAgreement_dialog = new NFDialog(
                this,
                -1, -1,
                R.layout.user_agreement_dialog,
                R.style.noform_dialog,false);
       userAgreement.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {  
               userAgreement_dialog.show();
            
           
           }
       });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });



    }
    
    public void closeAgreementDialog(View v){
        userAgreement_dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE ||
                    resultCode == Constants.REQUEST_QZONE_SHARE ||
                    resultCode == Constants.REQUEST_OLD_SHARE) {
                mTencent.handleResultData(data, loginListener);
            }
        }
    }

}
