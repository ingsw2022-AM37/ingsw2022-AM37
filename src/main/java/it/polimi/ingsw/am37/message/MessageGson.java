package it.polimi.ingsw.am37.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Personalized builder of google Gson object to enable the polymorphic serialization and deserialization of messages.
 *
 * @see RuntimeTypeAdapterFactory for reference of how register the new type
 */
public class MessageGson {
    private final static String packageReferece = "it.polimi.ingsw.am37.message";

    private final static String messageField = "messageType";

    public static Gson create() {
        RuntimeTypeAdapterFactory<Message> messageRuntimeTypeAdapterFactory =
                RuntimeTypeAdapterFactory.of(Message.class, messageField);

        for (MessageType type : MessageType.values()) {
            try {
                messageRuntimeTypeAdapterFactory.registerSubtype((Class<? extends Message>) Class.forName(packageReferece + type.getClassName()), type.name());
            } catch (ClassNotFoundException e) {
                System.err.println("MesssageGson#create(): class not found for type " + type);
                throw new RuntimeException(e);
            }
        }
        return new GsonBuilder().registerTypeAdapterFactory(messageRuntimeTypeAdapterFactory).create();
    }
}