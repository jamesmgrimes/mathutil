package mathutil ;
import Jama.* ;

/**
    P3n

*/
public class P3n  extends Object {

  protected double [][] vectors = null ;    

  public P3n(int n) { vectors = new double[n][3] ; }
  public P3n(double [][] invectors)
  {
    vectors = new double [invectors.length][3] ; 
    for (int i = 0 ; i < invectors.length ; i++)
    {
      vectors[i][0] = invectors[i][0]; 
      vectors[i][1] = invectors[i][1]; 
      vectors[i][2] = invectors[i][2]; 
    }
  }
  public P3n(double theta, double phi, double r)
  {
    vectors = new double [1][3] ;
    vectors[0][0] = theta;
    vectors[0][1] = phi;
    vectors[0][2] = r;
  }
  public void set (int i, P3n insert)
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

  public void set (int i, double theta, double phi, double r)
  {
    vectors[i][0] = theta ;
    vectors[i][1] = phi ;
    vectors[i][2] = r ;
  }
  public int size() { return vectors.length ; }
  public double getTheta(int i) { return vectors[i][0] ; }
  public double getPhi(int i) { return vectors[i][1] ; }
  public double getR(int i) { return vectors[i][2] ; }
  public P3 get (int i) 
  { 
    return new P3(vectors[i][0], vectors[i][1], vectors[i][2]) ;
  }

  public P3n timesEquals (double s) 
  {
    int n = size() ;
    for (int i = 0 ; i < n ; i++)
    {
      vectors[i][2] *= s ;
    }
    return this ;
  }

  private static final double rtod = 180.0 / Math.PI ;

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
  
}

