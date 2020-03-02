package com.noform.diaryofsuccess.Object;
import java.util.ArrayList;

public class Note
{
    private String time;//时间
    private String NoteContent;//便签内容
    private boolean file;//归档
    private boolean image;//显示图片
    private ArrayList<String> NoteToDoList;
  public Note(String time,String NoteContent){
      this.time = time;
      this.NoteContent = NoteContent;
      this.image = false;
  }
  
    public Note(String time,String NoteContent,boolean is){
        this.time = time;
        this.NoteContent = NoteContent;
        this.image = is;
    }

    public void setNoteToDoList(ArrayList<String> noteToDoList) {
        NoteToDoList = noteToDoList;
    }

    public ArrayList<String> getNoteToDoList() {
        return NoteToDoList;
    }

    public boolean isImage() {
        return image;
    }

  public String getNoteContent(){
      return this.NoteContent;
  }

    public String getNoteTime() {
      return this.time;
    }
}
