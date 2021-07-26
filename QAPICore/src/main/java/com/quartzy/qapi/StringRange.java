package com.quartzy.qapi;

import java.util.Objects;

public class StringRange {
    private int start;
    private int end;
    
    public StringRange(int start, int end) {
        this.start = start;
        this.end = end;
    }
    
    public static StringRange at(int pos) {
        return new StringRange(pos, pos);
    }
    
    public static StringRange between(int start, int end) {
        return new StringRange(start, end);
    }
    
    public static StringRange encompassing(StringRange a, StringRange b) {
        return new StringRange(Math.min(a.getStart(), b.getStart()), Math.max(a.getEnd(), b.getEnd()));
    }
    
    public StringRange add(int i){
        return between(this.start+i, this.end+i);
    }
    
    public int getStart() {
        return this.start;
    }
    
    public int getEnd() {
        return this.end;
    }
    
    public void setStart(int start){
        this.start = start;
    }
    
    public void setEnd(int end){
        this.end = end;
    }
    
    public String getTrim(String string) {
        return string.substring(this.start, this.end).trim();
    }
    
    public String get(String string) {
        return string.substring(this.start, this.end);
    }
    
    public boolean isEmpty() {
        return this.start == this.end;
    }
    
    public int getLength() {
        return this.end - this.start;
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof StringRange)) {
            return false;
        } else {
            StringRange that = (StringRange)o;
            return this.start == that.start && this.end == that.end;
        }
    }
    
    public int hashCode() {
        return Objects.hash(new Object[]{this.start, this.end});
    }
    
    public String toString() {
        return "StringRange{start=" + this.start + ", end=" + this.end + '}';
    }
}
