package enquiry.sp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Reference;
import javax.jcr.Session;
//Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory ;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.resource.ResourceResolver; 
//QUeryBuilder APIs
import com.day.cq.search.QueryBuilder; 
import com.day.cq.search.Query; 
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.result.SearchResult;
import com.day.cq.search.result.Hit; 

@Component

@Service
public class EnquiryServiceImpl implements EnquiryService {

	/** Default log. */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private Session session;

	//Inject a Sling ResourceResolverFactory
	@Reference
	private ResourceResolverFactory resolverFactory;

	@Reference
	private QueryBuilder builder;
	
	@Override
	public ArrayList<EnqueryResultsMapper>  getEnquiryResults() {
		// TODO Auto-generated method stub
		try { 
			//Invoke the adaptTo method to create a Session 
			ResourceResolver resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
			session = resourceResolver.adaptTo(Session.class);

			// create query description as hash map (simplest way, same as form post)
			Map<String, String> map = new HashMap<String, String>();

			// create query description as hash map (simplest way, same as form post)

			map.put("path", "/content/training/shadanand/enquiry/");
			map.put("type", "nt:unstructured");
			map.put("orderby", "@enq_id");

			Query query = builder.createQuery(PredicateGroup.create(map), session);

			query.setStart(0);
			query.setHitsPerPage(20);

			SearchResult result = query.getResult();

			// iterating over the results			
			ArrayList<EnqueryResultsMapper> arrList = new ArrayList<EnqueryResultsMapper>();
			for (Hit hit : result.getHits()) {
				
				EnqueryResultsMapper row = new EnqueryResultsMapper();
				ValueMap valueMap = hit.getProperties();
				
				row.setEnq_id(valueMap.get("enq_id").toString());
				row.setEnq_name(valueMap.get("enq_name").toString());
				row.setEnq_email(valueMap.get("enq_email").toString());
				row.setEnq_phone(valueMap.get("enq_phone").toString());
				row.setEnq_address(valueMap.get("enq_address").toString());
				row.setEnq_city(valueMap.get("enq_city").toString());
				row.setEnq_explain(valueMap.get("enq_explain").toString());		
				arrList.add(row);
			}

			//close the session
			session.logout();

			// return data 
			return arrList;

		}
		catch(Exception e){
			log.info(e.getMessage());
		}
		return null;
	}
}