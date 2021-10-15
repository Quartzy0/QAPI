package com.quartzy.qapi;

public class StringUtil{
    /**
     * Finds the 'count' occurrence of the search query " ".
     *
     * @param s Input string to be searched
     * @param offset Offset from the start of the input string
     * @param count Number of occurrences before the index is returned
     * @return The index of the 'count' occurrence of the search query " "
     */
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
    
    /**
     * Finds the 'count' occurrence of the search query c.
     *
     * @param s Input string to be searched
     * @param offset Offset from the start of the input string
     * @param count Number of occurrences before the index is returned
     * @param c The character to look for
     * @return The index of the 'count' occurrence of the search query c
     */
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
    
    /**
     * Finds the first occurrence of the character " ".
     *
     * @param s Input string to be searched
     * @param offset Offset from the start of the input string
     * @return The index of the first occurrence of the search query " ".
     */
    public static int indexOf(String s, int offset){
        return indexOf(s, offset, 1);
    }
}
