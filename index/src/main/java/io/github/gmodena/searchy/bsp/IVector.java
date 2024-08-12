package io.github.gmodena.searchy.bsp;

public interface IVector<T extends IVector<T>> {
    float distance(T that);

    float dot(T that);

    float[] raw();

    T sub(T that);

    T mean(T that);
}
