package com.noform.diaryofsuccess.Adapter;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.noform.diaryofsuccess.MainActivity;
import com.noform.diaryofsuccess.R;
import com.noform.diaryofsuccess.Util.ImageUtil;
import android.view.View.OnClickListener;
import android.content.Intent;
import com.noform.diaryofsuccess.StartActivity;
import com.noform.diaryofsuccess.ImageShowActivity;
import android.view.View.OnLongClickListener;


public class DiaryAdapter extends BaseAdapter {
    public ArrayList<HashMap<String, String>> balistItem = new ArrayList<HashMap<String, String>>();


    private String TAG = this.getClass().toString();

    @Override
    public int getCount() {
        return balistItem.size();
    }
    
    @Override
    public long getItemId(int p1) {
        return p1;
    }

    @Override
    public Object getItem(int position) {
        return balistItem.get(position);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        /*  Log.v("#debug-getView","position: " + position 
         + "  convertView: " + convertView
         );*/
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(MainActivity.CONTEXT, R.layout.diary_item, null);
            viewHolder.diary_DiaryContent = convertView.findViewById(R.id.diary_DiaryContent);
            viewHolder.diary_time = convertView.findViewById(R.id.diary_time);
            viewHolder.diary_date = convertView.findViewById(R.id.diary_date);
            viewHolder.diary_image = convertView.findViewById(R.id.diary_image);
            viewHolder.diary_address = convertView.findViewById(R.id.diary_address);
            convertView.setTag(viewHolder);
            
            //DeBug.print("用了getView而且convertView=null");
        } else {
           // DeBug.print("但是不等于null");
            viewHolder = (ViewHolder)convertView.getTag();
        }
        

        
        viewHolder.diary_DiaryContent.setText(balistItem.get(position).get("diary_DiaryContent"));
        viewHolder.diary_time.setText(balistItem.get(position).get("diary_time"));
        viewHolder.diary_date.setText(balistItem.get(position).get("diary_date"));
        if(balistItem.get(position).get("diary_image") != "null" && balistItem.get(position).get("diary_image") != null){
            Log.d(TAG,"balistItem.get(position).get(\"diary_image\"):" + balistItem.get(position).get("diary_image"));
            viewHolder.diary_image.setImageBitmap(ImageUtil.getLoacalBitmap(balistItem.get(position).get("diary_image")));
            viewHolder.diary_image.setVisibility(View.VISIBLE);
            viewHolder.diary_image.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v){
                    //进入多选模式时屏蔽单机事件
                    if(!MainActivity.isMultipleSelectionMode){
                    Intent image_show = new Intent(MainActivity.CONTEXT, ImageShowActivity.class);
                    image_show.putExtra("image_path",balistItem.get(position).get("diary_image"));
                    image_show.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
                    v.getContext().startActivity(image_show);
                    }
                }

           });
           //屏蔽长按事件
           viewHolder.diary_image.setOnLongClickListener(new OnLongClickListener(){
               @Override
               public boolean onLongClick(View v){
                   return false;
               }
           });
        }else{
            viewHolder.diary_image.setVisibility(View.GONE);
        }
        viewHolder.diary_address.setText(balistItem.get(position).get("diary_address"));
                
        GradientDrawable note_list_shape = (GradientDrawable)convertView.findViewById(R.id.diary_content_background).getBackground();


        //判断position位置是否被选中，改变颜色
        if (MainActivity.list.isItemChecked(position) && MainActivity.isMultipleSelectionMode) {
            note_list_shape.setColor(0xffff521d);
            convertView.findViewById(R.id.diary_triangle).setBackgroundResource(R.color.lightorange);
          //  Log.d(TAG,"getView被调用！切当前position:" + position +"为true");
            } else {
            note_list_shape.setColor(0xff1E90FF);
            convertView.findViewById(R.id.diary_triangle).setBackgroundResource(R.color.dodgerblue);
    
        }

        return convertView;
    }


    /*  public DiaryAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {

     super(context, data, resource, from, to);
     //Collections.fill(selectionItem, Boolean.TRUE);
     // TODO Auto-generated constructor stub

     }    */
    public DiaryAdapter(List<? extends Map<String, ?>> data) {

        this.balistItem = (ArrayList<HashMap<String, String>>) data;
        //Collections.fill(selectionItem, Boolean.TRUE);
        // TODO Auto-generated constructor stub

    }    
    private static class ViewHolder {
        TextView diary_DiaryContent;
        TextView diary_time;
        TextView diary_date;
        ImageView diary_image;
        TextView diary_address;

        public static ViewHolder newsInstance(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();

            if (holder == null) {
                holder = new ViewHolder();


                holder.diary_DiaryContent = convertView.findViewById(R.id.diary_DiaryContent);
                holder.diary_time = convertView.findViewById(R.id.diary_time);
                holder.diary_date = convertView.findViewById(R.id.diary_date);
                holder.diary_image = convertView.findViewById(R.id.diary_image);
                holder.diary_address = convertView.findViewById(R.id.diary_address);

                convertView.setTag(holder);
            }

            return holder;
        }

    }
}

