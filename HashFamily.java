import org.checkerframework.checker.index.qual.*;

public interface HashFamily<AnyType>
{
    int hash( AnyType x, @NonNegative int which );
    @Positive int getNumberOfFunctions( );
    void generateNewFunctions( );
}
