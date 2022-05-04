/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values.dataTypes;

import org.bedework.json.impl.JsonFactory;
import org.bedework.json.impl.values.JsonValueImpl;
import org.bedework.json.model.JsonTypes;
import org.bedework.json.model.values.dataTypes.JsonBoolean;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;

/**
 * User: mike Date: 5/18/20 Time: 17:50
 */
public class JsonBooleanImpl extends JsonValueImpl
        implements JsonBoolean {
  public JsonBooleanImpl(final JsonFactory factory,
                         final boolean value) {
    super(factory, JsonTypes.typeBoolean,
          value ? BooleanNode.TRUE: BooleanNode.FALSE);
  }

  public JsonBooleanImpl(final JsonFactory factory,
                         final String typeName,
                         final JsonNode node) {
    super(factory, typeName, node);
    assertBooleanNode();
  }

  @Override
  public boolean get() {
    return getNode().asBoolean();
  }

  public String toString() {
    return String.valueOf(get());
  }
}
