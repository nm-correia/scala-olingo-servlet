package pt.nmsc.odata.srv

import java.io.IOException
import java.io.InputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedList
import java.util.Properties
import java.util.Set
import java.util.List
import java.util.Map

import java.lang.Double
import java.lang.Integer
import java.lang.Boolean

import scala.collection.JavaConversions._
import collection.JavaConverters._
import scala.collection.mutable.ListBuffer

import util.control.Breaks._

//Service constants
import pt.nmsc.odata.srv.Service1Constants._
import scala.collection.mutable.SortedSet


class Service1DataStore {
  
  // Mock data
  private var productsMockData: List[java.util.Map[String, Object]] = new ArrayList
  private var product1: Map[String, Object] = new HashMap
  product1.put("ProductID", new Integer(1))
  product1.put("ProductName", "Chai")
  product1.put("SupplierID", new Integer(1))
  product1.put("CategoryID", new Integer(1))
  product1.put("QuantityPerUnit", "10 boxes x 20 bags")
  product1.put("UnitPrice", new Double(18.0000))
  product1.put("UnitsInStock", new Integer(39))
  product1.put("UnitsOnOrder", new Integer(0))
  product1.put("ReorderLevel", new Integer(10))
  product1.put("Discontinued", new Boolean(false))
  productsMockData.add(product1)
  
  private var product2: Map[String, Object] = new HashMap
  product2.put("ProductID", new Integer(2))
  product2.put("ProductName", "Chang")
  product2.put("SupplierID", new Integer(1))
  product2.put("CategoryID", new Integer(1))
  product2.put("QuantityPerUnit", "24 - 12 oz bottles")
  product2.put("UnitPrice", new Double(18.0000))
  product2.put("UnitsInStock", new Integer(39))
  product2.put("UnitsOnOrder", new Integer(0))
  product2.put("ReorderLevel", new Integer(10))
  product2.put("Discontinued", new Boolean(false)) 
  productsMockData.add(product2)
  
  private var categoriesMockData: List[Map[String, Object]] = new ArrayList
  private var category1: Map[String, Object] = new HashMap
  category1.put("CategoryID", new Integer(1))
  category1.put("CategoryName", "Beverages")
  category1.put("Description", "Soft drinks, coffees, teas, beers, and ales")
  categoriesMockData.add(category1)
  
  private var category2: Map[String, Object] = new HashMap
  category2.put("CategoryID", new Integer(2))
  category2.put("CategoryName", "Condiments")
  category2.put("Description", "Sweet and savory sauces, relishes, spreads, and seasonings")
  categoriesMockData.add(category2)

  
//  private val productsMockData: ListBuffer[String, Object] = ListBuffer(
//	  Map("ProductID" -> 1,
//		  "ProductName" -> "Chai", "SupplierID" -> 1,
//		  "CategoryID" -> 1, "QuantityPerUnit" -> "10 boxes x 20 bags",
//		  "UnitPrice" -> 18.0000, "UnitsInStock" -> 39,
//		  "UnitsOnOrder" -> 0,"ReorderLevel"-> 10, "Discontinued"-> false).asJava,
//		  
//	Map("ProductID" -> 2,
//		  "ProductName" -> "Chang", "SupplierID" -> 1,
//		  "CategoryID" -> 1, "QuantityPerUnit" -> "24 - 12 oz bottles",
//		  "UnitPrice" -> 18.0000, "UnitsInStock" -> 39,
//		  "UnitsOnOrder" -> 0,"ReorderLevel"-> 10, "Discontinued"-> false).asJava
//	).asJava;

	def this(n: Int) { this() }

	/**
	 * Get the count of entities from client 
	 * @param entity
	 * @return
	 */
	def getCounter(entity: String, filter: String): Int = {
		var counter: Int = -1
		
		if(entity.equals(ENTITY_SET_NAME_PRODUCT))
			counter = productsMockData.size()
		else if(entity.equals(ENTITY_SET_NAME_CATEGORY))
			counter = categoriesMockData.size()

		return counter
	}


	/**
	 * Get Products entity set 
	 * @param selects
	 * @param filters
	 * @param queryParam
	 * @return
	 */
	def getMasterSet(entityName: String, selects: SortedSet[String],
	    filters: String, queryParam: Map[String, String], basicFilter: String): List[Map[String, Object]] = {
		
		// Mock Data
		if(entityName.equals(ENTITY_SET_NAME_PRODUCT))
			return pagingOperation(productsMockData, queryParam)
		else if(entityName.equals(ENTITY_SET_NAME_CATEGORY))
			return pagingOperation(categoriesMockData, queryParam)
		else
		  return null
	}

	/**
	 * Paging query over return entities 
	 * @param data
	 * @param queryParam
	 * @return
	 */
	private def pagingOperation(data: List[Map[String, Object]],
	                            queryParam: Map[String, String]) : List[Map[String, Object]] = {
//	  var output: ListBuffer[Map[String, Any]] = ListBuffer[Map[String, Any]]()
//
//		if(queryParam == null ) return data
//
//		var top: Int = 0
//		var skip: Int = 0
//		var index: Int = 0
//		var counter: Int = 0
//
//		for(entry <- queryParam.entrySet()) {
//			if(entry.getKey().compareTo("top") == 0) top = new Integer( entry.getValue());
//			if(entry.getKey().compareTo("skip") == 0) skip = new Integer( entry.getValue());
//		}
//
//
//		for (element <- data) {
//			if(skip != 0) {
//				if (index < skip ) {
//				  index += 1
//					continue
//				}
//			}
//
//			if(top !=  0) {
//			  counter += 1;
//				if (counter == top ) {
//					break
//				}
//			}
//			output.add(element);   
//
//		}
//		output
	  data
	}


}
