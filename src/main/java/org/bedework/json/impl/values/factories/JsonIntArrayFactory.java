/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values.factories;

import org.bedework.json.impl.JsonFactory;
import org.bedework.json.impl.values.JsonValueFactoryImpl;
import org.bedework.json.impl.values.collections.JsonIntArrayImpl;
import org.bedework.json.model.values.JsonValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * User: mike Date: 10/25/19 Time: 14:59
 */
public class JsonIntArrayFactory extends JsonValueFactoryImpl {
  @Override
  public JsonValue newValue(final JsonFactory factory,
                            final String typeName,
                            final JsonNode nd) {
    if (nd != null) {
      return new JsonIntArrayImpl(factory, typeName, nd);
    }

    return new JsonIntArrayImpl(factory, typeName,
                                new ObjectNode(JsonNodeFactory.instance));
  }
}
