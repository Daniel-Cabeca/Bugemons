package ulb.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.*;

import org.junit.jupiter.api.Test;

import ulb.message.serverToClient.StatusMessage;
import ulb.communication.old_types.ConnectMessage;

public class MessageTest {

    public Serializable serializeNdeserialze(Serializable message) throws IOException, ClassNotFoundException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream serialised = new ObjectOutputStream(baos);
            serialised.writeObject(message);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream deserialized = new ObjectInputStream(bais);

            return (Serializable) deserialized.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            return null;
        }
        
    }

    @Test
    void testErrorMessageSerialization() throws IOException, ClassNotFoundException {
        StatusMessage originalMessage = new StatusMessage(false,"test");

        StatusMessage deserializedMessage = (StatusMessage) serializeNdeserialze(originalMessage);

        assertNotNull(deserializedMessage);
        assertEquals(originalMessage.getMessage(), deserializedMessage.getMessage());

    }

    @Test
    void testConnectMessageSerialization() throws IOException, ClassNotFoundException {
        ConnectMessage originalMessage = new ConnectMessage("test");

        ConnectMessage deserializedMessage = (ConnectMessage) serializeNdeserialze(originalMessage);

        assertNotNull(deserializedMessage);
        assertEquals(originalMessage.getConnectMessage(), deserializedMessage.getConnectMessage());

    }


    
}


