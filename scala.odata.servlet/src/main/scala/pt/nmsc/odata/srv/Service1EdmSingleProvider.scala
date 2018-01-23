package pt.nmsc.odata.srv

import java.io.InputStream
import java.net.URI
import java.util.{ArrayList, Map, HashSet, List, Set}
import scala.collection.JavaConversions._
import collection.JavaConverters._
import scala.collection.mutable.SortedSet

import org.apache.olingo.odata2.api.batch.
{BatchHandler, BatchRequestPart, BatchResponsePart}
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.ep.
{EntityProvider, EntityProviderBatchProperties, EntityProviderWriteProperties}
import org.apache.olingo.odata2.api.ep.
{EntityProviderBatchProperties, EntityProviderWriteProperties}
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder
import org.apache.olingo.odata2.api.exception.ODataException
import org.apache.olingo.odata2.api.exception.ODataNotFoundException
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor
import org.apache.olingo.odata2.api.uri.ExpandSelectTreeNode
import org.apache.olingo.odata2.api.uri.PathInfo
import org.apache.olingo.odata2.api.uri.SelectItem
import org.apache.olingo.odata2.api.uri.UriParser
import org.apache.olingo.odata2.api.uri.info.GetEntitySetCountUriInfo
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo

//Import service 1 constants
import pt.nmsc.odata.srv.Service1Constants._


class Service1EdmSingleProvider extends ODataSingleProcessor {

  private var dataStore : Service1DataStore = new Service1DataStore()

  
  @throws(classOf[ODataException])
	override def readEntitySet(uriInfo : GetEntitySetUriInfo, contentType : String) : ODataResponse = {

		var entitySet : EdmEntitySet = null

		if (uriInfo.getNavigationSegments().size() == 0) {
			entitySet = uriInfo.getStartEntitySet()

			if (entitySet.getName().equals(ENTITY_SET_NAME_PRODUCT) || 
					entitySet.getName().equals(ENTITY_SET_NAME_CATEGORY)) {
				// --- Check URI options ---
				// Basic filter
				var basicFilter: String = null
				var customQuery: Map[String, String] = uriInfo.getCustomQueryOptions()
				try { basicFilter = customQuery("FilterBasic") }
				catch { case e: Exception => {} }

				// Skip
				var queryParam: Map[String, String] = uriInfo.getCustomQueryOptions()
				if (uriInfo.getSkip() != null)
					queryParam.put("skip", uriInfo.getSkip().toString());

				// Select
				var select: java.util.List[SelectItem] = uriInfo.getSelect().toList
				var selectCall: SortedSet[String] = SortedSet[String]()
				for (element <- select)
					selectCall += element.getProperty().getName()

				// Filter
				var filter: String = null
				try	{ filter = uriInfo.getFilter().getExpressionString() }
				catch { case e: Exception => {} }

				// Get properties builder from provider context
				var odataContext: ODataContext = this.getContext()
				var pathInfo: PathInfo = odataContext.getPathInfo()
				var serviceRoot: URI = pathInfo.getServiceRoot()
				var builder: ODataEntityProviderPropertiesBuilder =
						EntityProviderWriteProperties.serviceRoot(serviceRoot)

				// Add 'expand' and 'select' tree from request uri for the properties builder
				var expandedSelectTree: ExpandSelectTreeNode =
						UriParser.createExpandSelectTree(uriInfo.getSelect(), uriInfo.getExpand())
				builder.expandSelectTree(expandedSelectTree)

				// Build properties
				var props: EntityProviderWriteProperties = builder.build()

				// Finally write feed for odata result
				var masterSet = dataStore.getMasterSet(entitySet.getName(), selectCall, filter, queryParam, basicFilter)
				var odataResponse: ODataResponse = EntityProvider.writeFeed(contentType, entitySet, masterSet, props)

				return odataResponse
			}
			else 
				throw new ODataNotFoundException(ODataNotFoundException.ENTITY)

		} else if (uriInfo.getNavigationSegments().size() == 1) {
			entitySet = uriInfo.getTargetEntitySet()
			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
		} else
			throw new ODataNotImplementedException();
	}

	@throws(classOf[ODataException])
	override def executeBatch(handler: BatchHandler, contentType: String, content: InputStream): ODataResponse = {

		var batchResponseParts: List[BatchResponsePart] = new ArrayList[BatchResponsePart]()
		var pathInfo: PathInfo = getContext().getPathInfo()
		var batchProperties: EntityProviderBatchProperties = EntityProviderBatchProperties.init().pathInfo(pathInfo).build()
		var batchParts: List[BatchRequestPart] = EntityProvider.parseBatchRequest(contentType, content, batchProperties).toList
		
		for (batchPart <- batchParts)
			batchResponseParts.add(handler.handleBatchPart(batchPart))
		
		return EntityProvider.writeBatchResponse(batchResponseParts)
	}


	@throws(classOf[ODataException])
	override def countEntitySet(uriInfo: GetEntitySetCountUriInfo, contentType: String): ODataResponse = {

		var filter: String = null
		try	{ filter = uriInfo.getFilter().getExpressionString() }
		catch { case e: Exception => {} }

		if (uriInfo.getTargetEntitySet().getName().equals(ENTITY_SET_NAME_PRODUCT) ||
				uriInfo.getTargetEntitySet().getName().equals(ENTITY_SET_NAME_CATEGORY))
			return ODataResponse.fromResponse(
					EntityProvider.writeText(
							String.valueOf(dataStore
									.getCounter(uriInfo.getTargetEntitySet().getName(), filter)))).build()
		else
		  return ODataResponse.fromResponse(EntityProvider.writeText(String.valueOf("0"))).build()
	}

}
