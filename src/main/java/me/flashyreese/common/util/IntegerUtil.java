package me.flashyreese.common.util;

public class IntegerUtil {

    public static boolean containsIndexFromArray(int[] array, int index){
        for (int i : array){
            if (index == i){
                return true;
            }
        }
        return false;
    }
}
