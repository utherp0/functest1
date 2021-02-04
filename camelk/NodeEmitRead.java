// camel-k: language=java
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.*;

public class NodeEmitRead extends org.apache.camel.builder.RouteBuilder
{
  @Override
  public void configure() throws Exception
  {

    from("knative:event/message.nodeemittedevent")
          .log("Received: ${body}");
  }
}