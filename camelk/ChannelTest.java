// camel-k: language=java
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.*;

public class ChannelTest extends org.apache.camel.builder.RouteBuilder
{
  @Override
  public void configure() throws Exception
  {

    Processor myProcessor = new MyProcessor();

    from("timer:clock?period=3s")
          //.setBody().simple("{\"test\":\"1\"}")
          .process(myProcessor)
          .setHeader("CE-Type", constant("message.channeltest"))
          .to("knative:event")
          .log("sent message to messages channel");
  
  }

  class MyProcessor implements Processor
  {
    public void process( Exchange exchange ) throws Exception
    {
        exchange.getIn().setBody( "{\"test\":\"2\"}");
    }
  }
}

