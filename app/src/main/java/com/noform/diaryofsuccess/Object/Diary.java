package com.noform.diaryofsuccess.Object;
import java.util.HashMap;
public class Diary extends HashMap
{
    private String date;//日期
    private String time;//时间
    private String DiaryContent;//日记内容
    private String address;//日记地址
    private String image_url;
    private int numberOf;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }


    public void setDiaryContent(String diaryContent) {
        DiaryContent = diaryContent;
    }

    public String getDiaryContent() {
        return DiaryContent;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setNumberOf(int numberOf) {
        this.numberOf = numberOf;
    }

    public int getNumberOf() {
        return numberOf;
    }//相同次数

    public void Diary(String date, String time, String diaryContent,String image_url, String address, int numberOf){
        //if(diaryTitle == null){diaryTitle = "null";}
        if(address == null){address = "null";}
        this.date = date;
        this.time = time;
        this.DiaryContent = diaryContent;
        this.image_url = image_url;
        this.address = address;
        this.numberOf = numberOf;
    }

    @Override
    public String toString() {
        String str = null;
        str = getDiaryContent() + getNumberOf()
         + getDate() + getAddress() + getTime();
        return str;
    }
    
     
}
