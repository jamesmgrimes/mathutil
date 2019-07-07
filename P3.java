package mathutil ;
import Jama.* ;

/**
    P3

*/
public class P3  extends Object {

  protected double theta = 0.0 ;
  protected double phi = 0.0 ;
  protected double r = 0.0 ;

  private static final double rtod = 180.0 / Math.PI ;
  private static final double dtor = Math.PI / 180.0 ;

  public P3() { }
  public P3(double theta, double phi, double r)
  {
    init (theta, phi, r, false) ;
  }
  public P3(double[] v)
  {
    init (v[0], v[1], v[2], false) ;
  }
  public P3 (double theta, double phi, double r, boolean isDegrees)
  {
    init (theta, phi, r, isDegrees) ;
  }
  public P3 (double[] v, boolean isDegrees)
  {
    init (v[0], v[1], v[2], isDegrees) ;
  }
  private void init (double theta, double phi, double r, boolean isDegrees)
  {
    this.theta = theta ;
    this.phi = phi ;
    this.r = r ;
    if (isDegrees) { this.theta *= dtor ; this.phi *= dtor ; }
  }
  public void setTheta (double theta) { this.theta = theta ; }
  public void setPhi (double phi) { this.phi = phi ; }
  public void setTheta (double theta, boolean isDegrees) 
  { 
    this.theta = theta ; 
    if (isDegrees) this.theta *= dtor ; 
  }
  public void setPhi (double phi, boolean isDegrees) 
  { 
    this.phi = phi ; 
    if (isDegrees) this.phi *= dtor ;
  }
  public void setR (double r) { this.r = r ; }
  public void set (double theta, double phi, double r)
  {
    this.theta = theta ;
    this.phi = phi ;
    this.r = r ;
  }

  public double getTheta() { return theta ; }
  public double getPhi() { return phi ; }
  public double getR() { return r ; }

  public P3 timesEquals (double s) 
  {
    r *= s ;
    return this ;
  }

  public String toString() 
  { 
    String s = "{" + theta + "," + phi + "," + r + "}" ;
    return s ;
  }
}

