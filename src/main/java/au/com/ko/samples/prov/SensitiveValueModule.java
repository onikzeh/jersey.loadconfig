package au.com.ko.samples.prov;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Support registration into an ObjectMapper of the appropriate SensitiveValue serializer instance.
 */
public class SensitiveValueModule extends SimpleModule {

    /**
     * Register a SensitiveValueSerializer with masking turned on or off.
     *
     * @param masking if true, sensitive values are replaced by the masking value during serialization.
     */
    public SensitiveValueModule(boolean masking) {
        super("Sensitive Value Module");
        this.addSerializer(new SensitiveValueSerializer(masking));
    }

    /**
     * Obtain a module instance that registers the SensitiveValueSerializer with masking enabled.
     *
     * @return the masking-enabled sensitive value module.
     */
    public static SensitiveValueModule masking() {
        return new SensitiveValueModule(true);
    }

    /**
     * Obtain a module instance that registers the SensitiveValueSerializer with masking disabled.
     *
     * @return the masking-disabled sensitive value module.
     */
    public static SensitiveValueModule nonMasking() {
        return new SensitiveValueModule(false);
    }
}
