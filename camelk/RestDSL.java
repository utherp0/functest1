// camel-k: language=java dependency=camel-jackson dependency=camel-jacksonxml dependency=camel-rss

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.*;

public class RestDSL extends RouteBuilder
{
  @Override
  public void configure() throws Exception
  {
      // Full Beeb - "rss:http://feeds.bbci.co.uk/news/rss.xml?edition=uk&splitEntries=false&delay=1200000"
      // Full SKY - "rss:http://feeds.skynews.com/feeds/rss/world.xml?splitEntries=false&delay=1200000"
      //from("timer:java?period=20000")
      from("rss:http://feeds.skynews.com/feeds/rss/technology.xml?splitEntries=false&delay=60000")
        .routeId("java")
        .setHeader("contentTypeHeader", simple("true"))
        .marshal().rss()
        .unmarshal().jacksonxml(true)
        .marshal().json(true)
        .log("The JSON body: ${body}");
        //.to("log:info");

  }
}