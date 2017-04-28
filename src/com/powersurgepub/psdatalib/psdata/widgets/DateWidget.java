/*
 * Copyright 1999 - 2017 Herb Bowie
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

package com.powersurgepub.psdatalib.psdata.widgets;

  import com.powersurgepub.psdatalib.psdata.values.*;
  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.psutils.*;
  import java.awt.*;
  import java.awt.event.*;
  import java.io.*;
  import java.net.*;
  import javax.swing.*;
  import javax.swing.event.*;
  import java.text.*;
  import java.util.*;

/**
  UI Component that allows the user to enter a date. 

  @author  Herb Bowie. 
 */
public class DateWidget 
    extends javax.swing.JPanel 
    implements
        DataWidget,
        DateWidgetOwner {
  
  private GridBagger              gb = new GridBagger();
  
  private JFrame                  frame = null;
  private DateWidgetOwner         dateWidgetOwner = null;
  
  private boolean                 modified = false;
  private JTextField              dateField;
  private JButton                 calendarButton;
  private JButton                 recurButton;
  private JButton                 todayButton;
  
  /** 
    Creates new panel DateWidget 
   */
  public DateWidget() {
    
    gb.startLayout (this, 4, 1);
    gb.setAllInsets (2);
    
    dateField = new JTextField();
    
    gb.setColumnWeight (0.4);
    gb.add(dateField);
    
    calendarButton = new JButton();
    calendarButton.setText("Calendar");
    calendarButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        dateButtonActionPerformed(evt);
      }
    });
    
    gb.setColumnWeight (0.2);
    gb.add(calendarButton);
    
    recurButton = new javax.swing.JButton();
    recurButton.setText("Recur");
    recurButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        recurButtonActionPerformed(evt);
      }
    });
    
    gb.setColumnWeight (0.2);
    gb.add(recurButton);
    
    todayButton = new javax.swing.JButton();
    todayButton.setText("Today");
    todayButton.setEnabled(true);
    todayButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        todayButtonActionPerformed(evt);
      }
    });
    
    gb.setColumnWeight (0.2);
    gb.add(todayButton);
    
    // setBorder (BorderFactory.createLineBorder(Color.black));
  }
  
  public void setOwner(DateWidgetOwner dateWidgetOwner) {
    this.dateWidgetOwner = dateWidgetOwner;
    recurButton.setEnabled(dateWidgetOwner.canRecur());
  }
  
  public void setFrame(JFrame frame) {
    this.frame = frame;
  }
  
    /**
   To be called whenever the date is modified by DateWidget.
   */
  public void dateModified (String date) {
    dateField.setText(date);
    if (dateWidgetOwner != null) {
      dateWidgetOwner.dateModified(date);
    }
  }
  
  /**
   Does this date have an associated rule for recurrence?
   */
  public boolean canRecur() {
    if (dateWidgetOwner != null) {
      return dateWidgetOwner.canRecur();
    } else {
      return false;
    }
  }
  
  /**
   Provide a text string describing the recurrence rule, that can
   be used as a tool tip.
   */
  public String getRecurrenceRule() {
    if (dateWidgetOwner != null) {
      return dateWidgetOwner.getRecurrenceRule();
    } else {
      return "";
    }
  }
  
  /**
   Apply the recurrence rule to the date.
  
   @param date Starting date.
  
   @return New date. 
   */
  public String recur (StringDate date) {
    if (dateWidgetOwner != null) {
      return dateWidgetOwner.recur(date);
    } else {
      return date.toString();
    }
  }
  
  /**
   Apply the recurrence rule to the date.
  
   @param date Starting date.
  
   @return New date. 
   */
  public String recur (String date) {
    if (dateWidgetOwner != null) {
      return dateWidgetOwner.recur(date);
    } else {
      return date;
    }
  }
  
  public void setText (String date) {
    dateField.setText(date);
  }
  
  public void setDate (Date date) {
    setText(StringDate.COMMON_FORMAT.format(date));
    modified = false;
    if (dateWidgetOwner != null) {
      recurButton.setEnabled(dateWidgetOwner.canRecur());
    } else {
      recurButton.setEnabled(false);
    }
  }
  
  public boolean isModified() {
    return modified;
  }
  
  public Date getDate() {
    StringDate str = new StringDate();
    str.set(dateField.getText());
    return str.getDate();
  }
  
  public String getText() {
    return dateField.getText();
  }
  
  /**
   Edit the date using the Calendar editor. 
  */
  public void editDate() {
    if (frame != null && dateWidgetOwner != null) {
      DateCalendarEditor editor = new DateCalendarEditor(frame, this);
      StringDate str = new StringDate();
      str.set(dateField.getText());
      Date date = str.getDate();
      if (date == null) {
        editor.setDateToToday();
      } else {
        editor.setDate (date);
      }
      if (editor.isNullDate()) {
        editor.setDateToToday();
      }
      editor.setLocationRelativeTo (frame);
      editor.setVisible(true);
      // if (editor.isModified()) {
      //   modified = true;
      //   date.setTime(editor.getDate().getTime());
      //   displayDate();
      //   dateWidgetOwner.dateModified(StringDate.COMMON_FORMAT.format(date));
      // }  // End if we have a valid date
    } // end if we have a date owner and frame
  } // end editDate method
  
  private void recurButtonActionPerformed (java.awt.event.ActionEvent evt) {
    if (dateWidgetOwner != null && dateWidgetOwner.canRecur()) {
      String oldDate = dateField.getText();
      String newDate = dateWidgetOwner.recur (oldDate);
      if (newDate != null && newDate.length() > 0) {
        modified = true;
        setText(newDate);
        dateWidgetOwner.dateModified(newDate);
      }
    }  
  }
  
  private void todayButtonActionPerformed (java.awt.event.ActionEvent evt) {
    setText(StringDate.getTodayCommon());
  }

  private void dateButtonActionPerformed(java.awt.event.ActionEvent evt) {
    editDate();
  }
  
}
