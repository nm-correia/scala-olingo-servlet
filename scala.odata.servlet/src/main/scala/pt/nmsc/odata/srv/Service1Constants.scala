package pt.nmsc.odata.srv

import org.apache.olingo.odata2.api.edm.FullQualifiedName

object Service1Constants {

	// --- Constants ---
	val NAMESPACE         = "Service1"
	val ENTITY_CONTAINER  = "ODataService1EntityContainer"

	val ENTITY_NAME_PRODUCT        = "Product"
	val ENTITY_SET_NAME_PRODUCT    = "Products"
	val ENTITY_NAME_CATEGORY       = "Category"
	val ENTITY_SET_NAME_CATEGORY   = "Categories"
	
	val ENTITY_TYPE_PRODUCT   = new FullQualifiedName(NAMESPACE, ENTITY_NAME_PRODUCT)
	val ENTITY_TYPE_CATEGORY  = new FullQualifiedName(NAMESPACE, ENTITY_NAME_CATEGORY)
	
}
