import org.checkerframework.checker.index.qual.*;


/**
 * A class that contains several sorting routines,
 * implemented as static methods.
 * Arrays are rearranged with smallest item first,
 * using compareTo.
 * @author Mark Allen Weiss
 */
public final class Sort
{
    /**
     * Simple insertion sort.
     * @param a an array of Comparable items.
     */
    public static <AnyType extends Comparable<? super AnyType>>
    void insertionSort( AnyType [ ] a )
    {
        @Positive int j;

        for( int p = 1; p < a.length; p++ )
        {
            AnyType tmp = a[ p ];
            for( j = p; j > 0 && tmp.compareTo( a[ j - 1 ] ) < 0; j-- )
                a[ j ] = a[ j - 1 ];
            a[ j ] = tmp;
        }
    }

    /**
     * Shellsort, using Shell's (poor) increments.
     * @param a an array of Comparable items.
     */
    public static <AnyType extends Comparable<? super AnyType>>
    // please refer to https://github.com/kelloggm/checker-framework/issues/137
    @SuppressWarnings("index")
    void shellsort( AnyType [ ] a )
    {
        @Positive int j;

        for( int gap = a.length / 2; gap > 0; gap /= 2 )
            for( int i = gap; i < a.length; i++ )
            {
                AnyType tmp = a[ i ];
                for( j = i; j >= gap &&
                            tmp.compareTo( a[ j - gap ] ) < 0; j -= gap )
                    a[ j ] = a[ j - gap ];
                a[ j ] = tmp;
            }
    }


    /**
     * Internal method for heapsort.
     * @param i the index of an item in the heap.
     * @return the index of the left child.
     */
    private static @NonNegative int leftChild(@NonNegative int i )
    {
        return 2 * i + 1;
    }
    
    /**
     * Internal method for heapsort that is used in deleteMax and buildHeap.
     * @param a an array of Comparable items.
     * @index i the position from which to percolate down.
     * @int n the logical size of the binary heap.
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void percDown( AnyType [ ] a, @IndexFor("#1") int i, @IndexOrHigh("#1") int n )
    {
        @IndexFor("a") int child;
        AnyType tmp;

        for( tmp = a[ i ]; leftChild( i ) < n; i = child )
        {
            /* note that this for loop checks the return value of the left child method and makes sure
            it is within bounds for array 'a', the return type of leftChild() is also @NonNegative, so
            tempChild will be @IndexFor("a")*/
            @SuppressWarnings("index")
            @IndexFor("a") int tempChild = leftChild(i);
            child = tempChild;
            /* the follwoing code is also right becuase if child != n - 1, it is less than n - 1, and
            if the code was rewritten as "if( child < n - 1 && a[ child ].compareTo( a[ child + 1 ] ) < 0 )"
            the index checker does not issue a warning*/
            @SuppressWarnings("index")
            @IndexFor("a")int childPlusOne = child + 1;
            if( child != n - 1 && a[ child ].compareTo( a[ childPlusOne ] ) < 0 )
                child++;
            if( tmp.compareTo( a[ child ] ) < 0 )
                a[ i ] = a[ child ];
            else
                break;
        }
        a[ i ] = tmp;
    }
    
    /**
     * Standard heapsort.
     * @param a an array of Comparable items.
     */
    public static <AnyType extends Comparable<? super AnyType>>
    void heapsort( AnyType [ ] a )
    {
        for( int i = a.length / 2 - 1; i >= 0; i-- )  /* buildHeap */
            percDown( a, i, a.length );
        for( int i = a.length - 1; i > 0; i-- )
        {
            // please refer to https://github.com/kelloggm/checker-framework/issues/138 
            assert a.length >= 1 : "@AssumeAssertion(index)";
            swapReferences( a, 0, i );                /* deleteMax */
            // please refer to https://github.com/kelloggm/checker-framework/issues/138
            assert a.length >= 1 : "@AssumeAssertion(index)";
            percDown( a, 0, i );
        }
    }



   /**
     * Mergesort algorithm.
     * @param a an array of Comparable items.
     */
    public static <AnyType extends Comparable<? super AnyType>>
    void mergeSort( AnyType [ ] a )
    {
        /*this cast is safe*/
        @SuppressWarnings("index")
        AnyType @SameLen("a")[ ] tmpArray = (AnyType @SameLen("a")[]) new Comparable[ a.length ];

        mergeSort( a, tmpArray, 0, a.length - 1 );
    }

    /**
     * Internal method that makes recursive calls.
     * @param a an array of Comparable items.
     * @param tmpArray an array to place the merged result.
     * @param left the left-most index of the subarray.
     * @param right the right-most index of the subarray.
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void mergeSort( AnyType [ ] a, AnyType @SameLen("#1")[ ] tmpArray,
               @NonNegative int left, @LTLengthOf("#1") int right )
    {
        if( left < right )
        {
            /*This code is correct becuase if left < right, left and right are indices for array 'a'
            left will be less than right only when the length of 'a' >= 2*/
            @SuppressWarnings("index")
            @IndexFor("a") int center = ( left + right ) / 2;
            /*this code is also correct for the same reason as noted above*/
            @SuppressWarnings("index")
            @IndexFor("a") int l = left; 
            mergeSort( a, tmpArray, l, center );
            mergeSort( a, tmpArray, center + 1, right );
            /*this code is correct please see explanation at the beginning of this if block*/
            assert a.length >= 2 : "@AssumeAssertion(index)";
            merge( a, tmpArray, l, center + 1, right );
        }
    }

    /**
     * Internal method that merges two sorted halves of a subarray.
     * @param a an array of Comparable items.
     * @param tmpArray an array to place the merged result.
     * @param leftPos the left-most index of the subarray.
     * @param rightPos the index of the start of the second half.
     * @param rightEnd the right-most index of the subarray.
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void merge( AnyType @MinLen(2)[ ] a, AnyType @SameLen("#1")[ ] tmpArray, @IndexFor("#1") int leftPos, @IndexOrHigh("#1") int rightPos, @IndexFor("#1") int rightEnd )
    {
        /*At this point we know that rightPos is not only an @IndexFor("a"), but it is also positive
        as the method that calls this method passes in the last valid index or the length of a @MinLen(2) array as the rightPos
        so leftend will be @NonNegative, the index checker however does not support the annotation
        @Positive @IndexOrHigh("a) int i = ...*/
        @SuppressWarnings("index")
        @IndexFor("a") int leftEnd = rightPos - 1;
        @IndexFor("a") int tmpPos = leftPos;
        int numElements = rightEnd - leftPos + 1;

        // Main loop
        while( leftPos <= leftEnd && rightPos <= rightEnd )
            if( a[ leftPos ].compareTo( a[ rightPos ] ) <= 0 )
                tmpArray[ tmpPos++ ] = a[ leftPos++ ];
            else
                tmpArray[ tmpPos++ ] = a[ rightPos++ ];

        while( leftPos <= leftEnd )    // Copy rest of first half
            tmpArray[ tmpPos++ ] = a[ leftPos++ ];

        while( rightPos <= rightEnd )  // Copy rest of right half
            tmpArray[ tmpPos++ ] = a[ rightPos++ ];

        // Copy tmpArray back
        for( int i = 0; i < numElements; i++, rightEnd-- )
            a[ rightEnd ] = tmpArray[ rightEnd ];
    }

     /**
     * Quicksort algorithm.
     * @param a an array of Comparable items.
     */
    public static <AnyType extends Comparable<? super AnyType>>
    /* the empty array case is correctly handled by the code below so as to not
    cause any OOB exceptions*/
    @SuppressWarnings("index")
    void quicksort( AnyType [ ] a )
    {
        quicksort( a, 0, a.length - 1 );
    }

    private static final int CUTOFF = 3;

    /**
     * Method to swap to elements in an array.
     * @param a an array of objects.
     * @param index1 the index of the first object.
     * @param index2 the index of the second object.
     */
    public static <AnyType> void swapReferences( AnyType [ ] a, @IndexFor("#1") int index1, @IndexFor("#1") int index2 )
    {
        AnyType tmp = a[ index1 ];
        a[ index1 ] = a[ index2 ];
        a[ index2 ] = tmp;
    }

    /**
     * Return median of left, center, and right.
     * Order these and hide the pivot.
     */
    private static <AnyType extends Comparable<? super AnyType>>
    AnyType median3( AnyType [ ] a, @IndexFor("#1") int left, @IndexFor("#1") int right )
    {
        @IndexFor("a") int center = ( left + right ) / 2;
        if( a[ center ].compareTo( a[ left ] ) < 0 )
            swapReferences( a, left, center );
        if( a[ right ].compareTo( a[ left ] ) < 0 )
            swapReferences( a, left, right );
        if( a[ right ].compareTo( a[ center ] ) < 0 )
            swapReferences( a, center, right );

            // Place pivot at position right - 1
        /* this code is correct becuase if we look at the only call site of this method
        in the quickSort method below, we know that right which is @IndexFor("a") is greater
        than or equal to left + 3, left also being @IndexFor("a"), this method should
        stylistically be private to avoid user error, but as the code sits it is correct */
        @SuppressWarnings("index")
        @IndexFor("a") int rightMinusOne = right - 1;
        swapReferences( a, center, rightMinusOne );
        return a[ rightMinusOne ];
    }

    /**
     * Internal quicksort method that makes recursive calls.
     * Uses median-of-three partitioning and a cutoff of 10.
     * @param a an array of Comparable items.
     * @param left the left-most index of the subarray.
     * @param right the right-most index of the subarray.
     */
    /* unfortunately the way this code is written I dont think I can
    narrow the scope of this @SuppressWarnings any further than this,
    please look for explanations of code correctness inside the body of
    this method, the index checker issues warnings in 5 lines noted below*/
    @SuppressWarnings("index")
    private static <AnyType extends Comparable<? super AnyType>>
    void quicksort( AnyType [ ] a, @IndexFor("#1") int left, @IndexFor("#1") int right )
    {
        if( left + CUTOFF <= right )
        {
            AnyType pivot = median3( a, left, right );

                // Begin partitioning
            int i = left;
            int j = right - 1;
            for( ; ; )
            {
                /* please note that this code is correct becuase the pivot is the
                median element in this subarray, so i cannot run off the end of this
                array as we are guranteed to find an element that is greater than or equal
                than the pivot element in this array, also please note that array
                is @MinLen(4) becuase of the condition in the if block left + CUTOFF(which is 3) <= right*/
                while( a[ ++i ].compareTo( pivot ) < 0 ) { } // INDEX CHECKER WARNING
                /* for the same reason as above, j can run off the beginning of the array as it is guaranteed to find
                and element smaller than or equal to the median in this subarray*/
                while( a[ --j ].compareTo( pivot ) > 0 ) { } // INDEX CHECKER WARNING
                if( i < j )
                    swapReferences( a, i, j );
                else
                    break;
            }
            /* The line below are correct becuase i will be @IndexFor("a")
            where the pivot should go, the pivot will be at right - 1*/
            swapReferences( a, i, right - 1 );   // Restore pivot // INDEX CHECKER WARNING
            /* In the following two lines either i - 1 and i + 1 will either be valid indices
            representing the start and end of a subarray of a, otherwise none of the code
            in quickSort will be executed and the control goes to insertionSort which 
            handles the case it which left and right could be invalid indices*/
            quicksort( a, left, i - 1 );    // Sort small elements // INDEX CHECKER WARNING
            quicksort( a, i + 1, right );   // Sort large elements // INDEX CHECKER WARNING
        }
        else  // Do an insertion sort on the subarray
            insertionSort( a, left, right );
    }

    /**
     * Internal insertion sort routine for subarrays
     * that is used by quicksort.
     * @param a an array of Comparable items.
     * @param left the left-most index of the subarray.
     * @param right the right-most index of the subarray.
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void insertionSort( AnyType [ ] a, @IndexFor("#1") int left, @IndexFor("#1") int right )
    {
        for( int p = left + 1; p <= right; p++ )
        {
            AnyType tmp = a[ p ];
            @IndexFor("a") int j;

            for( j = p; j > left && tmp.compareTo( a[ j - 1 ] ) < 0; j-- )
                a[ j ] = a[ j - 1 ];
            a[ j ] = tmp;
        }
    }

    /**
     * Quick selection algorithm.
     * Places the kth smallest item in a[k-1].
     * @param a an array of Comparable items.
     * @param k the desired rank (1 is minimum) in the entire array.
     */     
    /* the empty array case is correctly handled by the code below so as to not
    cause any OOB exceptions see the cutoff statement*/
    @SuppressWarnings("index")
    public static <AnyType extends Comparable<? super AnyType>>
    void quickSelect( AnyType [ ] a, @Positive int k )
    {
        quickSelect( a, 0, a.length - 1, k );
    }

    /**
     * Internal selection method that makes recursive calls.
     * Uses median-of-three partitioning and a cutoff of 10.
     * Places the kth smallest item in a[k-1].
     * @param a an array of Comparable items.
     * @param left the left-most index of the subarray.
     * @param right the right-most index of the subarray.
     * @param k the desired index (1 is minimum) in the entire array.
     */
    /* This is a repeat of the same five cases from quickSort*/
    @SuppressWarnings("index")
    private static <AnyType extends Comparable<? super AnyType>>
    void quickSelect( AnyType [ ] a, @IndexFor("#1") int left, @IndexFor("#1") int right, @Positive int k )
    {
        if( left + CUTOFF <= right )
        {
            AnyType pivot = median3( a, left, right );

                // Begin partitioning
            int i = left, j = right - 1;
            for( ; ; )
            {
                while( a[ ++i ].compareTo( pivot ) < 0 ) { } // INDEX CHECKER WARNING
                while( a[ --j ].compareTo( pivot ) > 0 ) { } // INDEX CHECKER WARNING
                if( i < j )
                    swapReferences( a, i, j );
                else
                    break;
            }

            swapReferences( a, i, right - 1 );   // Restore pivot // INDEX CHECKER WARNING

            if( k <= i )
                quickSelect( a, left, i - 1, k ); // INDEX CHECKER WARNING
            else if( k > i + 1 )
                quickSelect( a, i + 1, right, k ); // INDEX CHECKER WARNING
        }
        else  // Do an insertion sort on the subarray
            insertionSort( a, left, right );
    }


    private static final int NUM_ITEMS = 1000;
    private static int theSeed = 1;

    private static void checkSort( Integer [ ] a )
    {
        for( int i = 0; i < a.length; i++ )
            if( a[ i ] != i )
                System.out.println( "Error at " + i );
        System.out.println( "Finished checksort" );
    }


    public static void main( String [ ] args )
    {
        Integer [ ] a = new Integer[ NUM_ITEMS ];
        for( int i = 0; i < a.length; i++ )
            a[ i ] = i;

        for( theSeed = 0; theSeed < 20; theSeed++ )
        {
            Random.permute( a );
            Sort.insertionSort( a );
            checkSort( a );

            Random.permute( a );
            Sort.heapsort( a );
            checkSort( a );

            Random.permute( a );
            Sort.shellsort( a );
            checkSort( a );

            Random.permute( a );
            Sort.mergeSort( a );
            checkSort( a );

            Random.permute( a );
            Sort.quicksort( a );
            checkSort( a );

            Random.permute( a );
            Sort.quickSelect( a, NUM_ITEMS / 2 );
            System.out.println( a[ NUM_ITEMS / 2 - 1 ] + " " + NUM_ITEMS / 2 );
        }
        
        
        Integer [ ] b = new Integer[ 10_000_000 ];
        for( int i = 0; i < b.length; i++ )
            b[ i ] = i;
        
        Random.permute( b );
        long start = System.currentTimeMillis( );
        Sort.quickSelect( b, b.length  / 2 );
        long end = System.currentTimeMillis( );
        System.out.println( "Timing for Section 1.1 example: " );
        System.out.println( "Selection for N = " + b.length + " takes " + 
                             ( end - start ) + "ms." );
        System.out.println( b[ b.length / 2 - 1 ] + " " + b.length / 2 );
    }
}
