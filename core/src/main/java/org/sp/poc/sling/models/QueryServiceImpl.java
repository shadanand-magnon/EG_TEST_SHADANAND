package org.sp.poc.sling.models;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

//This is a component so it can provide or consume services
@Component

@Service
public class QueryServiceImpl implements QueryService {

	/** Default log. */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private Session session;

	//Inject a Sling ResourceResolverFactory
	@Reference
	private ResourceResolverFactory resolverFactory;

	@Reference
	private QueryBuilder builder;

	@Override
	public String SearchModifiedContent() {
		try { 

			//Invoke the adaptTo method to create a Session 
			ResourceResolver resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
			session = resourceResolver.adaptTo(Session.class);

			//String fulltextSearchTerm = "Hello";

			// create query description as hash map (simplest way, same as form post)
			Map<String, String> map = new HashMap<String, String>();

			// create query description as hash map (simplest way, same as form post)

			map.put("path", "/content/dam/training");
			//map.put("type", "sling:Folder");
			//map.put("group.p.or", "true"); // combine this group with OR
			//map.put("group.1_fulltext", fulltextSearchTerm);
			//map.put("group.1_fulltext.relPath", "jcr:content");
			//map.put("group.2_fulltext", fulltextSearchTerm);
			//map.put("group.2_fulltext.relPath", "jcr:content/@cq:tags");
			map.put("1_property", "jcr:lastModified");
			map.put("1_property.lowerBound", "2016-11-22");
			map.put("1_property.lowerOperation", ">=");
			// can be done in map or with Query methods
			//map.put("p.offset", "0"); // same as query.setStart(0) below
			//map.put("p.limit", "20"); // same as query.setHitsPerPage(20) below

			Query query = builder.createQuery(PredicateGroup.create(map), session);

			query.setStart(0);
			query.setHitsPerPage(20);

			SearchResult result = query.getResult();

			// iterating over the results
			String path = "";
			for (Hit hit : result.getHits()) {
				path = path + "\n"+ hit.getPath();		
			}

			//close the session
			session.logout();

			// return data 
			return path;

		}
		catch(Exception e){
			log.info(e.getMessage());
		}
		return null; 
	}   

}
