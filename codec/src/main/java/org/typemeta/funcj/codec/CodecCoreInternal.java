package org.typemeta.funcj.codec;

import org.typemeta.funcj.functions.Functions;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

/**
 * Internal interface for {@link CodecCore} implementations.
 * @param <IN>      the encoded input type
 * @param <OUT>     the encoded output type
 */
public interface CodecCoreInternal<IN, OUT> extends CodecCore<IN, OUT> {

    <T> Class<T> nameToClass(String name);

    String classToName(Class<?> clazz);

    String classToName(Class<?> clazz, Class<?>... classes);

    <X> Class<X> remapType(Class<X> type);

    <T> TypeConstructor<T> getTypeConstructor(Class<T> clazz);

    <T> boolean encodeNull(T val, OUT out);

    boolean decodeNull(IN in);

    <T> boolean encodeDynamicType(
            Codec<T, IN, OUT> codec,
            T val,
            OUT out,
            Functions.F<Class<T>, Codec<T, IN, OUT>> getDynCodec);

    default <T> boolean encodeDynamicType(Codec<T, IN, OUT> codec, T val, OUT out) {
        return encodeDynamicType(codec, val, out, this::getCodec);
    }

    <T> T decodeDynamicType(IN in, Functions.F<String, T> decoder);

    <T> T decodeDynamicType(IN in);

    Codec.BooleanCodec<IN, OUT> booleanCodec();

    Codec<boolean[], IN, OUT> booleanArrayCodec();

    Codec.ByteCodec<IN, OUT> byteCodec();

    Codec<byte[], IN, OUT> byteArrayCodec();

    Codec.CharCodec<IN, OUT> charCodec();

    Codec<char[], IN, OUT> charArrayCodec();

    Codec.ShortCodec<IN, OUT> shortCodec();

    Codec<short[], IN, OUT> shortArrayCodec();

    Codec.IntCodec<IN, OUT> intCodec();

    Codec<int[], IN, OUT> intArrayCodec();

    Codec.LongCodec<IN, OUT> longCodec();

    Codec<long[], IN, OUT> longArrayCodec();

    Codec.FloatCodec<IN, OUT> floatCodec();

    Codec<float[], IN, OUT> floatArrayCodec();

    Codec.DoubleCodec<IN, OUT> doubleCodec();

    Codec<double[], IN, OUT> doubleArrayCodec();

    Codec<String, IN, OUT> stringCodec();

    <EM extends Enum<EM>> Codec<EM, IN, OUT> enumCodec(Class<EM> enumType);

    <T> Codec<T, IN, OUT> getCodec(Class<T> type);

    <T> Codec<T, IN, OUT> getCodec(
            String name,
            Supplier<Codec<T, IN, OUT>> supp);

    <T> Codec<Collection<T>, IN, OUT> getCollCodec(
            Class<Collection<T>> collType,
            Codec<T, IN, OUT> elemCodec);

    <K, V> Codec<Map<K, V>, IN, OUT> getMapCodec(
            Class<Map<K, V>> mapType,
            Class<K> keyType,
            Class<V> valType);

    <V> Codec<Map<String, V>, IN, OUT> getMapCodec(
            Class<Map<String, V>> mapType,
            Class<V> valType);

    <K, V> Codec<Map<K, V>, IN, OUT> getMapCodec(
            Class<Map<K, V>> mapType,
            Codec<K, IN, OUT> keyCodec,
            Codec<V, IN, OUT> valueCodec);

    <V> Codec<Map<String, V>, IN, OUT> getMapCodec(
            Class<Map<String, V>> mapType,
            Codec<V, IN, OUT> valueCodec);

    <T> Codec<T, IN, OUT> createCodec(Class<T> type);

    <T> Codec<T, IN, OUT> createObjectCodec(
            Class<T> type,
            Map<String, FieldCodec<IN, OUT>> fieldCodecs);

    <K, V> Codec<Map<K, V>, IN, OUT> createMapCodec(
            Class<Map<K, V>> mapType,
            Class<K> keyType,
            Class<V> valType);

    <V> Codec<Map<String, V>, IN, OUT> createMapCodec(
            Class<Map<String, V>> type,
            Class<V> valType);

    <V> Codec<Map<String, V>, IN, OUT> createMapCodec(
            Class<Map<String, V>> type,
            Codec<V, IN, OUT> valueCodec);

    <K, V> Codec<Map<K, V>, IN, OUT> createMapCodec(
            Class<Map<K, V>> type,
            Codec<K, IN, OUT> keyCodec,
            Codec<V, IN, OUT> valueCodec);

    <T> Codec<Collection<T>, IN, OUT> createCollCodec(
            Class<Collection<T>> collType,
            Codec<T, IN, OUT> elemCodec);

    <T> Codec<T[], IN, OUT> createObjectArrayCodec(
            Class<T[]> arrType,
            Class<T> elemType,
            Codec<T, IN, OUT> elemCodec);

    <T> ObjectCodecBuilder<T, IN, OUT> createObjectCodecBuilder(Class<T> type);

    <T> ObjectCodecBuilder<T, IN, OUT> objectCodecDeferredRegister(Class<T> type);

    <T> Codec<T, IN, OUT> createObjectCodec(Class<T> type);

    <T> Codec<T, IN, OUT> createObjectCodec(
            Class<T> type,
            Map<String, ObjectCodecBuilder.FieldCodec<T, IN, OUT>> fieldCodecs,
            Functions.F<Object[], T> ctor);

    <T, RA extends ObjectMeta.ResultAccumlator<T>> Codec<T, IN, OUT> createObjectCodec(
            Class<T> type,
            ObjectMeta<T, IN, OUT, RA> objMeta);

    String getFieldName(Field field, int depth, Set<String> existingNames);

    <T> FieldCodec<IN, OUT> createFieldCodec(Field field);

    abstract class ObjectMeta<T, IN, OUT, RA extends CodecCoreInternal.ObjectMeta.ResultAccumlator<T>>
            implements Iterable<CodecCoreInternal.ObjectMeta.Field<T, IN, OUT, RA>> {
        public interface ResultAccumlator<T> {
            T construct();
        }

        public interface Field<T, IN, OUT, RA> {
            String name();
            OUT encodeField(T val, OUT out);
            RA decodeField(RA acc, IN in);
        }

        public abstract RA startDecode();

        public Stream<BaseCodecCore.ObjectMeta.Field<T, IN, OUT, RA>> stream() {
            return StreamSupport.stream(spliterator(), false);
        }

        public abstract int size();
    }
}
