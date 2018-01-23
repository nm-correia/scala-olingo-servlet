package pt.nmsc.odata.srv

import org.apache.olingo.odata2.api.ODataService
import org.apache.olingo.odata2.api.ODataServiceFactory
import org.apache.olingo.odata2.api.edm.provider.EdmProvider
import org.apache.olingo.odata2.api.exception.ODataException
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor

class Service1ServerFactory extends ODataServiceFactory {
	
  @throws(classOf[ODataException])
	override def createService(cx: ODataContext):ODataService = {
	    var edmProvider = new Service1EdmProvider()
	    var singleProcessor = new Service1EdmSingleProvider()
	    return createODataSingleProcessorService(edmProvider, singleProcessor)
	  }

}
