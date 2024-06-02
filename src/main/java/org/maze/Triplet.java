package org.maze;

public class Triplet<U, D, T> {
    public final U first;
    public final D second;
    public final T third;

    public Triplet(U first, D second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public U getFirst() {return first; }
    public D getSecond() {return second; }
    public T getThird() {return third; }
}
