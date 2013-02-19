/*
 * Copyright 1999 - 2013 Herb Bowie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.powersurgepub.psdatalib.ui;

  import com.powersurgepub.xos2.*;
  import java.text.*;
  import java.util.*;
  import javax.swing.*;

public class DateEditor 
    extends JDialog {
  
  /** Dates with a year at or after this one are considered null. */
  public final static int               NULL_YEAR = 2050;
  
  /** Null Year Display Value. */
  public final static String            NULL_DISPLAY_DATE = "N/A";
  
  /** Prior Default date for a new to do item. */
  public final static GregorianCalendar OLD_DEFAULT_DATE 
      = new GregorianCalendar (2050, 11, 31);
  
  /** Default date for a new to do item. */
  public final static GregorianCalendar DEFAULT_DATE 
      = new GregorianCalendar (2050, 11, 1);
  
  private XOS                           xos = XOS.getShared();
  
  private DateOwner                     dateOwner;
  
  private GridBagger                    gb = new GridBagger();
  
  private JPanel                        panel1 = new JPanel();
  private GridBagger                    gb1 = new GridBagger();
  
  private JButton                       monthDecrementButton;
  private JButton                       monthIncrementButton;
  private JTextField                    monthTextField;
  
  private JButton                       yearDecrementButton;
  private JButton                       yearIncrementButton;
  private JTextField                    yearTextField;
  
  private JButton                       dayDecrementButton;
  private JButton                       dayIncrementButton;
  private JTextField                    dayTextField;
  
  private JPanel                        panel2 = new JPanel();
  private GridBagger                    gb2 = new GridBagger();
  
  private JPanel                        panel3 = new JPanel();
  private GridBagger                    gb3 = new GridBagger();
  
  private JButton                       noDateButton;
  private JButton                       todayButton;
  private JButton                       recurButton;
  private JButton                       okButton;
  
  private GregorianCalendar             date = new GregorianCalendar();
  
  private boolean                       modified = false;
  
  private SimpleDateFormat              monthFormatter
      = new SimpleDateFormat ("MMMM");
  
  private SimpleDateFormat              actionFormatter
      = new SimpleDateFormat ("yyyy-MM-dd");
  
  public static final SimpleDateFormat  longDateFormatter
      = new SimpleDateFormat ("EEEE  MMMM d, yyyy");
  
  private ArrayList                     dayButton = new ArrayList ();
  
  /** Creates new form DateEditor */
  public DateEditor(JFrame frame, DateOwner dateOwner) {
    super (frame, "Date Editor", true);
    this.dateOwner = dateOwner;
    gb.startLayout (this.getContentPane(), 1, 3);
    
    yearDecrementButton = new javax.swing.JButton();
    yearTextField = new javax.swing.JTextField();
    yearIncrementButton = new javax.swing.JButton();
    
    monthDecrementButton = new javax.swing.JButton();
    monthTextField = new javax.swing.JTextField();
    monthIncrementButton = new javax.swing.JButton();
    
    dayDecrementButton = new JButton();
    dayTextField = new JTextField();
    dayIncrementButton = new JButton();
    
    
    // setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    
    gb.setAllInsets (2);
    
    gb1.startLayout (panel1, 3, 3);
    gb1.setAllInsets (4);
    
    yearDecrementButton.setText("<");
    yearDecrementButton.setFocusable(false);
    yearDecrementButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        decrementYear();
      }
    });
    gb1.add(yearDecrementButton);

    yearTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    yearTextField.setText("2005");
    yearTextField.addFocusListener(new java.awt.event.FocusListener() {
      public void focusGained(java.awt.event.FocusEvent evt) {
        yearTextField.selectAll();
      }
      public void focusLost(java.awt.event.FocusEvent evt) {
        modifyYear();
      }
    });
    gb1.add(yearTextField);

    yearIncrementButton.setText(">");
    yearIncrementButton.setFocusable(false);
    yearIncrementButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        incrementYear();
      }
    });
    gb1.add(yearIncrementButton);

    monthDecrementButton.setText("<");
    monthDecrementButton.setFocusable(false);
    monthDecrementButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        decrementMonth();
      }
    });
    gb1.add(monthDecrementButton);

    monthTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    monthTextField.setText("WWWWWWWWWWW");
    monthTextField.addFocusListener(new java.awt.event.FocusListener() {
      public void focusGained(java.awt.event.FocusEvent evt) {
        monthTextField.selectAll();
      }
      public void focusLost(java.awt.event.FocusEvent evt) {
        modifyMonth();
      }
    });
    gb1.add(monthTextField);

    monthIncrementButton.setText(">");
    monthIncrementButton.setFocusable(false);
    monthIncrementButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        incrementMonth();
      }
    });
    gb1.add(monthIncrementButton);
    
    dayDecrementButton.setText("<");
    dayDecrementButton.setFocusable(false);
    dayDecrementButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        decrementDay();
      }
    });
    gb1.add(dayDecrementButton);

    dayTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    dayTextField.setText("  ");
    dayTextField.addFocusListener(new java.awt.event.FocusListener() {
      public void focusGained(java.awt.event.FocusEvent evt) {
        dayTextField.selectAll();
      }
      public void focusLost(java.awt.event.FocusEvent evt) {
        modifyDay();
      }
    });
    gb1.add(dayTextField);

    dayIncrementButton.setText(">");
    dayIncrementButton.setFocusable(false);
    dayIncrementButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        incrementDay();
      }
    });
    gb1.add(dayIncrementButton);
    
    gb.add (panel1);

    gb2.startLayout (panel2, 7, 7);
    gb2.setAllInsets (1);
    
    JLabel sunday = new JLabel ("Su");
    sunday.setHorizontalAlignment (JLabel.CENTER);
    gb2.add (sunday);
    
    JLabel monday = new JLabel ("Mo");
    monday.setHorizontalAlignment (JLabel.CENTER);
    gb2.add (monday);
    
    JLabel tuesday = new JLabel ("Tu");
    tuesday.setHorizontalAlignment (JLabel.CENTER);
    gb2.add (tuesday);
    
    JLabel wednesday = new JLabel ("We");
    wednesday.setHorizontalAlignment (JLabel.CENTER);
    gb2.add (wednesday);
    
    JLabel thursday = new JLabel ("Th");
    thursday.setHorizontalAlignment (JLabel.CENTER);
    gb2.add (thursday);
    
    JLabel friday = new JLabel ("Fr");
    friday.setHorizontalAlignment (JLabel.CENTER);
    gb2.add (friday);
    
    JLabel saturday = new JLabel ("Sa");
    saturday.setHorizontalAlignment (JLabel.CENTER);
    gb2.add (saturday);
    
    for (int i = 0; i < 42; i++) {
      JButton db = new JButton("00");
      db.setFocusable(false);
      xos.setButtonType (db, "toolbar");
      db.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          chooseDate(evt.getActionCommand());
        }
      });
      gb2.add (db);
      dayButton.add (db);
    }
    
    gb.add (panel2);
    
    gb3.startLayout (panel3, 2, 2);
    
    noDateButton  = new JButton ("N/A");
    noDateButton.setFocusable(false);
    noDateButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        setDateToNull();
      }
    });
    gb3.add(noDateButton);
    
    todayButton   = new JButton ("Today");
    todayButton.setFocusable(false);
    todayButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        setDateToToday();
      }
    });
    gb3.add(todayButton);
    
    recurButton   = new JButton ("Recur");
    recurButton.setFocusable(false);
    recurButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        recurDate();
      }
    });
    gb3.add(recurButton);
    
    okButton      = new JButton ("OK"); 
    okButton.setFocusable(false);
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        hide();
      }
    });
    gb3.add(okButton);
    
    gb.add (panel3);
    
    yearTextField.requestFocus();
    
    pack();
  }     
  
  public static String formatLong (Date date) {

    if (isNullDate(date)) {
      return NULL_DISPLAY_DATE;
    } else {
      return longDateFormatter.format (date);
    }
  }
  
  public boolean isNullDate() {
    return isNullDate (date.getTime());
  }
  
  public static boolean isNullDate (Date date) {
    GregorianCalendar cal = new GregorianCalendar ();
    cal.setTime (date);
    return (cal.get (Calendar.YEAR) >= NULL_YEAR);
  }
  
  public void setDate (Date date) {

    this.date.setTime (date);
    displayDate();
    modified = false;

  }
  
  private void modifyYear() {
    String yearStr = yearTextField.getText();
    try {
      int year = Integer.parseInt (yearStr);
      if (year < 100) {
        year = year + 2000;
      }
      GregorianCalendar modDate 
          = new GregorianCalendar (year, date.get (date.MONTH), date.get (date.DATE));
      date = modDate;
      dateModified();
    } catch (NumberFormatException e) {
      // do nothing
    }
    displayDate();
  }
  
  private void decrementYear() {
    if (isNullDate()) {
      // do nothing
    } else {
      date.add (Calendar.YEAR, -1);
      dateModified();
      displayDate();
    }
  }
  
  private void incrementYear() {
    if (isNullDate()) {
      // do nothing
    } else {
      date.add (Calendar.YEAR, 1);
      dateModified();
      displayDate();
    }
  }
  
  private void modifyMonth() {
    String monthStr = monthTextField.getText().toLowerCase();
    int month = 0;
    if (isNullDate()) {
      month = 0;
    } else {
      month = date.get (Calendar.MONTH);
    }
    if (monthStr.startsWith ("ja")) {
      month = 1;
    }
    else
    if (monthStr.startsWith ("f")) {
      month = 2;
    }
    else
    if (monthStr.startsWith ("mar")) {
      month = 3;
    }
    else
    if (monthStr.startsWith ("ap")) {
      month = 4;
    }
    else
    if (monthStr.startsWith ("may")) {
      month = 5;
    }
    else
    if (monthStr.startsWith ("jun")) {
      month = 6;
    }
    else
    if (monthStr.startsWith ("jul")) {
      month = 7;
    }
    else
    if (monthStr.startsWith ("au")) {
      month = 8;
    }
    else
    if (monthStr.startsWith ("s")) {
      month = 9;
    }
    else
    if (monthStr.startsWith ("o")) {
      month = 10;
    }
    else
    if (monthStr.startsWith ("n")) {
      month = 11;
    }
    else
    if (monthStr.startsWith ("d")) {
      month = 12;
    }
    else {
      try {
        month = Integer.parseInt (monthStr);
      } catch (NumberFormatException e) {
        // do nothing
      }
    }
    if (month >= 1 && month <= 12) {
      GregorianCalendar modDate 
          = new GregorianCalendar (date.get (date.YEAR), month - 1, date.get (date.DATE));
      date = modDate;
      dateModified();
    }
    displayDate();
  }
  
  private void decrementMonth() {
    if (isNullDate()) {
      // do nothing
    } else {
      date.add (Calendar.MONTH, -1);
      dateModified();
      displayDate();
    }
  }
  
  private void incrementMonth() {
    if (isNullDate()) {
      // do nothing
    } else {
      date.add (Calendar.MONTH, 1);
      dateModified();
      displayDate();
    }
  }
  
  private void modifyDay() {
    String dayStr = dayTextField.getText().toLowerCase();
    int day = 0;
    if (isNullDate()) {
      day = 0;
    } else {
      day = date.get (Calendar.DATE);
    }

    try {
      day = Integer.parseInt (dayStr);
    } catch (NumberFormatException e) {
      // do nothing
    }

    if (day >= 1 && day <= 31) {
      GregorianCalendar modDate 
          = new GregorianCalendar (date.get (date.YEAR), date.get (date.MONTH), day);
      date = modDate;
      dateModified();
    }
    displayDate();
  }
  
  private void decrementDay() {
    if (isNullDate()) {
      // do nothing
    } else {
      date.add (Calendar.DATE, -1);
      dateModified();
      displayDate();
    }
  }
  
  private void incrementDay() {
    if (isNullDate()) {
      // do nothing
    } else {
      date.add (Calendar.DATE, 1);
      dateModified();
      displayDate();
    }
  }
  
  private void chooseDate (String dateString) {
    ParsePosition pos = new ParsePosition (0);
    try {
      date.setTime (actionFormatter.parse (dateString, pos));
      dateModified();
      displayDate();
    } catch (IllegalArgumentException e) {
      System.out.println (dateString + " cannot be formatted as a date");
    }
  }
  
  public void setDateToToday() {
    GregorianCalendar today = new GregorianCalendar();
    date.set (Calendar.YEAR, today.get (Calendar.YEAR));
    date.set (Calendar.MONTH, today.get (Calendar.MONTH));
    date.set (Calendar.DATE, today.get (Calendar.DATE));
    dateModified();
    displayDate();
  }
  
  private void setDateToNull() {
    date.setTime (DEFAULT_DATE.getTime());
    dateModified();
    displayDate();
  }
  
  private void recurDate() {
    if (dateOwner.canRecur()) {
      dateOwner.recur(date);
      dateModified();
      displayDate();
    }
  }
  
  private void displayDate () {
    
    int year = date.get (Calendar.YEAR);
    int month = 0;
    int day = 0;
    
    if (year >= NULL_YEAR) {
      yearTextField.setText (NULL_DISPLAY_DATE);
      monthTextField.setText ("         ");
      dayTextField.setText ("  ");
      for (int i = 0; i < 42; i++) {
        JButton db = (JButton)dayButton.get (i);
        db.setText ("  ");
        db.setActionCommand ("NULL");
      }
      recurButton.setEnabled (false);
      recurButton.setToolTipText ("");
    } else {
      yearTextField.setText (String.valueOf (year));
      month = date.get (Calendar.MONTH);
      monthTextField.setText (monthFormatter.format (date.getTime()));
      day = date.get (Calendar.DATE);
      dayTextField.setText (String.valueOf (day));
      int daysInMonth = date.getActualMaximum (GregorianCalendar.DAY_OF_MONTH);
      GregorianCalendar 
        firstDayOfMonth 
          = new GregorianCalendar (
              date.get (Calendar.YEAR),
              date.get (Calendar.MONTH), 
              1);
      int firstDayOfMonthDayOfWeek 
          = firstDayOfMonth.get (GregorianCalendar.DAY_OF_WEEK);
      GregorianCalendar firstDayToDisplay
          = new GregorianCalendar (
              firstDayOfMonth.get (Calendar.YEAR),
              firstDayOfMonth.get (Calendar.MONTH),
              firstDayOfMonth.get (Calendar.DATE));
      firstDayToDisplay.add (Calendar.DATE, ((firstDayOfMonthDayOfWeek * -1) + 1));
      GregorianCalendar calendarDay
          = new GregorianCalendar (
              firstDayToDisplay.get (Calendar.YEAR),
              firstDayToDisplay.get (Calendar.MONTH),
              firstDayToDisplay.get (Calendar.DATE));

      for (int i = 0; i < 42; i++) {
        JButton db = (JButton)dayButton.get (i);
        String dayText = String.valueOf (calendarDay.get (Calendar.DATE));
        String startItalics = "";
        String endItalics = "";
        String startBold = "";
        String endBold = "";
        if (month == calendarDay.get (Calendar.MONTH)) {
          if (day == calendarDay.get (Calendar.DATE)) {
            startBold = "<b><font color=red>";
            endBold = "</font></b>";
            db.setText ("<html>" + startItalics + startBold + dayText 
            + endBold + endItalics + "</html>");
          } else {
            db.setText (dayText);
          }
        } else {
          startItalics = "<i>";
          endItalics = "</i>";
          db.setText ("<html>" + startItalics + startBold + dayText 
            + endBold + endItalics + "</html>");
        }
        db.setActionCommand (actionFormatter.format(calendarDay.getTime()));
        calendarDay.add (Calendar.DATE, 1);
      } // end for each Calendar Day displayed
      if (dateOwner.canRecur()) {
        recurButton.setEnabled (true);
        recurButton.setToolTipText (dateOwner.getRecurrenceRule());
      } else {
        recurButton.setEnabled (false);
        recurButton.setToolTipText ("");
      }
    } // end if year not null
  } // end method displayDate
  
  private void dateModified() {
    modified = true;
    // dateOwner.dateModified (date.getTime());
  }
  
  public boolean isModified () {
    return modified;
  }
  
  public Date getDate () {
    return date.getTime();
  }
  
}

