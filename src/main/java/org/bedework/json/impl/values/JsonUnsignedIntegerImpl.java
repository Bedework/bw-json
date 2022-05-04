/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values;

import org.bedework.json.JsonException;
import org.bedework.json.impl.JsonFactory;
import org.bedework.json.model.JsonTypes;
import org.bedework.json.model.values.dataTypes.JsonUnsignedInteger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;

/**
 * User: mike Date: 5/18/20 Time: 17:50
 */
public class JsonUnsignedIntegerImpl extends JsonValueImpl
        implements JsonUnsignedInteger {
  public JsonUnsignedIntegerImpl(final JsonFactory factory,
                                 final int value) {
    super(factory, JsonTypes.typeUnsignedInt, new IntNode(value));
    if (value < 0) {
      throw new JsonException("Value < 0 for unsigned int:" + value);
    }
  }

  public JsonUnsignedIntegerImpl(final JsonFactory factory,
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
  public int compare(final JsonUnsignedInteger that) {
    return Integer.compareUnsigned(get(), that.get());
  }

  public String toString() {
    return String.valueOf(get());
  }

  @Override
  public int compareTo(final JsonUnsignedInteger that) {
    if (that == null) {
      return 1;
    }

    return Integer.compare(get(), that.get());

  }
}
