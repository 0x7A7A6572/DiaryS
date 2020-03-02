package com.noform.diaryofsuccess.Util;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.noform.diaryofsuccess.MainActivity;
import com.noform.diaryofsuccess.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import android.os.Environment;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import android.util.Log;
import android.media.ExifInterface;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;

public class ImageUtil {

   
    //public static final String TAG = getClass().toString();

    /**
     * 加载本地图片
     * @param url
     * @return
     */

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从服务器取图片
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    
    /**
     * 
     * @param inSampleSize  可以根据需求计算出合理的inSampleSize
     */
    public static void compress(String path,String toPath,int inSampleSize) {
        File originFile = new File(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置此参数是仅仅读取图片的宽高到options中，不会将整张图片读到内存中，防止oom
        options.inJustDecodeBounds = true;
        Bitmap emptyBitmap = BitmapFactory.decodeFile(originFile.getAbsolutePath(), options);
        
        //获取图片的宽高
        int height = options.outHeight;
        int width = options.outWidth;
        
        //为了不让图片压缩得太小
        if(height/inSampleSize < 800 ){
            inSampleSize = height/800;
        }else if(width/inSampleSize < 800){
            inSampleSize = width/800;
        }

        Log.i("#debug","图片的宽度:"+width+"图片的高度:"+height);
        
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
       // Bitmap resultBitmap = BitmapFactory.decodeFile(originFile.getAbsolutePath(), options);
        Bitmap resultBitmap = loadBitmap(originFile.getAbsolutePath(),true,options); //BitmapFactory.decodeFile(originFile.getAbsolutePath(), options);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        try {
            FileOutputStream fos = new FileOutputStream(new File(toPath));
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
        /** 从给定路径加载图片*/
    public static Bitmap loadBitmap(String imgpath,BitmapFactory.Options options) {
          
            return BitmapFactory.decodeFile(imgpath,options);
        }


        /** 从给定的路径加载图片，并指定是否自动旋转方向*/
    public static Bitmap loadBitmap(String imgpath, boolean adjustOritation,BitmapFactory.Options options) {
            if (!adjustOritation) {
                return loadBitmap(imgpath,options);
            } else {
                Bitmap bm = loadBitmap(imgpath,options);
                int digree = 0;
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(imgpath);
                } catch (IOException e) {
                    e.printStackTrace();
                    exif = null;
                }
                if (exif != null) {
                    // 读取图片中相机方向信息
                    int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                                   ExifInterface.ORIENTATION_UNDEFINED);
                    // 计算旋转角度
                    switch (ori) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            digree = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            digree = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            digree = 270;
                            break;
                        default:
                            digree = 0;
                            break;
                    }
                }
                if (digree != 0) {
                    // 旋转图片
                    Matrix m = new Matrix();
                    m.postRotate(digree);
                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                                             bm.getHeight(), m, true);
                }
                return bm;
            }
        }

 


}
