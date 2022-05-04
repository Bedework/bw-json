/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values;

import org.bedework.json.impl.JsonFactory;
import org.bedework.json.model.values.JsonUnknownType;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * User: mike Date: 10/25/19 Time: 12:45
 */
public class JsonUnknownTypeImpl extends JsonValueImpl
        implements JsonUnknownType {
  public JsonUnknownTypeImpl(final JsonFactory factory,
                             final String type,
                             final JsonNode node) {
    super(factory, type, node);
  }
}
