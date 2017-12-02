package net.plshark.notes.webservice;

import org.junit.Test;
import org.mockito.Mockito;

import net.plshark.notes.Note;
import net.plshark.notes.NotesService;

/**
 * Tests for {@link NotesController}
 */
public class NotesControllerTest {

    /**
     * Verify an exception is thrown if null objects are passed in the constructor
     */
    @Test(expected = NullPointerException.class)
    public void badConstructorTest() {
        new NotesController(null);
    }

    // TODO test to make sure get with no result is handled

    /**
     * Verify an exception is thrown if the note ID does not match the path ID
     * @throws BadRequestException expected
     */
    @Test(expected = BadRequestException.class)
    public void updateMismatchedIdTest() throws BadRequestException {
        NotesService service = Mockito.mock(NotesService.class);
        NotesController controller = new NotesController(service);

        controller.update(100, new Note(200, 1, 2, "", ""));
    }

    /**
     * Verify the note ID is set to the path ID if the note ID is unset
     * @throws BadRequestException not thrown
     */
    @Test
    public void updateMissingIdTest() throws BadRequestException {
        NotesService service = Mockito.mock(NotesService.class);
        NotesController controller = new NotesController(service);

        controller.update(100, new Note(1, 2, "", ""));
        Mockito.verify(service).save(Mockito.argThat(note -> note.getId().getAsLong() == 100));
    }
}
