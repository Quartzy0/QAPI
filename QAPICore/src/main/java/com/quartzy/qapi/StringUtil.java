package com.quartzy.qapi;

public class StringUtil{
    public static int indexOf(String s, int offset, int count){
        int j = 0;
        for(int i = offset; i < s.length(); i++){
            if(s.charAt(i) == ' '){
                j++;
            }
            if(j==count){
                return i;
            }
        }
        return j==count-1 ? s.length() : -1;
    }
    
    public static int indexOf(String s, int offset, int count, char c){
        int j = 0;
        for(int i = offset; i < s.length(); i++){
            if(s.charAt(i) == c){
                j++;
            }
            if(j==count){
                return i;
            }
        }
        return j==count-1 ? s.length() : -1;
    }
    
    private static int indexOf(String s, int offset){
        return indexOf(s, offset, 1);
    }
}
