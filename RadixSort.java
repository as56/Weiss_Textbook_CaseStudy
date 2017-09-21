import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import org.checkerframework.checker.index.qual.*;

public class RadixSort
{
    /*
     * Radix sort an array of Strings
     * Assume all are all ASCII
     * Assume all have same length
     */
    public static void radixSortA( String [ ] arr, @NonNegative int stringLen )
    {
        final int BUCKETS = 256;
        
        ArrayList<String> @MinLen(256) [ ] buckets = (ArrayList<String>[]) new ArrayList[ BUCKETS ];
        
        for( int i = 0; i < BUCKETS; i++ ) {
            /* BUCKETS = 256, which is = buckets.length*/
            @SuppressWarnings("index")
            @IndexFor("buckets") int index = i;
            buckets[ index ] = new ArrayList<>( );
        }
        
        for( int pos = stringLen - 1; pos >= 0; pos-- )
        {
            for( String s : arr ) {
                /* ascii values range from 0-255, therefore any call int s.charAt() in this
                context is a valid index for buckets which is @MinLen(256)*/
                @SuppressWarnings("index")
                @IndexFor("buckets") int idx = s.charAt( pos );
                buckets[ idx ].add( s );
            }
            
            int idx = 0;
            for( ArrayList<String> thisBucket : buckets )
            {
                for( String s : thisBucket ) {
                    /* each array list in the bucket array is
                    filled up with the strings in arr, so 'thisBucket.size()' is
                    equal to arr.length, require a @SameLen type for lists*/
                    @SuppressWarnings("index")
                    @IndexFor("arr") int index = idx;
                    arr[ index++ ] = s;
                }
                
                thisBucket.clear( );
            }
        }
    }
       
    /*
     * Counting radix sort an array of Strings
     * Assume all are all ASCII
     * Assume all have same length
     */
    public static void countingRadixSort( String [ ] arr, @NonNegative int stringLen )
    {
        final int BUCKETS = 256;
        
        @IndexOrHigh("arr") int N = arr.length;
         
        /* The following line is obviously correct*/
        @SuppressWarnings("index")
        String @SameLen("arr") [ ] buffer = new String[ N ];

        String @SameLen("arr") [ ] in = arr;
        String @SameLen("arr") [ ] out = buffer;
        
        for( int pos = stringLen - 1; pos >= 0; pos-- )
        {
            int @MinLen(257) [ ] count = new int [ BUCKETS + 1 ];
            
            for( int i = 0; i < N; i++ ) {
                /* ascii values range from 0-255, therefore any call int s.charAt() + 1 in this
                context is a valid index for buckets which is @MinLen(257)*/
                @SuppressWarnings("index")
                @IndexFor("count") int countIdx = in[ i ].charAt( pos ) + 1;
                count[ countIdx ]++;
            }

            for( int b = 1; b <= BUCKETS; b++ )
                count[ b ] += count[ b - 1 ];

            for( int i = 0; i < N; i++ ) {
                /* ascii values range from 0-255, therefore any call int s.charAt() in this
                context is a valid index for buckets which is @MinLen(256)*/
                @SuppressWarnings("index")
                @IndexFor("count") int countIdx = in[ i ].charAt( pos );
                /* the count array contains the count of characters at one
                specific index of all the strings, in this case the index represented
                by pos. Note: out has length = number of strings, and the maximum
                value that will be stored in count = number of strings, becuase at one
                certain 'pos' index all could be the same character as in alto, all, amber, alpha
                the character at index 0 of all these strings is a which is equal to the number of 
                strings. Please also note this example is not entireley appropriate in terms of this
                code which requires all strings to be of the same length*/
                @SuppressWarnings("index")
                @IndexFor("out") int outIdx =  count[ countIdx ];
                out[ outIdx++ ] = in[ i ];
            }
            
              // swap in and out roles
            String [ ] tmp = in;
            in = out;
            out = tmp;
        }
        
           // if odd number of passes, in is buffer, out is arr; so copy back
        if( stringLen % 2 == 1 )
            for( int i = 0; i < arr.length; i++ )
                out[ i ] = in[ i ];
    }
    
    /*
     * Radix sort an array of Strings
     * Assume all are all ASCII
     * Assume all have length bounded by maxLen
     */
    public static void radixSort( String [ ] arr, @NonNegative int maxLen )
    {
        final int BUCKETS = 256;
        
        ArrayList<String> [ ] wordsByLength = (ArrayList<String>[]) new ArrayList[ maxLen + 1 ];
        ArrayList<String> @MinLen(256) [ ] buckets = (ArrayList<String>[]) new ArrayList[ BUCKETS ];
        
        for( int i = 0; i < wordsByLength.length; i++ )
            wordsByLength[ i ] = new ArrayList<>( );
        
        for( int i = 0; i < BUCKETS; i++ ) {
            /* BUCKETS = 256, which is = buckets.length*/
            @SuppressWarnings("index")
            @IndexFor("buckets") int index = i;
            buckets[ index ] = new ArrayList<>( );
        }

        for( String s : arr ) {
            /* please refer to documentation of this method, all string length are bounded by maxlen*/
            @SuppressWarnings("index")
            @IndexFor("wordsByLength") int idx = s.length();
            wordsByLength[ idx ].add( s );
        }
       
        int idx = 0;
        for( ArrayList<String> wordList : wordsByLength )
            for( String s : wordList ) {
                /* each array list in the wordsByLength array is
                filled up with the strings in arr, so 'wordlist.size()' is
                equal to arr.length, require a @SameLen type for lists*/
                @SuppressWarnings("index")
                @IndexFor("arr") int index = idx;
                arr[ index++ ] = s;
            }
        
        @IndexOrHigh("arr") int startingIndex = arr.length;    
        for( int pos = maxLen - 1; pos >= 0; pos-- )
        { 
            /* Please note that the length of wordsByLength = maxlen + 1, pos
            goes from maxlen - 1 to 0, pos + 1goes from maxlen, to 1. So it
            is clear that pos + 1 is a valid index of wordsByLength*/
            @SuppressWarnings("index")
            @IndexFor("wordsByLength") int posPlusOne = pos + 1;
            startingIndex -= wordsByLength[ posPlusOne ].size( );
            
            for( int i = startingIndex; i < arr.length; i++ ) {
                /* ascii values range from 0-255, therefore any call int s.charAt() in this
                context is a valid index for buckets which is @MinLen(256)*/
                @SuppressWarnings("index")
                @IndexFor("buckets") int indx = arr[ i ].charAt( pos );
                buckets[ indx ].add( arr[ i ] );
            }
            
            idx = startingIndex;
            for( ArrayList<String> thisBucket : buckets )
            {
                for( String s : thisBucket ) {
                    /* each array list in the buckets array is
                    filled up with the strings in arr, so 'thisBucket.size()' is
                    equal to arr.length, require a @SameLen type for lists*/
                    @SuppressWarnings("index")
                    @IndexFor("arr") int index = idx;
                    arr[ index++ ] = s;
                }
                thisBucket.clear( );
            }
        }
    }

    public static void main( String [ ] args )
    {
        List<String> lst = new ArrayList<>( );
        Random r = new Random( );

        final int LEN = 5;
        
        for( int i = 0; i < 100000; i++ )
        {
            String str = "";
            int len = LEN; // 3 + r.nextInt( 7 ); // between 3 and 9 characters

            for( int j = 0; j < len; j++ )
                str += (char) ( 'a' + r.nextInt( 26 ) );

            lst.add( str );
        }

        String [ ] arr1 = new String[ lst.size( ) ];
        /* both array here are being created with length lst.size(), and lst.size()
        does not change in between*/
        @SuppressWarnings("index")
        String @SameLen("arr1") [ ] arr2 = new String[ lst.size( ) ];

        lst.toArray( arr1 );
        lst.toArray( arr2 );

        long start, end;

        start = System.currentTimeMillis( );
        Arrays.sort( arr1 );
        end = System.currentTimeMillis( );
        System.out.println( "Elapsed: " + ( end - start ) );


        start = System.currentTimeMillis( );
        countingRadixSort( arr2, LEN );
        end = System.currentTimeMillis( );
        System.out.println( "Elapsed: " + ( end - start ) );

        for( int i = 0; i < arr1.length; i++ )
            if( !arr1[ i ].equals( arr2[ i ]  ) )
                System.out.println( "OOPS!!" );
    }
    
}
