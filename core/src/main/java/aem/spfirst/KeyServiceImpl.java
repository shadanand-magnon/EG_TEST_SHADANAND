/**
 * 
 */
package aem.spfirst;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

/**
 * @author shadanand.pandeya
 *
 */

//This is a component so it can provide or consume services
@Component

@Service
public class KeyServiceImpl implements KeyService {
	
	//Define a class member named key
    private int key = 0 ;

	/* (non-Javadoc)
	 * @see aem.spfirst.KeyService#setKey(int)
	 */
	@Override
	public void setKey(int val) {
		// TODO Auto-generated method stub
		//Set the key class member
        this.key = val ;
	}

	/* (non-Javadoc)
	 * @see aem.spfirst.KeyService#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		
		//return the value of the key class member
        
        //Convert the int to a String to display it within an AEM web page
        String strI = Integer.toString(this.key);
        return strI; 
	}
}
