/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.properties;

import org.bedework.json.impl.JsonFactory;
import org.bedework.json.impl.values.JsonValueImpl;
import org.bedework.json.model.JsonProperty;
import org.bedework.json.model.values.JsonValue;

/**
 * User: mike Date: 10/23/19 Time: 23:36
 */
public class JsonPropertyImpl<T extends JsonValue>
        implements JsonProperty<T> {
  private final JsonFactory factory;

  private final String name;
  private final T value;

  public JsonPropertyImpl(final JsonFactory factory,
                          final String name,
                          final T value) {
    this.factory = factory;
    this.name = name;
    this.value = value;
    ((JsonValueImpl)value).setParentProperty(this);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    if (value == null) {
      return null;
    }
    return value.getType();
  }

  @Override
  public T getValue() {
    return value;
  }

  @Override
  public JsonProperty<T> copy() {
    return new JsonPropertyImpl<T>(factory,
                                   getName(),
                                   (T)getValue().copy());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof JsonPropertyImpl)) {
      return false;
    }

    final var that = (JsonPropertyImpl<?>)o;

    if (!name.equals(that.name)) {
      return false;
    }

    return getValue().equals(that.getValue());
  }
}
