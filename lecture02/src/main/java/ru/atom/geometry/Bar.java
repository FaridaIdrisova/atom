package ru.atom.geometry;

public class Bar implements Collider {
    private Point point1;

    private Point point2;

    private boolean isPointInsideBar(Point point, Bar bar) {
        return point.getX() >= bar.point1.getX()
                && point.getY() >= bar.point1.getY()
                && point.getX() <= bar.point2.getX()
                && point.getY() <= bar.point2.getY();
    }

    private void setupPoints() {
        if (this.point1.getX() > this.point2.getX()) {
            int tmp = this.point1.getX();
            this.point1.setX(this.point2.getX());
            this.point2.setX(tmp);
        }

        if (this.point1.getY() > this.point2.getY()) {
            int tmp = this.point1.getY();
            this.point1.setY(this.point2.getY());
            this.point2.setY(tmp);
        }
    }

    public Bar(int xTop, int yTop, int xBottom, int yBottom) {
        this.point1 = new Point(xTop, yTop);
        this.point2 = new Point(xBottom, yBottom);

        this.setupPoints();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Bar) {
            Bar bar = (Bar) other;
            Point otherPoint1 = new Point(bar.point2.getX(), bar.point1.getY());
            Point otherPoint2 = new Point(bar.point1.getX(), bar.point2.getY());

            if (this.isPointInsideBar(bar.point1, this)
                    || this.isPointInsideBar(bar.point2, this)
                    || this.isPointInsideBar(otherPoint1, this)
                    || this.isPointInsideBar(otherPoint2, this)) {
                return true;
            } else return bar.point2.getX() <= this.point2.getX()
                    && bar.point1.getX() >= this.point1.getX()
                    && bar.point2.getY() >= this.point2.getY()
                    && bar.point1.getY() <= this.point1.getY()
                    || bar.point2.getY() <= this.point2.getY()
                    && bar.point1.getY() >= this.point1.getY()
                    && bar.point2.getX() >= this.point2.getX()
                    && bar.point1.getX() <= this.point1.getX();
        } else if (other instanceof Point) {
            Point otherPoint = (Point) other;
            return this.isPointInsideBar(otherPoint, this);
        }

        return false;
    }

    @Override
    public boolean isColliding(Collider other) {
        return this.equals(other);
    }
}