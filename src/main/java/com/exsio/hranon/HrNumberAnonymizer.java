package com.exsio.hranon;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import static com.exsio.hranon.HrAnonUtil.randomInt;

class HrNumberAnonymizer {

    private interface Anonymizer<T extends Number> extends Function<T, T> {
    }

    private static final Map<Class<? extends Number>, Anonymizer<? extends Number>> ANONYMIZERS = new HashMap<>();

    static {
        ANONYMIZERS.put(Integer.class, (Anonymizer<Integer>) source -> Integer.parseInt(anonymizeNumber(Integer.toString(source))));
        ANONYMIZERS.put(AtomicInteger.class, (Anonymizer<AtomicInteger>) source -> new AtomicInteger(Integer.parseInt(anonymizeNumber(Integer.toString(source.get())))));
        ANONYMIZERS.put(Long.class, (Anonymizer<Long>) source -> Long.parseLong(anonymizeNumber(Long.toString(source))));
        ANONYMIZERS.put(AtomicLong.class, (Anonymizer<AtomicLong>) source -> new AtomicLong(Long.parseLong(anonymizeNumber(Long.toString(source.get())))));
        ANONYMIZERS.put(Short.class, (Anonymizer<Short>) source -> Short.parseShort(anonymizeNumber(Short.toString(source))));
        ANONYMIZERS.put(Byte.class, (Anonymizer<Byte>) source -> Byte.parseByte(anonymizeNumber(Byte.toString(source))));
        ANONYMIZERS.put(Float.class, (Anonymizer<Float>) source -> Float.parseFloat(anonymizeNumber(Float.toString(source))));
        ANONYMIZERS.put(Double.class, (Anonymizer<Double>) source -> Double.parseDouble(anonymizeNumber(Double.toString(source))));
        ANONYMIZERS.put(BigDecimal.class, (Anonymizer<BigDecimal>) source -> new BigDecimal(anonymizeNumber(source.toPlainString())));
        ANONYMIZERS.put(BigInteger.class, (Anonymizer<BigInteger>) source -> new BigInteger(anonymizeNumber(source.toString())));
    }

    @SuppressWarnings("unchecked")
    static <T extends Number> T anonymize(T source) {
        if (!ANONYMIZERS.containsKey(source.getClass())) {
            throw new RuntimeException("Unsupported Number Class: " + source.getClass());
        }
        Anonymizer<Number> anonymizer = (Anonymizer<Number>) ANONYMIZERS.get(source.getClass());
        return (T) anonymizer.apply(source);
    }

    static String anonymizeNumber(String source) {
        var replacement = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            if (source.charAt(i) == '.') {
                replacement.append('.');
            } else {
                if (i == 0 || i == source.length() - 1) {
                    int random = randomInt(8) + 1;
                    replacement.append(random);
                } else {
                    int random = randomInt(9);
                    replacement.append(random);
                }

            }
        }
        var result = replacement.toString();
        if (result.equals(source)) {
            return anonymizeNumber(source);
        }
        return result;
    }
}
