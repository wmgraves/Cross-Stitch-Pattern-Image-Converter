package IO;

/**
 * Used for storing floss information
 * @param <T> The floss code
 * @param <U> The floss description
 * @param <V> Whether the floss is currently in stock
 */
public class Triplet<T, U, V> {
    private final T first;
    private final U second;
    private final V third;

    public Triplet(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }
    public V getThird() { return third; }
}