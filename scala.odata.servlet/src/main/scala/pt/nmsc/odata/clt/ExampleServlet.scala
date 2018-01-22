package pt.nmsc.odata.clt

import javax.servlet.http._
import javax.servlet._

class ExampleServlet extends HttpServlet {


  override def doGet(request:HttpServletRequest, response:HttpServletResponse) =
    response.getWriter().println(exampleContent)

    
  def exampleContent(): String = { 
    val serviceUrl = "http://services.odata.org/V2/Northwind/Northwind.svc"
    val contentType = "application/atom+xml"
    val edm = ExampleODataClient.readEdm(serviceUrl)
    val feed = ExampleODataClient.readFeed(edm, serviceUrl, contentType, "Categories")
    
    if(feed.getEntries() != null && feed.getEntries().size() > 0)
      feed.getEntries().get(0).toString()
    else
      "Error"
  }

}