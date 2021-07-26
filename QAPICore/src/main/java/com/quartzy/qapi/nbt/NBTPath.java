package com.quartzy.qapi.nbt;

import com.quartzy.qapi.StringRange;
import com.quartzy.qapi.nbt.list.NBTList;

import java.util.ArrayList;

public class NBTPath{
    public static final char PATH_SEPARATOR = '.';
    public static final char ARRAY_BEGIN = '[';
    public static final char ARRAY_END = ']';
    
    private String src;
    
    public NBTPath(String src){
        this.src = src;
    }
    
    public Object getMatch(NBTBase in){
        Object narrowed = in;
        String[] pathSplit = src.split("\\.");
        for(String currentPath : pathSplit){
            int endIndex = currentPath.indexOf(ARRAY_BEGIN);
            String nameOnly = currentPath.substring(0, endIndex == -1 ? currentPath.length() : endIndex);
            if(narrowed instanceof NBTCompound){
                NBTCompound nbtCompound = (NBTCompound) narrowed;
                NBTBase nbtBase = nbtCompound.get(nameOnly);
                if(nbtBase instanceof NBTList){
                    if(endIndex!=-1){
                        Object o;
                        try{
                            o = traverseList(((NBTList<?>) nbtBase), currentPath.substring(endIndex), src, endIndex);
                        } catch(InvalidPathException e){
                            return new ArrayList<>();
                        }
                        narrowed = o;
                    }else{
                        narrowed = nbtBase;
                    }
                }else{
                    narrowed = nbtBase;
                }
            }else if(narrowed instanceof NBTList){
                if(endIndex!=-1){
                    Object o;
                    try{
                        o = traverseList(((NBTList<?>) narrowed), currentPath.substring(endIndex), src, endIndex);
                    } catch(InvalidPathException e){
                        return new ArrayList<>();
                    }
                    narrowed = o;
                }else{
                    int i;
                    try{
                        i = Integer.parseInt(currentPath);
                        narrowed = ((NBTList<?>) narrowed).get(i);
                    }catch(NumberFormatException ignored){
                    }
                }
            }else{
                return null;
            }
        }
        return narrowed;
    }
    
    public static Object traverseList(NBTList listIn, String path, String fullPath, int offset) throws InvalidPathException{
        int i = -1;
        int endIndex = path.indexOf(ARRAY_END);
        StringRange range = new StringRange(offset + 1, endIndex + offset + 1);
        try{
            i = Integer.parseInt(path.substring(1, endIndex));
        }catch(NumberFormatException e){
            throw new InvalidPathException("Invalid array path: " + path, range, fullPath);
        }
        Object o;
        try{
            o = listIn.get(i);
        }catch(IndexOutOfBoundsException e){
            throw new InvalidPathException("Index is out of bounds (size: " + listIn.size() + ", in: " + i + ")", range, fullPath);
        }
        String newPath = path.substring(endIndex);
        if(o instanceof NBTList && !newPath.isEmpty()){
            return traverseList(((NBTList<?>) o), newPath, fullPath, offset);
        }
        return o;
    }
}
