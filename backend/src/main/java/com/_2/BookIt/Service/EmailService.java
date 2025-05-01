package com._2.BookIt.Service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {
	@Value ("${spring.sendgrid.api-key}")
	private String sendgridApiKey;
	
	public void sendBookingConfirmation (String toEmail, String subject, String body, boolean isHtml) throws IOException {
		Email from = new Email("vshamathmika@gmail.com");
		Email to = new Email(toEmail);
		Content content = new Content(isHtml ? "text/html" : "text/plain", body);
		Mail mail = new Mail(from, subject, to, content);
		
		SendGrid sg = new SendGrid(sendgridApiKey);
		Request request = new Request();
		
		request.setMethod(Method.POST);
		request.setEndpoint("mail/send");
		request.setBody(mail.build());
		
		Response response = sg.api(request);
		
		System.out.println("Status Code: " + response.getStatusCode());
		System.out.println("Body: " + response.getBody());
		System.out.println("Headers: " + response.getHeaders());
	}
}
