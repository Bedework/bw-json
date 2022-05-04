/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.impl;

import org.bedework.json.JsonRegistration;
import org.bedework.json.JsonTypeInfo;
import org.bedework.json.JsonValueFactory;
import org.bedework.json.impl.values.factories.JsonBooleanFactory;
import org.bedework.json.impl.values.factories.JsonIntArrayFactory;
import org.bedework.json.impl.values.factories.JsonIntegerFactory;
import org.bedework.json.impl.values.factories.JsonStringArrayFactory;
import org.bedework.json.impl.values.factories.JsonStringFactory;
import org.bedework.json.impl.values.factories.JsonUnsignedIntArrayFactory;
import org.bedework.json.impl.values.factories.JsonUnsignedIntFactory;
import org.bedework.json.model.JsonTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Mappings for property names to types and valid objects
 *
 * User: mike Date: 10/23/19 Time: 16:53
 */
class JsonPropertyAttributes implements JsonRegistration {
  private final static String registrationName =
          "bw-json";

  // Type names for properties
  private final static Map<String, String> ptypes =
          new HashMap<>();

  // Type info for types
  private final static Map<String, JsonTypeInfo> types =
          new HashMap<>();

  private final static Map<String, List<String>> validFor =
          new HashMap<>();

  private final static Map<String, List<String>> contains =
          new HashMap<>();

    /* ===== info for types ======================= */

  static {
    type(JsonTypes.typeBoolean,
         false, // requiresType
         false, // valueList
         false, // propertyList
         null, // elementType
         false, // object
         JsonBooleanFactory.class); // factoryClass

    type(JsonTypes.typeInt,
         false, // requiresType
         false, // valueList
         false, // propertyList
         null, // elementType
         false, // object
         JsonIntegerFactory.class); // factoryClass

    type(JsonTypes.typeIntArray,
         false, // requiresType
         true, // valueList
         false, // propertyList
         null, // elementType
         false, // object
         JsonIntArrayFactory.class); // factoryClass

    type(JsonTypes.typeString,
         false, // requiresType
         false, // valueList
         false, // propertyList
         null, // elementType
         false, // object
         JsonStringFactory.class); // factoryClass

    type(JsonTypes.typeStringArray,
         false, // requiresType
         true, // valueList
         false, // propertyList
         types(JsonTypes.typeString), // elementType
         false, // object
         JsonStringArrayFactory.class); // factoryClass

    // Internal types

    type(JsonTypes.typeUnsignedInt,
         false, // requiresType
         false, // valueList
         false, // propertyList
         null, // elementType
         false, // object
         JsonUnsignedIntFactory.class); // factoryClass

    type(JsonTypes.typeUnsignedIntArray,
         false, // requiresType
         true, // valueList
         false, // propertyList
         types(JsonTypes.typeUnsignedInt), // elementType
         false, // object
         JsonUnsignedIntArrayFactory.class); // factoryClass

    // Replace all contains values with immutable lists
    for (final var name: contains.keySet()) {
      final var val = contains.get(name);
      if (val != null) {
        contains.put(name, List.copyOf(val));
      }
    }
  }

  private static void ptype(final String name,
                            final String type) {
    ptypes.put(name, type);
  }

  private static void type(final String typeName,
                           final boolean requiresType,
                           final boolean valueList,
                           final boolean propertyList,
                           final String[] elementType,
                           final boolean object,
                           final Class<? extends JsonValueFactory> factoryClass) {
    types.put(typeName,
              new JsonTypeInfo(typeName, requiresType, valueList, propertyList,
                               elementType, object, factoryClass));
  }

  private static void validFor(final String name,
                               final String... types) {
    validFor.put(name, List.of(types));

    for (final var type: types) {
      final List<String> contained =
              contains.computeIfAbsent(type,
                                       k -> new ArrayList<>());
      contained.add(name);
    }
  }

  private static String[] types(final String... types) {
    return types;
  }

  @Override
  public String getRegistrationName() {
    return registrationName;
  }

  @Override
  public Set<String> propertyNames() {
    return ptypes.keySet();
  }

  @Override
  public String getType(final String propertyName) {
    return ptypes.get(propertyName);
  }

  /**
   *
   * @param name of type
   * @return type information - null if unknown type
   */
  public JsonTypeInfo getTypeInfo(final String name) {
    return types.get(name);
  }

  static List<String> getValidFor(final String name) {
    return validFor.get(name);
  }

  static List<String> getContained(final String name) {
    return contains.get(name);
  }
}
