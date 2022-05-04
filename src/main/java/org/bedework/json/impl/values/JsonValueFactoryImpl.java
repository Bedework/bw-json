/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values;

import org.bedework.json.JsonValueFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * User: mike Date: 10/25/19 Time: 15:00
 */
public abstract class JsonValueFactoryImpl extends JsonValueFactory {
  /**
   *
   * @param typeName to be added as property
   * @return ObjectNode
   */
  protected JsonNode newObject(final String typeName) {
    final var node = new ObjectNode(JsonNodeFactory.instance);

    node.put("@type", typeName);

    return node;
  }

  /**
   *
   * @param typeName to be added as property
   * @return ObjectNode
   */
  protected JsonNode ensureType(final String typeName,
                                final ObjectNode node) {
    if (node.get("@type") == null) {
      node.put("@type", typeName);
    }

    return node;
  }
}
