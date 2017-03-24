import org.checkerframework.checker.index.qual.*;
import java.lang.*;

public class Fig02_09
{
    public static final int NOT_FOUND = -1;

    /**
     * Performs the standard binary search.
     * @return index where item is found, or -1 if not found
     */
    public static <AnyType extends Comparable<? super AnyType>>
    //please refer to the comments in this method explaining the correctness of this code
    @SuppressWarnings("index")
    int binarySearch(AnyType @MinLen(1)[ ] a, AnyType x )
    {
        @IndexFor("a") int low = 0, high = a.length - 1;
	
        /*the following code is correct because while
        low(index used to scan the left of the array) is less than high
        (index scanning the right of the array), mid - 1, and mid + 1
        will be indices of the array a for example take the edge case of a single
        element array where low = 0, and high = 0 therefore mid = 0, suppose low is assigned to mid + 1
        low > high the while loop breaks, if high is assigned mid - 1 also low > high the loop breaks
        so inside this loop there will never be an illegal dereference*/
        while( low <= high )
        {
            int mid = ( low + high ) / 2;
	    
            if( a[ mid ].compareTo( x ) < 0 )
                low = mid + 1;
            else if( a[ mid ].compareTo( x ) > 0 )
                high = mid - 1;
            else
                return mid;   // Found
        }
        return NOT_FOUND;     // NOT_FOUND is defined as -1
    }

    // Test program
    public static void main( String [ ] args )
    {
        int SIZE = 8;
        Integer [ ] a = new Integer [ SIZE ];
        for( int i = 0; i < SIZE; i++ )
            a[ i ] = i * 2;

        for( int i = 0; i < SIZE * 2; i++ )
            System.out.println( "Found " + i + " at " + binarySearch( a, i ) );
    }
}
