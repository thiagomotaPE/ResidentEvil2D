package com.studio.world;

public class Camera {
    public static int x = 10;
    public static int y = 10;

    public static int clamp(int Atual, int Min, int Max) {
        if(Atual < Min) {
            Atual = Min;
        }
        if(Atual > Max) {
            Atual = Max;
        }
        return Atual;
    }
}
