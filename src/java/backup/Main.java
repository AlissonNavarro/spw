/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package backup;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Alexandre
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        String local = "HUSE";
        String caminho = "d:\\backup\\teste";
        File file = new File(caminho);
        file.delete();
        Process p = Runtime.getRuntime().exec("sqlcmd.exe -S (local)\\SQLExpress -Q \"BACKUP DATABASE huse TO DISK='" + caminho + "' WITH DIFFERENTIAL\"");
        p.waitFor();
        Compactador.compactar(caminho, local);


    }
}
