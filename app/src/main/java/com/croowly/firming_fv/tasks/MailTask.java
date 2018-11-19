package com.croowly.firming_fv.tasks;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.croowly.firming_fv.FaceActivity;
import com.croowly.firming_fv.R;
import com.croowly.firming_fv.VerifResultActivity;
import com.croowly.firming_fv.managers.Config;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Belal on 10/30/2015.
 */

//Class is extending AsyncTask because this class is going to perform a networking operation
public class MailTask extends AsyncTask<Void, Integer, Boolean>  {

    public enum Modes { SENDING , SUCCESSFUL, FAILED   }

    //Declaring Variables
    @SuppressLint("StaticFieldLeak")
    private Context context;

    private boolean bCompleted = false;
    //Information to send email
    private String email;
    private String subject;
    private String message;
    private boolean attachment;

    private MailTask.ProgressListener<String> callback;
    private Config config;

    //Progressdialog to show while sending email
  //  private ProgressDialog progressDialog;

    //Class Constructor
    public MailTask(VerifResultActivity context, String email, String subject, String message, boolean attachment){


        this.callback = context;
        //Initializing variables
        this.context = context;
        config = new Config(context);
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.attachment = attachment;
    }

    @Override
    protected void onPreExecute() {
        bCompleted = false;

        publishProgress(0);
        //Showing progress dialog while sending email
        //progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();
        //Configuring properties
        props.put("mail.smtp.host", "mail.croowly.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.getUser(), config.getPass());
                    }
                });

        try {

                //Creating MimeMessage object
                MimeMessage mm = new MimeMessage(session);

                //Setting sender address
                mm.setFrom(new InternetAddress(config.getUser()));
                //Adding receiver
                mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                //Adding subject
                mm.setSubject(subject);

                if(this.attachment) {
                    // Create a multipar message
                    Multipart multipart = new MimeMultipart();

                    // Create the message part
                    BodyPart messageBodyPart = new MimeBodyPart();

                    // Now set the actual message
                    messageBodyPart.setText(message);

                    // Set text message part
                    multipart.addBodyPart(messageBodyPart);

                    // Part two is attachment
                    messageBodyPart = new MimeBodyPart();

                    String path = "android.resource://" + context.getPackageName() + R.raw.contrato;
                    //Uri video = Uri.parse("android.resource://com.cpt.sample/raw/filename");
                    DataSource source = new FileDataSource(path);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(path);
                    multipart.addBodyPart(messageBodyPart);

                    // Send the complete message parts
                    mm.setContent(multipart);

                }
                else {
                    mm.setText(" ACCOUNT CONTRACT");
                }
                //Sending email
                Transport.send(mm);

                bCompleted = true;
        } catch (MessagingException e) {
            e.printStackTrace();
            bCompleted = false;
        }
        catch (Exception e){
            e.printStackTrace();
            bCompleted = false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(final Boolean status) {

        if (bCompleted) {

            publishProgress(1);
//            callback.progressTask(DNIModes.SUCCESSFUL);
        }
        else{

            publishProgress(2);
        }

    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        switch (values[0]){
            case 0:
                callback.progressTask(MailTask.Modes.SENDING);
                break;
            case 1:
                callback.progressTask(MailTask.Modes.SUCCESSFUL);
                break;
            case 2:
                callback.progressTask(MailTask.Modes.FAILED);
                break;

        }
    }

    public interface ProgressListener<String>  {

        void progressTask(MailTask.Modes mode);

    }
}
