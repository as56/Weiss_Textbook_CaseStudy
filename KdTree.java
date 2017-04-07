import org.checkerframework.checker.index.qual.*;

/**
 * Quick illustration of a two-dimensional tree.
 */
public class KdTree<AnyType extends Comparable<? super AnyType>>
{
    private static class KdNode<AnyType>
    {
        AnyType @MinLen(2)[ ]     data;
        KdNode<AnyType> left;
        KdNode<AnyType> right;

        KdNode( AnyType item @MinLen(2)[ ] )
        {
            data = (AnyType[]) new Comparable[ 2 ];
            data[ 0 ] = item[ 0 ];
            data[ 1 ] = item[ 1 ];
            left = right = null;
        }
    }

    private KdNode<AnyType> root;

    public KdTree( )
    {
        root = null;
    }

    public void insert( AnyType @MinLen(2)[ ] x )
    {
        root = insert( x, root, 0 );
    }
    /*I have marked the problem lines inside this method's body, as for an explanation
    of why this code is correct please check the reasoning in the private 'printRange' method of this
    class. That same explanantion holds here*/
    @SuppressWarnings("index")
    private KdNode<AnyType> insert( AnyType @MinLen(2)[ ] x, KdNode<AnyType> t, @IndexFor("#1") int level )
    {
        if( t == null )
            t = new KdNode<>( x );
        else if( x[ level ].compareTo( t.data[ level ] ) < 0 )
            t.left = insert( x, t.left, 1 - level );
        else
            t.right = insert( x, t.right, 1 - level );
        return t;
    }

    /**
     * Print items satisfying
     * low[ 0 ] <= x[ 0 ] <= high[ 0 ] and
     * low[ 1 ] <= x[ 1 ] <= high[ 1 ].
     */
    
    public void printRange( AnyType @MinLen(2)[ ] low, AnyType @MinLen(2)[ ] high )
    {	
        printRange( low, high, root, 0 );
    }
    /*Please see explanation of why this code is right inside the method body*/
    @SuppressWarnings("index")
    private void printRange( AnyType @MinLen(2)[ ] low, AnyType @MinLen(2)[ ] high,
                             KdNode<AnyType> t, @IndexFor("#1") int level )
    {
        if( t != null )
        {
            if( low[ 0 ].compareTo( t.data[ 0 ] ) <= 0 &&
                        low[ 1 ].compareTo( t.data[ 1 ] ) <= 0 &&
                       high[ 0 ].compareTo( t.data[ 0 ] ) >= 0 &&
                       high[ 1 ].compareTo( t.data[ 1 ] ) >= 0 )
                System.out.println( "(" + t.data[ 0 ] + ","
                                        + t.data[ 1 ] + ")" );
            
            /*The following 4 lines are the problem section for the index checker
            as it fails to realize that this method will only ever be called with
            level = 0 or 1 which are valid indices for low, t.data, and high. Please
            see the public print range method above this method for proof, from there
            this private method is called with 0, then recursively called with 1, (1 - 0)
            when its called with 1, it will be recursively called again with 0 (1 - 1)*/
            if( low[ level ].compareTo( t.data[ level ] ) <= 0 )
                printRange( low, high, t.left, 1 - level );
            if( high[ level ].compareTo( t.data[ level ] ) >= 0 )
                printRange( low, high, t.right, 1 - level );
        }
    }

    public static void main( String [ ] args )
    {
        KdTree<Integer> t = new KdTree<>( );
        
        System.out.println( "Starting program" );
        for( int i = 300; i < 370; i++ )
        {
            Integer [ ] it = new Integer[ 2 ];
            it[ 0 ] = i;
            it[ 1 ] = 2500 - i;
            t.insert( it );
        }

        Integer [ ] low = { 70, 2186 };
        Integer [ ] high = { 1200, 2200 };

        t.printRange( low, high );
    }
}
