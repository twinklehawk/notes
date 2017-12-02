package net.plshark.notes.service;

import org.junit.Test;
import org.mockito.Mockito;

import net.plshark.notes.Note;
import net.plshark.notes.NotesService;
import net.plshark.notes.repo.NotesRepository;

/**
 * Tests for {@link NotesServiceImpl}
 */
public class NotesServiceImplTest {

    /**
     * Verify an exception is thrown if null objects are passed in the constructor
     */
    @Test(expected = NullPointerException.class)
    public void badConstructorTest() {
        new NotesServiceImpl(null);
    }

    /**
     * Verify a note is inserted if the ID is not set
     */
    @Test
    public void saveInsertTest() {
        NotesRepository repo = Mockito.mock(NotesRepository.class);
        NotesService service = new NotesServiceImpl(repo);

        service.save(new Note(1, 2, "", ""));

        // should have called insert since the ID was not set
        Mockito.verify(repo).insert(Mockito.any());
        Mockito.verifyNoMoreInteractions(repo);
    }

    /**
     * Verify a note is updated if the ID is set
     */
    @Test
    public void saveUpdateTest() {
        NotesRepository repo = Mockito.mock(NotesRepository.class);
        NotesService service = new NotesServiceImpl(repo);

        service.save(new Note(4, 1, 2, "", ""));

        // should have called update since the ID was set
        Mockito.verify(repo).update(Mockito.any());
        Mockito.verifyNoMoreInteractions(repo);
    }
}
