package backup;

   import java.security.Security;
   import java.util.Date;
   import java.util.Properties;
   import javax.mail.Authenticator;
   import javax.mail.Message;
   import javax.mail.MessagingException;
   import javax.mail.PasswordAuthentication;
   import javax.mail.Session;
   import javax.mail.Transport;
   import javax.mail.internet.AddressException;
   import javax.mail.internet.InternetAddress;
   import javax.mail.internet.MimeMessage;


   public class Email {

      public static void main(String[] args) throws AddressException, MessagingException {


         Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
         final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
      // Get a Properties object
         Properties props = System.getProperties();
  	 props.setProperty("proxySet","true");
         props.setProperty("socksProxyHost","172.20.133.243");
         props.setProperty("socksProxyPort","8080");
         props.setProperty("mail.smtp.host", "200.199.118.158");
         props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
         props.setProperty("mail.smtp.socketFactory.fallback", "false");
         props.setProperty("mail.smtp.port", "143");
         props.setProperty("mail.smtp.socketFactory.port", "143");
         props.put("mail.smtp.auth", "true");
         props.put("mail.debug", "true");
         props.put("mail.store.protocol", "pop3");
         props.put("mail.transport.protocol", "smtp");
         final String username = "folha.envio@fhs.saude.se.gov.br";
         final String password = "1234567";
         Session session = Session.getDefaultInstance(props,
                              new Authenticator(){
                                 protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                                 }});

       // -- Create a new message --
         Message msg = new MimeMessage(session);

      // -- Set the FROM and TO fields --
         msg.setFrom(new InternetAddress("folha.envio@fhs.saude.se.gov.br"));
         msg.setRecipients(Message.RecipientType.TO,
                          InternetAddress.parse("sgn.backup@gmail.com",false));
         msg.setSubject("Hello");
         msg.setText("How are you");
         msg.setSentDate(new Date());
         Transport.send(msg);
         System.out.println("Message sent.");
      }
   }
