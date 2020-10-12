package ru.atom.geometry;

/**
 * Template class for
 */
public class Point implements Collider /* super class and interfaces here if necessary */ {
    public Point(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    // fields
    // and methods
    public int X;
    public int Y;

    public void setX(int x) {
        this.X = x;
    }

    public void setY(int y) {
        this.Y = y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    /**
     * @param o - other object to check equality with
     * @return true if two points are equal and not null.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        // cast from Object to Point
        Point point = (Point) o;

        // your code here
        if (this.X == point.X && this.Y == point.Y) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isColliding(Collider other) {
        return this.equals(other);
    }
}