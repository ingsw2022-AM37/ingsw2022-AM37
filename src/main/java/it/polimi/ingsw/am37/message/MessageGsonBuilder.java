package it.polimi.ingsw.am37.message;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import it.polimi.ingsw.am37.model.UpdatableObject;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;

/**
 * Personalized builder of google Gson object to enable the polymorphic serialization and deserialization of messages.
 * This
 *
 * @see RuntimeTypeAdapterFactory for reference of how register the new type
 */
public class MessageGsonBuilder {
    private final static String messagePackageReference = "it.polimi.ingsw.am37.message.";

    private final static String messageField = "messageType";

    private final static String updatableObjectPackageReference = "it.polimi.ingsw.am37.model.";

    private final static String updatableObjectField = "type";

    private final GsonBuilder gsonBuilder = new GsonBuilder();

    private static JsonDeserializer<StudentsContainer> deserializer() {
        return (jsonElement, type, jsonDeserializationContext) -> jsonDeserializationContext.deserialize(jsonElement,
                UnlimitedStudentsContainer.class);
    }

    public MessageGsonBuilder registerMessageAdapter() {
        RuntimeTypeAdapterFactory<Message> messageRuntimeTypeAdapterFactory =
                RuntimeTypeAdapterFactory.of(Message.class, messageField, true);

        for (MessageType type : MessageType.values()) {
            try {
                messageRuntimeTypeAdapterFactory.registerSubtype((Class<? extends Message>) Class.forName(
                        messagePackageReference + type.getClassName()), type.name());
            } catch (ClassNotFoundException e) {
                System.err.println("MesssageGson#registerMessageAdapter(): class not found for type " + type + ": " +
                        messagePackageReference + type.getClassName());
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

    public MessageGsonBuilder registerUpdatableObjectAdapter() {
        RuntimeTypeAdapterFactory<UpdatableObject> updatableObjectRuntimeTypeAdapterFactory =
                RuntimeTypeAdapterFactory.of(UpdatableObject.class, updatableObjectField, true);

        for (UpdatableObject.UpdatableType type : UpdatableObject.UpdatableType.values()) {
            try {
                updatableObjectRuntimeTypeAdapterFactory.registerSubtype((Class<? extends UpdatableObject>) Class.forName(
                        updatableObjectPackageReference + type.getClassName()), type.name());
            } catch (ClassNotFoundException e) {
                System.err.println("MesssageGson#registerMessageAdapter(): class not found for type " + type + ": " +
                        updatableObjectPackageReference + type.getClassName());
                throw new RuntimeException(e);
            }
        }
        gsonBuilder.registerTypeAdapterFactory(updatableObjectRuntimeTypeAdapterFactory);
        return this;
    }
}