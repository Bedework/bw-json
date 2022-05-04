/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json;

import java.util.Set;

/** Allow the registration of new types
 *
 * User: mike Date: 7/25/20 Time: 22:13
 */
public interface JsonRegistration {
  /**
   *
   * @return identifier for registration - e.g. rfc or vendor
   */
  String getRegistrationName();

  /**
   *
   * @return list of names to register
   */
  Set<String> propertyNames();

  /**
   *
   * @param propertyName from list above
   * @return type name
   */
  String getType(String propertyName);

  JsonTypeInfo getTypeInfo(String typeName);
}
