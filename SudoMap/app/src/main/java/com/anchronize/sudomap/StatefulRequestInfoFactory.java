package com.anchronize.sudomap;


import android.content.Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hound.android.fd.DefaultRequestInfoFactory;
import com.hound.core.model.sdk.ClientMatch;
import com.hound.core.model.sdk.HoundRequestInfo;

import java.util.ArrayList;

/**
 * We use a singleton in order to not hold a memory reference to the host activity since this is registered in the Houndify
 * singleton.
 */
public class StatefulRequestInfoFactory extends DefaultRequestInfoFactory {

    public static StatefulRequestInfoFactory instance;

    private JsonNode conversationState;

    public static StatefulRequestInfoFactory get(final Context context) {
        if (instance == null) {
            instance= new StatefulRequestInfoFactory(context);
        }
        return instance;
    }

    private StatefulRequestInfoFactory(Context context) {
        super(context);
    }

    public void setConversationState(JsonNode conversationState) {
        this.conversationState = conversationState;
    }

    @Override
    public HoundRequestInfo create() {
        final HoundRequestInfo requestInfo = super.create();
        requestInfo.setConversationState(conversationState);

        /*
         * "Client Match"
         *
         * Below is sample code to demonstrate how to use the "Client Match" Houndify feature which
         * lets client apps specify their own custom phrases to match.  To try out this
         * feature you must:
         *
         * 1. Enable the "Client Match" domain from the Houndify website: www.houndify.com.
         * 2. Uncomment the code below.
         * 3. And finally, to see how the response is handled in go to the HoundVoiceSearchExampleActivity and see
         *    "Client Match" demo code inside of onResponse()
         *
         * This example allows the user to say "turn on the lights", "turn off the lights", and
         * other variations on these phases.
         */


        ArrayList<ClientMatch> clientMatchList = new ArrayList<>();

        // client match 1
        ClientMatch clientMatch1 = new ClientMatch();
        clientMatch1.setExpression("([1/100 (\"can\"|\"could\"|\"will\"|\"would\").\"you\"].[1/10 \"please\"].(\"turn\"|\"switch\"|(1/100 \"flip\")).\"on\".[\"the\"].(\"light\"|\"lights\").[1/20 \"for\".\"me\"].[1/20 \"please\"]) \n" +
                "| \n" +
                "([1/100 (\"can\"|\"could\"|\"will\"|\"would\").\"you\"].[1/10 \"please\"].[100 (\"turn\"|\"switch\"|(1/100 \"flip\"))].[\"the\"].(\"light\"|\"lights\").\"on\".[1/20 \"for\".\"me\"].[1/20 \"please\"]) \n" +
                "| \n" +
                "(((\"i\".(\"want\"|\"like\"))|(((\"i\".[\"would\"])|(\"i'd\")).(\"like\"|\"want\"))).[\"the\"].(\"light\"|\"lights\").[\"turned\"|\"switched\"|(\"to\".\"go\")|(1/100\"flipped\")].\"on\".[1/20\"please\"]) ");

        clientMatch1.setSpokenResponse("Ok, I'm turning the lights on.");
        clientMatch1.setSpokenResponseLong("Ok, I am turning the lights on.");
        clientMatch1.setWrittenResponse("Ok, I'm turning the lights on.");
        clientMatch1.setWrittenResponseLong("Ok, I am turning the lights on.");

        final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode result1Node = nodeFactory.objectNode();
        result1Node.put("Intent", "TURN_LIGHT_ON");
        clientMatch1.setResult(result1Node);

        // add first client match data to the array/list
        clientMatchList.add(clientMatch1);

        // client match 2
        ClientMatch clientMatch2 = new ClientMatch();
        clientMatch2.setExpression("([1/100 (\"can\"|\"could\"|\"will\"|\"would\").\"you\"].[1/10 \"please\"].(\"turn\"|\"switch\"|(1/100 \"flip\")).\"off\".[\"the\"].(\"light\"|\"lights\").[1/20 \"for\".\"me\"].[1/20 \"please\"]) \n" +
                "| \n" +
                "([1/100 (\"can\"|\"could\"|\"will\"|\"would\").\"you\"].[1/10 \"please\"].[100 (\"turn\"|\"switch\"|(1/100 \"flip\"))].[\"the\"].(\"light\"|\"lights\").\"off\".[1/20 \"for\".\"me\"].[1/20 \"please\"]) \n" +
                "| \n" +
                "(((\"i\".(\"want\"|\"like\"))|(((\"i\".[\"would\"])|(\"i'd\")).(\"like\"|\"want\"))).[\"the\"].(\"light\"|\"lights\").[\"turned\"|\"switched\"|(\"to\".\"go\")|(1/100\"flipped\")].\"off\".[1/20\"please\"]) ");

        clientMatch2.setSpokenResponse("Ok, I'm turning the lights off.");
        clientMatch2.setSpokenResponseLong("Ok, I am turning the lights off.");
        clientMatch2.setWrittenResponse("Ok, I'm turning the lights off.");
        clientMatch2.setWrittenResponseLong("Ok, I am turning the lights off.");



        ObjectNode result2Node = nodeFactory.objectNode();
        result2Node.put("Intent", "TURN_LIGHT_OFF");
        clientMatch2.setResult(result2Node);

        // add next client match data to the array/list
        clientMatchList.add(clientMatch2);

        // add as many more client match entries as you like...
        // Client matching for creating events
        ClientMatch clientMatch3 = new ClientMatch();

        clientMatch3.setExpression("([1/100 (\"can\"|\"could\"|\"will\"|\"would\").\"you\"].[1/10 \"please\"].(\"add\"|\"create\"|\"submit\").[\"a\"|\"an\"].\"new\".(\"event\").[1/20 \"for\".\"me\"].[1/20 \"please\"]) \n" +
                "| \n" + // can you please create a new event
                "(((\"i\".(\"want\"|\"like\"))|(((\"i\".[\"would\"])|(\"i'd\")).(\"like\"|\"want\"))).[\"a\"].\"new\".(\"event\").[\"created\"|\"generated\"].[1/20\"please\"]) \n" +
                "| \n" + // I would like a new event created.
                "([1/100 (\"I'd\"|(\"i\".\"would\")|\"would\")].[1/10 (\"like\".\"to\")].(\"add\"|\"create\"|\"submit\").[\"a\"|\"an\"].\"new\".(\"event\").[1/20 \"for\".\"me\"].[1/20 \"please\"]) \n" +
                "| \n" + // I want to add a new event.
                "([1/100 ((\"i\".\"want\")|\"want\")].[1/10 \"to\"].(\"add\"|\"create\"|\"submit\").[\"a\"|\"an\"].\"new\".(\"event\").[1/20 \"please\"]) \n" +
                "| \n" +
                "(\"add\".\"event\")");

        clientMatch3.setSpokenResponse("Ok, I'm adding a new event.");
        clientMatch3.setSpokenResponseLong("Ok, I am adding a new event for you.");
        clientMatch3.setWrittenResponse("Ok, I'm adding a new event.");
        clientMatch3.setWrittenResponseLong("Ok, I am adding a new event for you.");

        ObjectNode result3Node = nodeFactory.objectNode();
        result3Node.put("Intent", "ADD_EVENT");
        clientMatch3.setResult(result3Node);
        clientMatchList.add(clientMatch3);

        // Client match for event name ("Call it ...");
        ClientMatch clientMatch4 = new ClientMatch();
//        Log.d("EVENT", clientMatch4.getExpression());
//        clientMatch4.toString();
        clientMatch4.setExpression("springfest");

        clientMatch4.setSpokenResponse("Ok, I'm adding the event Springfest.");
        clientMatch4.setSpokenResponseLong("Ok, I'm adding the event Springfest.");
        clientMatch4.setWrittenResponse("Ok, I'm adding the event Springfest.");
        clientMatch4.setWrittenResponseLong("Ok, I'm adding the event Springfest.");

        ObjectNode result4Node = nodeFactory.objectNode();
        result4Node.put("Intent", "EVENT_NAME");
        clientMatch4.setResult(result4Node);
        clientMatchList.add(clientMatch4);


        // add the list of matches to the request info object
        requestInfo.setClientMatches(clientMatchList);


        return requestInfo;
    }
}