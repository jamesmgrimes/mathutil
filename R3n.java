package mathutil ;
import Jama.* ;

/**
    R3n

*/
public class R3n  extends Object 
{

    protected double [][] vectors = null ;    

    public R3n(int n) { vectors = new double[n][3] ; }
    public R3n(double [][] invectors)
    {
        vectors = new double [invectors.length][3] ; 
        for (int i = 0 ; i < invectors.length ; i++)
        {
            vectors[i][0] = invectors[i][0]; 
            vectors[i][1] = invectors[i][1]; 
            vectors[i][2] = invectors[i][2]; 
        }
    }
    public R3n(double x, double y, double z)
    {
        vectors = new double [1][3] ;
        vectors[0][0] = x;
        vectors[0][1] = y;
        vectors[0][2] = z;
    }

    public void set (int i, R3 insert)
    {
        vectors[i][0] = insert.x ;
        vectors[i][1] = insert.y ;
        vectors[i][2] = insert.z ;
    }

    public void set (int i, R3n insert)
    {
        for (int j = i ; j < i+insert.vectors.length ; j++)
            set (j, insert.vectors[j-i]) ;
    }

    public void set (int i, double[] v) 
    { 
        vectors[i][0] = v[0]; 
        vectors[i][1] = v[1]; 
        vectors[i][2] = v[2]; 
    }

    public void set (int i, double x, double y, double z)
    {
        vectors[i][0] = x ;
        vectors[i][1] = y ;
        vectors[i][2] = z ;
    }

    public void setX (int i, double x) { vectors[i][0] = x ; }
    public void setY (int i, double y) { vectors[i][1] = y ; }
    public void setZ (int i, double z) { vectors[i][2] = z ; }
    public int size() { return vectors.length ; }
    public double getX(int i) { return vectors[i][0] ; }
    public double getY(int i) { return vectors[i][1] ; }
    public double getZ(int i) { return vectors[i][2] ; }
    public R3 get (int i) 
    { 
        return new R3(vectors[i][0], vectors[i][1], vectors[i][2]) ;
    }

    public R3n timesEquals (double s) 
    {
        int n = size() ;
        for (int i = 0 ; i < n ; i++)
        {
            vectors[i][0] *= s ;
            vectors[i][1] *= s ;
            vectors[i][2] *= s ;
        }
        return this ;
    }

    public R3 sum ()
    {
        int n = size() ;
        double x = 0.0 ;
        double y = 0.0 ;
        double z = 0.0 ;
        for (int i = 0 ; i < n ; i++)
        {
            x += vectors[i][0] ;
            y += vectors[i][1] ;
            z += vectors[i][2] ;
        }
        R3 result = new R3(x, y, z) ;
        return result ;
    }

    public R3 mean ()
    {
        R3 result = this.sum() ;
        int n = this.size() ;
        result.timesEquals (1.0/n) ;
        return result ;
    }

    /** return R3n length 1 which is cartesian version of given theta, 
    * phi in radians.  Assume theta is angle of point from x toward y, 
    * phi is angle from xy plane toward z, radius is 1
    * */
    public static R3n polarToCartesian (double theta, double phi)
    {
        double r = Math.cos(phi) ;
        double z = Math.sin(phi) ;
        double x = r*Math.cos(theta) ;
        double y = r*Math.sin(theta) ;
        return new R3n (x, y, z) ;
    }

    // public static WHAT? cartesianToPolar
    // convenience - first vector only
    public static double theta (R3n v0) 
    { 
        double x = v0.vectors[0][0] ; 
        double y = v0.vectors[0][1] ; 
        return Math.atan2 (y, x) ; 
    }
    public static double phi (R3n v0) 
    { 
        double x = v0.vectors[0][0] ; 
        double y = v0.vectors[0][1] ; 
        double z = v0.vectors[0][2] ; 
        return Math.atan2 (z, Math.sqrt(x*x+y*y)) ; 
    }
    private static final double rtod = 180.0 / Math.PI ;
    private static final double dtor = Math.PI / 180.0 ;
    public static double latitude (R3n v0) { return rtod * phi(v0) ; }
    public static double longitude (R3n v0) { return rtod * theta(v0) ; }

    public String toString() 
    { 
        String s = "{" ;
        for (int row = 0 ; row < vectors.length ; row++) 
        {
            s += "{" 
            + vectors[row][0] + "," 
            + vectors[row][1] + "," 
            + vectors[row][2] + "}" ;
            if (row < vectors.length-1) s += "," ;
        }
        s += "}" ;

        return s ;
    }

    /* hermite calculates n steps, which returns n+1 positions */
    public R3n hermite (int n)
    {
        double [][] Tnx4 = new double[n+1][4] ;  // for efficiency, could 
                             // make a new hermite class 
                             // and have Tnx4 be a data 
                             // member 

        for (int i = 0 ; i <= n ; i++)
        {
            double t = (double)(i)/n ;
            double t2 = t*t ;
            double t3 = t2*t ;
            Tnx4[i][0] = t3 ;
            Tnx4[i][1] = t2 ;
            Tnx4[i][2] = t ;
            Tnx4[i][3] = 1.0 ;
        }
        Matrix T = new Matrix (Tnx4) ;
        double [][] m = 
            {
                { 2.0, -2.0,  1.0,  1.0},
                {-3.0,  3.0, -2.0, -1.0},
                { 0.0,  0.0,  1.0,  0.0},
                { 1.0,  0.0,  0.0,  0.0} 
            } ;
        Matrix M = new Matrix (m) ;
        Matrix PR = new Matrix (this.vectors) ;
        Matrix MPR = M.times(PR) ;
        Matrix R = T.times (MPR) ;

        return new R3n(R.getArray()) ;  // getArray should avoid making copy
    }

    private static R3 findtan3 (R3 pi, R3 pj, R3 pk)
    {
        R3 ij = pj.minus(pi) ;
        R3 jk = pk.minus(pj) ;
        double ijabs = ij.vabs() ;
        double jkabs = jk.vabs() ;
        ij.timesEquals (1./ijabs) ;
        jk.timesEquals (1./jkabs) ;  // if we don't need ijabs otherwise, 
                 // could use a "normalize" method 
        ////////////////////////////////////////////////////////////////////
        // the direction of the tangent will be the sum of the ij and jk. //
        // Never mind length.  If they're colinear we'll get a vector of  //
        // length zero, or really small, or length 2, or really close to  //
        // that.  But what then? - did something                          //
        ////////////////////////////////////////////////////////////////////
        R3 ijk = ij.plus(jk) ;
        double ijkabs = ijk.vabs() ;
        double f = (ijabs + jkabs) / 2.0 / ijkabs ; // could be trouble here
        // was ijk.timesEquals (1./ijkabs) ; and didn't work - tangents too long
        // this is what was in the perl version:
        ijk.timesEquals(f) ;   // so why did we switch?
        return ijk ;
    }

    private static final double Rearth = 6371.0 ;
    public static double distHaversine (double lat1, double lon1, 
            double lat2, double lon2)
    {
        lat1 *= dtor ;
        lat2 *= dtor ;
        lon1 *= dtor ;
        lon2 *= dtor ;
        double dlat = lat2-lat1 ;
        double dlon = lon2-lon1 ;
        double sindlato2 = Math.sin (dlat/2.0) ;
        double sindlono2 = Math.sin (dlon/2.0) ;
        double a = sindlato2*sindlato2 
            + Math.cos(lat1)*Math.cos(lat2)*sindlono2*sindlono2 ;
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0-a)) ;
        double d = Rearth * c ;
        return d ;
    }

    public void qdsort()
    {
        int n = this.size() ;
        for (int i = 0 ; i < n-1 ; i++)
        {
            double lon1 = this.getX(i) ;
            double lat1 = this.getY(i) ;
            double mindist = 1000000.0 ;
            int minindex = 0 ;
            int j ;
            for (j = i+1 ; j < n ; j++)
            {
                double lon2 = this.getX(j) ;
                double lat2 = this.getY(j) ;
                double d = distHaversine(lat1, lon1, lat2, lon2) ;
                if (d < mindist) { mindist = d ; minindex = j ; }
            }
            j = i+1 ;
            if ( j != minindex )
            {
                double tmplon = this.getX(j) ;
                double tmplat = this.getY(j) ;
                this.setX(j, this.getX(minindex)) ;
                this.setY(j, this.getY(minindex)) ;
                this.setX(minindex, tmplon) ;
                this.setY(minindex, tmplat) ;
            }
        }
    }





    public R3n hermiteInterpolate (int n)
    {
        int nlla = this.size() ;
        if (nlla < 2) return null ;  // don't bother if too small
        Hermite hermiten = new Hermite(n) ;
        R3n path = null ;
        R3n totalpath = new R3n(n*(nlla-1)+1) ;
        R3n PR = new R3n(4) ;
        for (int j = 0 ; j < nlla ; j++)
        {
            R3 llai = null ;
            R3 llaj = null ;
            R3 llak = null ;
            R3 llal = null ;
            if (j-1 >= 0) llai = this.get(j-1) ;
            llaj = this.get(j) ;
            if (j+1 < nlla) llak = this.get(j+1) ;
            if (j+2 < nlla) llal = this.get(j+2) ;
            boolean islast = false ;   // needed?
            R3 plast = null ;

            R3 rj = null ;
            R3 rk = null ;
            if (llai != null && llak != null) rj = findtan3(llai, llaj, llak) ;
            if (llak != null && llal != null) rk = findtan3(llaj, llak, llal) ;

            // first, four are define
            if (llai != null && llak != null && llal != null)
            {
                rj = findtan3 (llai, llaj, llak) ;
                rk = findtan3 (llaj, llak, llal) ;
                PR.set(0, llaj) ; PR.set(1, llak) ;
                PR.set(2, rj) ; PR.set(3, rk) ;
                // System.out.println (PR) ;
                path = hermiten.hermite(PR) ;
            }
            //  Now maybe there are ehrte defined
            else if (llai != null && llak != null) // we have i, j, k but not l
            {
                rj = findtan3(llai, llaj, llak) ;
                rk = llak.minus(llaj) ;
                PR.set(0, llaj) ; PR.set(1, llak) ;
                PR.set(2, rj) ; PR.set(3, rk) ;
                path = hermiten.hermite(PR) ;
            }
            else if (llak != null && llal != null) // we have j, k, l but not i
            {
                rj = llak.minus(llaj) ;
                rk = findtan3 (llaj, llak, llal) ;
                PR.set(0, llaj) ; PR.set(1, llak) ;
                PR.set(2, rj) ; PR.set(3, rk) ;
                path = hermiten.hermite(PR) ;
            }
            // no, two are defined: - can only be i or k, not l, because j
            // is defined and the defined points have to be contiguous
            else if (llak != null) // we have j and k
            {
                rj = llak.minus(llaj) ;
                rk = rj ;
                PR.set(0, llaj) ; PR.set(1, llak) ;
                PR.set(2, rj) ; PR.set(3, rk) ;
                path = hermiten.hermite(PR) ;
            }
            else if (llai != null) // then there are only two  - i, and j
            {
                // oops, in this case we only put one on the stack...
                islast = true ;
                plast = llaj  ;
            }
            else
            {
                System.err.println (" HUH only one defined - shouldn't happen!") ;
            }
            if (islast)
            {
                totalpath.set (n*j, plast) ;
            }
            else
            {
                totalpath.set (n*j, path) ;
            }
        }
        return totalpath ;
    }
}

