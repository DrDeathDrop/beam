package org.example.beam.service;

import org.example.beam.dto.*;
import org.example.beam.model.*;
import org.example.beam.repository.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTests {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGame_success() {
        CreateGameDto dto = new CreateGameDto();
        dto.setTitle("Service Game");
        dto.setPublisherId(10L);

        Publisher publisher = new Publisher();
        publisher.setId(10L);
        publisher.setName("Epic Games");

        when(publisherRepository.findById(10L)).thenReturn(Optional.of(publisher));
        when(gameRepository.save(any(Game.class))).thenAnswer(i -> i.getArguments()[0]);

        Game savedGame = gameService.createGame(dto);

        assertNotNull(savedGame);
        assertEquals("Service Game", savedGame.getTitle());
        assertEquals("Epic Games", savedGame.getPublisher().getName());
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void createGame_publisherNotFound_throwsException() {
        CreateGameDto dto = new CreateGameDto();
        dto.setPublisherId(99L);

        when(publisherRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gameService.createGame(dto));
        verify(gameRepository, never()).save(any());
    }

    @Test
    void deleteGame_success() {
        Long gameId = 1L;
        Game game = new Game();
        game.setId(gameId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        gameService.deleteGame(gameId);

        verify(gameRepository).delete(game);
    }

    @Test
    void deleteGame_notFound_throwsException() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gameService.deleteGame(1L));
        verify(gameRepository, never()).delete(any());
    }

    @Test
    void updateGame_fullUpdate_success() {
        Long gameId = 1L;
        Game existingGame = new Game();
        existingGame.setTitle("Old Title");

        UpdateGameDto updateDto = new UpdateGameDto();
        updateDto.setTitle("New Title");
        updateDto.setPublisherId(2L);

        Publisher newPublisher = new Publisher();
        newPublisher.setName("New Publisher");

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(existingGame));
        when(publisherRepository.findById(2L)).thenReturn(Optional.of(newPublisher));
        when(gameRepository.save(any(Game.class))).thenAnswer(i -> i.getArguments()[0]);

        Game updatedGame = gameService.updateGame(gameId, updateDto);

        assertEquals("New Title", updatedGame.getTitle());
        assertEquals("New Publisher", updatedGame.getPublisher().getName());
        verify(gameRepository).save(existingGame);
    }

    @Test
    void updateGame_partialUpdate_onlyTitle() {
        Long gameId = 1L;
        Game existingGame = new Game();
        existingGame.setTitle("Old Title");
        existingGame.setGenre("Action");

        UpdateGameDto updateDto = new UpdateGameDto();
        updateDto.setTitle("Only Title Updated");

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(existingGame));
        when(gameRepository.save(any(Game.class))).thenAnswer(i -> i.getArguments()[0]);

        Game result = gameService.updateGame(gameId, updateDto);

        assertEquals("Only Title Updated", result.getTitle());
        assertEquals("Action", result.getGenre());
        verify(publisherRepository, never()).findById(anyLong());
    }

    @Test
    void getGame_success() {
        Long gameId = 1L;

        Publisher publisher = new Publisher();
        publisher.setName("Test Publisher");

        Game game = new Game();
        game.setId(gameId);
        game.setTitle("Test Game");
        game.setGenre("Action");
        game.setPrice(BigDecimal.valueOf(49.99));
        game.setDescription("A test game");
        game.setReleaseDate("2024");
        game.setPublisher(publisher);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        ShowGameDto result = gameService.getGame(gameId);

        assertNotNull(result);
        assertEquals("Test Game", result.getTitle());
        assertEquals("Action", result.getGenre());
        assertEquals(BigDecimal.valueOf(49.99), result.getPrice());
        assertEquals("A test game", result.getDescription());
        assertEquals("2024", result.getReleaseDate());
        assertEquals("Test Publisher", result.getPublisherName());
    }

    @Test
    void getGame_notFound_throwsException() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> gameService.getGame(99L));

        assertEquals("Game not found", exception.getMessage());
        verify(gameRepository).findById(99L);
    }

}