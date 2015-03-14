/**
 * @author Rafał Zawadzki
 */
public class Point {
    public Long x;
    public Long y;
    public Integer ID;

    public Point(Long x, Long y, Integer ID) {
        this.x = x;
        this.y = y;
        this.ID = ID;
    }

    public Point() {
    }

//    @Override
//    public boolean equals(Object obj) {
//        Point p = (Point) obj;
//        if (p.x.equals(this.x) && p.y.equals(this.y)) {
//            return true;
//        }
//        return false;
//    }

//    @Override
//    public boolean equals(Object obj) {
//        if (!(obj instanceof Point))
//            return false;
//        if (obj == this)
//            return true;
//
//        Point p = (Point) obj;
//        if (p.x.equals(this.x) && p.y.equals(this.y)) {
//            return true;
//        }
//        return false;
//    }
}
