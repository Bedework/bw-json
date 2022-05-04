/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl.values;

import org.bedework.json.JsonException;
import org.bedework.json.impl.JsonFactory;
import org.bedework.json.model.JsonProperty;
import org.bedework.json.model.values.JsonValue;
import org.bedework.json.model.values.dataTypes.JsonString;
import org.bedework.json.model.values.dataTypes.JsonUnsignedInteger;
import org.bedework.util.misc.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mike Date: 10/24/19 Time: 10:35
 *
 * Provides a view of the current json node and allows updates
 * to that node.
 *
 * <p>
 * When handling overrides we interact with the masterCopy.
 * However the original node is still attached to the parent
 * so will be output when serializing the object.
 * </p>
 *
 * <p>
 *   When we update an override we need to update the original
 *   node with the appropriate patches.
 * </p>
 */
public abstract class JsonValueImpl implements JsonValue {
  protected final JsonFactory factory;

  protected final String type;

  // Parsed or updated value
  private JsonNode node;

  // This is set when processing overrides.
  private JsonNode masterCopy;

  private JsonProperty<?> parentProperty;

  /* childProperties is only updated when a property is modified or
     removed.
   */
  private final Map<String, JsonProperty<?>> childProperties =
          new HashMap<>();

  private JsonValue owner;

  private boolean changed;

  private boolean overrideGenerated;

  public JsonValueImpl(final JsonFactory factory,
                       final String type,
                       final JsonNode node) {
    if (node == null) {
      throw new JsonException("Null node value");
    }
    if (type == null) {
      throw new JsonException("Null value type for " + node);
    }

    this.factory = factory;
    this.type = type;
    this.node = node;
  }

  /**
   * Only used for proxies.
   * /
  protected JsonValueImpl() {
    type = null;
    node = null;
  }*/

  public String getObjectType() {
    return type;
  }

  public JsonNode getNode() {
    if (masterCopy != null) {
      return masterCopy;
    }

    return node;
  }

  @Override
  public void preWrite() {
    for (final var p: getProperties()) {
      p.getValue().preWrite();
    }
  }

  protected JsonFactory getFactory() {
    return factory;
  }

  /** The owner is the value containing this value.
   *
   * @param val the owner of this value
   */
  public void setOwner(final JsonValue val) {
    if (owner != null) {
      throw new JsonException("Value owner already set");
    }
    owner = val;
  }

  public JsonValue getOwner() {
    return owner;
  }

  /** The owner is the property containing the value.
   *
   * @param val the property containing the value
   */
  public void setParentProperty(final JsonProperty<?> val) {
    if (parentProperty != null) {
      throw new JsonException("Value property already set");
    }
    parentProperty = val;
  }

  public JsonProperty<?> getParentProperty() {
    return parentProperty;
  }

  @Override
  public boolean getChanged() {
    return changed;
  }

  @Override
  public boolean hasChanges() {
    if (changed) {
      return true;
    }

    for (final var prop: childProperties.values()) {
      if (prop.getValue().hasChanges()) {
        return true;
      }
    }

    return false;
  }

  protected void setMasterCopy(final JsonNode copyNode) {
    masterCopy = copyNode;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public boolean hasProperty(final String propertyName) {
    return getNode().has(propertyName);
  }

  @Override
  public List<JsonProperty<?>> getProperties() {
    final var nd = getNode();

    if (!nd.isObject()) {
      return Collections.emptyList();
    }

    final var props = new ArrayList<JsonProperty<?>>();

    for (final var it = nd.fieldNames(); it.hasNext(); ) {
      final var fieldName = it.next();

//      final var p = factory.makeProperty(fieldName,
//                                         getNode().get(fieldName));
      final var p = getProperty(fieldName);

      props.add(p);
    }

    return props;
  }

  @Override
  public <T extends JsonValue> JsonProperty<T> getProperty(
          final TypeReference<T> type,
          final String name) {
    // Return if it's in childrenProperties
    JsonProperty<T> p= (JsonProperty<T>)childProperties.get(name);
    if (p != null) {
      return p;
    }

    assertObject("getProperty");

    final var pnode = getNode().get(name);

    if (pnode == null) {
      return null;
    }

    p = (JsonProperty<T>)makeProperty(name, pnode);
    ((JsonValueImpl)p.getValue()).setOwner(this);
    childProperties.put(name, p);

    return p;
  }

  @Override
  public JsonProperty<?> getProperty(final String name) {
    // Return if it's in childrenProperties
    JsonProperty<?> p= childProperties.get(name);
    if (p != null) {
      return p;
    }

    assertObject("getProperty");

    final var pnode = getNode().get(name);

    if (pnode == null) {
      return null;
    }

    p = makeProperty(name, pnode);
    ((JsonValueImpl)p.getValue()).setOwner(this);
    childProperties.put(name, p);

    return p;
  }

  @Override
  public JsonValue getPropertyValue(final String name) {
    final var prop = getProperty(new TypeReference<>() {}, name);

    if (prop == null) {
      return null;
    }

    return prop.getValue();
  }

  @Override
  public String getStringProperty(final String name) {
    final var prop = getProperty(new TypeReference<>() {},name);

    if (prop == null) {
      return null;
    }

    return prop.getValue().getStringValue();
  }

  @Override
  public boolean getBooleanProperty(final String name) {
    final var prop = getProperty(new TypeReference<>() {},name);

    if (prop == null) {
      return false;
    }

    return prop.getValue().getBooleanValue();
  }

  @Override
  public boolean isString() {
    return getNode().isTextual();
  }

  @Override
  public JsonUnsignedInteger getUnsignedIntegerProperty(final String name) {
    final var prop = getProperty(new TypeReference<>() {},name);

    if (prop == null) {
      return null;
    }

    final var val = ((JsonValueImpl)prop.getValue()).getNode();
    return new JsonUnsignedIntegerImpl(factory, val.intValue());
  }

  @Override
  public String getStringValue() {
    if (getNode().isTextual()) {
      return getNode().textValue();
    }

    throw new JsonException("Not String value");
  }

  @Override
  public boolean getBooleanValue() {
    if (getNode().isBoolean()) {
      return getNode().asBoolean();
    }

    throw new JsonException("Not boolean value");
  }

  @Override
  public <T extends JsonValue> T getValue(
          final TypeReference<T> type,
          final String pname,
          final boolean create) {
    JsonProperty<T> p = getProperty(new TypeReference<>() {}, pname);

    if (p == null) {
      if (!create) {
        return null;
      }

      p = (JsonProperty<T>)addProperty(factory.makeProperty(pname));
    }

    return p.getValue();
  }

  @Override
  public void writeValue(final Writer wtr,
                         final ObjectMapper mapper) {
    preWrite();
    try {
      mapper.writeValue(wtr, getNode());
    } catch (final Throwable t) {
      throw new JsonException(t);
    }
  }

  @Override
  public String writeValueAsString(final ObjectMapper mapper) {
    preWrite();
    try {
      return mapper.writeValueAsString(getNode());
    } catch (final JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  @Override
  public String writeValueAsStringFormatted(final ObjectMapper mapper) {
    preWrite();
    try {
      return mapper.writerWithDefaultPrettyPrinter()
                   .writeValueAsString(getNode());
    } catch (final JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  @Override
  public <T extends JsonValue> JsonProperty<T> newProperty(
          final TypeReference<T> typeRef,
          final String name,
          final String type) {
    return getFactory().makeProperty(name,
                                     type,
                                     null);
  }

  /* ---------------------------- Write methods ------------------- */

  @Override
  public void removeProperty(final String name) {
    assertObject("removeProperty");
    changed = true;

    ((ObjectNode)getNode()).remove(name);
    childProperties.put(name, null);
  }

  @Override
  public void clear() {
    assertObject("clear");
    changed = true;

    ((ObjectNode)getNode()).removeAll();
    childProperties.clear();
  }

  @Override
  public <ValType extends JsonValue> JsonProperty<ValType> setProperty(
          final JsonProperty<ValType> val) {
    final var name = val.getName();

    final var prop = getProperty(new TypeReference<>() {}, name);

    if (prop != null) {
      if (prop.equals(val)) {
        return val;
      }
      // Update the property with the value
      return (JsonProperty<ValType>)updateProperty(prop,
                                                 val);
    }

    return addProperty(val);
  }

  @Override
  public JsonProperty<JsonString> setProperty(final String name,
                                              final String val) {
    return setProperty(factory.makeProperty(name, val));
  }

  @Override
  public JsonProperty<?> setProperty(final String name,
                                   final JsonValue val) {
    return setProperty(factory.makeProperty(name, val));
  }

  @Override
  public JsonProperty<?> setProperty(final String name,
                                   final Integer val) {
    return setProperty(factory.makeProperty(name, val));
  }

  @Override
  public JsonProperty<?> setProperty(final String name,
                                   final boolean val) {
    return setProperty(factory.makeProperty(name, val));
  }

  @Override
  public <T extends JsonValue> JsonProperty<T> makeProperty(
          final TypeReference<T> typeRef,
          final String name,
          final String type) {
    final JsonProperty<T> p =
            getFactory().makeProperty(name,
                                      type,
                                      null);
    setProperty(p);

    return p;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if (!getClass().equals(o.getClass())) {
      return false;
    }

    final var that = (JsonValueImpl)o;

    if (Util.cmpObjval(this.type, that.type) != 0) {
      return false;
    }

    return this.getNode().equals(that.getNode());
  }

  protected JsonProperty<?> makeProperty(final String name,
                                       final JsonNode node) {
    return factory.makeProperty(name, node);
  }

  protected void assertStringNode() {
    if (getNode() == null) {
      throw new JsonException("Null node for type: "
                                         + type);
    }

    if (getNode().isTextual()) {
      return;
    }

    throw new JsonException("Not String value. Type: "
                                       + type);
  }

  protected void assertIntNode() {
    if (getNode() == null) {
      throw new JsonException("Null node for type: "
                                         + type);
    }

    if (getNode().isInt()) {
      return;
    }

    throw new JsonException("Not int value. Type: "
                                       + type);
  }

  protected void assertBooleanNode() {
    if (getNode() == null) {
      throw new JsonException("Null node for type: "
                                         + type);
    }

    if (getNode().isBoolean()) {
      return;
    }

    throw new JsonException("Not boolean value. Type: "
                                       + type);
  }

  protected void assertObject(final String action) {
    if (getNode().isObject()) {
      return;
    }

    if (action == null) {
      throw new JsonException("Not object value. Type: "
                                         + type);
    }

    throw new JsonException("Not object value. Type: "
                                       + type + " action: " + action);
  }

  protected void assertArray(final String action) {
    if (getNode().isArray()) {
      return;
    }

    if (action == null) {
      throw new JsonException("Not array value. Type: "
                                         + type);
    }

    throw new JsonException("Not array value. Type: "
                                       + type + " action: " + action);
  }

  private <ValType extends JsonValue> JsonProperty<ValType> addProperty(
          final JsonProperty<ValType> val) {
    changed = true;
    assertObject("addProperty");

    final var name = val.getName();
    if (getNode().get(name) != null) {
      throw new JsonException("Property " + name + " already present");
    }
    final var value = (JsonValueImpl)val.getValue();
    value.setOwner(this);
    value.changed = true;
    childProperties.put(name, val);

    ((ObjectNode)getNode()).set(name, value.getNode());

    return val;
  }

  private <ValType extends JsonValue> JsonProperty<ValType> updateProperty(
          final JsonProperty<JsonValue> val,
          final JsonProperty<ValType> newval) {
    assertObject("updateProperty");

    final var name = val.getName();

    final var theNode = (ObjectNode)getNode();
    if (theNode.get(name) != null) {
      theNode.remove(name);
    }

    ((JsonValueImpl)val.getValue()).changed = true;

    final var newValue = (JsonValueImpl)newval.getValue();
    theNode.set(name, newValue.getNode());

    // Replace value in property value
    final var value = (JsonValueImpl)val.getValue();
    value.node = newValue.getNode();

    return (JsonProperty<ValType>)val;
  }
}
