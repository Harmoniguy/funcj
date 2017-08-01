package org.funcj.util;

/**
 * Interfaces for composable functions which throw a specific exception type.
 */
public abstract class FunctionsGenEx {

    /**
     * Function of arity 0.
     * @param <R> return type
     * @param <X> exception type
     */
    @FunctionalInterface
    public interface F0<R, X extends Exception> {
        static <R, X extends Exception> F0<R, X> of(F0<R, X> f) {
            return f;
        }

        static <R, X extends Exception> F0<R, X> konst(R r) {
            return () -> r;
        }

        R apply() throws X;
    }

    /**
     * Function of arity 1.
     * Note: if the input type to {@code F} fixed to type T then the result is a monad,
     * where pure = {@code konst} and bind = {@code flatMap}.
     * @param <A> 1st argument type
     * @param <R> return type
     * @param <X> exception type
     */
    @FunctionalInterface
    public interface F<A, R, X extends Exception> {
        static <A, R, X extends Exception> F<A, R, X> of(F<A, R, X> f) {
            return f;
        }

        static <A, X extends Exception> F<A, A, X> id() {
            return x -> x;
        }

        static <A, R, X extends Exception> F<A, R, X> konst(R r) {
            return a -> r;
        }

        static <A, B, R, X extends Exception> F<B, F<A, R, X>, X> flip(F<A, F<B, R, X>, X> f) {
            return b -> a -> f.apply(a).apply(b);
        }

        R apply(A a) throws X;


        default <T> F<T, R, X> compose(F<? super T, ? extends A, X> f) {
            return t -> this.apply(f.apply(t));
        }

        default <T> F<A, T, X> andThen(F<? super R, ? extends T, X> f) {
            return t -> f.apply(this.apply(t));
        }

        default <U> F<A, U, X> map(F<R, U, X> f) {
            return f.compose(this);
        }

        default <U> F<A, U, X> flatMap(F<R, F<A, U, X>, X> f) {
            return of(s -> f.compose(this).apply(s).apply(s));
        }
    }

    /**
     * Function of arity 2.
     * @param <A> 1st argument type
     * @param <B> 2nd argument type
     * @param <R> return type
     * @param <X> exception type
     */
    @FunctionalInterface
    public interface F2<A, B, R, X extends Exception> {
        static <A, B, R, X extends Exception> F2<A, B, R, X> of(F2<A, B, R, X> f) {
            return f;
        }

        static <A, B, R, X extends Exception> F<A, F<B, R, X>, X> curry(F2<A, B, R, X> f) {
            return f.curry();
        }

        static <A, B, R, X extends Exception> F2<A, B, R, X> uncurry(F<A, F<B, R, X>, X> f) {
            return (a, b) -> f.apply(a).apply(b);
        }

        static <A, B, X extends Exception> F2<A, B, A, X> first() {
            return (a, b) -> a;
        }

        static <A, B, X extends Exception> F2<A, B, B, X> second() {
            return (a, b) -> b;
        }

        R apply(A a, B b) throws X;

        default F<B, R, X> partial(A a) {
            return b -> apply(a, b);
        }

        default F<A, F<B, R, X>, X> curry() {
            return a -> b -> apply(a, b);
        }

        default F2<B, A, R, X> flip() {
            return (b, a) -> apply(a, b);
        }
    }

    /**
     * Function of arity 3.
     * @param <A> 1st argument type
     * @param <B> 2nd argument type
     * @param <C> 3rd argument type
     * @param <R> return type
     * @param <X> exception type
     */
    @FunctionalInterface
    public interface F3<A, B, C, R, X extends Exception> {
        static <A, B, C, R, X extends Exception> F3<A, B, C, R, X>of(F3<A, B, C, R, X> f) {
            return f;
        }

        static <A, B, C, R, X extends Exception> F<A, F<B, F<C, R, X>, X>, X> curry(F3<A, B, C, R, X> f) {
            return f.curry();
        }

        static <A, B, C, R, X extends Exception> F3<A, B, C, R, X> uncurry(F<A, F<B, F<C, R, X>, X>, X> f) {
            return (a, b, c) -> f.apply(a).apply(b).apply(c);
        }

        R apply(A a, B b, C c) throws X;

        default F2<B, C, R, X> partial(A a) {
            return (b, c) -> apply(a, b, c);
        }

        default F<C, R, X> partial(A a, B b) {
            return c -> apply(a, b, c);
        }

        default F<A, F<B, F<C, R, X>, X>, X> curry() {
            return a -> b -> c -> apply(a, b, c);
        }
    }

    /**
     * Function of arity 4.
     * @param <A> 1st argument type
     * @param <B> 2nd argument type
     * @param <C> 3rd argument type
     * @param <D> 4th argument type
     * @param <R> return type
     * @param <X> exception type
     */
    @FunctionalInterface
    public interface F4<A, B, C, D, R, X extends Exception> {
        static <A, B, C, D, R, X extends Exception> F4<A, B, C, D, R, X> of(F4<A, B, C, D, R, X> f) {
            return f;
        }

        static <A, B, C, D, R, X extends Exception> F<A, F<B, F<C, F<D, R, X>, X>, X>, X> curry(F4<A, B, C, D, R, X> f) {
            return f.curry();
        }

        static <A, B, C, D, R, X extends Exception> F4<A, B, C, D, R, X> uncurry(F<A, F<B, F<C, F<D, R, X>, X>, X>, X> f) {
            return (a, b, c, d) -> f.apply(a).apply(b).apply(c).apply(d);
        }

        R apply(A a, B b, C c, D d) throws X;

        default F3<B, C, D, R, X> partial(A a) {
            return (b, c, d) -> apply(a, b, c, d);
        }

        default F2<C, D, R, X> partial(A a, B b) {
            return (c, d) -> apply(a, b, c, d);
        }

        default F<D, R, X> partial(A a, B b, C c) {
            return d -> apply(a, b, c, d);
        }

        default F<A, F<B, F<C, F<D, R, X>, X>, X>, X> curry() {
            return a -> b -> c -> d -> apply(a, b, c, d);
        }
    }

    /**
     * Function of arity 5.
     * @param <A> 1st argument type
     * @param <B> 2nd argument type
     * @param <C> 3rd argument type
     * @param <D> 4th argument type
     * @param <E> 5th argument type
     * @param <R> return type
     * @param <X> exception type
     */
    @FunctionalInterface
    public interface F5<A, B, C, D, E, R, X extends Exception> {
        static <A, B, C, D, E, R, X extends Exception> F5<A, B, C, D, E, R, X> of(F5<A, B, C, D, E, R, X> f) {
            return f;
        }

        static <A, B, C, D, E, R, X extends Exception> F<A, F<B, F<C, F<D, F<E, R, X>, X>, X>, X>, X> curry(F5<A, B, C, D, E, R, X> f) {
            return f.curry();
        }

        static <A, B, C, D, E, R, X extends Exception> F5<A, B, C, D, E, R, X> uncurry(F<A, F<B, F<C, F<D, F<E, R, X>, X>, X>, X>, X> f) {
            return (a, b, c, d, e) -> f.apply(a).apply(b).apply(c).apply(d).apply(e);
        }

        R apply(A a, B b, C c, D d, E e) throws X;

        default F4<B, C, D, E, R, X> partial(A a) {
            return (b, c, d, e) -> apply(a, b, c, d, e);
        }

        default F3<C, D, E, R, X> partial(A a, B b) {
            return (c, d, e) -> apply(a, b, c, d, e);
        }

        default F2<D, E, R, X> partial(A a, B b, C c) {
            return (d, e) -> apply(a, b, c, d, e);
        }

        default F<E, R, X> partial(A a, B b, C c, D d) {
            return e -> apply(a, b, c, d, e);
        }

        default F<A, F<B, F<C, F<D, F<E, R, X>, X>, X>, X>, X> curry() {
            return a -> b -> c -> d -> e -> apply(a, b, c, d, e);
        }
    }

    /**
     * Function of arity 6.
     * @param <A> 1st argument type
     * @param <B> 2nd argument type
     * @param <C> 3rd argument type
     * @param <D> 4th argument type
     * @param <E> 5th argument type
     * @param <G> 6th argument type
     * @param <R> return type
     * @param <X> exception type
     */
    @FunctionalInterface
    public interface F6<A, B, C, D, E, G, R, X extends Exception> {
        static <A, B, C, D, E, G, R, X extends Exception> F6<A, B, C, D, E, G, R, X> of(F6<A, B, C, D, E, G, R, X> f) {
            return f;
        }

        static <A, B, C, D, E, G, R, X extends Exception> F<A, F<B, F<C, F<D, F<E, F<G, R, X>, X>, X>, X>, X>, X> curry(F6<A, B, C, D, E, G, R, X> f) {
            return f.curry();
        }

        static <A, B, C, D, E, G, R, X extends Exception> F6<A, B, C, D, E, G, R, X> uncurry(F<A, F<B, F<C, F<D, F<E, F<G, R, X>, X>, X>, X>, X>, X> f) {
            return (a, b, c, d, e, g) -> f.apply(a).apply(b).apply(c).apply(d).apply(e).apply(g);
        }

        R apply(A a, B b, C c, D d, E e, G g) throws X;

        default F5<B, C, D, E, G, R, X> partial(A a) {
            return (b, c, d, e, g) -> apply(a, b, c, d, e, g);
        }

        default F4<C, D, E, G, R, X> partial(A a, B b) {
            return (c, d, e, g) -> apply(a, b, c, d, e, g);
        }

        default F3<D, E, G, R, X> partial(A a, B b, C c) {
            return (d, e, g) -> apply(a, b, c, d, e, g);
        }

        default F2<E, G, R, X> partial(A a, B b, C c, D d) {
            return (e, g) -> apply(a, b, c, d, e, g);
        }

        default F<G, R, X> partial(A a, B b, C c, D d, E e) {
            return g -> apply(a, b, c, d, e, g);
        }

        default F<A, F<B, F<C, F<D, F<E, F<G, R, X>, X>, X>, X>, X>, X> curry() {
            return a -> b -> c -> d -> e -> g -> apply(a, b, c, d, e, g);
        }
    }

    /**
     * Unary operator interface.
     * @param <T> operand type
     * @param <X> exception type
     */
    @FunctionalInterface
    public interface Op<T, X extends Exception> extends F<T, T, X> {
        T apply(T t) throws X;
    }

    /**
     * Binary operator interface.
     * @param <T> operand type
     * @param <X> exception type
     */
    @FunctionalInterface
    public interface Op2<T, X extends Exception> extends F2<T, T, T, X> {
        T apply(T l, T r) throws X;

        default Op2<T, X> flip() {
            return (b, a) -> apply(a, b);
        }
    }

    /**
     * Predicate interface
     * @param <T> operand type
     * @param <X> exception type
     */
    @FunctionalInterface
    public interface Predicate<T, X extends Exception> extends F<T, Boolean, X> {
        Boolean apply(T t) throws X;
    }
}