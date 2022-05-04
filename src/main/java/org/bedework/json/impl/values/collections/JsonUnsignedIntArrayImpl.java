/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values.collections;

import org.bedework.json.JsonException;
import org.bedework.json.impl.JsonFactory;
import org.bedework.json.impl.values.dataTypes.JsonIntegerImpl;
import org.bedework.json.model.JsonTypes;
import org.bedework.json.model.values.dataTypes.JsonUnsignedInteger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;

/**
 * User: mike Date: 5/11/20 Time: 00:46
 */
public class JsonUnsignedIntArrayImpl
        extends JsonArrayImpl<JsonUnsignedInteger> {
  public JsonUnsignedIntArrayImpl(final JsonFactory factory,
                                  final String type,
                                  final JsonNode node) {
    super(factory, type, node);
  }

  @Override
  protected JsonNode convertToElement(final JsonUnsignedInteger val) {
    return ((JsonIntegerImpl)val).getNode();
  }

  @Override
  protected JsonUnsignedInteger convertToT(final JsonNode node) {
    if (!(node instanceof IntNode)) {
      throw new JsonException("Bad node class: " + node.getClass());
    }

    return (JsonUnsignedInteger)factory.newValue(
            JsonTypes.typeUnsignedInt, node);
  }
}
