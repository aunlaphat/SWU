package com.arg.swu.mailgateway.googleapi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Properties;
// import javax.mail.MessagingException;
// import javax.mail.Session;
// import javax.mail.internet.InternetAddress;
// import javax.mail.internet.MimeMessage;
import org.apache.commons.codec.binary.Base64;

/* Class to demonstrate the use of Gmail Create Draft API */
public class CreateDraft {

    
    /**
   * Create a draft email.
   *
   * @param fromEmailAddress - Email address to appear in the from: header
   * @param toEmailAddress   - Email address of the recipient
   * @return the created draft, {@code null} otherwise.
   * @throws MessagingException - if a wrongly formatted address is encountered.
   * @throws IOException        - if service account credentials file not found.
     * @throws GeneralSecurityException 
   */
  public static Draft createDraftMessage(String fromEmailAddress,
  String toEmailAddress)
    throws MessagingException, IOException, GeneralSecurityException {
    /* Load pre-authorized user credentials from the environment.
    TODO(developer) - See https://developers.google.com/identity for
    guides on implementing OAuth2 for your application.*/
    // GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
    InputStream in = GmailApi.class.getResourceAsStream(GmailApi.CREDENTIALS_FILE_PATH);
    // if (in == null) {
    //   throw new FileNotFoundException("Resource not found: ");
    // }

    // GoogleClientSecrets clientSecrets =
    //     GoogleClientSecrets.load(GmailApi.JSON_FACTORY, new InputStreamReader(in));

    GoogleCredentials credentials = GoogleCredentials.fromStream(in)
    .createScoped(GmailScopes.GMAIL_LABELS);
    
    
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

    // // Create the gmail API client
    Gmail service = new Gmail.Builder(new NetHttpTransport(),
    GsonFactory.getDefaultInstance(),
    requestInitializer)
    .setApplicationName("SWU")
    .build();

    // final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    // Gmail service = new Gmail.Builder(HTTP_TRANSPORT, GmailApi.JSON_FACTORY, GmailApi.getCredentials(HTTP_TRANSPORT))
    //     .setApplicationName(GmailApi.APPLICATION_NAME)
    //     .build();

    // Create the email content
    String messageSubject = "Test message";
    String bodyText = "lorem ipsum.";

    // Encode as MIME message
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);
    MimeMessage email = new MimeMessage(session);
    email.setFrom(new InternetAddress(fromEmailAddress));
    email.addRecipient(jakarta.mail.Message.RecipientType.TO,
    new InternetAddress(toEmailAddress));
    email.setSubject(messageSubject);
    email.setText(bodyText);

    // Encode and wrap the MIME message into a gmail message
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    email.writeTo(buffer);
    byte[] rawMessageBytes = buffer.toByteArray();
    String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
    Message message = new Message();
    message.setRaw(encodedEmail);

    try {
    // Create the draft message
    Draft draft = new Draft();
    draft.setMessage(message);
    draft = service.users().drafts().create("me", draft).execute();
    System.out.println("Draft id: " + draft.getId());
    System.out.println(draft.toPrettyString());
    return draft;
    } catch (GoogleJsonResponseException e) {
    // TODO(developer) - handle error appropriately
    GoogleJsonError error = e.getDetails();
    if (error.getCode() == 403) {
    System.err.println("Unable to create draft: " + e.getMessage());
    } else {
    throw e;
    }
    }
    return null;
    }
}
