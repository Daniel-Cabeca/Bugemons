package ulb.service.chat;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InappropriateWordFilterTest {
    @Test
    void censorMasksConfiguredWordsAndKeepsOtherWordsUntouched() {
        InappropriateWordFilter filter = new InappropriateWordFilter(Set.of("idiot", "salope"));

        String censored = filter.censor("Idiot, salut salope !");

        assertEquals("I***t, salut s****e !", censored);
    }

    @Test
    void censorIgnoresCaseAndAccents() {
        InappropriateWordFilter filter = new InappropriateWordFilter(Set.of("imbecile", "cretin"));

        String censored = filter.censor("IMBÉCILE ! quel crétin.");

        assertEquals("I******E ! quel c****n.", censored);
    }

    @Test
    void censorKeepsShortWordsAndPunctuation() {
        InappropriateWordFilter filter = new InappropriateWordFilter(Set.of("oh", "eh"));

        String censored = filter.censor("oh ? eh !");

        assertEquals("oh ? eh !", censored);
    }
}
