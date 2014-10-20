/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package backup;

import java.io.File;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

public class SendMail {

    /**
     * @param args
     */
    private String smtp;
    private String port;
    private Boolean isSSL;

    public boolean email(String remetente, String senha, String destinatario, File f, String title, String msg) {

        boolean flag = true;
        // Create the attachment
        // Caminho do arquivo a ser enviado
        Integer porta = Integer.parseInt(port);
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(f.getPath()); // Obtem o caminho do arquivo
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("File");
        attachment.setName(f.getName()); // Obtem o nome do arquivo

        try {
            // Create the email message
            MultiPartEmail email = new MultiPartEmail();
            email.setDebug(true);
            email.setHostName(smtp);
            email.setSmtpPort(porta);
            email.setAuthentication(remetente, senha);
            email.setSSL(isSSL);
            email.addTo(destinatario); //pode ser qualquer um email
            email.setFrom(remetente); //aqui necessita ser o email que voce fara a autenticacao
            email.setSubject(title);
            email.setMsg(msg);
            email.setTLS(true);
            // add the attachment
            email.attach(attachment);
            // send the email

            email.send();
        } catch (EmailException e) {
            System.out.println(e.getMessage());
            flag = false;
        }
        return flag;
    }

    public boolean teste(String remetente, String senha, String destinatario) {

        boolean flag = true;
        // Create the attachment
        // Caminho do arquivo a ser enviado
        Integer porta = Integer.parseInt(port);       

        try {
            // Create the email message
            MultiPartEmail email = new MultiPartEmail();
            email.setDebug(true);
            email.setHostName(smtp);
            email.setSmtpPort(porta);
            email.setAuthentication(remetente, senha);
            email.setSSL(isSSL);
            email.addTo(destinatario); //pode ser qualquer um email
            email.setFrom(remetente); //aqui necessita ser o email que voce fara a autenticacao
            email.setSubject("TESTE");
            email.setMsg("TESTE");
            email.setTLS(true);           

            email.send();
        } catch (EmailException e) {
            System.out.println(e.getMessage());
            flag = false;
            FacesMessage msgErro = new FacesMessage( e.toString());
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        return flag;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public Boolean getIsSSL() {
        return isSSL;
    }

    public void setIsSSL(Boolean isSSL) {
        this.isSSL = isSSL;
    }

    public static void main(String[] args) {
    }
}
