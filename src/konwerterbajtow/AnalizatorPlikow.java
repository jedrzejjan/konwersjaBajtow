/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package konwerterbajtow;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jędrzej
 */
public class AnalizatorPlikow {
    
    private List<Byte> data = new ArrayList<Byte>();
    private List<Byte> newData = new ArrayList<Byte>();
    
    private int stringCounter;
    AnalizatorPlikow(List<String> files, List<Byte> stream1, List<Byte> stream2) throws IOException{
        changeFilesInThisDirectory(files, stream1, stream2);
    }
    private void changeFilesInThisDirectory(List<String> files, List<Byte> stream1, List<Byte> stream2) throws IOException{
        
        for(String file : files){
            try{
                Path path = Paths.get(file);
                byte[] allBytesFromFile = Files.readAllBytes(path);
//                w tej wersji programu zliczane są wszystkie znalezione ciągi, więc nie zerujemy stringCountera przy każdej iteracji
//                stringCounter = 0;
                System.out.println("przetwarzanie pliku: "+path+" o rozmiarze "+allBytesFromFile.length+" bajtów");
//            przejscie z primitive byte na listę Byte
                for(byte b : allBytesFromFile){
                    data.add(b);
                }

                for (int i=0;i<data.size();i++){

                    newData.add(data.get(i));
                    if (data.get(i)==stream1.get(0)){
                        int nextMatch = 0;
//                        jeśli bajtów w pliku jest 100, a długość ciągu poszukiwanego wynosi 5, 
//                        to maksymalne i = 96, bo wyższy nie miałby sensu, 
//                        bo i tak jest już za mało bajtów w pliku, aby stworzyć porządany ciąg,
//                        wystąpiłby również błąd.
//                        pętla sprawdza, czy ciąg został znaleziony
                        for (int j=0;j<stream1.size()&&i<(data.size()-stream1.size()+1);j++){
                            if (data.get(i+j)==stream1.get(j)){
                                nextMatch++;
                            }
                        }
//                        ten warunek zostanie spełniony tylko gdy będzie się zgadzać długość ciągu rozpoznanego, 
//                        czyli ciąg zostanie znaleziony. Wówczas dane bajty zostaną zamienione.
                        if (nextMatch==stream1.size()){
                            stringCounter++;
                            System.out.println("znaleziono ciąg");                      
                            System.out.println("-------------");  
                            newData.remove(newData.size()-1);

                            for(Byte b : stream2){
                                newData.add(b);
                            }
//                       ominięcie pozostałych bajtów w pliku poprzez pominięcie iteracji
                            i = i+stream1.size()-1;

                        }
                    }
                }
//                konwersja ArrayList na byte[]
                int n = newData.size();
                byte[] newFile = new byte[n];
                for(int i =0;i<n;i++){
                    newFile[i]=newData.get(i);
                }
//                nadpisanie istniejącego pliku
                FileOutputStream stream = new FileOutputStream(file);
                stream.write(newFile);
                System.out.println("zakończono przetwarzanie tego pliku, zamieniono "+stringCounter+" ciągów");
                System.out.println("_________________________");
            }
//            w przypadku wystąpienia zbyt dużego pliku wykonana zostaje alternatywna metoda konwersji,
//            niezależna od wielkości pliku, ale też wolniejsza
            catch(OutOfMemoryError e){
                System.out.println("OutOfMemoryError, przejście do alternatywnej metody konwersji");
//                EditFile longFile = new EditFile(file, stream1, stream2);
            }
            data.clear();
            newData.clear();
        }
        System.out.println("Zakonczono przeszukiwanie tego katalogu. przoszę wybrać inny");
        String ttt = "5";
        System.out.println(ttt);
        
    }
    public int getStringCounter(){
        return stringCounter;
    }
}
