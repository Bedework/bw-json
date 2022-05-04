package org.bedework.json.model.values.collections;

import org.bedework.json.model.values.JsonValue;

import java.util.List;

/** Have type xxx[]
 *
 * User: mike Date: 10/25/19 Time: 12:46
 */
public interface JsonArray<T> extends JsonValue {
  /**
   * Returns the number of elements in this array.  If this array contains
   * more than {@code Integer.MAX_VALUE} elements, returns
   * {@code Integer.MAX_VALUE}.
   *
   * @return the number of elements in this array
   */
  int size();

  /** Returns a list even though it's an array in json
   *
   * @return all the values or empty list
   */
  List<T> get();

  /**
   *
   * @param index of entry
   * @return the value
   * throws JsonException if index out of bounds
   */
  T get(int index);

  /**
   *
   * @param val to be added to list
   */
  void add(T val);

  /**
   *
   * @param index of element to remove
   * throws JsonException if index out of bounds
   */
  void remove(int index);

  /**
   *
   * @param val to remove
   * @return  false if not found
   */
  boolean remove(T val);
}
