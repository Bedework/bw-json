package org.bedework.json.model.values;

import org.bedework.json.JsonException;
import org.bedework.json.model.JsonProperty;
import org.bedework.json.model.values.dataTypes.JsonUnsignedInteger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Writer;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * User: mike Date: 10/24/19 Time: 10:21
 */
public interface JsonValue {
  /**
   *
   * @return type passed to constructor
   */
  String getObjectType();

  /**
   *
   * @return node which currently represents this object
   */
  JsonNode getNode();

  /**
   * Called before we output the object. Objects MUST call all
   * children to allow any processing before output - e.g. generate
   * patches.
   */
  void preWrite();

  /**
   * @return true if this value was changed - i.e a value was changed
   * or a sub-property added or removed.
   */
  boolean getChanged();

  /**
   * @return true if this or any sub-value was changed.
   */
  boolean hasChanges();

  /**
   * @return next value up in hierarchy
   */
  JsonValue getOwner();

  /**
   *
   * @return property containing this value.
   */
  JsonProperty<?> getParentProperty();

  /**
   *
   * @return the type of the value
   */
  String getType();

  /**
   *
   * @param propertyName to test for
   * @return true if value contains named property
   */
  boolean hasProperty(String propertyName);

  /** Return all contained properties
   *
   * @return properties
   * throws JsforjException if not an object
   */
  List<JsonProperty<?>> getProperties();

  /** Return named property
   *
   * @param type expected type
   * @param name of property
   * @return property or null
   * throws JsforjException if not an object
   */
  <T extends JsonValue> JsonProperty<T> getProperty(
          TypeReference<T> type,
          String name);

  /** Return named property
   *
   * @param name of property
   * @return property or null
   * throws JsforjException if not an object
   */
  JsonProperty<?> getProperty(String name);

  /** Return a deep copy of this value
   *
   * @return value
   */
  default JsonValue copy() {
    try {
      final Constructor<?> constructor =
              getClass().getConstructor(String.class,
                                        JsonNode.class);
      return (JsonValue)constructor.newInstance(getObjectType(),
                                                getNode().deepCopy());
    } catch (final Throwable t) {
      throw new JsonException("Exception thrown creating JsonValue copy");
    }
  }

  /** Remove named property
   *
   * throws JsforjException if not an object
   */
  void removeProperty(String name);

  /**
   * Remove all contained properties and values
   */
  void clear();

  /** Add or replace the named property
   *
   * @param val the property - non null
   * @return the property
   */
  <ValType extends JsonValue> JsonProperty<ValType> setProperty(
          JsonProperty<ValType> val);

  /** Set the value for a string type property
   *
   * @param name the property name - non null
   * @param val the property value - non null
   * @return the property
   */
  JsonProperty<?> setProperty(String name, String val);

  /** Get the value. Return null if absent.
   *
   * @param name of property
   * @return the value or null
   */
  JsonValue getPropertyValue(String name);

  /** Returns value of named boolean property.
   *
   * @param name the property name - non null
   * @return the value of the property - false if absent
   * throws JsforjException if not a boolean property
   */
  boolean getBooleanProperty(String name);

  /** Returns value of named long property.
   *
   * @param name the property name - non null
   * @return the value of the property - false if absent
   * throws JsforjException if not a boolean property
   */
  Long getLongProperty(String name);

  /** Returns value of named double property.
   *
   * @param name the property name - non null
   * @return the value of the property - false if absent
   * throws JsforjException if not a boolean property
   */
  Double getDoubleProperty(String name);

  /** Returns value of named String property.
   *
   * @param name the property name - non null
   * @return the value of the property
   * throws JsforjException if not a String property
   */
  String getStringProperty(String name);

  /**
   *
   * @return true if this is a string
   */
  boolean isString();

  /** Set the value for an SOValue type property
   *
   * @param name the property name - non null
   * @param val the property value - non null
   * @return the property
   */
  JsonProperty<?> setProperty(String name, JsonValue val);

  /** Set the value for an UnsignedInteger type property
   *
   * @param name the property name - non null
   * @param val the property value - non null
   * @return the property
   */
  JsonProperty<?> setProperty(String name, Integer val);

  /** Set the value for a long type property
   *
   * @param name the property name - non null
   * @param val the property value
   * @return the property
   */
  JsonProperty<?> setProperty(String name, Long val);

  /** Set the value for a double type property
   *
   * @param name the property name - non null
   * @param val the property value
   * @return the property
   */
  JsonProperty<?> setProperty(String name, Double val);

  /** Set the value for a boolean type property
   *
   * @param name the property name - non null
   * @param val the property value
   * @return the property
   */
  JsonProperty<?> setProperty(String name, boolean val);

  /** Add a property of given type.
   *
   * @param name the property name - non null
   * @param type the property type
   * @return the property
   * throws JsforjException if property already exists
   */
  <T extends JsonValue> JsonProperty<T> makeProperty(
          TypeReference<T> typeRef,
          String name, String type);

  /** Create a property of the given type. NOT added or set
   *
   * @param name the property name - non null
   * @param type the property type
   * @return the property
   * throws JsforjException if property already exists
   */
  <T extends JsonValue> JsonProperty<T> newProperty(
          TypeReference<T> typeRef,
          String name, String type);

  /** Returns value of named UnsignedInteger property.
   *
   * @param name the property name - non null
   * @return the value of the property
   * throws JsforjException if not a String property
   */
  JsonUnsignedInteger getUnsignedIntegerProperty(String name);

  /** Returns value as a String.
   *
   * @return the value of the property
   * throws JsforjException if not a String property
   */
  String getStringValue();

  /** Returns value as a boolean.
   *
   * @return the value of the property
   * throws JsforjException if not a boolean property
   */
  boolean getBooleanValue();

  /** Returns value as a long.
   *
   * @return the value of the property
   * throws JsforjException if not a long property
   */
  long getLongValue();

  /** Returns value as a double.
   *
   * @return the value of the property
   * throws JsforjException if not a double property
   */
  double getDoubleValue();

  /** Convert to json with this as root
   *
   * @param wtr to write to
   * @param mapper to convert
   */
  void writeValue(Writer wtr,
                  ObjectMapper mapper);

  /** Convert to json with this as root
   *
   * @param mapper to convert
   * @return Json
   */
  String writeValueAsString(ObjectMapper mapper);

  /** Convert to formatted json with this as root
   *
   * @param mapper to convert
   * @return Json
   */
  String writeValueAsStringFormatted(ObjectMapper mapper);

  /** Returns a value of the desired type and adds as a sub-property
   * of this value.
   *
   * @param type reference
   * @param pname property name
   * @param create true if it shoudl be created if absent
   * @param <T> type of value
   * @return value
   */
  <T extends JsonValue> T getValue(TypeReference<T> type,
                                   String pname,
                                   boolean create);
}
