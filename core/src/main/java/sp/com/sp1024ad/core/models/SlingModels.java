package sp.com.sp1024ad.core.models;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@SlingServlet(paths="/bin/slingmodel", methods="GET")
public class SlingModels extends SlingAllMethodsServlet{

	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Reference
	ResourceResolverFactory resourceResolverFactory;    
	ResourceResolver resourceResolver;
	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)throws ServletException,IOException{
		logger.info("inside sling model test servlet");
		response.setContentType("text/html");
		try {
			resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
			Resource resource = resourceResolver.getResource("/content/training/shadanand/testsling/slingmodel");
			ValueMap valueMap=resource.adaptTo(ValueMap.class);

			response.getWriter().write("Output from ValueMap is First Name: "+valueMap.get("firstName").toString()+" Last Name: "+valueMap.get("lastName").toString()+" Technology: "+valueMap.get("technology").toString()+"");

			org.sp.poc.sling.models.UserInfo userInfo = resource.adaptTo(org.sp.poc.sling.models.UserInfo.class);
			//response.getWriter().write("Output from Sling Model is First Name: "+userInfo.getFirstName()+" Last Name: "+userInfo.getLastName()+" Technology: "+userInfo.getTechnology());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		finally{
			if(resourceResolver.isLive())
				resourceResolver.close();
		}

	}
}