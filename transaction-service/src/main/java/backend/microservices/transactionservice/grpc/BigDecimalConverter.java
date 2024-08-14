package backend.microservices.transactionservice.grpc;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDecimalConverter {

    public static main.proto.BigDecimal toProto(BigDecimal value) {
        main.proto.BigDecimal.Builder builder = main.proto.BigDecimal.newBuilder();
        if (value != null) {
            builder.setUnscaledValue(value.unscaledValue().longValue())
                    .setScale(value.scale());
        }
        return builder.build();
    }

    public static BigDecimal fromProto(main.proto.BigDecimal proto) {
        if (proto == null) {
            return null;
        }
        return new BigDecimal(new BigInteger(Long.toString(proto.getUnscaledValue())), proto.getScale());
    }
}
