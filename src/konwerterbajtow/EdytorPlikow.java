/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package konwerterbajtow;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jędrzej
 */
public class EdytorPlikow {

    private List<Byte> data = new ArrayList<Byte>();
    private List<Byte> stream1List = new ArrayList<Byte>();
    private List<Byte> stream2List = new ArrayList<Byte>();
    
    EdytorPlikow(List<String> files, byte[] stream1, byte[] stream2) throws IOException{
//        changeFilesInThisDirectory(files, stream1, stream2);        
        changeFilesInThisDirectory(files, stream1, stream2);
//        getContentFromInputStream(files);
    }
    
    
    private void changeFilesInThisDirectory(List<String> files, byte[] stream1, byte[] stream2) throws IOException{
//        obiekt data będzie przechowywał plik który w danej chwili będzie analizowany i zmieniany
        int string1Size = stream1.length;
        int string2Size = stream2.length;
        long processTimeStamp = 0;

        int chunkSize = 20;
        byte[] partOfByteStream = new byte[chunkSize];
        
        

        
        
        for(String file : files){
            File f = new File(file);
            RandomAccessFile fileAccess = new RandomAccessFile(f, "rw");
            long pointer=0;
            int nextMatch = 1;
            byte singleByte;
            long tempPointer = 0;
            while(true){
                processTimeStamp++;
                if(processTimeStamp%100000==0){
                    System.out.println("przetworzono "+processTimeStamp*100/fileAccess.length()+"%");
                }
                try {
                    pointer = fileAccess.getFilePointer();
                    singleByte = fileAccess.readByte();
                    data.add(singleByte);
                    if (singleByte==stream1[0]){
                        
                        for(int i=1;i<string1Size;i++){
                            if (fileAccess.readByte()==stream1[i]){
                                nextMatch++;
                            }
                        }
                    }
                    if(nextMatch==string1Size){
                        System.out.println("znaleziono ciąg");
                        fileAccess.seek(fileAccess.getFilePointer()-string1Size+string2Size);
                        data.remove(data.size()-1);
                        for(int i=0;i<string2Size;i++){
                            data.add(stream2[i]);
                        }
                    }
                    else if(nextMatch>1){
                        fileAccess.seek(fileAccess.getFilePointer()-nextMatch+1);
                    }
                    nextMatch=1;
                    
                }
                catch(OutOfMemoryError e){
                    System.out.println("OutOfMemoryError, przepisuje plik i czyszczę listę");
                    int n=data.size();
                    for(int i=0;i<n&&tempPointer<pointer;i++){
                        fileAccess.write(data.get(i));
                        tempPointer++;
                    }
                    
                    for(int i=0;i<n;i++){
                        data.remove(0);
                    }
                    fileAccess.seek(pointer);
                }
                catch(EOFException e){
                    System.out.println("EOFException - koniec pliku: "+file.toString());
                    int n = data.size();
                    byte[] newFile = new byte[n];
                    for(int i =0;i<n;i++){
                        newFile[i]=data.get(i);
                    }
    //                nadpisanie istniejącego pliku
                    FileOutputStream stream = new FileOutputStream(file,true);
                    stream.write(newFile);
                    data.clear();
                    break;
                }
            }
            
        }
        }
        
    }

    

