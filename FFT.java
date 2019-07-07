package mathutil ;
import java.lang.Math.* ;

public class FFT 
{
    int fftlength ;
    Complex[] wfor ;
    Complex[] winv ;
    int n, nn ;

    public FFT (int length)
    {
        fftlength = length ;
        nn = fftlength ;    // need to consolidate, but debug first
        n = 2* nn ;
        wfor = new Complex[fftlength] ;
        winv = new Complex[fftlength] ;

        int isign = 1 ;     // artifact, sort of, meaning forward fft
        int iw = 0 ;
        int mmax = 2 ;
        while (n > mmax)
        {
            int istep = 2 * mmax;
            double theta = 2.0*Math.PI / (isign * mmax);
            double sinhalftheta = Math.sin (0.5 * theta) ;
            Complex wp = new Complex (-2.0 * sinhalftheta*sinhalftheta, Math.sin(theta));
            Complex wpplus1 = wp.plus(new Complex (1.0, 0)) ;

            ////////////////////////////////////////////////
            // Observation: in following loop, "w"        //
            // describes a nice semicircle, starting at   //
            // 1+0i, and ending at -1.0i non-inclusive.   //
            // The semi-circle is divided into a certain  //
            // number of segments.  The first time        //
            // through the outerloop that we're already   //
            // inside, [while (n . mmax) above], w is     //
            // used just once, next time twice, third     //
            // time four times, etc.  So we get arguments //
            // for w as follows: 0 ; 0, pi/2 ; 0, pi/4,   //
            // pi/2, 3pi/4 ; ... We go through this loop  //
            // nloop = (log2 nn) times.  The number of    //
            // entries in each series is 2**iseries where //
            // iseries = 1 .. nloop-1 Thus we could get   //
            // away with an array of size 2**(nloop-1),   //
            // and re-use it for each time through.  That //
            // would involve figuring out the right step.  
            // Not too hard. The brute force efficiency   //
            // fix is to set up an array of size 2**nloop //
            // and just iterate through it by one.        //
            ////////////////////////////////////////////////
            Complex w = new Complex (1.0, 0.0) ;
            for (int m = 1; m <= mmax; m += 2)
            {
                wfor[iw] = new Complex (w);    // makes copy
                winv[iw] = w.conj(new Complex()) ;
                // in actual fft - the business goes here
                w.times(wpplus1) ;
                // System.out.println ("iw,w\t" + iw + "\t" + w) ;
                iw++ ;
            }
            mmax = istep;
        }
    }

    /** forward fft */
    public void fftfor (Complex[] data)     
    {
        Complex temp = new Complex() ;
        int j = 1;
        for (int i = 1; i <= n; i += 2)
        {
            int io2 = i/2 ;
            if (j > i)
            {
                int jo2 = j/2 ;
                data[jo2].copyinto(temp) ;
                data[io2].copyinto(data[jo2]) ;
                temp.copyinto(data[io2]) ;
            }
            int m = n / 2;
            while ((m >= 2) && (j > m))
            {
                j = j - m;
                m = m / 2;
            }
            j = j + m;
        }

        int mmax = 2;

        int iw = 0 ;
        while (n > mmax)
        {
            int istep = 2 * mmax;			
            // following is a reference - should be ok, because we don't touch w
            Complex w = wfor[iw] ;
            for (int m = 1; m <= mmax; m += 2)
            {
                for (int i = m; i <= n; i += istep)
                {
                    j = i + mmax;
                    int io2 = i/2 ;
                    int jo2 = j/2 ;
                    // temp = w*data[jo2]
                    // data[jo2] = data[io2] - temp
                    // data[io2] = data[io2] + temp
                    //    note to self: copyinto 
                    //    returns temp, and this is 
                    //    the in-place version of 
                    //    times 
                    w.copyinto(temp).times(data[jo2]) ;
                    // use the in-place version of minus and plus
                    data[io2].copyinto(data[jo2]).minus(temp) ;
                    data[io2].plus(temp) ;
                }
                iw++ ;
                w = wfor[iw] ;
            }
            mmax = istep;
        }
    }

    /** inverse fft */
    public void fftinv (Complex data[])
    {
        Complex temp = new Complex() ;
        int j = 1;
        for (int i = 1; i <= n; i += 2)
        {
            int io2 = i/2 ;
            if (j > i)
            {
                int jo2 = j/2 ;
                data[jo2].copyinto(temp) ;
                data[io2].copyinto(data[jo2]) ;
                temp.copyinto(data[io2]) ;
            }
            int m = n / 2;
            while ((m >= 2) && (j > m))
            {
                j = j - m;
                m = m / 2;
            }
            j = j + m;
        }

        int mmax = 2;

        int iw = 0 ;
        while (n > mmax)
        {
        int istep = 2 * mmax;			
        // following is a reference - should be ok, because we don't touch w
        Complex w = winv[iw] ;
        for (int m = 1; m <= mmax; m += 2)
        {
            for (int i = m; i <= n; i += istep)
            {
                j = i + mmax;
                int io2 = i/2 ;
                int jo2 = j/2 ;
                // temp = w*data[jo2]
                // data[jo2] = data[io2] - temp
                // data[io2] = data[io2] + temp
                //    note to self: copyinto 
                //    returns temp, and this is 
                //    the in-place version of 
                //    times 
                w.copyinto(temp).times(data[jo2]) ;
                // use the in-place version of minus and plus
                data[io2].copyinto(data[jo2]).minus(temp) ;
                data[io2].plus(temp) ;
            }
            iw++ ;
            w = winv[iw] ;
        }
        mmax = istep;
        }
        for (int i = 0 ; i < fftlength ; i++)
        data[i].div((double)fftlength) ;
    }

}
