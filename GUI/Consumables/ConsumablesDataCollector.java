package Consumables;

/*
 * The MIT License
 *
 * Copyright 2019 mayankpandey.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Unsure whether the data type should be localdate or date, will require testing
import java.time.LocalDate;
import utility.DataCollector;
import utility.DateUtil;
import utility.ErrorMessage;

/**
 *
 * @author mayankpandey
 */

public class ConsumablesDataCollector extends DataCollector {
    
    //Made this class static to be usable in the SQL
    class Paper {
        int id;
        int A3reams;
        int A4reams;
        Date Start_Date;
        Date End_Date;  
    }//end of paper class
    
    private static ConsumablesDataCollector singleInstance = null;
    HashMap <Integer, ConsumablesDataCollector.Paper> paperDetails;
    
    private Date lastDate;
    
    public static ConsumablesDataCollector getInstance() 
    { 
        if (singleInstance == null) 
            singleInstance = new ConsumablesDataCollector(); 
  
        return singleInstance; 
    } // end of getInstance method
       
    // Creator is private to make this a singleton class
    private ConsumablesDataCollector(){
        super();
        getAllPaper();
    } // end of constructor
    
    private void getAllPaper(){
        paperDetails = new HashMap(); 
        // TODO vehicle list needs to come from the database
         // our SQL SELECT query. 
      String query = "SELECT * FROM Paper";

       ResultSet rs = doQuery(query);
      
      try {
      // iterate through the java resultset
      while (rs.next())
      {
          Paper paper = new Paper();
        
        //Find the tables with the same name located in the literal string and add them to paper's properties
        paper.id = rs.getInt("Paper_ID");
        Date startDate  = rs.getDate("Start_Date");
        Date endDate = rs.getDate("End_Date");
        int A3reams = rs.getInt("A3");
        int A4reams = rs.getInt("A4");
        
        // Add that information into the hashmap
        paperDetails.put(paper.id, paper);
        
        if (paper.End_Date == null || paper.End_Date.compareTo(lastDate) > 0){
            lastDate = paper.End_Date;
        }
        
      }//end of while loop 
      } //end of try statement
     catch(SQLException error){
            ErrorMessage.display(error.getMessage());
     }
     catch(Exception e) 
    {
      System.err.println("Returned SQL exception e");
      System.err.println(e.getMessage());
    }//end of try-catch codeblock    
    }//end of getAllPaper method
    
      public java.util.Date getLastDate() {
        return DateUtil.addDays(lastDate, 1);
    } // end method getStartDate()
    
    //gets the end date of a given order
    public Date getEndDate(Date startDate){
        return paperDetails.get(startDate).End_Date;
    } // end of method getEndDate
                      
}//end of ConsumablesDataCollector class