/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl;

import org.bedework.jsforj.impl.values.JSUnknownTypeImpl;
import org.bedework.json.JsonException;
import org.bedework.json.JsonRegistration;
import org.bedework.json.JsonTypeInfo;
import org.bedework.json.JsonValueFactory;
import org.bedework.json.impl.properties.JsonPropertyImpl;
import org.bedework.json.impl.values.dataTypes.JsonNullImpl;
import org.bedework.json.impl.values.JsonUnknownTypeImpl;
import org.bedework.json.model.JsonProperty;
import org.bedework.json.model.JsonTypes;
import org.bedework.json.model.values.JsonValue;
import org.bedework.json.model.values.dataTypes.JsonString;
import org.bedework.json.model.values.dataTypes.JsonUnsignedInteger;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mike Date: 10/24/19 Time: 10:51
 */
public class JsonFactory {
  private final static JsonNodeFactory nodeFactory =
          JsonNodeFactory.withExactBigDecimals(false);

  private final Map<Class<?>, JsonValueFactory> valueFactories =
          new HashMap<>();

  private final List<JsonRegistration> registrations =
          new ArrayList<>();


  public static JsonFactory getFactory(final JsonRegistration types) {
    final var factory = new JsonFactory();

    factory.register(new JsonPropertyAttributes());
    factory.register(types);
    return factory;
  }

  public void register(final JsonRegistration val) {
    registrations.add(val);
  }

  /**
   *
   * @param name of property
   * @return type name - null if unknown property
   */
  public static String getPropertyType(final String name) {
    for (final var registration: registrations) {
      final var ptype = registration.getType(name);
      if (ptype != null) {
        return ptype;
      }
    }

    return null;
  }

  /**
   *
   * @param name of type
   * @return type information - null if unknown type
   */
  public static JsonTypeInfo getTypeInfo(final String name) {
    for (final var registration: registrations) {
      final var typeInfo = registration.getTypeInfo(name);
      if (typeInfo != null) {
        return typeInfo;
      }
    }

    return null;
  }

  /**
   * Creates objects which may be independent top level objects or
   * entries in a group.
   * @param nd
   * @return
   */
  public JsonValue makeValue(final JsonNode nd) {
    if (!nd.isObject()) {
      throw new JsonException("Not an object node");
    }

    final String type = getType(nd);

    try {
      return newValue(type, nd);
    } catch (final Throwable t) {
      throw new JsonException(t);
    }
  }

  /**
   *
   * @param propertyName - may be a path
   * @param nd - representing value
   * @return the new value
   */
  public JsonValue makePropertyValue(final String propertyName,
                                     final JsonNode nd,
                                     final String providedType) {
    final String theName;
    if (propertyName.contains("/")) {
      theName = JsonPointer.compile("/" + propertyName)
                           .last().getMatchingProperty();
    } else {
      theName = propertyName;
    }

    String type = getPropertyType(theName);

    if (type == null) {
      type = providedType;
    } else if ((providedType != null) &&
            !providedType.equals(type)) {
      throw new JsonException("Mismatched types, expected: " +
              type + " provided: " + providedType);
    }
    if (type == null) {
      if ((nd == null) || (!nd.isObject())) {
        type = JsonTypes.typeUnknown;
      } else {
        type = getType(nd);
      }
    } else {
      final var typeInfo = getTypeInfo(theName);
      if ((typeInfo != null) && typeInfo.getRequiresType()) {
        // Could validate here
        if (nd.isObject() && !type.equals(JsonTypes.typePatchObject)) {
          final var ntype = getType(nd);
          if (type.equals(ntype)) {
            throw new JsonException("Invalid type for ",
                                      nd.toString());
          }
        }
      }
    }

    return newValue(type, nd);
  }

  public JsonValue makePropertyValueWithType(final String type,
                                           final JsonNode nd) {
    return newValue(type, nd);
  }

  /** Create a string property
   *
   * @param propertyName of property
   * @param value String
   * @return the property
   */
  public JsonProperty<JsonString> makeProperty(final String propertyName,
                                               final String value) {
    final var node = new TextNode(value);

    return (JsonProperty<JsonString>)makeProperty(propertyName, node,
                                              JsonTypes.typeString);
  }

  /** Create an UnsignedInteger property
   *
   * @param propertyName of property
   * @param value UnsignedInteger
   * @return the property
   */
  public JsonProperty<?> makeProperty(final String propertyName,
                                    final JsonUnsignedInteger value) {
    final var node = new IntNode(value.get());

    return makeProperty(propertyName, node,
                        JsonTypes.typeUnsignedInt);
  }

  /** Create an Integer property
   *
   * @param propertyName of property
   * @param value Integer
   * @return the property
   */
  public JsonProperty<?> makeProperty(final String propertyName,
                                    final Integer value) {
    final var node = new IntNode(value);

    return makeProperty(propertyName, node,
                        JsonTypes.typeInt);
  }

  /** Create a boolean property
   *
   * @param propertyName of property
   * @param value true/false
   * @return the property
   */
  public JsonProperty<?> makeProperty(final String propertyName,
                                    final boolean value) {
    final JsonNode node;

    if (value) {
      node = BooleanNode.getTrue();
    } else {
      node = BooleanNode.getFalse();
    }

    return makeProperty(propertyName, node,
                        JsonTypes.typeBoolean);
  }

  public JsonProperty<?> makeProperty(final String propertyName) {
    return makeProperty(propertyName, (JsonNode)null);
  }

  public JsonProperty<?> makeProperty(final String propertyName,
                                    final JsonNode nd) {
    //final var pInfo = JsonPropertyAttributes.getPropertyTypeInfo(name);
    final var value = makePropertyValue(propertyName, nd, null);

    return new JsonPropertyImpl<>(this, propertyName, value);
  }

  public JsonProperty<?> makeProperty(final String propertyName,
                                    final JsonNode nd,
                                    final String providedType) {
    //final var pInfo = JsonPropertyAttributes.getPropertyTypeInfo(name);
    final var value = makePropertyValue(propertyName, nd, providedType);

    return new JsonPropertyImpl<>(this, propertyName, value);
  }

  /** Used for the situations where we have no @type - path objects.
   * These are used for overrides and localizations.
   *
   * @param propertyName the name of the property
   * @param type the type the value it needs to be
   * @param nd the node with the value
   * @return a new property
   */
  public <ValClass extends JsonValue> JsonProperty<ValClass> makeProperty(
          final String propertyName,
          final String type,
          final JsonNode nd) {
    //final var pInfo = JsonPropertyAttributes.getPropertyTypeInfo(name);
    final var value = makePropertyValueWithType(type, nd);

    return new JsonPropertyImpl<>(this, propertyName, (ValClass)value);
  }

  public JsonProperty<?> makeProperty(final String propertyName,
                                 final JsonValue value) {
    return new JsonPropertyImpl<>(this, propertyName, value);
  }

/*  public JsonValue newStringValue(final String val) {
    return new JsonValueImpl(JsonTypes.typeString,
                           nodeFactory.textNode(val));
  }

  public JsonValue newValue(final String type,
                          final List<JsonValue> val) {
    final ArrayNode nd = nodeFactory.arrayNode(val.size());

    for (final var el: val) {
      nd.add(((JsonValueImpl)el).getNode());
    }

    return new JsonValueImpl(type, nd);
  }*/

  public JsonValue newValue(final String type) {
    return newValue(type, (JsonNode)null);
  }

  public JsonValue newValue(final String type,
                          final JsonNode node) {
    if (node instanceof NullNode) {
      return new JsonNullImpl(this);
    }

    final var typeInfo = getTypeInfo(type);
    var theNode = node;

    if (typeInfo == null) {
      if (theNode == null) {
        theNode = new ObjectNode(JsonNodeFactory.instance);
      }
      return new JsonUnknownTypeImpl(this, type, theNode);
    }

    final var factoryClass = typeInfo.getFactoryClass();

    if (theNode == null) {
      if (typeInfo.getObject() || typeInfo.getPropertyList()) {
        theNode = new ObjectNode(JsonNodeFactory.instance);
      } else if (typeInfo.getValueList()) {
        theNode = new ArrayNode(JsonNodeFactory.instance);
      } else {
        throw new JsonException("Unable to create node for " +
                                           type);
      }
    }

    if (factoryClass == null) {
      return new JsonUnknownTypeImpl(this, type, theNode);
    }

    JsonValueFactory vfactory = valueFactories.get(factoryClass);

    if (vfactory == null) {
      try {
        vfactory =
                (JsonValueFactory)factoryClass
                        .getConstructor().newInstance();
      } catch (final Throwable t) {
        throw new JsonException(t);
      }

      valueFactories.put(factoryClass, vfactory);
    }

    return vfactory.newValue(this, type, theNode);
  }

  public String getType(final JsonNode nd) {
    final JsonNode typeNode = nd.get("@type");

    if (typeNode == null) {
      throw new JsonException("No @type for json object: ",
                                nd.toString());
    }

    if (!typeNode.isTextual()) {
      throw new JsonException("Wrong type for @type");
    }

    return typeNode.asText();
  }
}
