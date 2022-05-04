/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json;

import org.bedework.json.model.JsonTypes;

/**
 * User: mike Date: 7/23/20 Time: 13:30
 */
public class JsonTypeInfo {
  final String name;
  private final boolean requiresType;
  private final boolean valueList;
  private final boolean propertyList;
  private final String[] elementType;
  private final boolean object;
  private final Class<? extends JsonValueFactory> factoryClass;

  public JsonTypeInfo(final String name,
                      final boolean requiresType,
                      final boolean valueList,
                      final boolean propertyList,
                      final String[] elementType,
                      final boolean object,
                      final Class<? extends JsonValueFactory> factoryClass) {
    this.name = name;
    this.requiresType = requiresType;
    this.valueList = valueList;
    this.propertyList = propertyList;
    this.elementType = elementType;
    this.object = object;
    this.factoryClass = factoryClass;
  }

  public String getName() {
    return name;
  }

  public boolean getRequiresType() {
    return requiresType;
  }

  public boolean getValueList() {
    return valueList;
  }

  public boolean getPropertyList() {
    return propertyList;
  }

  /**
   *
   * @return true if we need a boolean node
   */
  public boolean getBoolean() {
    return name.equals(JsonTypes.typeBoolean);
  }

  /**
   *
   * @return true if we need an integer node
   */
  public boolean getInteger() {
    return name.equals(JsonTypes.typeInt) ||
            name.equals(JsonTypes.typeUnsignedInt);
  }

  /**
   *
   * @return true if we need a String node
   */
  public boolean getString() {
    return name.equals(JsonTypes.typeString);
  }

  public String[] getElementType() {
    return elementType;
  }

  public boolean getObject() {
    return object;
  }

  /**
   *
   * @return class used to create objects - maybe the class represented here
   *               or elements of the property list.
   */
  public Class<?> getFactoryClass() {
    return factoryClass;
  }
}
