package au.com.ko.samples.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holder for a potentially sensitive value such as a password or passcode.
 * Use of this wrapper class helps to prevent accidental exposure in trace logs or audit event records.
 * Register an instance of {@link au.com.ko.samples.prov.SensitiveValueSerializer}
 * to control whether the value gets masked or not.
 */
public class SensitiveValue {

	private final char[] value;

	public SensitiveValue(char[] value) {
		this.value = value;
	}

	public SensitiveValue(String value) {
		this(value != null ? value.toCharArray() : null);
	}

	@JsonCreator
	public static SensitiveValue wrap(@JsonProperty("value") char[] value) {
		if (value != null) {
			return new SensitiveValue(value);
		} else {
			return null;
		}
	}

	/**
	 * Return the sensitive value as a char array.
	 *
	 * @return the sensitive value
	 */
	@JsonIgnore
	public char[] getValue() {
		return value;
	}

}
