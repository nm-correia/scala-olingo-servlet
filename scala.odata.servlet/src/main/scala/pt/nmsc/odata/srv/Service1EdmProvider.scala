package pt.nmsc.odata.srv;

import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.AliasInfo;
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.EntityContainer;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.Key;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.PropertyRef;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.apache.olingo.odata2.api.exception.ODataException;

import scala.collection.JavaConversions._

// Import service 1 constants
import pt.nmsc.odata.srv.Service1Constants._

class Service1EdmProvider extends EdmProvider {

	@throws(classOf[ODataException])
	override def getSchemas(): java.util.List[Schema] = {

		// Schema
		var schemas: java.util.List[Schema] = new java.util.ArrayList[Schema]()
		var schema: Schema = new Schema()
		schema.setNamespace(NAMESPACE)
				
		var element: AnnotationAttribute = new AnnotationAttribute()

		element.setName("xmlns:sap")
		element.setText("http://www.sap.com/Protocols/SAPData")

		var annotationElements: java.util.List[AnnotationAttribute] = new java.util.ArrayList[AnnotationAttribute]()
		
		annotationElements.add(element) 
		schema.setAnnotationAttributes(annotationElements)

		// Entity types
		var entityTypes: java.util.List[EntityType] = new java.util.ArrayList[EntityType]()
		entityTypes.add(getEntityType(ENTITY_TYPE_PRODUCT))
		entityTypes.add(getEntityType(ENTITY_TYPE_CATEGORY))
		schema.setEntityTypes(entityTypes)

		// Entity container
		var annotationattribute: java.util.List[AnnotationAttribute] = new java.util.ArrayList[AnnotationAttribute]()
		var attribute: AnnotationAttribute = new AnnotationAttribute()

		attribute.setName("sap:supported-formats");
		attribute.setText("atom json xlsx");

		annotationattribute.add(attribute);
		
		var entityContainers: java.util.List[EntityContainer] = new java.util.ArrayList[EntityContainer]()
		var entityContainer: EntityContainer = new EntityContainer()
		entityContainer.setName(ENTITY_CONTAINER).setDefaultEntityContainer(true).setAnnotationAttributes(annotationattribute)

		// Entity sets
		var entitySets: java.util.List[EntitySet] = new java.util.ArrayList[EntitySet]()
		entitySets.add(getEntitySet(ENTITY_CONTAINER, ENTITY_SET_NAME_PRODUCT))
		entitySets.add(getEntitySet(ENTITY_CONTAINER, ENTITY_SET_NAME_CATEGORY))
		entityContainer.setEntitySets(entitySets)

		// Schemas and Containers
		entityContainers.add(entityContainer)
		schema.setEntityContainers(entityContainers)
		schemas.add(schema)

		return schemas;
	}
	
//	@Override
//	public List<AliasInfo> getAliasInfos() throws ODataException {
//		// TODO Auto-generated method stub
//		List<AliasInfo> list = super.getAliasInfos();
//		if(list == null){
//			list = new ArrayList<AliasInfo>();
//		} 
//		AliasInfo alias = new AliasInfo();
//		alias.setAlias("xmlns:sap");
//		alias.setNamespace("http://www.sap.com/Protocols/SAPData");
//		
//		list.add(alias);
//		
//		return list;
//	}	

	@throws(classOf[ODataException])
	override def getEntityType(edmFQName: FullQualifiedName): EntityType = {

		if (edmFQName.getNamespace().equals(NAMESPACE)) {
			if (edmFQName.getName().equals(ENTITY_TYPE_PRODUCT.getName())) {

				var annotationElements: java.util.List[AnnotationAttribute] = new java.util.ArrayList[AnnotationAttribute]()

				var element: AnnotationAttribute = new AnnotationAttribute()

				element.setName("sap:filterable")
				element.setText("false")

				annotationElements.add(element)

				var properties: java.util.List[Property] = new java.util.ArrayList[Property]()
				properties.add(new SimpleProperty().setName("ProductID").setAnnotationAttributes(annotationElements)
				    .setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)));
				properties.add(new SimpleProperty().setName("ProductName").setAnnotationAttributes(annotationElements)
				    .setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
				properties.add(new SimpleProperty().setName("SupplierID").setAnnotationAttributes(annotationElements)
						.setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
				properties.add(new SimpleProperty().setName("CategoryID").setAnnotationAttributes(annotationElements)
						.setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
				properties.add(new SimpleProperty().setName("QuantityPerUnit").setAnnotationAttributes(annotationElements)
						.setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
				properties.add(new SimpleProperty().setName("UnitPrice").setAnnotationAttributes(annotationElements)
						.setType(EdmSimpleTypeKind.Decimal).setFacets(new Facets().setNullable(true)));
				properties.add(new SimpleProperty().setName("UnitsInStock").setAnnotationAttributes(annotationElements)
						.setType(EdmSimpleTypeKind.Int16).setFacets(new Facets().setNullable(true)));
				properties.add(new SimpleProperty().setName("UnitsOnOrder").setAnnotationAttributes(annotationElements)
						.setType(EdmSimpleTypeKind.Int16).setFacets(new Facets().setNullable(true)));
				properties.add(new SimpleProperty().setName("ReorderLevel").setAnnotationAttributes(annotationElements)
						.setType(EdmSimpleTypeKind.Int16).setFacets(new Facets().setNullable(true)));
				properties.add(new SimpleProperty().setName("Discontinued").setAnnotationAttributes(annotationElements)
						.setType(EdmSimpleTypeKind.Boolean).setFacets(new Facets().setNullable(true)));

				// Key
				var keyProperties: java.util.List[PropertyRef] = new java.util.ArrayList[PropertyRef]()
				keyProperties.add(new PropertyRef().setName("ProductID"));
				var key: Key = new Key().setKeys(keyProperties)

				// Build entity type
				return new EntityType().setName(ENTITY_TYPE_PRODUCT.getName())
						.setProperties(properties)
						.setKey(key);
				//.setNavigationProperties(navigationProperties);
			}
			else if (edmFQName.getName().equals(ENTITY_TYPE_CATEGORY.getName())) {
				
				var annotationElements: java.util.List[AnnotationAttribute] = new java.util.ArrayList[AnnotationAttribute]()

				var element: AnnotationAttribute = new AnnotationAttribute()

				element.setName("sap:filterable")
				element.setText("true")

				annotationElements.add(element)

				var properties: java.util.List[Property] = new java.util.ArrayList[Property]()
				properties.add(new SimpleProperty().setName("CategoryID").setAnnotationAttributes(annotationElements)
				    .setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)));
				properties.add(new SimpleProperty().setName("CategoryName").setAnnotationAttributes(annotationElements)
						.setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
				properties.add(new SimpleProperty().setName("Description").setAnnotationAttributes(annotationElements)
						.setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));

				// Key
				var keyProperties: java.util.List[PropertyRef] = new java.util.ArrayList[PropertyRef]()
				keyProperties.add(new PropertyRef().setName("CategoryID"));
				var key: Key = new Key().setKeys(keyProperties)

				// Build entity type
				return new EntityType().setName(ENTITY_TYPE_CATEGORY.getName())
						.setProperties(properties)
						.setKey(key);
				//.setNavigationProperties(navigationProperties);
			}
		}
		return null;
	}

	@throws(classOf[ODataException])
	override def getEntitySet(entityContainer: String, name: String): EntitySet = {

		var annotationElements: java.util.List[AnnotationAttribute] = new java.util.ArrayList[AnnotationAttribute]()

		var element: AnnotationAttribute = new AnnotationAttribute()

		element.setName("sap:content-version")
		element.setText("1")

		annotationElements.add(element);

		
		if(entityContainer.equals(ENTITY_CONTAINER))

			if(name.equals(ENTITY_SET_NAME_PRODUCT))
				return new EntitySet().setName(name).setAnnotationAttributes(annotationElements)
						.setEntityType(ENTITY_TYPE_PRODUCT)

			else if(name.equals(ENTITY_SET_NAME_CATEGORY))
				return new EntitySet().setName(name).setAnnotationAttributes(annotationElements)
						.setEntityType(ENTITY_TYPE_CATEGORY)

		return null
	}

	@throws(classOf[ODataException])
	override def getEntityContainerInfo(name: String): EntityContainerInfo = {
		
		var annotationElements: java.util.List[AnnotationAttribute] = new java.util.ArrayList[AnnotationAttribute]()

		var element: AnnotationAttribute = new AnnotationAttribute()

		element.setName("sap:supported-formats")
		element.setText("atom json xlsx")

		annotationElements.add(element)
		
		
		if (name == null || name.equals(ENTITY_CONTAINER))
			return new EntityContainerInfo().setAnnotationAttributes(annotationElements)
					.setName(ENTITY_CONTAINER)
					.setDefaultEntityContainer(true)
		return null
	}

}
