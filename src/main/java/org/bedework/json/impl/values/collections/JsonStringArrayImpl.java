/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values.collections;

import org.bedework.json.JsonException;
import org.bedework.json.impl.JsonFactory;
import org.bedework.json.impl.values.dataTypes.JsonStringImpl;
import org.bedework.json.model.JsonTypes;
import org.bedework.json.model.values.dataTypes.JsonString;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * User: mike Date: 5/11/20 Time: 15:30
 */
public class JsonStringArrayImpl extends JsonArrayImpl<JsonString> {
  public JsonStringArrayImpl(final JsonFactory factory,
                             final String type,
                             final JsonNode node) {
    super(factory, type, node);
  }

  @Override
  protected JsonNode convertToElement(final JsonString val) {
    return ((JsonStringImpl)val).getNode();
  }

  @Override
  protected JsonString convertToT(final JsonNode node) {
    if (!(node instanceof TextNode)) {
      throw new JsonException("Bad node class: " + node.getClass());
    }

    return (JsonString)factory.newValue(JsonTypes.typeString, node);
  }
}
