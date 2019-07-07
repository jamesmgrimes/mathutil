package mathutil ;

/**
    Complex implements a complex number and defines complex
    arithmetic and mathematical functions
    Last Updated February 27, 2001
    Copyright 1997-2001
    @version 1.0
    @author Andrew G. Bennett
*/
public class Complex extends Object 
{

    private double x,y;

    /**
        Constructs the complex number 0,0
    */
    public Complex() { x = 0d ; y = 0d ; }

    /**
        Constructs the complex number z = u + i*v
        @param real Real part
        @param imag Imaginary part
    */
    public Complex(double real, double imag) 
    {
        x=real;
        y=imag;
    }

    /**
    Constructs a complex number as a copy of another.
    @param w complex number to copy
    */
    public Complex (Complex w) 
    {
        x=w.x;
        y=w.y;
    }

    /** 
    Copies "this" complex number into another, which <u>must</u> be 
    pre-allocated, and returning its reference (for convenience in 
    chaining).
    @param dest complex number to copy into
    @return Complex dest 
    */
    public Complex copyinto (Complex dest) 
    {
        dest.x = x ; dest.y = y ;
        return dest ;
    }

    /** 
    Sets the real and imaginary parts of "this"
    be pre-allocated, and returning its reference (for convenience in 
    chaining).
    @param real    scalar to which to set real part of "this"
    @param imag    scalar to which to set imaginary part of "this"
    @return Complex "this"
    */
    public Complex set (double real, double imag) 
    {
        x = real ; y = imag ;
        return this ;
    }

    /**
    Real part of this Complex number 
    (the x-coordinate in rectangular coordinates).
    @return Re[z] where z is this Complex number.
    */
    public double real() 
    {
        return x;
    }

    /**
    Imaginary part of this Complex number 
    (the y-coordinate in rectangular coordinates).
    @return Im[z] where z is this Complex number.
    */
    public double imag() 
    {
        return y;
    }

    /**
    Modulus of this Complex number
    (the distance from the origin in polar coordinates).
    @return |z| where z is this Complex number.
    */
    public double mod() 
    {
        if (x!=0 || y!=0) 
        {
            return Math.sqrt(x*x+y*y);
        } 
        else 
        {
            return 0d;
        }
    }

    /**
    Argument of this Complex number 
    (the angle in radians with the x-axis in polar coordinates).
    @return arg(z) where z is this Complex number.
    */
    public double arg() 
    {
        return Math.atan2(y,x);
    }

    /**
    Complex conjugate of this Complex number.
    @return "this" 
    */
    public Complex conj() 
    {
        y = -y ;
        return this ;
    }
    /**
    Complex conjugate of this Complex number.
    @param dest   reference to destination, preallocated
    @return "this" 
    @throws   java.lang.NullPointerException
    */
    public Complex conj(Complex dest) 
    {
        dest.x = x ; dest.y = -y ;
        return dest ;
    }

    /**
        Negative of this complex number (chs stands for change sign). 
        <br>-(x+i*y) = -x-i*y.
        @return -z where z is this Complex number.
    */
    public Complex chs() 
    {
        x = -x ; y = -y ;
        return this ;
    }
    public Complex chs (Complex dest) 
    {
        dest.x = -x ; dest.y = -y ;
        return dest ;
    }

    ////////////////////////////////////////////////////////
    // Following (+, -, *, /) are paired.  First is       //
    // "operate into" i.e., it does change "this" and     //
    // returns "this".  Second includes as second         //
    // argument a "dest" Complex, which <u>must</u> be    //
    // pre-allocated; "dest" is the return reference.     //
    ////////////////////////////////////////////////////////

    /**
    Addition of Complex numbers 
    @param w is the number to add.
    @return z+w where z is this Complex number.
    */
    public Complex plus(Complex w) 
    {
        x += w.x ; y += w.y ;
        return this ;
    }
    public Complex plus (Complex w, Complex dest)
    {
        dest.x = x + w.x ; 
        dest.y = y + w.y ;
        return dest ;
    }

    /**
    Subtraction of Complex numbers 
    @param w is the number to subtract.
    @return z-w where z is this Complex number.
    */
    public Complex minus(Complex w) 
    {
        x -= w.x ; y -= w.y ;
        return this ;
    }
    public Complex minus (Complex w, Complex dest)
    {
        dest.x = x - w.x ; 
        dest.y = y - w.y ;
        return dest ;
    }

    /**
    Complex multiplication 
    @param w is the number to multiply by.
    @return z*w where z is this Complex number.
    */
    public Complex times(Complex w) 
    {
        double tx = x*w.x - y*w.y ;
        double ty = x*w.y + y*w.x ;
        x = tx ; y = ty ;
        return this ;
    }
    public Complex times (Complex w, Complex dest)
    {
        dest.x = x*w.x - y*w.y ;
        dest.y = x*w.y + y*w.x ;
        return dest ;
    }

    /**
    Division of Complex numbers 
    <br>(x+i*y)/(s+i*t) = ((x*s+y*t) + i*(y*s-y*t)) / (s^2+t^2)
    @param w is the number to divide by
    @return z/w where z is this Complex number  
    */
    public Complex div(Complex w) 
    {
        double den=Math.pow(w.mod(),2);
        double tx = (x*w.x + y*w.y)/den ;
        double ty = (y*w.x - x*w.y)/den ;
        x = tx ; y = ty ;
        return this ;
    }
    public Complex div (Complex w, Complex dest)
    {
        double den=Math.pow(w.mod(),2);
        dest.x = (x*w.x + y*w.y)/den ;
        dest.y = (y*w.x - x*w.y)/den ;
        return dest ;
    }

    /** 
    Multiplication by scalar, in-place
    @param x is the number to multiply by
    @return "this" now == z*x ;
    */
    public Complex times (double x)
    {
        this.x *= x ;
        this.y *= x ;
        return this ;
    }

    /** 
    Division by scalar, in-place
    @param x is the number to divide by
    @return "this" now == z/*x ;
    */
    public Complex div (double x)
    {
        this.x /= x ;
        this.y /= x ;
        return this ;
    }

    /**
    Complex exponential 
    @return exp(z) where z is this Complex number.
    */
    public Complex exp(Complex dest) 
    {
        double expx = Math.exp(x) ;
        dest.x = expx*Math.cos(y) ;
        dest.y = expx*Math.sin(y) ;
        return dest ;
    }

    /**
    Principal branch of the Complex logarithm of this Complex number.
    The principal branch is the branch with -pi < arg <= pi.
    @return log(z) where z is this Complex number.
    */
    public Complex log(Complex dest) 
    {
        dest.x = Math.log(this.mod()) ;
        dest.y = this.arg() ;
        return dest ;
    }

    /**
    Complex square root (doesn't change this complex number).
    Computes the principal branch of the square root, which 
    is the value with 0 <= arg < pi.
    @return sqrt(z) where z is this Complex number.
    */
    public Complex sqrt(Complex dest) 
    {
        double r=Math.sqrt(this.mod());
        double theta=this.arg()/2;
        dest.x = r*Math.cos(theta) ;
        dest.y = r*Math.sin(theta);
        return dest ;
    }

    // Real cosh function (used to compute complex trig functions)
    private double cosh(double theta) 
    {
        return (Math.exp(theta)+Math.exp(-theta))/2;
    }

    // Real sinh function (used to compute complex trig functions)
    private double sinh(double theta) 
    {
        return (Math.exp(theta)-Math.exp(-theta))/2;
    }

    /**
    Sine of this Complex number (doesn't change this Complex number).
    <br>sin(z) = (exp(i*z)-exp(-i*z))/(2*i).
    @return sin(z) where z is this Complex number.
    */
    public Complex sin(Complex dest) 
    {
        dest.x = cosh(y)*Math.sin(x) ;
        dest.y = sinh(y)*Math.cos(x) ;
        return dest ;
    }

    /**
    Cosine of this Complex number 
    <br>cos(z) = (exp(i*z)+exp(-i*z))/ 2.
    @return cos(z) where z is this Complex number.
    */
    public Complex cos(Complex dest) 
    {
        dest.x = cosh(y)*Math.cos(x) ;
        dest.y = -sinh(y)*Math.sin(x);
        return dest ;
    }

    /** Hyperbolic sine of this Complex number 
    <br>sinh(z) = (exp(z)-exp(-z))/2.
    @return sinh(z) where z is this Complex number.
    */
    public Complex sinh(Complex dest) 
    {
        dest.x = sinh(x)*Math.cos(y) ;
        dest.y = cosh(x)*Math.sin(y);
        return dest ;
    }

    /**
    Hyperbolic cosine of this Complex number 
    (doesn't change this Complex number).
    <br>cosh(z) = (exp(z) + exp(-z)) / 2.
    @return cosh(z) where z is this Complex number.
    */
    public Complex cosh(Complex dest) 
    {
        dest.x = cosh(x)*Math.cos(y);
        dest.y = sinh(x)*Math.sin(y);
        return dest ;
    }

    // UNIMPLEMENTED UNTIL WE GET tan(z) without needing an intermediate 
    // Complex number 
    // /**
    //         Tangent of this Complex number (doesn't change this Complex number).
    //         <br>tan(z) = sin(z)/cos(z).
    //         @return tan(z) where z is this Complex number.
    //     */
    //     public Complex tan(Complex dest) {
    // 		  if (dest == null) dest = new Complex() ;
    // 
    // 
    //         return (this.sin()).div(this.cos());
    //     }

    /**
    String representation of this Complex number.
    @return x+i*y, x-i*y, x, or i*y as appropriate.
    */
    public String toString() 
    {
        if (x!=0 && y>0) 
        {
            return x+" + "+y+"i";
        }
        if (x!=0 && y<0) 
        {
            return x+" - "+(-y)+"i";
        }
        if (y==0) 
        {
            return String.valueOf(x);
        }
        if (x==0) 
        {
            return y+"i";
        }
        // shouldn't get here (unless Inf or NaN)
        return x+" + i*"+y;

    }       
}

