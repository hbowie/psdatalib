/*
 * Copyright 2015 - 2015 Herb Bowie
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

package com.powersurgepub.psdatalib.psdata.values;

  import org.hamcrest.CoreMatchers.*;
  import org.junit.After;
  import org.junit.AfterClass;
  import org.junit.Before;
  import org.junit.BeforeClass;
  import org.junit.Test;
  import static org.junit.Assert.*;

/**
 Tests for ItemStatus. 

 @author Herb Bowie
 */
public class ItemStatusTest {
  
  public ItemStatusTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of set method, of class ItemStatus.
   */
  @Test
  public void testSet_String() {
    ItemStatus instance = new ItemStatus();
    
    instance.set("5");
    assertEquals("Set with String containing single digit", 
        5, instance.getStatus());
    
    instance.set("1 - Proposed");
    assertEquals("Set with String containing digit - string", 
        1, instance.getStatus());
    
    instance.set("1 - Completed");
    assertEquals("Set with String containing digit - string", 
        6, instance.getStatus());
    
    instance.set("Ca");
    assertEquals("Set with String containing two letters",
        8, instance.getStatus());
    
    instance.set("I");
    assertEquals("Set with String containing one letter",
        4, instance.getStatus());
  }

  /**
   * Test of setValue method, of class ItemStatus.
   */
  @Test
  public void testSetValue_String() {
    ItemStatus instance = new ItemStatus();
    
    instance.setValue("5");
    assertEquals("SetValue with String containing single digit", 
        5, instance.getStatus());
  }

  /**
   * Test of setValue method, of class ItemStatus.
   */
  @Test
  public void testSetValue_Object() {

  }

  /**
   * Test of setStatusFromLabel method, of class ItemStatus.
   */
  @Test
  public void testSetStatusFromLabel() {
    ItemStatus instance = new ItemStatus();
    
    instance.setStatusFromLabel("5");
    assertEquals("SetStatusFromLabel with String containing single digit", 
        5, instance.getStatus());
  }

  /**
   * Test of setStatus method, of class ItemStatus.
   */
  @Test
  public void testSetStatus_String() {
    ItemStatus instance = new ItemStatus();
    
    instance.setStatus("5");
    assertEquals("SetStatus with String containing single digit", 
        5, instance.getStatus());
  }

  /**
   * Test of setValue method, of class ItemStatus.
   */
  @Test
  public void testSetValue_boolean() {
    ItemStatus instance = new ItemStatus();
    
    instance.setValue(true);
    assertEquals("SetValue with boolean closed?", 
        9, instance.getStatus());
    
    instance.setValue(false);
    assertEquals("SetValue with boolean closed?", 
        0, instance.getStatus());
  }

  /**
   * Test of setStatus method, of class ItemStatus.
   */
  @Test
  public void testSetStatus_boolean() {
    ItemStatus instance = new ItemStatus();
    
    instance.setStatus(true);
    assertEquals("SetStatus with boolean closed?", 
        9, instance.getStatus());
    
    instance.setStatus(false);
    assertEquals("SetStatus with boolean closed?", 
        0, instance.getStatus());
  }

  /**
   * Test of setValue method, of class ItemStatus.
   */
  @Test
  public void testSetValue_int() {
    ItemStatus instance = new ItemStatus();
    // System.out.println("Item Status Config");
    // System.out.println(ItemStatusConfig.getShared().toString());
    
    instance.setValue(5);
    assertEquals("Set Value with integer", 
        5, instance.getStatus());
    
    instance.setValue(-1);
    assertEquals("Set Value with integer", 
        5, instance.getStatus());
    
    instance.setValue(10);
    assertEquals("Set Value with integer", 
        5, instance.getStatus());
  }

  /**
   * Test of setStatus method, of class ItemStatus.
   */
  @Test
  public void testSetStatus_int() {
    ItemStatus instance = new ItemStatus();

    instance.setStatus(5);
    assertEquals("Set Status with integer", 
        5, instance.getStatus());
  }

  /**
   * Test of set method, of class ItemStatus.
   */
  @Test
  public void testSet_int() {
    System.out.println("set");
    int status = 0;
    ItemStatus instance = new ItemStatus();
    instance.set(status);
    assertEquals("Set with integer", 0, instance.getStatus());
    instance.set(5);
    assertEquals("Set with Integer", 5, instance.getStatus());
  }

  /**
   * Test of toString method, of class ItemStatus.
   */
  @Test
  public void testToString() {
    ItemStatus instance = new ItemStatus();
    instance.set(5);
    String expResult = "5 - Held";
    String result = instance.toString();
    assertEquals("To String", expResult, result);
  }

  /**
   * Test of getValueAsInt method, of class ItemStatus.
   */
  @Test
  public void testGetValueAsInt() {
    ItemStatus instance = new ItemStatus();
    instance.set(5);
    int expResult = 5;
    int result = instance.getValueAsInt();
    assertEquals("Get Value as Int", expResult, result);
  }

  /**
   * Test of getValueAsChar method, of class ItemStatus.
   */
  @Test
  public void testGetValueAsChar() {
    ItemStatus instance = new ItemStatus();
    instance.set(9);
    char expResult = '9';
    char result = instance.getValueAsChar();
    assertEquals("Get Value as Char", expResult, result);
  }

  /**
   * Test of getStatus method, of class ItemStatus.
   */
  @Test
  public void testGetStatus() {
    ItemStatus instance = new ItemStatus();
    instance.set(2);
    int expResult = 2;
    int result = instance.getStatus();
    assertEquals("Get Status", expResult, result);
  }

  /**
   * Test of getStatusLabel method, of class ItemStatus.
   */
  @Test
  public void testGetStatusLabel() {
    ItemStatus instance = new ItemStatus();
    instance.set(4);
    String expResult = "Active";
    String result = instance.getStatusLabel();
    assertEquals("get Status Label", expResult, result);
  }

  /**
   * Test of getValue method, of class ItemStatus.
   */
  @Test
  public void testGetValue() {
    ItemStatus instance = new ItemStatus();
    instance.set(9);
    Object expResult = "Closed";
    Object result = instance.getValue();
    assertEquals(expResult, result.toString());
  }

  /**
   * Test of getLabel method, of class ItemStatus.
   */
  @Test
  public void testGetLabel_0args() {
    ItemStatus instance = new ItemStatus();
    instance.set(6);
    String expResult = "Completed";
    String result = instance.getLabel();
    assertEquals(expResult, result);
  }

  /**
   * Test of getElementClass method, of class ItemStatus.
   */
  @Test
  public void testGetElementClass() {
    ItemStatus instance = new ItemStatus();
    Class expResult = String.class;
    Class result = instance.getElementClass();
    assertEquals("get Element Class", expResult, result);
  }

  /**
   * Test of getLabel method, of class ItemStatus.
   */
  @Test
  public void testGetLabel_int() {
    int status = 4;
    ItemStatus instance = new ItemStatus();
    String expResult = "Active";
    String result = instance.getLabel(status);
    assertEquals("get Label from integer", expResult, result);
  }

  /**
   * Test of isDone method, of class ItemStatus.
   */
  @Test
  public void testIsDone() {
    ItemStatus instance = new ItemStatus();
    instance.set(0);
    assertEquals("is 0 Done?", false, instance.isDone());
    
    instance.set(7);
    assertEquals("is 7 Done?", false, instance.isDone());
    
    instance.set(8);
    assertEquals("is 8 Done?", true, instance.isDone());
    
    instance.set(9);
    assertEquals("is 9 Done?", true, instance.isDone());
  }

  /**
   * Test of isNotDone method, of class ItemStatus.
   */
  @Test
  public void testIsNotDone() {
    ItemStatus instance = new ItemStatus();
    instance.set(0);
    assertEquals("is 0 Not Done?", true, instance.isNotDone());
    instance.set(9);
    assertEquals("is 9 Not Done?", false, instance.isNotDone());
  }
  
  /**
   * Test of getSimpleName method, of class ItemStatus.
   */
  @Test
  public void testGetSimpleName() {
    ItemStatus instance = new ItemStatus();
    assertEquals("get Simple Name", "status", instance.getSimpleName());
  }

  /**
   * Test of getDisplayName method, of class ItemStatus.
   */
  @Test
  public void testGetDisplayName() {
    ItemStatus instance = new ItemStatus();
    assertEquals("get Display Name", "Status", instance.getDisplayName());
  }

  /**
   * Test of getBriefName method, of class ItemStatus.
   */
  @Test
  public void testGetBriefName() {
    ItemStatus instance = new ItemStatus();
    assertEquals("get Brief Name", "Status", instance.getBriefName());
  }

  /**
   * Test of getColumnWidth method, of class ItemStatus.
   */
  @Test
  public void testGetColumnWidth() {
    ItemStatus instance = new ItemStatus();
    assertEquals("get Column Width", 80, instance.getColumnWidth());
  }

  /**
   * Test of compareTo method, of class ItemStatus.
   */
  @Test
  public void testCompareTo() {
    ItemStatus instance = new ItemStatus();
    instance.set(4);
    ItemStatus instance2 = new ItemStatus();
    
    instance2.set(0);
    assertEquals("Compare To", 1, instance.compareTo(instance2));
    
    instance2.set("Active");
    assertEquals("Compare To", 0, instance.compareTo(instance2));
    
    instance2.set("In Work");
    assertEquals("Compare To", 0, instance.compareTo(instance2));
    
    instance2.set(9);
    assertEquals("Compare To", -1, instance.compareTo(instance2));
    
    String str2 = "4 - Active";
    assertEquals("Compare To", 0, instance.compareTo(str2));
  }

  /**
   * Test of hasData method, of class ItemStatus.
   */
  @Test
  public void testHasData() {
    ItemStatus instance = new ItemStatus();
    assertEquals("has Data", true, instance.hasData());
  }
  
}
