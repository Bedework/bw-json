/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values.dataTypes;

import org.bedework.json.impl.JsonFactory;
import org.bedework.json.impl.values.JsonValueImpl;
import org.bedework.json.model.JsonTypes;
import org.bedework.json.model.values.dataTypes.JsonInteger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;

/**
 * User: mike Date: 5/18/20 Time: 17:50
 */
public class JsonIntegerImpl extends JsonValueImpl
        implements JsonInteger {
  public JsonIntegerImpl(final JsonFactory factory,
                         final int value) {
    super(factory, JsonTypes.typeInt, new IntNode(value));
  }

  public JsonIntegerImpl(final JsonFactory factory,
                         final String typeName,
                         final JsonNode node) {
    super(factory, typeName, node);
    assertIntNode();
  }

  @Override
  public int get() {
    return getNode().asInt();
  }

  @Override
  public int compare(final int that) {
    return Integer.compareUnsigned(get(), that);
  }

  @Override
  public int compare(final JsonInteger that) {
    return Integer.compareUnsigned(get(), that.get());
  }

  public String toString() {
    return String.valueOf(get());
  }
}
