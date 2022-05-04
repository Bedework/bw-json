package org.bedework.json.model;

/** Basic Json types
 * User: mike Date: 5/3/22 Time: 15:20
 */
public interface JsonTypes {
  String typeUnknown = "Unknown";

  String typeBoolean = "Boolean";
  String typeInt = "Int";
  String typeIntArray = "Int[]";
  String typeNumber = "Number";
  String typeString = "String";
  String typeStringArray = "String[]";
  String typeUnsignedInt = "UnsignedInt";
  String typeUnsignedIntArray = "UnsignedInt[]";

  // Internally used types
  String typeNull = "null";
}
