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
    @Inject
    Vertx vertx;

    @ConfigProperty(name = "TESTENV")
    String exampleENVvariable;

    @Funq
    @CloudEventMapping(responseType = "message.processedbyquarkus")
    //public Uni<MessageOutput> function( Input input, @Context CloudEvent cloudEvent)
    public Uni<MessageOutput> function( Map<String, List<String>> input, @Context CloudEvent cloudEvent)
    {
      System.out.println( "Recv: " + input );

      return Uni.createFrom().emitter(emitter -> 
      {
        buildResponse(input, cloudEvent, emitter);
      });    
    }
 
    public void buildResponse( Map<String, List<String>> input, CloudEvent cloudEvent, UniEmitter<? super MessageOutput> emitter )
    {

    }

    /** 
    public void buildResponse( Input input, CloudEvent cloudEvent, UniEmitter<? super MessageOutput> emitter )
    {
        System.out.println( "In the BuildResponse method...");
        System.out.println( " Received:");
        System.out.println( "    chatid: " + input.getChat());
        System.out.println( "    text: " + input.getText());
        System.out.println( "    username: " + input.getUsername());
        System.out.println( "    isBot: " + input.getIsBot());
        
    } 
    */
}
