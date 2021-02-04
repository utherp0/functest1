package functions;

import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniEmitter;
import io.vertx.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Function 
{
    private long start = System.currentTimeMillis();

    @Inject
    Vertx vertx;

    @ConfigProperty(name = "TESTENV")
    String exampleENVvariable;

    @Funq
    @CloudEventMapping(responseType = "message.processedbyquarkus")
    //public Uni<MessageOutput> function( Input input, @Context CloudEvent cloudEvent)
    public Uni<MessageOutput> function( Map<String, List<String>> input, @Context CloudEvent cloudEvent)
    {
      return Uni.createFrom().emitter(emitter -> 
      {
        buildResponse(input, cloudEvent, emitter);
      });    
    }
 
    public void buildResponse( Map<String, List<String>> input, CloudEvent cloudEvent, UniEmitter<? super MessageOutput> emitter )
    {
      System.out.println( "Number of elements in input " + input.size());
      List<String> json = input.get("payload");

      System.out.println( json );

      // Build a return packet
      MessageOutput output = new MessageOutput();

      output.setElapsed(System.currentTimeMillis() - start );
      output.setName("Payload Check");
      output.setDetails("Length is " + json.size());
      output.setResponseCode(200);

      emitter.complete(output);
    }
}
