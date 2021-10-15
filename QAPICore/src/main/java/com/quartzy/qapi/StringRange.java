package com.quartzy.qapi;

import java.util.Objects;

/**
 * A class representing a range of a string.
 *
 * <p>For example:</p>
 *      <p>
 *          The string range
 *          {@code StringRange(2,4)}
 *
 *      on the string {@code "Hello world!"},
 *      would represent the string {@code "ll"}</p>
 */
public class StringRange {
    private int start;
    private int end;
    
    public StringRange(int start, int end) {
        this.start = start;
        this.end = end;
    }
    
    /**
     * @param start Start index of the string range
     * @param end End index of the string range
     * @return A string range with the specified start and end indexes
     */
    public static StringRange between(int start, int end) {
        return new StringRange(start, end);
    }
    
    /**
     * Create a new string range that includes both string ranges provided and anything in between.
     *
     * @param a The first string range
     * @param b The second string range
     * @return A new string range which includes both of the string ranges
     */
    public static StringRange encompassing(StringRange a, StringRange b) {
        return new StringRange(Math.min(a.getStart(), b.getStart()), Math.max(a.getEnd(), b.getEnd()));
    }
    
    /**
     * Creates a new StringRange whose start and end are the same as the current ones plus the amount specified.
     *
     * @param i Amount to add to the current position
     * @return A new StringRange with the added position
     */
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
    
    /**
     * Apply a string range to a string.
     *
     * @param string The string this string range should be applied to
     * @return The string after the string range has been applied
     */
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
        return Objects.hash(this.start, this.end);
    }
    
    public String toString() {
        return "StringRange{start=" + this.start + ", end=" + this.end + '}';
    }
}
