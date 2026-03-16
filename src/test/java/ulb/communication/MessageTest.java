package ulb.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ser.std.ByteArraySerializer;

import ulb.communication.types.*;
import ulb.controller.action.*;
import ulb.repository.ItemRepository;
import ulb.repository.mock.ItemMockRepository;
import ulb.model.item.Item;

public class MessageTest {

    public Message serializeNdeserialze(Message message) throws IOException, ClassNotFoundException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream serialised = new ObjectOutputStream(baos);
            serialised.writeObject(message);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream deserialized = new ObjectInputStream(bais);

            return (Message) deserialized.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            return null;
        }
        
    }

    @Test
    void testErrorMessageSerialization() throws IOException, ClassNotFoundException {
        ErrorMessage originalMessage = new ErrorMessage("test");

        ErrorMessage deserializedMessage = (ErrorMessage) serializeNdeserialze(originalMessage);

        assertNotNull(deserializedMessage);
        assertEquals(originalMessage.getError(), deserializedMessage.getError());

    }

    @Test
    void testConnectMessageSerialization() throws IOException, ClassNotFoundException {
        ConnectMessage originalMessage = new ConnectMessage("test");

        ConnectMessage deserializedMessage = (ConnectMessage) serializeNdeserialze(originalMessage);

        assertNotNull(deserializedMessage);
        assertEquals(originalMessage.getConnectMessage(), deserializedMessage.getConnectMessage());

    }

    // @Test
    // void testActionMessageSerialization() throws IOException, ClassNotFoundException {
    //     ItemRepository items = new ItemMockRepository();
    //     Item originalItem = items.findById("baie_revigorante");
    //     Action originalAction = new UseItem(originalItem);

    //     ActionMessage originalMessage = new ActionMessage(originalAction);

    //     ActionMessage deserializedMessage = (ActionMessage) serializeNdeserialze(originalMessage);

    //     Action deserializedAction = deserializedMessage.getAction();
    //     Item deserializedItem = ((UseItem) deserializedAction).getItem();

    //     assertNotNull(deserializedMessage);

    //     assertEquals(deserializedItem.getId(), deserializedItem.getId());

    // }


    
}


