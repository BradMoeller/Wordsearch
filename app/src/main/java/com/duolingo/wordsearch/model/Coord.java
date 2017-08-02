package com.duolingo.wordsearch.model;


import java.io.PrintWriter;

/**
 * Created by brad on 8/1/17.
 */

public class Coord {
    public int x;
    public int y;

    public Coord() {}

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord(Coord src) {
        this.x = src.x;
        this.y = src.y;
    }

    /**
     * Set the point's x and y coordinates
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Negate the point's coordinates
     */
    public final void negate() {
        x = -x;
        y = -y;
    }

    /**
     * Offset the point's coordinates by dx, dy
     */
    public final void offset(int dx, int dy) {
        x += dx;
        y += dy;
    }

    /**
     * Returns true if the point's coordinates equal (x,y)
     */
    public final boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coord coord = (Coord) o;

        if (x != coord.x) return false;
        if (y != coord.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Coord(" + x + ", " + y + ")";
    }

    /** @hide */
    public void printShortString(PrintWriter pw) {
        pw.print("["); pw.print(x); pw.print(","); pw.print(y); pw.print("]");
    }
}
