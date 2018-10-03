/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package konwerterbajtow;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Jędrzej
 */
public class EditFile {
    
    EditFile(String files, byte[] stream1, byte[] stream2) throws IOException{
        changeFilesInThisDirectory(files, stream1, stream2);
    }
    
//    metoda działa tylko gdy ciąg1 i ciąg2 są równej długości
    private void changeFilesInThisDirectory(String files, byte[] stream1, byte[] stream2) throws FileNotFoundException, IOException{
        long processTimeStamp = 0;
        int stringSize = stream1.length;
        File f = new File(files);
        System.out.println("dlugosc pliku "+f+ " wynosi "+f.length());
        System.out.println("dlugosc ciągu bajtów wynosi "+stringSize);
        RandomAccessFile fileAccess = new RandomAccessFile(f, "rw");
        
        int nextMatch = 1;
            try {
                while(true){
                    processTimeStamp++;
                    if(processTimeStamp%100000==0){
                        System.out.println("przetworzono "+processTimeStamp*100/fileAccess.length()+"%");
                    }
                    byte singleByte = fileAccess.readByte();
                    
                    if (singleByte==stream1[0]){
                        
                        for(int i=1;i<stringSize;i++){
                            if (fileAccess.readByte()==stream1[i]){
                                nextMatch++;
                            }
                        }
                    }
                    if(nextMatch==stringSize){
                        fileAccess.seek(fileAccess.getFilePointer()-stringSize);
                        System.out.println("znaleziono ciąg");
                        fileAccess.write(stream2, 0, stringSize);
                        
                    }
                    else if(nextMatch>1){
                        fileAccess.seek(fileAccess.getFilePointer()-nextMatch+1);
                    }
                    nextMatch=1;
                    
                }
            }catch(EOFException e){
                System.out.println("EOFException - koniec pliku: "+f.toString());
            }
    }
}
