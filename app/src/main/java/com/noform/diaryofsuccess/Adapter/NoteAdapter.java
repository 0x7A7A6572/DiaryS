package com.noform.diaryofsuccess.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.noform.diaryofsuccess.Object.Note;
import com.noform.diaryofsuccess.R;
import java.util.List;
import com.noform.diaryofsuccess.CustomView.RoundCornersImageView;

import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.CheckBox;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    private List<Note> mNoteList;
    private ArrayList<HashMap<String, Boolean>> item_list;
    private HashMap<String, Boolean> map;
    private Context context;
    
    
  static class ViewHolder extends RecyclerView.ViewHolder{
      TextView NoteContent;
      TextView NoteTime;
      RoundCornersImageView NoteImage;
     // ListView NoteToDoList;
      LinearLayout NoteToDoList;
      public ViewHolder(View view){
          super(view);
          NoteContent = view.findViewById(R.id.note_item_content);
          NoteTime = view.findViewById(R.id.note_item_time);
          NoteImage = view.findViewById(R.id.note_item_image);
          NoteToDoList = view.findViewById(R.id.note_item_listview);
          
      }
  }

  public NoteAdapter(List<Note> NoteList){
      this.mNoteList = NoteList;
  }
  @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
      View view = LayoutInflater.from(parent.getContext())
              .inflate(R.layout.note_item,parent,false);
      ViewHolder holder = new ViewHolder(view);
      context = view.getContext();
      Log.v("#debug","这是Context:" + context.toString());
      return holder;
  }

  @Override
    public void onBindViewHolder(final ViewHolder holder,int position){
        Log.v("#debug","调用了onBindViewHolder这是接收的context" + context.toString());
      Note note = mNoteList.get(position);    
     
      holder.NoteContent.setText(note.getNoteContent());
      holder.NoteTime.setText(note.getNoteTime());
           String data[] = {"aa","bb","cc","dd","aa","bb","cc","dd","aa","bb","cc","dd","aa","bb","cc","dd"};//假数据
          ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,data);//新建并配置ArrayAapeter
      //holder.NoteToDoList.setAdapter(adapter);
      for(int i = 0 ;i < new Random().nextInt(6); i++){
          /*往NoteToDoList中添加CheckBox*/
          CheckBox listCheckBox = new CheckBox(context);
          listCheckBox.setSingleLine(false);
          listCheckBox.setMaxEms(7);
          listCheckBox.setMaxLines(1);
          listCheckBox.setEllipsize(android.text.TextUtils.TruncateAt.valueOf("END"));
          listCheckBox.setText("代办事项" + i + ": xxxx");
           
          
          LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1,-1);
          lp.setMargins(0,5,0,5);
          
          holder.NoteToDoList.addView(listCheckBox,lp);
          
      }
      
    
      /*
      *  解决滑动冲突
      */
      holder.NoteToDoList.findViewById(R.id.note_item_listview).setOnTouchListener(new OnTouchListener() {
              public boolean onTouch(View v, MotionEvent event) {

                  holder.NoteToDoList.findViewById(R.id.note_item_listview).getParent().requestDisallowInterceptTouchEvent(true);
                  return false;
              }
          });
      if(note.isImage()){
          
      }else{
          holder.NoteImage.setVisibility(View.GONE);
      }
  }
  @Override
    public int getItemCount(){
      return mNoteList.size();
  }

}
