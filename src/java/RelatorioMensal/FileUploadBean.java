/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import Metodos.Metodos;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

/**
 *
 * @author amsgama
 */
public class FileUploadBean implements Serializable {

    private UploadFile file;
    private boolean autoUpload = false;
    private boolean useFlash = false;
    private boolean logoExiste = false;

    public FileUploadBean() {
        Metodos.setServidorAtivo();
        if (Metodos.getServidorAtivo()) {
            Banco banco = new Banco();
            try {
                if (banco.hasConnection) {
                    file = new UploadFile();
                    byte[] imageByte = banco.getImageLogo();
                    if (imageByte != null) {
                        file.setData(imageByte);
                        logoExiste = true;
                    } else {
                        logoExiste = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("FileUploadBean: " + e);
            }
        }

    }

    public void listener(UploadEvent event) throws Exception {
        logoExiste = true;
        UploadItem item = event.getUploadItem();
        file.setData(getBytesFromFile(item.getFile()));
        //tamanho em bytes
        int tamanho = file.getData().length;
        //arquivo nao pode ser maior que 350kb
        if (tamanho > 358400) {
            FacesMessage msgErro = new FacesMessage("O arquivo não pode ser maior que 350kb");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            Banco banco = new Banco();
            banco.insertImage(file.getData());
        }
    }

    public void atualizarFileUpload(ActionListener l) {
        logoExiste = true;
    }

    public void paint(OutputStream stream, Object object) throws IOException {
        if (file != null) {
            stream.write(file.getData());
        }
    }

    public String clearUploadData() {
        logoExiste = false;
        Banco banco = new Banco();
        banco.deleteImage();
        return null;
    }

    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public boolean isAutoUpload() {
        return autoUpload;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    public boolean isUseFlash() {
        return useFlash;
    }

    public void setUseFlash(boolean useFlash) {
        this.useFlash = useFlash;
    }

    public UploadFile getFile() {
        return file;
    }

    public void setFile(UploadFile file) {
        this.file = file;
    }

    public boolean isLogoExiste() {
        return logoExiste;
    }

    public void setLogoExiste(boolean logoExiste) {
        this.logoExiste = logoExiste;
    }
}
