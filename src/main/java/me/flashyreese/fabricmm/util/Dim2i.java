package me.flashyreese.fabricmm.util;

public class Dim2i {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    public Dim2i(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getOriginX() {
        return this.x;
    }

    public int getOriginY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getLimitX(){
        return this.x + this.width;
    }

    public int getLimitY(){
        return this.y + this.height;
    }
}
