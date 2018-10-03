/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package konwerterbajtow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jędrzej
 */
public class PrzeszukiwaczKatalogow {
    private List<String> files = new ArrayList<String>(); 
    
    PrzeszukiwaczKatalogow(String dirName, String extension){
        finder(dirName, extension);
        
    }
//    metoda wykonuje na katalogu głównym i wszystkich podkatalogach metodę searchHere
    private void finder(String dirName, String extension){
//    obiekt katalogu ktory chcemy przeszukac
        File dir = new File(dirName);
//    przeszukanie katalogu głównego w poszukiwaniu plików 
        searchHere(dir, extension);
        
//    przeszukanie wszystkich podkatalogów, wywołanie na nich metody finder
        String[] names = dir.list();
        for(String name : names){
            File checkIfDirectory = new File(dir.toString()+"\\"+name);
            if(checkIfDirectory.isDirectory()){
                finder((dir.toString()+"\\"+name), extension);
            }
        } 
    }
    
//    metoda znajduje pliki o podanym rozszerzeniu i zapisuje ścieżki dostępu do listy files
    private void searchHere(File dir, String extension){
        String[] names = dir.list();
        for(String name : names){
            if(name.endsWith(extension)){
                files.add(dir.toString()+"\\"+name);
            }
        }
    }
//    metoda zwraca liste ścieżek dostępu do plików z porządanym rozszerzeniem
    public List<String> getFiles(){
        return files;
    }
}

