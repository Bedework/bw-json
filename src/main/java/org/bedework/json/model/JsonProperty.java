package org.bedework.json.model;

import org.bedework.json.model.values.JsonValue;

/**
 * User: mike Date: 10/23/19 Time: 13:25
 */
public interface JsonProperty<T extends JsonValue> {
  /**
   *
   * @return String name of the property
   */
  String getName();

  /**
   *
   * @return the type of the value
   */
  String getType();

  /** Returns values as Objects - all int and boolean values will be returned as
   * the corresponding object class.
   *
   * @return the value of the property
   */
  T getValue();

  JsonProperty<T> copy();
}
