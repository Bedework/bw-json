/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl;

import org.bedework.json.JsonException;
import org.bedework.json.model.values.JsonValue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Reader;

/**
 * User: mike Date: 10/23/19 Time: 23:44
 */
public class JsonMapper extends ObjectMapper {
  final static JsonFactory factory = JsonFactory.getFactory();

  public JsonMapper() {
    setSerializationInclusion(JsonInclude.Include.NON_NULL);
    // configure(JsonFactory.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public JsonValue parse(final Reader rdr) {
    final JsonNode nd;

    try {
      nd = readTree(rdr);
    } catch (final Throwable t) {
      throw new JsonException(t);
    }

    return factory.makeValue(nd);
  }
}
