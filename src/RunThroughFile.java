import java.io.File;

public class RunThroughFile {

  public static void main(String[] args){

    // Gesamten Ordner einer Wahlperiode durchlaufen lassen
    String path = "C:/Users/jojoz/IdeaProjects/DHProject/files/input/16";

    File file = new File(path);
    File[] files = file.listFiles();

    for(int i = 0; i<files.length; i++){
      System.out.println(files[i].getAbsoluteFile());
    }

    String path1 = "C:/Users/jojoz/IdeaProjects/DHProject/files/input";
    File file1 = new File(path1);
    File[] files1 = file1.listFiles();

    for(int i = 0; i<files1.length; i++){
      String name = files1[i].getName();
      System.out.println(name);
    }

  }
}
