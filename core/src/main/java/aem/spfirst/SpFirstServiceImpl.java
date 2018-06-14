/**
 * 
 */
package aem.spfirst;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Component

@Service
public class SpFirstServiceImpl implements SpFirstService {

	/* (non-Javadoc)
	 * @see aem.spfirst.SpFirstService#getMessage()
	 */
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Hello World , OSGI Service Executed. !!! ";
	}

}
