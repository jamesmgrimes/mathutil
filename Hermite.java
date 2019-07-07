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

    public R3n hermite (R3n vectors)
    {
        Matrix PR = new Matrix (vectors.vectors) ;
        Matrix R = TM.times (PR) ;

        return new R3n(R.getArray()) ;  // getArray should avoid making copy
    }


}

