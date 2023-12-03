package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;


public class Organizer {
    
        public static ArrayManager am = new ArrayManager();
        public static ArrayManager tract;
        
        public static int singleTractCount = 0;
        public static int multiTractCount = 0;
        
        public static HSSFWorkbook nb = new HSSFWorkbook();
        
        public static String newFileName = "C:\\Users\\Vijay\\Desktop\\Test.xls";
        public static String oldFileName = "C:/data.xls";
        
        public static int sheetCount = 0;
        
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException{    
        //Variables 
        String[] town = {};
        int[] population = {};
        
        populateArray();
        getAreas();
        
        //breakBook();
        findRelations(22, 23);
        createFile();

//        for (int i = 0; i < tract.size(); i++) {
//            
//            if (((Tract)tract.Getelementbypos(i)).getUsed() == false && ((Tract)tract.Getelementbypos(i)).getOwnerStatus().toLowerCase().contains("owner")){
//                linePlacer(i);
//            }
//        }
//        
//        for (int i = 0; i < tract.size(); i++) {
//            
//            if (((Tract)tract.Getelementbypos(i)).getUsed() == false){
//                linePlacer(i);
//            }
//        }

        for (int i = 0; i < tract.size(); i++) {
            if (((Tract)tract.Getelementbypos(i)).getUsed() == false){
               linePlacer(i); 
            }
        }
        
        System.out.println("was not used:");
                for (int i = 0; i < tract.size(); i++) {
            if (((Tract)tract.Getelementbypos(i)).getUsed() == false){
                System.out.println(((Tract)tract.Getelementbypos(i)).getName());
            }
        }

        //writeToFile(1);
        
        System.out.println("Single tract: " + singleTractCount + "\nmulti tract: " + multiTractCount);
        
    }
    
    public static void populateArray() throws FileNotFoundException, IOException{
        
        //Opens the excel file
        InputStream oldFile = new FileInputStream(oldFileName);
        HSSFWorkbook wb = new HSSFWorkbook(oldFile);
        
            //Declaring the sheet info
            HSSFSheet sheet = wb.getSheetAt(0);

            tract = new ArrayManager(0);
        
            //Populating the arraymanager with each tracts info
            int y = 1;
            while(sheet.getRow(y) != null){

                for (int i = 0; i < 15; i++) {
                    if (sheet.getRow(y).getCell(i) == null) {
                        sheet.getRow(y).createCell(i);
                    }
                }

                tract.add(new Tract(
                    (int)(Math.round(Double.parseDouble(sheet.getRow(y).getCell(0).toString()))), 
                    sheet.getRow(y).getCell(1).toString(),
                    sheet.getRow(y).getCell(2).toString(), 
                    sheet.getRow(y).getCell(3).toString(), 
                    sheet.getRow(y).getCell(4).toString(), 
                    sheet.getRow(y).getCell(5).toString(), 
                    sheet.getRow(y).getCell(6).toString(), 
                    sheet.getRow(y).getCell(7).toString(), 
                    sheet.getRow(y).getCell(8).toString(), 
                    sheet.getRow(y).getCell(9).toString(), 
                    sheet.getRow(y).getCell(10).toString(), 
                    sheet.getRow(y).getCell(11).toString(), 
                    sheet.getRow(y).getCell(12).toString(), 
                    sheet.getRow(y).getCell(13).toString(), 
                    sheet.getRow(y).getCell(14).toString(),
                    sheet.getRow(y).getCell(15).toString(),
                    "",
                    false));
                    y++;
            }
            
        oldFile.close();
        wb.close(); 
    }
    
    public static void createFile() throws FileNotFoundException, IOException{
       
        //Creates a new Excel sheet
        HSSFSheet newSheet = nb.createSheet("MISSING");
        createHeader(nb.getSheetIndex(newSheet));
        
        //Gathers a list of cities to create sheets for
        ArrayList<String> areas = getAreas();
        
        //Creates a Sheet for each city
        for (int i = 0; i < areas.size(); i++) {
            newSheet = nb.createSheet(areas.get(i));
            createHeader(nb.getSheetIndex(newSheet));
        }
        
        //Saves Changes 
        OutputStream newFile = new FileOutputStream(new File(newFileName));
        nb.write(newFile);
        
        //closes file and stream
        newFile.close();
        nb.close();
    }
    
    public static void createHeader(int sheetNO){
        
        HSSFSheet newSheet = nb.getSheetAt(sheetNO);
        
        String[] header = {"TRACT", "PARCEL ID/PARCEL LLD", "STRUCTURE TYPE/ STATUS", "INTEREST STATUS", "CONTACT STATUS", "NAME(S)", "STREET ADDRESS/HOME QUARTER", "MAILING ADDRESS", "PHONE #'s", "# OF OCCUPANTS", "WORKS LAND (Y/N)", "CON- TACTED (Y/N)", "ATTEMPT DETAILS", "CONSULT-ATION DATE", "FOLLOW UP (Y/N)", "COMMENTS", "CALL DETAILS"};
        
        //Format the record being input
        HSSFCellStyle cellStyle = nb.createCellStyle();
        newSheet.setDefaultColumnWidth(20);
        cellStyle.setWrapText(true);
        
        //Creates the row
        newSheet.createRow(0);
        
        //Populates the row
        for (int i = 0; i < header.length; i++) {
            newSheet.getRow(0).createCell(i);
            newSheet.getRow(0).getCell(i).setCellType(CellType.STRING);
            newSheet.getRow(0).getCell(i).setCellStyle(cellStyle);
            newSheet.getRow(0).getCell(i).setCellValue(header[i]);
        }
        
    }
    
    public static void linePlacer(int tractNo) throws FileNotFoundException, IOException{
        //Grabs the first sheet
        HSSFSheet newSheet = nb.getSheetAt(0);
        
        //Grabs the contents of the object in an array form
        String[] record = ObjectToArray(tractNo);
        
        //Grabs the city name
        String[] city = record[7].split(",");
        
        //Looks for the sheet with the correct city and sets the book to it
        for (int i = 0; i < nb.getNumberOfSheets(); i++) {
            if (record[7] != ""){
                if (nb.getSheetName(i).equals(city[1])){
                   newSheet = nb.getSheetAt(i); 
                }   
            }
            else if (record[7] == "MISSING"){
                newSheet = nb.getSheetAt(0);
            }
        }
        
        int startingRow = 0;
        boolean done = false;
        int count = 0;
        
        while (done == false){       
            
            if (newSheet.getRow(startingRow) == null){
                count++;
                startingRow++;
            }
            if (newSheet.getRow(startingRow) != null){
                count = 0;
                startingRow++;
            }
            if (newSheet.getRow(startingRow) == null && count == 1){
                 newSheet.createRow(startingRow).createCell(0);
                 done = true;
            }
        }
        
        lineWriter(tractNo, startingRow, newSheet);   
        
//        ArrayList people = Createlist(tractNo);
//        String jk = people.get(0);
//        for (int i = 0; i < people.size(); i++) {
//            lineWriter(Integer.parseInt(people.get(i)), i, newSheet);
//        }
        

        
//        for (int i = 0; i < tract.size(); i++) {         
//            
//            if (((Tract)tract.Getelementbypos(i)).getTractNo() != ((Tract)tract.Getelementbypos(tractNo)).getTractNo() && 
//                    ((Tract)tract.Getelementbypos(i)).getName().contains(((Tract)tract.Getelementbypos(tractNo)).getName()) &&
//                    ((Tract)tract.Getelementbypos(i)).getUsed() == false){
//               startingRow++;
//               lineWriter(i, startingRow, newSheet);
//            }
//        }
//
//        for (int i = 0; i < tract.size(); i++) {
//            if (((Tract)tract.Getelementbypos(i)).getTractNo() == ((Tract)tract.Getelementbypos(tractNo)).getTractNo() && 
//                    ((Tract)tract.Getelementbypos(i)).getName().equals(((Tract)tract.Getelementbypos(tractNo)).getName()) == false && 
//                    ((Tract)tract.Getelementbypos(tractNo)).getOwnerStatus().toLowerCase().contains("owner") && 
//                    ((Tract)tract.Getelementbypos(i)).getUsed() == false){
//               startingRow++;
//               lineWriter(i, startingRow, newSheet);
//            }
//        }
//        
//        for (int i = 0; i < tract.size(); i++) {
//            if (((Tract)tract.Getelementbypos(i)).getTractNo() == ((Tract)tract.Getelementbypos(tractNo)).getTractNo() && 
//                    ((Tract)tract.Getelementbypos(i)).getName().equals(((Tract)tract.Getelementbypos(tractNo)).getName()) == false && 
//                    ((Tract)tract.Getelementbypos(tractNo)).getOwnerStatus().toLowerCase().contains("renter") && 
//                    ((Tract)tract.Getelementbypos(i)).getUsed() == false){
//               startingRow++;
//               lineWriter(i, startingRow, newSheet);
//               ((Tract)tract.Getelementbypos(i)).setUsed(true);
//            }
//        }
    }
    
    public static void lineWriter(int tractNo, int row, HSSFSheet sheet) throws FileNotFoundException, IOException{
        
        singleTractCount++;
        
        //Grabs the first sheet
        HSSFSheet newSheet = sheet;
        
        sheet.createRow(row).createCell(0);
        
        //Grabs the contents of the object in an array form
        String[] record = ObjectToArray(tractNo);
        
        //Format the record being input
        HSSFCellStyle cellStyle = nb.createCellStyle();
        newSheet.setDefaultColumnWidth(20);
        cellStyle.setWrapText(true);
        //cellStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        //cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        
        //Populates the row
        for (int i = 0; i < 16; i++) {
            newSheet.getRow(row).createCell(i);
            newSheet.getRow(row).getCell(i).setCellType(CellType.STRING);
            newSheet.getRow(row).getCell(i).setCellValue(record[i]);
            newSheet.getRow(row).getCell(i).setCellStyle(cellStyle);
        }
        
        //if (((Tract)tract.Getelementbypos(tractNo)).getOwnerStatus().contains("OWNER")){
        newSheet.getRow(row).getCell(0).setCellValue(findMultiTracts(tractNo));
        //}
        
        newSheet.getRow(row).getCell(15).setCellValue(relativeNotes(tractNo));
        
        //Writes changes
        FileOutputStream newFile = new FileOutputStream(newFileName);
        nb.write(newFile);
        
        //closes stream and book
        newFile.close();
        nb.close();
        
    }
    
    public static List<Integer> relatedTractsByIndex(int TractNo){
        
        List<Integer> relatedTracts = new ArrayList<Integer>();
        
        for (int i = 0; i < tract.size(); i++) {
            
            if (((Tract)tract.Getelementbypos(i)).getTractNo() == TractNo && i != TractNo){
                relatedTracts.add(i);
            }  
        }
        
        return relatedTracts;
        
    }
    
    
    public static String findMultiTracts(int tractNo){
         int count = 0; 
        String multiTract = "";
        
        for (int i = 0; i < tract.size(); i++) {
            
            if (((Tract)tract.Getelementbypos(tractNo)).getName().equals(((Tract)tract.Getelementbypos(i)).getName()) && ((Tract)tract.Getelementbypos(i)).getTractNo() != tractNo && ((Tract)tract.Getelementbypos(tractNo)).getOwnerStatus().equals(((Tract)tract.Getelementbypos(i)).getOwnerStatus()) && ((Tract)tract.Getelementbypos(i)).getUsed() == false){
                multiTract = multiTract + ((Tract)tract.Getelementbypos(i)).getTractNo() + "\n";
                count++;
                ((Tract)tract.Getelementbypos(i)).setUsed(true);
            }
           
        }
        
        if (count > 1){
            singleTractCount--;
            multiTractCount++;
        }
        
        return multiTract;
        
    }
    
    public static ArrayList<String> getAreas(){
        //Stores the names of different towns and cities
        ArrayList<String> areas = new ArrayList<String>();
        
        //goes through every record and if the place is not in the list it adds it
        for (int i = 0; i < tract.size(); i++) {
            System.out.println(((Tract)tract.Getelementbypos(i)).getName());
            if (((Tract)tract.Getelementbypos(i)).getAddress() != ""){
                String[] tmp = ((Tract)tract.Getelementbypos(i)).getAddress().split(",");
                if (areas.contains(tmp[1]) == false){
                    areas.add(tmp[1].toUpperCase());
                }
            }
        }
        
        //Sends out the array list
        return areas;
    }
    
    public static String[] ObjectToArray(int elementPos){
    //Creates an array the size of the amount of items in a tract object    
    String[] record = new String[16];
    
    //populates each position in  the array with items in the object
    record[0] = Integer.toString(((Tract)tract.Getelementbypos(elementPos)).getTractNo());
    record[1] = ((Tract)tract.Getelementbypos(elementPos)).getPin();
    record[2] = ((Tract)tract.Getelementbypos(elementPos)).getStructureType();
    record[3] = ((Tract)tract.Getelementbypos(elementPos)).getOwnerStatus();
    record[4] = ((Tract)tract.Getelementbypos(elementPos)).getContactStatus();
    record[5] = ((Tract)tract.Getelementbypos(elementPos)).getName();
    record[6] = ((Tract)tract.Getelementbypos(elementPos)).getCoordinates();
    record[7] = ((Tract)tract.Getelementbypos(elementPos)).getAddress();
    record[8] = ((Tract)tract.Getelementbypos(elementPos)).getPhoneNo();
    record[9] = ((Tract)tract.Getelementbypos(elementPos)).getOccupentNo();
    record[10] = ((Tract)tract.Getelementbypos(elementPos)).getWorksLand();
    record[11] = ((Tract)tract.Getelementbypos(elementPos)).getContacted();
    record[12] = ((Tract)tract.Getelementbypos(elementPos)).getAttemptDetails();
    record[13] = ((Tract)tract.Getelementbypos(elementPos)).getConsultationDate();
    record[14] = ((Tract)tract.Getelementbypos(elementPos)).getFollowUP();
    record[15] = ((Tract)tract.Getelementbypos(elementPos)).getComments();
    
    //sends out the array
    return record;
    
    } 
    
    public static String relativeNotes(int tractNo){
        
        ArrayList<String> names = new ArrayList<String>();
        String relatives = "Has the same last name as: ";
        
        for (int i = 0; i < tract.size(); i++) {
            boolean related = findRelations(tractNo, i);
            if (related == true ){
                if (names.contains(((Tract)tract.Getelementbypos(i)).getName()) == false){
                    names.add(((Tract)tract.Getelementbypos(i)).getName());
                }
            }
        }
        
        for (int i = 0; i < names.size(); i++) {
            relatives = relatives + "\n-" + names.get(i);
        }
    
        return relatives;
    }
    
        public static ArrayList Createlist(int tractNo){
            
        ArrayList<String> people = new ArrayList<String>();    
        
            for (int i = 0; i < tractNo; i++) {
                if(findRelations(tractNo, i) == true && ((Tract)tract.Getelementbypos(i)).getUsed() == false){
                    people.add(Integer.toString(i));
                }
            }
            
            return people;
            
        }
    
        public static boolean findRelationsList(int itemOne, int itemTwo){
       
        boolean related = false;
        
        String[] tractOne = ObjectToArray(itemOne);
        String[] tractTwo = ObjectToArray(itemTwo);
        
        String[] nameOne = tractOne[5].split(",");
        String[] nameTwo = tractTwo[5].split(",");

                
        int x = 1;
        int y = 1;
        
        
        for (int i = 0; i < nameOne.length; i++) {
            String[] lastNameOne = nameOne[i].split(" ");
            
            for (int j = 0; j < nameTwo.length; j++) {
                String[] lastNameTwo = nameTwo[j].split(" ");
                               
                if (((Tract)tract.Getelementbypos(itemTwo)).getName().contains(lastNameOne[lastNameOne.length - 1]) == true && 
                        ((Tract)tract.Getelementbypos(itemOne)).getName().equals(((Tract)tract.Getelementbypos(itemTwo)).getName()) == false &&
                        lastNameOne[lastNameOne.length - 1].startsWith("(") == false && lastNameTwo[lastNameTwo.length - 1].startsWith("(") == false &&
                        lastNameOne[lastNameOne.length - 1].contains("LIMITED") == false && lastNameTwo[lastNameTwo.length - 1].contains("LIMITED") == false &&
                        lastNameOne[lastNameOne.length - 1].contains("INC") == false && lastNameTwo[lastNameTwo.length - 1].contains("LIMITED") == false &&
                        lastNameOne[lastNameOne.length - 1].contains("LTD") == false && lastNameTwo[lastNameTwo.length - 1].contains("LTD") == false &&
                        lastNameOne[lastNameOne.length - 1].contains("LEASE)") == false && lastNameTwo[lastNameTwo.length - 1].contains("LEASE)") == false &&
                        lastNameOne[lastNameOne.length - 1].contains("FARMS") == false && lastNameTwo[lastNameTwo.length - 1].contains("FARMS") == false
                        ){
                    related = true;
                }
            }
        }
        
    if (((Tract)tract.Getelementbypos(itemTwo)).getPhoneNo().equals(((Tract)tract.Getelementbypos(itemOne)).getPhoneNo()) && 
        ((Tract)tract.Getelementbypos(itemTwo)).getAddress().equals(((Tract)tract.Getelementbypos(itemOne)).getAddress())){
                
                related = true;
                
        }
        
        return related;
        
    }
    
    public static boolean findRelations(int itemOne, int itemTwo){
       
        boolean related = false;
        
        String[] tractOne = ObjectToArray(itemOne);
        String[] tractTwo = ObjectToArray(itemTwo);
        
        String[] nameOne = tractOne[5].split(",");
        String[] nameTwo = tractTwo[5].split(",");

        int x = 1;
        int y = 1;
        
        
        for (int i = 0; i < nameOne.length; i++) {
            String[] lastNameOne = nameOne[i].split(" ");
            
            for (int j = 0; j < nameTwo.length; j++) {
                String[] lastNameTwo = nameTwo[j].split(" ");
                               
                if (((Tract)tract.Getelementbypos(itemTwo)).getName().contains(lastNameOne[lastNameOne.length - 1]) == true && 
                        ((Tract)tract.Getelementbypos(itemOne)).getName().equals(((Tract)tract.Getelementbypos(itemTwo)).getName()) == false &&
                        lastNameOne[lastNameOne.length - 1].startsWith("(") == false && lastNameTwo[lastNameTwo.length - 1].startsWith("(") == false &&
                        lastNameOne[lastNameOne.length - 1].contains("LIMITED") == false && lastNameTwo[lastNameTwo.length - 1].contains("LIMITED") == false &&
                        lastNameOne[lastNameOne.length - 1].contains("INC") == false && lastNameTwo[lastNameTwo.length - 1].contains("LIMITED") == false &&
                        lastNameOne[lastNameOne.length - 1].contains("LTD") == false && lastNameTwo[lastNameTwo.length - 1].contains("LTD") == false &&
                        lastNameOne[lastNameOne.length - 1].contains("LEASE)") == false && lastNameTwo[lastNameTwo.length - 1].contains("LEASE)") == false &&
                        lastNameOne[lastNameOne.length - 1].contains("FARMS") == false && lastNameTwo[lastNameTwo.length - 1].contains("FARMS") == false
                        ){
                    related = true;
                }
            }
        }
        
    if (((Tract)tract.Getelementbypos(itemTwo)).getPhoneNo().equals(((Tract)tract.Getelementbypos(itemOne)).getPhoneNo()) && 
        ((Tract)tract.Getelementbypos(itemTwo)).getAddress().equals(((Tract)tract.Getelementbypos(itemOne)).getAddress())){
                
                related = true;
                
        }
        
        return related;
        
    }

    private static void While(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
