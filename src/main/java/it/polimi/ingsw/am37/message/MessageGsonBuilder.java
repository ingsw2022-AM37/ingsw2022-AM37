package it.polimi.ingsw.am37.message;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;

/**
 * Personalized builder of google Gson object to enable the polymorphic serialization and deserialization of messages.
 * This
 *
 * @see RuntimeTypeAdapterFactory for reference of how register the new type
 */
public class MessageGsonBuilder {
    private final static String packageReference = "it.polimi.ingsw.am37.message.";

    private final static String messageField = "messageType";

    private final GsonBuilder gsonBuilder = new GsonBuilder();

    private static JsonDeserializer<StudentsContainer> deserializer() {
        return (jsonElement, type, jsonDeserializationContext) -> jsonDeserializationContext.deserialize(jsonElement,
                UnlimitedStudentsContainer.class);
    }

    public MessageGsonBuilder registerMessageAdapter() {
        RuntimeTypeAdapterFactory<Message> messageRuntimeTypeAdapterFactory =
                RuntimeTypeAdapterFactory.of(Message.class, messageField);

        for (MessageType type : MessageType.values()) {
            try {
                messageRuntimeTypeAdapterFactory.registerSubtype((Class<? extends Message>) Class.forName(packageReference + type.getClassName()), type.name());
            } catch (ClassNotFoundException e) {
                System.err.println("MesssageGson#registerMessageAdapter(): class not found for type " + type + ": " + packageReference + type.getClassName());
                throw new RuntimeException(e);
            }
        }
        gsonBuilder.registerTypeAdapterFactory(messageRuntimeTypeAdapterFactory);
        return this;
    }

    public MessageGsonBuilder registerStudentContainerAdapter() {
        gsonBuilder.registerTypeAdapter(StudentsContainer.class, deserializer());
        return this;
    }

    public GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }
}