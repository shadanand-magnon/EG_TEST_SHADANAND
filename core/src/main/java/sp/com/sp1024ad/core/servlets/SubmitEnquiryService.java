package sp.com.sp1024ad.core.servlets;

import java.io.IOException;
import java.rmi.ServerException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import java.util.Iterator;
import java.util.UUID;

import javax.jcr.Node;
import javax.jcr.Session;

//Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceResolver;


@SlingServlet(paths="/bin/submitEnquiryServlet", methods = "POST", metatype=false)
public class SubmitEnquiryService extends org.apache.sling.api.servlets.SlingAllMethodsServlet {
	private static final long serialVersionUID = 2598426539166789515L;

	@Reference
	private SlingRepository repository;

	// Inject a Sling ResourceResolverFactory
	@Reference
	private ResourceResolverFactory resolverFactory;

	private Session session;

	public void bindRepository(SlingRepository repository) {
		this.repository = repository; 
	}

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServerException, IOException {

		try
		{
			//Get the submitted form data that is sent from the
			//CQ web page  
			String id = UUID.randomUUID().toString();
			String enq_name = request.getParameter("enq_name");
			String enq_email = request.getParameter("enq_email");
			String enq_address = request.getParameter("enq_address");
			String enq_phone = request.getParameter("enq_phone");
			String enq_city = request.getParameter("enq_city");
			String enq_explain = request.getParameter("enq_explain");

			//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
			//LocalDateTime now = LocalDateTime.now();  

			//Encode the submitted form data to JSON			
			JSONObject obj = new JSONObject();
			obj.put("id",id);
			obj.put("name",enq_name);
			obj.put("email",enq_email);
			obj.put("phone",enq_phone);
			obj.put("address",enq_address);        
			obj.put("city",enq_city);
			obj.put("explain",enq_explain);
			//obj.put("date",now);
			//Get the JSON formatted data    
			String jsonData = obj.toString();

			// Invoke the adaptTo method to create a Session
			ResourceResolver resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
			session = resourceResolver.adaptTo(Session.class);

			// Create a node that represents the root node
			Node root = session.getRootNode();

			// Get the content node in the JCR
			Node content = root.getNode("content/training/shadanand");

			// Determine if the content/customer node exists
			Node enqRoot = null;
			int enqRec = doesEnqExist(content);

			// -1 means that content/customer does not exist
			if (enqRec == -1)
				// content/customer does not exist -- create it
				enqRoot = content.addNode("enquiry", "sling:OrderedFolder");
			else
				// content/customer does exist -- retrieve it
				enqRoot = content.getNode("enquiry");


			int enqId = enqRec + 1; // assign a new id to the customer node

			// Store content from the client JSP in the JCR
			Node enqNode = enqRoot.addNode("enquiry" + enq_name + enq_email + enqId, "nt:unstructured");

			// make sure name of node is unique
			enqNode.setProperty("enq_id", enqId);
			enqNode.setProperty("enq_name", enq_name);
			enqNode.setProperty("enq_email", enq_email);
			enqNode.setProperty("enq_phone", enq_phone);
			enqNode.setProperty("enq_address", enq_address);
			enqNode.setProperty("enq_city", enq_city);
			enqNode.setProperty("enq_explain", enq_explain);


			// Save the session changes and log out
			session.save();
			session.logout();
			// Return the JSON formatted data
			response.getWriter().write(jsonData);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private int doesEnqExist(Node content) {
		try {
			int index = 0;
			int childRecs = 0;

			java.lang.Iterable<Node> enqNode = JcrUtils.getChildNodes(content, "enquiry");
			Iterator<Node> it = enqNode.iterator();

			// only going to be 1 content/enquiry node if it exists
			if (it.hasNext()) {
				// Count the number of child nodes in content/enquiry
				Node enqRoot = content.getNode("enquiry");
				Iterable<?> itCust = JcrUtils.getChildNodes(enqRoot);
				Iterator<?> childNodeIt = itCust.iterator();

				// Count the number of customer child nodes
				while (childNodeIt.hasNext()) {
					childRecs++;
					childNodeIt.next();
				}
				return childRecs;
			} else
				return -1; // content/enquiry does not exist
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}