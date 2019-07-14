package mathutil ;
import Jama.* ;

/**
    Hermite

*/
public class Hermite  extends Object 
{

    private Matrix TM = null ;

    /* Hermite has nsteps steps, which results in n+1 positions */
    public Hermite(int nsteps) 
    {
        ////////////////////////////////////////////////////////////////////
        // if we were doing this just once, we'd figure MP once, first,   //
        // then do all the Ts separately.  But the premise here is that   //
        // the P matrix is the dependent variable and needs to be done    //
        // each time.                                                     //
        ////////////////////////////////////////////////////////////////////
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


}

