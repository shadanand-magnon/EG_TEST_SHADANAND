package sp.com.sp1024ad.core.workflow;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Properties;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

@Component
@Service
@Properties({
	@Property(name = Constants.SERVICE_DESCRIPTION, value = "Test Email workflow process implementation."),
	@Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
	@Property(name = "process.label", value = "SHADANAND EAMIL WORKFLOW") })
public class CustomStepWF implements WorkflowProcess {

	private static final Logger logger = LoggerFactory.getLogger(CustomStepWF.class);
	@Reference
	private MessageGatewayService messageGatewayService;

	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
		// TODO Auto-generated method stub
		logger.error("email testing execute");
		// Declare a MessageGateway service
		MessageGateway<Email> messageGateway;
		final WorkflowData workflowData = item.getWorkflowData();
		//final String type = workflowData.getPayloadType();


		// Get the path to the JCR resource from the payload
		final String path = workflowData.getPayload().toString();

		// Set up the Email message
		Email email = new SimpleEmail();
		email.setHostName("smtp.gmail.com");
		//email.setSmtpPort(25);
		email.setTLS(true);
		email.setAuthentication("testtrainingmagnon@gmail.com", "M@gn0ntbwa");

		// Set the mail values
		//String emailToRecipients = "shadanand.pandey@magnontbwa.com";
		String emailToRecipients = "anju.thakur@magnon-egplus.com";

		try {
			email.addTo(emailToRecipients);
			email.setSubject("AEM Custom Workflow Step");
			email.addCc("sunil.ahuja@magnon-egplus.com");
			email.addBcc("shadanand.pandey@magnontbwa.com");
			email.setFrom("testtrainingmagnon@gmail.com");
			/*email.setMsg("This message is to inform you that the CQ Home page created\n"
			+ "<b>Path:</b>http://52.221.39.146:4502/"+path+".html");*/
			email.setContent("<p>This message is to inform you that the CQ Home page created. Use below credential for opening page</p>"+
					"<b>UserName:</b> traininguser <br>"+
					"<b>Password:</b> user@1234 <br>"+
					"<b>Path:</b> http://52.221.39.146:4502/"+path+".html<br>"+
					"<b>Github Link:</b> https://github.com/shadanand-magnon/EG_TEST_SHADANAND <br>"+					
					"<br><br><br></br>"+
					"<b>Regards,</b></br> Shadanand Pandeya<br>","text/html; charset=utf-8");
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			logger.error("email testing EmailException",e);
			e.printStackTrace();
		}

		// Inject a MessageGateway Service and send the message
		messageGateway = messageGatewayService.getGateway(Email.class);

		// Check the logs to see that messageGateway is not null
		messageGateway.send((Email) email);

	}
}