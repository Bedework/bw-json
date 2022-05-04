/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.json;

/**
 * User: mike Date: 7/19/20 Time: 00:20
 */
public class JsonException extends RuntimeException {
  private String extra;


  /** Constructor
   *
   */
  public JsonException() {
    super();
  }

  /**
   * @param t a Throwable
   */
  public JsonException(final Throwable t) {
    super(t);
  }

  /**
   * @param s a message
   */
  public JsonException(final String s) {
    super(s);
  }

  /**
   * @param s  - retrieve with getMessage()
   * @param extra String extra text
   */
  public JsonException(final String s, final String extra) {
    super(s);
    this.extra = extra;
  }

  /**
   * @return String extra text
   */
  public String getExtra() {
    return extra;
  }

  /**
   * @return String message and 'extra'
   */
  @Override
  public String getMessage() {
    if (getExtra() != null) {
      return super.getMessage() + "\t" + getExtra();
    }

    return super.getMessage();
  }

  /**
   * @return String message without 'extra'
   */
  public String getDetailMessage() {
    return super.getMessage();
  }
}
