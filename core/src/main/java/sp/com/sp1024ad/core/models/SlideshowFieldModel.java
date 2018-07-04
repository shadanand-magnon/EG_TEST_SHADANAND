package sp.com.sp1024ad.core.models;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

@Model(adaptables = Resource.class)
public class SlideshowFieldModel {

	ArrayList<SlideshowFiledValuesPOJO> filedValues = new ArrayList<SlideshowFiledValuesPOJO>();
	
	@Inject @Named("slideshow") @Default(values="No resourceType")
	@Optional
	private Resource iItems;

	private String message;
	
	@PostConstruct
	protected void init() {
		if(iItems != null && iItems.hasChildren()){
			for (Resource resource : iItems.getChildren()){
				if(resource != null){
					SlideshowFiledValuesPOJO values = new SlideshowFiledValuesPOJO();
					values.setStrTitle(""+resource.getValueMap().get("title"));
					values.setStrPath(""+resource.getValueMap().get("fileReference"));
					filedValues.add(values);
					message +=  "\n page name ::: "+resource.getValueMap().get("title");
				}
			}
		}
	}
	
	
	public ArrayList<SlideshowFiledValuesPOJO> getFiledValues() {
		return filedValues;
	}

	public Resource getiItems() {
		return iItems;
	}

	public String getMessage() {
		return message;
	}

}
