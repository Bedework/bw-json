package org.bedework.json;

import org.bedework.json.impl.JsonFactory;
import org.bedework.json.model.values.JsonValue;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * User: mike Date: 10/25/19 Time: 14:32
 */
public abstract class JsonValueFactory {
  /**
   *
   * @param typeName for value
   * @param nd never null
   * @return new cvalue
   */
  public abstract JsonValue newValue(JsonFactory factory,
                                     String typeName,
                                     JsonNode nd);
}
