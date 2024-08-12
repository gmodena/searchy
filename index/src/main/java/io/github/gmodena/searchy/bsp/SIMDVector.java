package io.github.gmodena.searchy.bsp;

public class SIMDVector implements IVector<SIMDVector> {
    @Override
    public float distance(SIMDVector that) {
        return 0;
    }

    @Override
    public float dot(SIMDVector that) {
        return 0;
    }

    @Override
    public float[] raw() {
        return new float[0];
    }

    @Override
    public SIMDVector sub(SIMDVector that) {
        return null;
    }

    @Override
    public SIMDVector mean(SIMDVector that) {
        return null;
    }
}
