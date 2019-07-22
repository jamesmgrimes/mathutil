package mathutil ;
import Jama.* ;

/**
    Hermite

*/
public class Hermite  extends Object 
{

    private Matrix TM = null ;
    private int nsteps ;

    /* Hermite has nsteps steps, which results in n+1 positions */
    public Hermite(int nsteps) 
    {
        ////////////////////////////////////////////////////////////////////
        // if we were doing this just once, we'd figure MP once, first,   //
        // then do all the Ts separately.  But the premise here is that   //
        // the P matrix is the dependent variable and needs to be done    //
        // each time.                                                     //
        ////////////////////////////////////////////////////////////////////
        this.nsteps = nsteps ;
        double [][] Tnx4 = new double[nsteps+1][4] ;  
        for (int i = 0 ; i <= nsteps ; i++)
        {
            double t = (double)(i)/nsteps ;
            double t2 = t*t ;
            double t3 = t2*t ;
            Tnx4[i][0] = t3 ;
            Tnx4[i][1] = t2 ;
            Tnx4[i][2] = t ;
            Tnx4[i][3] = 1.0 ;
        }
        // System.out.println ("i = 1, Tnx4[1][0,1,2,3] = " 
        //              + Tnx4[1][0] + "," 
        //              + Tnx4[1][1] + "," 
        //              + Tnx4[1][2] + "," 
        //              + Tnx4[1][3]) ;
        Matrix T = new Matrix (Tnx4) ;
        double [][] m = {
                { 2.0, -2.0,  1.0,  1.0},
                {-3.0,  3.0, -2.0, -1.0},
                { 0.0,  0.0,  1.0,  0.0},
                { 1.0,  0.0,  0.0,  0.0} 
            } ;
        Matrix M = new Matrix (m) ;
        TM = T.times (M) ;
    }
    
    public String toString()
    {
        String result = "Hermite Matrix T x M is\n" ;
        double[][] mm = TM.getArray() ;
        for (int irow = 0 ; irow < mm.length ; irow++)
        {
            result += "" + irow ;
            for (int icol = 0 ; icol < mm[irow].length ; icol++)
            {
                result += "\t" + mm[irow][icol] ;
            }
            result += "\n" ;
        }
        return result ;
    }

    // hermite calculates the interpolated positions between two points and their
    // first derivatives.  R3n vectors contains four rows of x,y,z triplets P0, P1, 
    // M0, M1, where 0 and 1 are the beginning and end points, and P is the position
    // and M is the 1st derivative. 
    public R3n hermite (R3n vectors)
    {
        Matrix PR = new Matrix (vectors.vectors) ;
        Matrix R = TM.times (PR) ;

        return new R3n(R.getArray()) ;  // getArray should avoid making copy
    }
    // hermite calculates the interpolated positions between two points and their
    // first derivatives.  R3n vectors contains four rows of x,y,z triplets P0, P1, 
    // M0, M1, where 0 and 1 are the beginning and end points, and P is the position
    // and M is the 1st derivative. 
    public R3n hermite (R3 p0, R3 p1, R3 m0, R3 m1)
    {
        // lotta overhead here BOZO
        R3n pr = new R3n(4) ;
        pr.set(0, p0) ;
        pr.set(1, p1) ;
        pr.set(2, m0) ;
        pr.set(3, m1) ;
        
        Matrix PR = new Matrix (pr.vectors) ;
        Matrix R = TM.times (PR) ;

        return new R3n(R.getArray()) ;  // getArray should avoid making copy
    }

    ////////////////////////////////////////////////////////////////////
    // Now interpolate between each of a set of n positions,          //
    // returning a single series. Set the tangent vector depending on //
    // whether we're on the ends or in the middle. And we really need //
    // to assume there are more than two total, else we're going to   //
    // have to test everything all the time. What's the point of      //
    // having just two anyway? /                                      //
    ////////////////////////////////////////////////////////////////////
    public R3n hermitePath (R3n positions)
    {
        // Yikes - how big is our total path!?
        // it is the size of herm x the number of positions, + 1.
        // we discard the last interpolated position (except very last
        // one) of each set because that's the same as the beginning
        // of the next one.
        R3n completePath = new R3n ( this.nsteps * (positions.size()-1) + 1) ;                                          
        R3 m0 = null ;
        R3 m1 = null ;
        for ( int ipos = 0 ; ipos < positions.size()-1 ; ipos++)
        {
            R3 p0 = positions.get(ipos) ;
            R3 p1 = positions.get(ipos+1) ;
            R3 p2 = new R3() ;
            // m0 will always be previous m1, unless this is first
            if (ipos == 0) 
            {
                p2.set(positions.get(ipos+2)) ;
                m0 = p1.minus(p0) ;
                ////////////////////////////////////////////////////////
                // this won't always work.  magnitude could be quite  //
                // small, or enormous                               //
                ////////////////////////////////////////////////////////
                 m1 = p2.minus(p0).timesEquals(0.5) ; 
            }
            else if (ipos < positions.size() -2) 
            {
                p2.set(positions.get(ipos+2)) ;
                m0 = new R3(m1) ;
                m1 = p2.minus(p0).timesEquals(0.5) ;
            }
            else // no p2 available
            {
                m0 = new R3(m1) ;
                m1 = p1.minus(p0) ;
            }

//             System.out.println ("p0=" + p0.toString()) ;
//             System.out.println ("p1=" + p1.toString()) ;
//             System.out.println ("m0=" + m0.toString()) ;
//             System.out.println ("m1=" + m1.toString()) ;
            R3n path = hermite(p0, p1, m0, m1) ;
            // now put the result into completePath
            // the last position of path will be overwritten
            // by the first postion of the next one, but they
            // should be identical
            completePath.set (ipos*this.nsteps, path) ;
        }
        return completePath ;
    }

}

