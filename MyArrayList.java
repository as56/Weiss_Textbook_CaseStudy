import org.checkerframework.checker.index.qual.*;
import java.lang.*;
public class MyArrayList<AnyType> implements Iterable<AnyType>
{
    /**
     * Construct an empty ArrayList.
     */
    public MyArrayList( )
    {
        doClear( );
    }
    
    /**
     * Returns the number of items in this collection.
     * @return the number of items in this collection.
     */
    public @IndexOrHigh("theItems") int size( )
    {
        return theSize;
    }
    
    /**
     * Returns true if this collection is empty.
     * @return true if this collection is empty.
     */ 
    public boolean isEmpty( )
    {
        return size( ) == 0;
    }
    
    /**
     * Returns the item at position idx.
     * @param idx the index to search in.
     * @throws ArrayIndexOutOfBoundsException if index is out of range.
     */
    public AnyType get(@IndexFor("theItems") int idx )
    {
        if( idx < 0 || idx >= size( ) )
            throw new ArrayIndexOutOfBoundsException( "Index " + idx + "; size " + size( ) );
        return theItems[ idx ];    
    }
        
    /**
     * Changes the item at position idx.
     * @param idx the index to change.
     * @param newVal the new value.
     * @return the old value.
     * @throws ArrayIndexOutOfBoundsException if index is out of range.
     */
    public AnyType set(@IndexFor("theItems") int idx, AnyType newVal )
    {
        if( idx < 0 || idx >= size( ) )
            throw new ArrayIndexOutOfBoundsException( "Index " + idx + "; size " + size( ) );
        AnyType old = theItems[ idx ];    
        theItems[ idx ] = newVal;
        
        return old;    
    }

    
    public void ensureCapacity(@NonNegative int newCapacity )
    {
        if( newCapacity < theSize )
            return;

        AnyType @SameLen("theItems")[ ] old = theItems;
        theItems = (AnyType []) new Object[ newCapacity ];
        for( int i = 0; i < size( ); i++ )
            theItems[ i ] = old[ i ];
    }
    
    /**
     * Adds an item to this collection, at the end.
     * @param x any object.
     * @return true.
     */
    public boolean add( AnyType x )
    {
    add( size( ), x );
        return true;            
    }
    
    /**
     * Adds an item to this collection, at the specified index.
     * @param x any object.
     * @return true.
     */
    /*the following code is correct because when the ensure
    capacity method expands the 'theItems' array if the 
    'theSize' variable is == the length of the array
    theSize will be a valid index for the array. accordingly
    idx which is atmost equal to 'theSize' will also be a valid
    index for the array*/
    @SuppressWarnings("index")
    public void add(@IndexOrHigh("theItems") int idx, AnyType x )
    {
        if( theItems.length == size( ) )
            ensureCapacity( size( ) * 2 + 1 );

        for( int i = theSize; i > idx; i-- ) // warning here
            theItems[ i ] = theItems[ i - 1 ];

        theItems[ idx ] = x;
        theSize++;  
    }
      
    /**
     * Removes an item from this collection.
     * @param idx the index of the object.
     * @return the item was removed from the collection.
     */
    public AnyType remove(@IndexFor("theItems") int idx )
    {
        AnyType removedItem = theItems[ idx ];
        
        for( int i = idx; i < size( ) - 1; i++ )
            theItems[ i ] = theItems[ i + 1 ];
        theSize--;    
        
        return removedItem;
    }
    
    /**
     * Change the size of this collection to zero.
     */
    public void clear( )
    {
        doClear( );
    }
    
    private void doClear( )
    {
        theSize = 0;
        ensureCapacity( DEFAULT_CAPACITY );
    }
    
    /**
     * Obtains an Iterator object used to traverse the collection.
     * @return an iterator positioned prior to the first element.
     */
    public java.util.Iterator<AnyType> iterator( )
    {
        return new ArrayListIterator( );
    }

    /**
     * Returns a String representation of this collection.
     */
    public String toString( )
    {
            StringBuilder sb = new StringBuilder( "[ " );

            for( AnyType x : this )
                sb.append( x + " " );
            sb.append( "]" );

            return new String( sb );
    }
    
    /**
     * This is the implementation of the ArrayListIterator.
     * It maintains a notion of a current position and of
     * course the implicit reference to the MyArrayList.
     */
    private class ArrayListIterator implements java.util.Iterator<AnyType>
    {
	/*the index checker issues a warning here becuase 'theItems' could be 
        an array of length 0, but the hasNext method in this class ensures that
        current remains an index for 'theItems by ensuring the upperbound'*/
	@SuppressWarnings("index")
        @IndexFor("theItems") private int current = 0;
        private boolean okToRemove = false;
        
        public boolean hasNext( )
        {
            return current < size( );
        }
        
        
        public AnyType next( )
        {
            if( !hasNext( ) ) 
                throw new java.util.NoSuchElementException( ); 
                  
            okToRemove = true;    
            return theItems[ current++ ];
        }

        /*the following code is correct because the okToRemove variable
        ensures that --current is a valid index of 'theItems'*/
        @SuppressWarnings("index") 
        public void remove( )
        {
            if( !okToRemove )
                throw new IllegalStateException( );
           
	    if(current >= 0) {
            	MyArrayList.this.remove( --current );
	    }
            okToRemove = false;
        }
    }
    
    @Positive private static final int DEFAULT_CAPACITY = 10;
    
    private AnyType [ ] theItems;
    @IndexOrHigh("this.theItems") private int theSize;
}


