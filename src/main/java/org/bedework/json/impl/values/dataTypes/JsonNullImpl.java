/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values.dataTypes;

import org.bedework.json.impl.JsonFactory;
import org.bedework.json.impl.values.JsonValueImpl;
import org.bedework.json.model.JsonTypes;
import org.bedework.json.model.values.JsonNull;

import com.fasterxml.jackson.databind.node.NullNode;

/**
 * User: mike Date: 10/25/19 Time: 12:45
 */
public class JsonNullImpl extends JsonValueImpl
        implements JsonNull {
  public JsonNullImpl(final JsonFactory factory) {
    super(factory, JsonTypes.typeNull, NullNode.getInstance());
  }
}
