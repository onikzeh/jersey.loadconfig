package au.com.ko.samples.prov;

import au.com.ko.samples.protocol.SensitiveValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * JSON serialization support for sensitive values.
 */
public class SensitiveValueSerializer extends StdSerializer<SensitiveValue> {

    public static final String MASK_VALUE = "***";
    private final boolean masking;

    /**
     * Constructor indicating whether sensitive value masking is enabled or not.
     *
     * @param masking true if the value should be replaced by the MASK_VALUE when serializing.
     */
    public SensitiveValueSerializer(boolean masking) {
        super(SensitiveValue.class);
        this.masking = masking;
    }

    /**
     * Serialize the SensitiveValue by writing the char buffer as a string if masking is off,
     * or as the MASK_VALUE if masking is on.
     *
     * @param value    The SensitiveValue to serialize, can be null.
     * @param gen      The JSON generator.
     * @param provider The serializer provider.
     * @throws IOException if the serialization fails.
     */
    @Override
    public void serialize(SensitiveValue value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        char[] val;
        if (value != null && (val = value.getValue()) != null) {
            if (masking) {
                gen.writeString(MASK_VALUE);
            } else {
                gen.writeString(val, 0, val.length);
            }
        } else {
            gen.writeNull();
        }
    }
}
