/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values.dataTypes;

import org.bedework.json.impl.JsonFactory;
import org.bedework.json.impl.values.JsonValueImpl;
import org.bedework.json.model.JsonTypes;
import org.bedework.json.model.values.dataTypes.JsonString;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * User: mike Date: 5/18/20 Time: 17:50
 */
public class JsonStringImpl extends JsonValueImpl
        implements JsonString {
  public JsonStringImpl(final JsonFactory factory,
                        final String value) {
    super(factory, JsonTypes.typeString, new TextNode(value));
  }

  public JsonStringImpl(final JsonFactory factory,
                        final String typeName,
                        final JsonNode node) {
    super(factory, typeName, node);
    assertStringNode();
  }

  @Override
  public String get() {
    return getNode().asText();
  }

  @Override
  public int compare(final String that) {
    return get().compareTo(that);
  }

  @Override
  public int compare(final JsonString that) {
    return get().compareTo(that.get());
  }

  public String toString() {
    return get();
  }
}
