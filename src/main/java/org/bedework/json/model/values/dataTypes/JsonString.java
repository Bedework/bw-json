/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json.model.values.dataTypes;

import org.bedework.json.model.values.JsonValue;

/**
 * User: mike Date: 10/28/19 Time: 12:09
 */
public interface JsonString extends JsonValue {
  String get();

  /**
   * Compares with a String value.
   *
   * @param that the value to compare
   * @return the value 0 if this == that; a value less than 0 if this < that; and a value greater than 0 if this > that
   */
  int compare(String that);

  /**
   * Compares with a JSString value.
   *
   * @param that the value to compare
   * @return the value 0 if this == that; a value less than 0 if this < that; and a value greater than 0 if this > that
   */
  int compare(JsonString that);
}
