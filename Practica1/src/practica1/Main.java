/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Sau
 */
public class Main {
    
    public static final int tMax = 100;
   
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        for (String s : args)
            System.out.println("Arg -> " + s);
        
        int n = 8;
        NPuzzle puzzle;
        String output = null;
        
        if (args.length < 2) {
            System.out.println("NPuzzle <puzzleFile> 8|15 <outputFile>");
            
            System.out.println("Using by default:8 Random Puzzle");
            n = 8;
            puzzle = new NPuzzle(n);
            while(!puzzle.resoluble()){
                puzzle = new NPuzzle(8);
            }

        }else{
            String puzzleFile = args[0];
            
            int pN;
            try{
                pN = Integer.valueOf(args[1]);
            }catch(NumberFormatException e){
                System.out.println("Args[1] is not a number.Using by default:8 Random Puzzle");
                pN = 8;
            }

            if (pN != 8 && pN != 15) {
                System.out.println("Size must be 8 or 15. Using by default 8.");
                n = 8;
            }else{
                n = pN;
            }
            
            if (args.length > 2) {
                output = args[2];
            }
            
            
            puzzle = new NPuzzle(puzzleFile, n);
        }
        
 
     
        System.out.println("Puzzle to solve:\n\n" + puzzle);
        
      
        if (!puzzle.resoluble()){
            System.out.println("Puzzle has not solution");
            System.exit(0);
        } else  System.out.println("Solvable Puzzle");

        NPuzzle puzzleCopy = new NPuzzle(puzzle);
        
        long start = System.currentTimeMillis();
        //ArrayList<Integer> nodes = puzzleCopy.busquedaAleatoria(tMax);
        ArrayList<Integer> nodes = puzzleCopy.improvedRandomSearch(tMax);
       
        long end = System.currentTimeMillis();
        
        
        if(!puzzleCopy.objetivo()){
            System.out.println("Solution not found!");
        }else{
            System.out.println("Solution found!");
        }
        
        System.out.println("Tiempo = "+ (end - start)/1000.0 +"s");
        
        
        /*Save output*/
        if (output != null && nodes != null) {
            try (Writer out = new FileWriter(output)){
                for (Integer i: nodes)
                        out.write(i+" ");
            } catch (IOException ex) {
                Logger.getLogger(NPuzzle.class.getName()).log(Level.SEVERE, "ERROR", ex);
            }
        }
        
    }
    
    
    
}
