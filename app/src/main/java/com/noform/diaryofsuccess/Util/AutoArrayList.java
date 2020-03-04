package com.noform.diaryofsuccess.Util;

import java.util.ArrayList;
import java.util.stream.Stream;

public class AutoArrayList extends ArrayList {

	@Override
	public Stream stream() {
		return null;
	}

	@Override
	public Stream parallelStream() {
		return null;
	}

    private int OriginalSize = 0;
    
    public void setOriginalSize(int originalSize) {
        OriginalSize = originalSize;
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
