package com.company;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import java.io.*;
import java.util.ArrayList;

public class BookOrganizer {

    public static ArrayList<Tract> tracts = new ArrayList<Tract>();

    public static int singleTractCount = 0;
    public static int multiTractCount = 0;

    public static HSSFWorkbook nb = new HSSFWorkbook();

    public static String newFileName = "C:\\Users\\vijay\\OneDrive\\Desktop\\TestG.xls";
    public static String oldFileName = "C:/Ontario.xls";

    public static int sheetCount = 0;

    public static void main(String[] args) throws IOException {
        populateBook();
    }

    public static void populateBook() throws IOException {

        populateArray();
        getAreas();
        createFile();

        ArrayList<Integer> test = returnTracts(1);

        for (int i = 0; i < tracts.size(); i++) {

            if (tracts.get(i).getUsed() == false && tracts.get(i).getOwnerStatus().toLowerCase().contains("renter")){
                myMethod(i);
            }
        }

        for (int i = 0; i < tracts.size(); i++) {

            if (tracts.get(i).getUsed() == false && tracts.get(i).getOwnerStatus().toLowerCase().contains("owner")){
                myMethod(i);
            }
        }

        for (int i = 0; i < tracts.size(); i++) {

            if (tracts.get(i).getUsed() == false && tracts.get(i).getOwnerStatus().toLowerCase().contains("")){
                myMethod(i);
            }
        }

        System.out.println("\n\nthe following people failed to make it to the book");

        int count = 0;

        for (int i = 0; i < tracts.size(); i++) {
            if(!tracts.get(i).getUsed()) {
                System.out.println("Name: " + tracts.get(i).getName() + "Status: " + tracts.get(i).getOwnerStatus() + " Tract: " + tracts.get(i).getTractNo());
            }
            else {
                count++;
            }
        }

        System.out.println(count++ + " People were printed");

    }

    public static ArrayList<Tract> populateArray() throws FileNotFoundException, IOException {

        //Opens the excel file
        InputStream oldFile = new FileInputStream(oldFileName);
        HSSFWorkbook wb = new HSSFWorkbook(oldFile);
        int y = 1;

        //Declaring the sheet info
        HSSFSheet sheet = wb.getSheetAt(0);

        //Populating the arraylist with each tracts info
        while(sheet.getRow(y) != null){

            //Checks for cells containing a null value
            for (int i = 0; i < 16; i++) {
                if (sheet.getRow(y).getCell(i) == null) {
                    sheet.getRow(y).createCell(i);
                }
            }

            tracts.add(new Tract(
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

        return tracts;
    }

    public static ArrayList<String> getAreas(){
        //Stores the names of different towns and cities
        ArrayList<String> cities = new ArrayList<String>();

        //goes through every record and if the place is not in the list it adds it
        for (int i = 0; i < tracts.size(); i++) {
            //System.out.println(((Tract)tract.Getelementbypos(i)).getName());
            //if (((Tract)tract.Getelementbypos(i)).getAddress() != ""){
            if (validAddress(i) == false){
                String[] tmp = tracts.get(i).getAddress().split(",");
                try{
                    if (cities.contains(tmp[1]) == false){
                        cities.add(tmp[1].toUpperCase());
                    }
                }
                catch(Exception e){

                }

            }
        }

        //Sends out the array list
        return cities;
    }

    public static boolean validAddress(int index){

        boolean missing = false;

        if (tracts.get(index).getAddress() == ""){
            missing = true;
        }

        try {
            String[] address = tracts.get(index).getAddress().split(",");
            String city = address[1];
        }
        catch (Exception e) {
            missing = true;
        }

        return missing;
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

    public static String[] ObjectToArray(int elementPos){
        //Creates an array the size of the amount of items in a tract object
        String[] record = new String[16];

        //populates each position in  the array with items in the object
        record[0] = Integer.toString(tracts.get(elementPos).getTractNo());
        record[1] = tracts.get(elementPos).getPin();
        record[2] = tracts.get(elementPos).getStructureType();
        record[3] = tracts.get(elementPos).getOwnerStatus();
        record[4] = tracts.get(elementPos).getContactStatus();
        record[5] = tracts.get(elementPos).getName();
        record[6] = tracts.get(elementPos).getCoordinates();
        record[7] = tracts.get(elementPos).getAddress();
        record[8] = tracts.get(elementPos).getPhoneNo();
        record[9] = tracts.get(elementPos).getOccupentNo();
        record[10] = tracts.get(elementPos).getWorksLand();
        record[11] = tracts.get(elementPos).getContacted();
        record[12] = tracts.get(elementPos).getAttemptDetails();
        record[13] = tracts.get(elementPos).getConsultationDate();
        record[14] = tracts.get(elementPos).getFollowUP();
        record[15] = tracts.get(elementPos).getComments();

        //sends out the array
        return record;

    }

    public static void linePlacer(int elementPos) throws FileNotFoundException, IOException{
        //Grabs the first sheet
        HSSFSheet newSheet = nb.getSheetAt(0);

        //Grabs the contents of the object in an array form
        String[] record = ObjectToArray(elementPos);

        //Grabs the city name
        String[] city = record[7].split(",");

        //Looks for the sheet with the correct city and sets the book to it
        for (int i = 0; i < nb.getNumberOfSheets(); i++) {
            if (!validAddress(elementPos)){
                if (nb.getSheetName(i).equals(city[1])){
                    newSheet = nb.getSheetAt(i);
                }
            }
            else if (record[7] == "MISSING"){
                newSheet = nb.getSheetAt(0);
            }
        }

        int startingRow = 1;
        boolean done = false;
        int count = 0;

        while (!done){
            //if the first row in null move on
            if (newSheet.getRow(startingRow) == null){
                count++;
                startingRow++;
            }
            //
            if (newSheet.getRow(startingRow) != null){
                count = 0;
                startingRow++;
            }
            if (newSheet.getRow(startingRow) == null && count == 1){
                newSheet.createRow(startingRow).createCell(0);
                done = true;
            }
        }

        lineWriter(elementPos, startingRow, newSheet);

        //different track number, same name, not used
        for (int i = 0; i < tracts.size(); i++) {
            if (
                    tracts.get(i).getTractNo() != tracts.get(elementPos).getTractNo() &&
                    tracts.get(i).getName().contains(tracts.get(elementPos).getName()) &&
                            !tracts.get(i).getUsed()
            )
            {
                startingRow++;
                lineWriter(i, startingRow, newSheet);
            }
        }

        //same tract no, different name, owner status, not used
        for (int i = 0; i < tracts.size(); i++)
        {
            if (
                    tracts.get(i).getTractNo() == tracts.get(elementPos).getTractNo() &&
                    tracts.get(i).getName().equals(tracts.get(elementPos).getName()) == false &&
                    tracts.get(elementPos).getOwnerStatus().toLowerCase().contains("owner") &&
                    tracts.get(i).getUsed() == false
            )
            {
                startingRow++;
                lineWriter(i, startingRow, newSheet);
            }
        }

        //same tract no, different name, renter status, not used
        for (int i = 0; i < tracts.size(); i++) {
            if (
                    tracts.get(i).getTractNo() == tracts.get(elementPos).getTractNo() &&
                    tracts.get(i).getName().equals(tracts.get(elementPos).getName()) == false &&
                    tracts.get(elementPos).getOwnerStatus().toLowerCase().contains("renter") &&
                    !tracts.get(i).getUsed()
            )
            {
                startingRow++;
                lineWriter(i, startingRow, newSheet);
//                tracts.get(i).setUsed(true);
            }
        }
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
        try{
            newSheet.getRow(row).getCell(15).setCellValue(relativeNotes(tractNo));
        }
        catch(Exception e){
            System.out.println(tracts.get(tractNo).getName());
        }


        //Writes changes
        FileOutputStream newFile = new FileOutputStream(newFileName);
        nb.write(newFile);

        //closes stream and book
        newFile.close();
        nb.close();

    }

    public static String findMultiTracts(int tractNo){

        int count = 0;
        String multiTract = "";

        for (int i = 0; i < tracts.size(); i++) {

            if (
                    tracts.get(tractNo).getName().equals(tracts.get(i).getName()) &&
                            tracts.get(tractNo).getTractNo() != tractNo &&
                            tracts.get(tractNo).getOwnerStatus().equals(tracts.get(i).getOwnerStatus()) &&
                            tracts.get(i).getUsed() == false
            )
            {
                multiTract = multiTract + tracts.get(i).getTractNo() + "\n";
                count++;
                tracts.get(i).setUsed(true);
            }

        }

        if (count > 1){
            singleTractCount--;
            multiTractCount++;
        }

        return multiTract;

    }

    public static String relativeNotes(int tractNo){

        ArrayList<String> names = new ArrayList<String>();
        String relatives = "Has the same last name as: ";

        for (int i = 0; i < tracts.size(); i++) {
            boolean related = findRelations(tractNo, i);
            if (related){
                if (names.contains(tracts.get(i).getName()) == false){
                    names.add(tracts.get(i).getName());
                }
            }
        }

        for (int i = 0; i < names.size(); i++) {
            relatives = relatives + "\n-" + names.get(i);
        }

        return relatives;
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

                if (
                        tracts.get(itemTwo).getName().contains(lastNameOne[lastNameOne.length - 1]) == true &&
                        tracts.get(itemOne).getName().equals(tracts.get(itemTwo).getName()) == false &&
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

        if (tracts.get(itemTwo).getPhoneNo().equals((tracts.get(itemOne)).getPhoneNo()) &&
                tracts.get(itemTwo).getAddress().equals(tracts.get(itemOne).getAddress())){

            related = true;

        }

        return related;

    }

    public static void myMethod(int index) throws IOException {

//        System.out.println(tracts.get(index).getName());

        //Grabs the first sheet
        HSSFSheet newSheet = nb.getSheetAt(0);

        //Grabs the contents of the object in an array form
        String[] record = ObjectToArray(index);

        //Grabs the city name
        String[] city = record[7].split(",");

        //Looks for the sheet with the correct city and sets the book to it
        for (int i = 0; i < nb.getNumberOfSheets(); i++) {
            if (!validAddress(index)){
                if (nb.getSheetName(i).equals(city[1])){
                    newSheet = nb.getSheetAt(i);
                }
            }
            else if (record[7] == "MISSING"){
                newSheet = nb.getSheetAt(0);
            }
        }

        int row = 1;
        boolean done = false;
        int count = 0;

//        while (!done){
//            if (newSheet.getRow(row) == null){
////                newSheet.createRow(row).createCell(0);
//                done = true;
//            }
//            if (newSheet.getRow(row) != null){
////                count = 0;
////                row++;
//                count++;
//                row++;
//            }
////            if (newSheet.getRow(row) == null && count == 1){
//////                newSheet.createRow(row).createCell(0);
//////                done = true;
////            }
//        }

        while (done == false){
            if (newSheet.getRow(row) == null){
                count++;
                row++;
            }
            if (newSheet.getRow(row) != null){
                count = 0;
                row++;
            }
            if (newSheet.getRow(row) == null && count == 1){
                newSheet.createRow(row).createCell(0);
                done = true;
            }
        }

        myMethod2(index, row, newSheet);

        ArrayList<Integer> temp = returnTracts(index);

        for (int i = 0; i < temp.size(); i++) {
                row++;
            myMethod2(temp.get(i), row, newSheet);
        }

    }

    private static void myMethod2(int index, int row, HSSFSheet sheet) throws IOException {

        //Grabs the first sheet
        HSSFSheet newSheet = sheet;

        sheet.createRow(row).createCell(0);

        //Grabs the contents of the object in an array form
        String[] record = ObjectToArray(index);

        newSheet.createRow(row).createCell(0);

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

//        //if (((Tract)tract.Getelementbypos(tractNo)).getOwnerStatus().contains("OWNER")){
        newSheet.getRow(row).getCell(0).setCellValue(record[0]);
//        //}
//        try{
//            newSheet.getRow(row).getCell(15).setCellValue(relativeNotes(tractNo));
//        }
//        catch(Exception e){
//            System.out.println(((Tract)tract.Getelementbypos(tractNo)).getName());
//        }


        //Writes changes
        FileOutputStream newFile = new FileOutputStream(newFileName);
        nb.write(newFile);

        //closes stream and book
        newFile.close();
        nb.close();

        tracts.get(index).setUsed(true);



    }

    public static ArrayList<Integer> returnTracts(int index){

        ArrayList<Integer> temp = new ArrayList<Integer>();

        int count = 0;

        for (int i = 0; i < tracts.size(); i++) {

            //if name is the same but tract no is different
            if (tracts.get(index).getName() == tracts.get(i).getName() && tracts.get(index).getTractNo() != tracts.get(i).getTractNo())
            {
                temp.add(i);
                count++;
                tracts.get(i).setUsed(true);
            }
            //if the same tract is tied to other renters or owners
            if (tracts.get(index).getName() != tracts.get(i).getName() && tracts.get(index).getTractNo() == tracts.get(i).getTractNo())
            {
                temp.add(i);
                count++;
                tracts.get(i).setUsed(true);
            }

        }

        if (count > 1){
            singleTractCount--;
            multiTractCount++;
        }

        return temp;

    }

}
