/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values.collections;

import org.bedework.json.JsonException;
import org.bedework.json.impl.JsonFactory;
import org.bedework.json.impl.values.dataTypes.JsonIntegerImpl;
import org.bedework.json.model.JsonTypes;
import org.bedework.json.model.values.dataTypes.JsonInteger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;

/**
 * User: mike Date: 5/11/20 Time: 00:46
 */
public class JsonIntArrayImpl extends JsonArrayImpl<JsonInteger> {
  public JsonIntArrayImpl(final JsonFactory factory,
                          final String type,
                          final JsonNode node) {
    super(factory, type, node);
  }

  @Override
  protected JsonNode convertToElement(final JsonInteger val) {
    return ((JsonIntegerImpl)val).getNode();
  }

  @Override
  protected JsonInteger convertToT(final JsonNode node) {
    if (!(node instanceof IntNode)) {
      throw new JsonException("Bad node class:",
                              node.getClass().getName());
    }

    return (JsonInteger)factory.newValue(JsonTypes.typeInt, node);
  }
}
