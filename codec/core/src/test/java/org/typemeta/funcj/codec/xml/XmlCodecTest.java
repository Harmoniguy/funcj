package org.typemeta.funcj.codec.xml;

import org.junit.Assert;
import org.typemeta.funcj.codec.*;

import java.io.*;

public class XmlCodecTest extends TestBase {

    @Override
    protected <T> void roundTrip(T val, Class<T> clazz) {
        final XmlCodecCore codec = prepareCodecCore(Codecs.xmlCodec());

        final StringWriter sw = new StringWriter();
        codec.encode(clazz, val, sw);

        if (printData()) {
            System.out.println(sw);
        }

        final String data = sw.toString();

        if (printSizes()) {
            System.out.println("Encoded XML " + clazz.getSimpleName() + " data size = " + data.length() + " chars");
        }

        final StringReader sr = new StringReader(data);

        final T val2 = codec.decode(clazz, sr);

        if (!printData() && !val.equals(val2)) {
            System.out.println(sw);
        }

        Assert.assertEquals(val, val2);
    }
}
