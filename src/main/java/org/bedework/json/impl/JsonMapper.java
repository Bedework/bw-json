/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl;

import org.bedework.json.JsonException;
import org.bedework.json.JsonRegistration;
import org.bedework.json.model.values.JsonValue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Reader;

/**
 * User: mike Date: 10/23/19 Time: 23:44
 */
public class JsonMapper extends ObjectMapper {
  final JsonFactory factory;

  public JsonMapper() {
    this.factory = JsonFactory.getFactory(new JsonPropertyAttributes());

    setSerializationInclusion(JsonInclude.Include.NON_NULL);
    // configure(JsonFactory.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public JsonMapper(final JsonFactory factory) {
    this.factory = factory;

    setSerializationInclusion(JsonInclude.Include.NON_NULL);
    // configure(JsonFactory.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public void registerTypes(final JsonRegistration val) {
    factory.register(val);
  }

  public JsonFactory getJFactory() {
    return factory;
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
