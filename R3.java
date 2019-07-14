package mathutil ;
import Jama.* ;

/**
R3

*/
public class R3  extends Object 
{

    protected double x = 0.0 ;
    protected double y = 0.0 ;
    protected double z = 0.0 ;

    public R3() { }
    public R3(R3 src)
    {
        this.x = src.x ;
        this.y = src.y ;
        this.z = src.z ;
    }
    public R3(double x, double y, double z)
    {
        this.x = x ;
        this.y = y ;
        this.z = z ;
    }
    public R3(double[] v)
    {
        x = v[0] ;
        y = v[1] ;
        z = v[2] ;
    }
    public void setX (double x) { this.x = x ; }
    public void setY (double y) { this.y = y ; }
    public void setZ (double z) { this.z = z ; }
    public void set (double x, double y, double z)
    {
        this.x = x ;
        this.y = y ;
        this.z = z ;
    }
    public void set (R3 src)
    {
        this.x = src.x ;
        this.y = src.y ;
        this.z = src.z ;
    }

    public double getX() { return x ; }
    public double getY() { return y ; }
    public double getZ() { return z ; }

    public R3 timesEquals (double s) 
    {
        x *= s ;
        y *= s ;
        z *= s ;
        return this ;
    }
    public R3 plus(R3 r)
    {
        R3 result = new R3 (this.x+r.x, this.y+r.y, this.z+r.z) ;
        return result ;
    }
    public R3 minus(R3 r)
    {
        R3 result = new R3 (this.x-r.x, this.y-r.y, this.z-r.z) ;
        return result ;
    }

    /** return R3n length 1 which is cartesian version of given theta, 
    * phi in radians.  Assume theta is angle of point from x toward y, 
    * phi is angle from xy plane toward z, radius is 1
    * */
    public static R3 polarToCartesian (double theta, double phi)
    {
        double r = Math.cos(phi) ;
        double z = Math.sin(phi) ;
        double x = r*Math.cos(theta) ;
        double y = r*Math.sin(theta) ;
        return new R3 (x, y, z) ;
    }

    // public static WHAT? cartesianToPolar
    // convenience - first vector only
    public static double theta (R3 v) 
    { 
        return Math.atan2 (v.y, v.x) ; 
    }
    public static double phi (R3 v) 
    { 
        return Math.atan2 (v.z, Math.sqrt(v.x*v.x+v.y*v.y)) ; 
    }
    private static final double rtod = 180.0 / Math.PI ;
    public static double latitude (R3 v) { return rtod * phi(v) ; }
    public static double longitude (R3 v) { return rtod * theta(v) ; }

    public double vabs () { return Math.sqrt(x*x+y*y+z*z) ; }


    public String toString() 
    { 
        String s = "{" + x + "," + y + "," + z + "}" ;
        return s ;
    }
}

