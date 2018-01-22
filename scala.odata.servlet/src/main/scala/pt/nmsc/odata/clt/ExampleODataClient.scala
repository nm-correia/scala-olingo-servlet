package pt.nmsc.odata.clt

import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.edm.Edm
import org.apache.olingo.odata2.api.edm.EdmEntityContainer
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.ep.EntityProvider
import org.apache.olingo.odata2.api.ep.EntityProviderException
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.ep.feed.ODataDeltaFeed
import org.apache.olingo.odata2.api.ep.feed.ODataFeed
import org.apache.olingo.odata2.api.exception.ODataException
import org.apache.olingo.odata2.api.processor.ODataResponse
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.TrustManager
import com.sun.net.ssl.X509TrustManager
import org.apache.http.impl.client.{BasicResponseHandler, DefaultHttpClient}
import org.apache.http.{HttpRequest, HttpRequestInterceptor}
import org.apache.http.client.methods.HttpGet
import org.apache.http.protocol.HttpContext
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.conn.params.ConnRoutePNames
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.methods.HttpRequestBase
import scala.collection.JavaConversions._

object ExampleODataClient {
  
  //Http & oData constants 
  val HTTP_METHOD_PUT       = "PUT"
  val HTTP_METHOD_GET       = "GET"
  val HTTP_METHOD_POST      = "POST"
  val HTTP_METHOD_DELETE    = "DELETE"  
  
  val HTTP_HEADER_CONTENT_TYPE = "Content-Type"
  val HTTP_HEADER_ACCEPT       = "Accept"
    
  val APPLICATION_JSON     = "application/json"
  val APPLICATION_XML      = "application/xml"
  val APPLICATION_ATOM_XML = "application/atom+xml"
  val APPLICATION_FORM     = "application/x-www-form-urlencoded"
  val METADATA             = "$metadata"
  val SEPARATOR            = "/"

  
  def main(args: Array[String]) {
    val serviceUrl = "http://services.odata.org/V2/Northwind/Northwind.svc"
    val contentType = APPLICATION_JSON
      
    println("------------Read Metadata-------------------")
    val edm = readEdm(serviceUrl);
    println("Read default entity container " + edm.getDefaultEntityContainer().getName())
    
    println("---------------Read Feed--------------------")
    val feed = readFeed(edm, serviceUrl, contentType, "Products")
    
    println("Read: " + feed.getEntries().size() + " entries:")
    
    for(entry<-feed.getEntries()) {
      println("Entry for details " +
          entry.getMetadata().getUri().replace(serviceUrl + SEPARATOR, ""))
      printEntry(entry)
    }
    
    println("---------------Read Entry--------------------")
    val entry = readEntry(edm, serviceUrl, contentType, "Products", "1")
    printEntry(entry)
  }  

  
  def readEdm(serviceUrl:String):Edm = {
		val content = execute(serviceUrl + SEPARATOR + METADATA,
		                        APPLICATION_XML, HTTP_METHOD_GET)
		EntityProvider.readMetadata(content, false)
  }  

  
  def readFeed(edm:Edm,serviceUri:String, contentType:String,
              entitySetName:String):ODataFeed = {
    val entityContainer = edm.getDefaultEntityContainer()
    val absoluteUri = createUri(serviceUri, entitySetName)
    println("Reading feed from url: " + absoluteUri)
    val content = execute(absoluteUri, contentType, HTTP_METHOD_GET)
    EntityProvider.readFeed(contentType, entityContainer.getEntitySet(entitySetName),
        content, EntityProviderReadProperties.init().build())
  }

  
  def readEntry(edm:Edm, serviceUri:String, contentType:String,
      entitySetName:String, keyValue:String, expandRelationName:String = ""):ODataEntry = {
    val entityContainer = edm.getDefaultEntityContainer()
    val absoluteUri = createUri(serviceUri, entitySetName, keyValue, expandRelationName)
    println("Reading feed from url: " + absoluteUri)
    val content = execute(absoluteUri, contentType, HTTP_METHOD_GET)
    EntityProvider.readEntry(contentType, entityContainer.getEntitySet(entitySetName),
        content, EntityProviderReadProperties.init().build())
  }

  
  def printEntry(entry:ODataEntry):Unit = {
	  for(property<-entry.getProperties().entrySet())
	      println(s"key : ${property.getKey()} and value : ${property.getValue()}")
  }

  
  def createUri(serviceUri:String, entitySetName:String,
      id:String = "", expand:String = "") = {
    var absoluteUri = s"$serviceUri$SEPARATOR$entitySetName"
    
    if(id != "") absoluteUri += s"($id)"
    if(expand != "") absoluteUri += s"/?expand=$expand"
    
    absoluteUri
  }

  
  def getClient():DefaultHttpClient = {
    val client = new DefaultHttpClient
    //val proxy = new HttpHost("proxy", 8080)
    //client.getCredentialsProvider().setCredentials(AuthScope.ANY,
    // 											new UsernamePasswordCredentials("<user>","<Password>") )
    //client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy)
    client
  }

  
  def execute(relativeUri:String,contentType:String, httpMethod:String):InputStream = {
    val client = getClient()
    httpMethod match {
      case HTTP_METHOD_GET=>
        	val httpget = new HttpGet(relativeUri)
        	httpget.addHeader(HTTP_HEADER_ACCEPT, contentType) 
        	val response = client.execute(httpget)
        	response.getEntity().getContent()
    }
  }

}