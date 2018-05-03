package com.example.pengfeisong.videochatdemo.model;

/**
 * Created by pengfeisong on 4/30/18.
 */

public class FacialInfo {
    private float[] joys;
    private int joyNum;
    public FacialInfo(int size) {
        joys = new float[size];
        joyNum = 0;
    }

    public void addJoy(float jl) {
        if(joyNum != joys.length) {
            joys[joyNum++] = jl;
        } else {
            joys = shrink(joys);
            joyNum /= 2;
            joys[joyNum++] = jl;
        }
    }

    private float[] shrink(float[] emo) {
        float[] res = new float[emo.length];
        int j = 0;
        for(int i = 0; i < emo.length; i += 2) {
            res[j++] = emo[i];
        }
        return res;
    }

    public float[] getJoyData() {
        return joys;
    }
}
