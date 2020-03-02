package com.noform.diaryofsuccess.Util;

import java.util.ArrayList;
import java.util.stream.Stream;

public class AutoArrayList extends ArrayList {
    private int OriginalSize = 0;
    
    public void setOriginalSize(int originalSize) {
        OriginalSize = originalSize;
    }
    
    @Override
    public Stream stream() {
        return this.stream();
    }

    @Override
    public Stream parallelStream() {
        return this.parallelStream();
    }
    

    
    
    @Override
    public Object get(int index) {
        
        setOriginalSize(size());
        if(OriginalSize < index) {
           // System.out.println("size:" + size() + "大于index:" + index);
            for(int i = 0;i <= (index - OriginalSize);i++){
                add("null");
            }
        }
        return super.get(index);
    }

    @Override
    public void add(int index, Object element) {
        setOriginalSize(size());
        if(OriginalSize < index) {
            // System.out.println("size:" + size() + "大于index:" + index);
            for(int i = 0;i <= (index - OriginalSize);i++){
                add("null");
            }
        }
        super.add(index, element);
        
    }
    
    
   
     
}
