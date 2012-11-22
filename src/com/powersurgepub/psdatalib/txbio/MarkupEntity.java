package com.powersurgepub.psdatalib.txbio;

/**
 This class is used to represent a single HTML entity.
 */
public class MarkupEntity {

  private String name;
  private int    number;
  private String replacement;

  public MarkupEntity (String name, int number, String replacement) {
    this.name = name;
    this.number = number;
    this.replacement = replacement;
  }

  public String getName () {
    return name;
  }

  public boolean equalsName (String name2) {
    return (name.equalsIgnoreCase (name2));
  }

  public int getNumber () {
    return number;
  }

  public boolean equalsNumber (int number2) {
    return (number == number2);
  }

  public String getReplacement () {
    return replacement;
  }
}
