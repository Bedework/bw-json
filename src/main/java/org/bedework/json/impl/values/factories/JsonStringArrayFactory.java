/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values.factories;

import org.bedework.json.impl.JsonFactory;
import org.bedework.json.impl.values.JsonValueFactoryImpl;
import org.bedework.json.impl.values.collections.JsonStringArrayImpl;
import org.bedework.json.model.values.JsonValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * User: mike Date: 10/25/19 Time: 14:59
 */
public class JsonStringArrayFactory extends JsonValueFactoryImpl {
  @Override
  public JsonValue newValue(final JsonFactory factory,
                            final String typeName,
                            final JsonNode nd) {
    if (nd != null) {
      return new JsonStringArrayImpl(factory, typeName, nd);
    }

    return new JsonStringArrayImpl(factory, typeName,
                                   new ArrayNode(JsonNodeFactory.instance));
  }
}
