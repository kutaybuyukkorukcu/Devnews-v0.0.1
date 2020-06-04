package service;

import com.sun.mail.smtp.SMTPTransport;
import domain.Data;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class MailService {

    public String getAPIKEY() {
        Path path = Paths.get("src/main/resources/config.properties");

        try(BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String();
    }

    private static final String SMTP_SERVER = "smtp.sendgrid.net";
    private static final String USERNAME = "apikey";
    private static final String PASSWORD = new MailService().getAPIKEY();

    private static final String EMAIL_FROM = "kutaybuyukkorukcu@gmail.com";
    private static final String EMAIL_TO = "kutaybuyukkorukcu@gmail.com";

    private static final String EMAIL_SUBJECT = "InfoQ Onerilen Icerikler";
    private static final String EMAIL_TEXT = "";

    public void sendMail(String sb) {
        // TODO : get props from config.properties -> helper.ConfigPropertyValues
        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", SMTP_SERVER);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "25");

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        try {
            msg.setFrom(new InternetAddress(EMAIL_FROM));

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO, false));

            msg.setSubject(EMAIL_SUBJECT);

            msg.setDataHandler(new DataHandler(new HTMLDataSource(sb)));

            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

            t.connect(SMTP_SERVER, USERNAME, PASSWORD);

            t.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Response: " + t.getLastServerResponse());

            t.close();

        } catch (AddressException e) {
            // TODO : error handling
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO : error handling
            e.printStackTrace();
        }
    }

    static class HTMLDataSource implements DataSource {

        private String html;

        public HTMLDataSource(String htmlString) {
            html = htmlString;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (html == null) throw new IOException("html message is null!");
            return new ByteArrayInputStream(html.getBytes());
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }

        @Override
        public String getContentType() {
            return "text/html";
        }

        @Override
        public String getName() {
            return "HTMLDataSource";
        }
    }


    public String createMail(Data data) {
        // verilerden mail formati olusturmasini bekleriz.
        String mainTopic = data.getMainTopic();
        String title = data.getTitle();
        String url = data.getArticleLink();

        String html = "<h2> " + mainTopic + " </h2>" + "\n"
                + "<h4> " + title + " </h4>" + "\n"
                + "<h5> " + url + " </h5>";

        return html;
    }
}
