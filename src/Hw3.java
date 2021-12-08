/********************************************************************
 Date Due:  11/10/2021

 Purpose:   This java program reads the movies from a file and converts
            the non-Ascii characters and stores the movies in a output
            file. Also it reads the movie-matrix file and expands the
            data and append the values into a matrix.

 Execution: java Hw3

 java hw3
 *********************************************************************/

import java.io.*;
import java.util.*;
import java.lang.Character;
public class Hw3 {

    public static void main(String[] args) throws IOException {
       readingMovies();
       readingMatrix();
    }
    //map that can be used to store the values into the HashMap
    public static  Map<Character, Character> ascii;
    static{ ascii = new HashMap<>();
        ascii.put('é' ,'e');
        ascii.put('è', 'e');
        ascii.put('ö', 'o');
        ascii.put('Á', 'A');}
    //returns the size of the list of movies
    public static int buildNameFile(ArrayList movielist){
        return movielist.size();
    }

    // returns the size of the matrix(i.e number of rows
    public static int buildMatrix(List<List<Integer>> matrix){
        return matrix.size();
    }
    //counts the number of columns in a first row of matrix
    public static int columns(List<List<Integer>> matrix){
        return matrix.get(0).size();
    }
    //compares if all rows has equal number of columns
    public static boolean columnscompare(List<List<Integer>> matrix){
        for(int i = 0; i < matrix.size(); i++)
        {
                if(!Objects.equals(matrix.get(0).size(), matrix.get(i).size()))
                {
                    return false;
                }
            }
        return true;
    }
    //displays the necessary print statements on the console
    public static void display(int ch,int count, ArrayList words) throws IOException {
        System.out.println("No. of high order chars:       "+counting());
        System.out.println("Lines with high order chars:    "+ch);
        System.out.println("Total record count:          "+count);
        System.out.println();
        System.out.println("*** First row has "+columns(readingMatrix())  +" columns");
        System.out.println("*** No. of rows (movies) in matrix = "  +buildMatrix(readingMatrix()));
        if(columnscompare(readingMatrix()))
        {
            System.out.println("*** All rows have the same number of columns as the first row");
        }
        else
        {
            System.out.println("*** All rows does not have the same number of columns as the first row");
        }
        System.out.println();
        if(Objects.equals(buildMatrix(readingMatrix()), buildNameFile(words)))
        {
            System.out.println("Name file and matrix both have " + buildMatrix(readingMatrix()) + " records");
        }
    }

   //Converts the non-ascii character to a ascii character
    public static String map_Ascii(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str);
        for(int i = 0; i < str.length(); i++) {
            Character c = ascii.get(sb.charAt(i));
            if(c != null) {
                System.out.print(sb.charAt(i) +"(0x" +  Integer.toHexString(sb.charAt(i))+") "+" in line " + str.split("[\\|]")[0]+ " char "+(str.indexOf(sb.charAt(i))+1)+" converted to "+ ascii.get(sb.charAt(i))+"\n");;
                sb.setCharAt(i, c.charValue());
                counting();
            }
        }

        return sb.toString();
    }

    public static int lines = 0;
    //counts the occurences of the non-ascii character
    public static int counting()
    {
        lines= lines + 1;
        return lines - 1;
    }
    //This functions takes the movies list with non-ascii  and converts and stores the movie list into a output file
    public static ArrayList readingMovies() throws IOException {
        BufferedReader rd = new BufferedReader(new FileReader("input/movie-names"));
        List<String> words = new ArrayList<String>();
        String line;
        int  ch = 0;
        int count = 0;
       /* for (Map.Entry<Character, Character> entry : ascii.entrySet()) {
            char value1 = entry.getKey();
            char value2 = entry.getValue();
            System.out.println(value1 + " -> " + value2);
        }*/
        ascii.forEach((k, v) -> {
            System.out.println("Key: " + k + ", Value: " + v);
        });
        System.out.println();
        while ((line = rd.readLine()) != null) {

            count= count + 1;
            if(line.contains("è")||line.contains("é")||line.contains("ö")||line.contains("Á"))
            {
                ch = ch + 1;
                System.out.print(line + "\n");
                line= map_Ascii(line);
                System.out.println(line+"\n");
            }
            words.add(replaceVertical(line+"\n"));

        }
        display(ch,count,(ArrayList)words);

  //      System.out.println("No. of high order chars:       "+counting());
 //       System.out.println("Lines with high order chars:    "+ch);
 //       System.out.println("Total record count:          "+count);
 //       System.out.println();

        saving_movies((ArrayList) words);
        rd.close();
        return (ArrayList)words;
    }

    //stores the movies into an output file
    public static void saving_movies(ArrayList movies) throws IOException {
        FileWriter myWriter = new FileWriter("output/movienames2.txt");
        for(Object str: movies) {
            myWriter.write(str + "");
        }
        myWriter.close();

    }

    //Convert the each movie row into the required format
    public static String replaceVertical(String s){
        String old= s.split("[\\|]")[0];
        String newstring=Ascii(old);
        String finalstring=s.replaceFirst(old,newstring);
        return finalstring.replaceAll("[\\|]","");

    }

    //converts the movie number into the appropriate format
    public static String Ascii(String str){
        int value= 0;
        //Counts each character except space
       /* for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) != ' ')
                value++;
        }*/
        if(str.length()==3){
            str="0"+str;
        }
        if(str.length()==2){
            str="00"+str;
        }
        if(str.length()==1){
            str="000"+str;
        }
        return str;
    }

    //This function takes the movie-matrix.txt and split the values and stored into an output file as matrix
    private static List<List<Integer>> readingMatrix() throws IOException {
     //   ArrayList mymatrix=new ArrayList();
        FileOutputStream f = new FileOutputStream("output/hw3-gurr-movie-matrix2.ser");
        List<List<Integer>> reviewTable = new ArrayList<List<Integer>>();
        StringBuilder Sbuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("input/movie-matrix.txt")))
        {
            String[] values = new String[0];
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null)
            {
                List<Integer> Table = new ArrayList<>();
                values= sCurrentLine.split(";",-1);
                for(String w:values){
                    if (w.isEmpty())
                    {
                        Table.add(0);
                    }
                    else {
                        Table.add(Integer.parseInt(w));
                    }
                }
                reviewTable.add(Table);
                Sbuilder.append(sCurrentLine).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(reviewTable);
        o.close();
        return reviewTable;
    }
}