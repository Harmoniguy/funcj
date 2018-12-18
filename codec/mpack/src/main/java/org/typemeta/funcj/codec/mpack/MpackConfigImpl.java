package org.typemeta.funcj.codec.mpack;

import org.typemeta.funcj.codec.CodecConfigImpl;
import org.typemeta.funcj.codec.utils.NotSupportedException;

/**
 * Base class for {@link MpackTypes.Config} implementations.
 */
public class MpackConfigImpl extends CodecConfigImpl implements MpackTypes.Config {
    @Override
    public void dynamicTypeTags(boolean enable) {
        throw new NotSupportedException();
    }

    @Override
    public void failOnNoTypeConstructor(boolean enable) {
        throw new NotSupportedException();
    }
}
