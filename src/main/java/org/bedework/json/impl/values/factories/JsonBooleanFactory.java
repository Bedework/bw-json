/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values.factories;

import org.bedework.json.impl.JsonFactory;
import org.bedework.json.impl.values.dataTypes.JsonBooleanImpl;
import org.bedework.json.impl.values.JsonValueFactoryImpl;
import org.bedework.json.model.values.JsonValue;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * User: mike Date: 10/25/19 Time: 14:59
 */
public class JsonBooleanFactory extends JsonValueFactoryImpl {
  @Override
  public JsonValue newValue(final JsonFactory factory,
                            final String typeName,
                            final JsonNode nd) {
    return new JsonBooleanImpl(factory, typeName, nd);
  }
}
