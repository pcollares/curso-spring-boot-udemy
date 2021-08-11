package br.com.paulocollares.libraryapi.service;

import br.com.paulocollares.libraryapi.exception.BusinessException;
import br.com.paulocollares.libraryapi.model.entity.Book;
import br.com.paulocollares.libraryapi.model.repository.BookRepository;
import br.com.paulocollares.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTeste {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar o livro")
    public void saveBookTest() {
        //Cenario
        Book book = createValidBook();

        Mockito.when(
                repository.existsByIsbn(Mockito.anyString())
        ).thenReturn(false);

        Mockito.when(
                repository.save(book)
        ).thenReturn(
                Book.builder()
                        .id(1L)
                        .isbn("123123123")
                        .author("Paulo")
                        .title("Meu livro")
                        .build()
        );

        //execução
        Book savedBook = service.save(book);

        //verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("Meu livro");
        assertThat(savedBook.getAuthor()).isEqualTo("Paulo");
        assertThat(savedBook.getIsbn()).isEqualTo("123123123");
    }


    @Test
    @DisplayName("Deve lançat erro de regra de negócio quando tentar cadastrar livro com ISBN duplicado")
    public void shouldNotSaveBookDuplicatedISBN() {
        //Cenario
        Book book = createValidBook();

        Mockito.when(
                repository.existsByIsbn(Mockito.anyString())
        ).thenReturn(true);

        //execução
        Throwable ex = Assertions.catchThrowable(() -> service.save(book));

        //verificações
        assertThat(ex)
                .isInstanceOf(BusinessException.class)
                .hasMessage("ISBN já cadastrado");

        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN(){
        //cenario
        Book book = createValidBook();
        Mockito.when( repository.existsByIsbn(Mockito.anyString()) ).thenReturn(true);

        //execucao
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        //verificacoes
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("ISBN já cadastrado");

        Mockito.verify(repository, Mockito.never()).save(book);

    }

    @Test
    @DisplayName("Deve obter um livro por Id")
    public void getByIdTest(){
        Long id = 1l;
        Book book = createValidBook();
        book.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        //execucao
        Optional<Book> foundBook = service.getById(id);

        //verificacoes
        assertThat( foundBook.isPresent() ).isTrue();
        assertThat( foundBook.get().getId()).isEqualTo(id);
        assertThat( foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat( foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
        assertThat( foundBook.get().getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por Id quando ele não existe na base.")
    public void bookNotFoundByIdTest(){
        Long id = 1l;
        Mockito.when( repository.findById(id) ).thenReturn(Optional.empty());

        //execucao
        Optional<Book> book = service.getById(id);

        //verificacoes
        assertThat( book.isPresent() ).isFalse();

    }

    @Test
    @DisplayName("Deve deletar um livro.")
    public void deleteBookTest(){
        Book book = Book.builder().id(1l).build();

        //execucao
        org.junit.jupiter.api.Assertions.assertDoesNotThrow( () -> service.delete(book) );

        //verificacoes
        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar deletar um livro inexistente.")
    public void deleteInvalidBookTest(){
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        Mockito.verify( repository, Mockito.never() ).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar atualizar um livro inexistente.")
    public void updateInvalidBookTest(){
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));

        Mockito.verify( repository, Mockito.never() ).save(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro.")
    public void updateBookTest(){
        //cenário
        long id = 1l;

        //livro a atualizar
        Book updatingBook = Book.builder().id(id).build();

        //simulacao
        Book updatedBook = createValidBook();
        updatedBook.setId(id);
        Mockito.when(repository.save(updatingBook)).thenReturn(updatedBook);

        //exeucao
        Book book = service.update(updatingBook);

        //verificacoes
        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());

    }

    private Book createValidBook() {
        return Book.builder()
                .isbn("123123123")
                .author("Paulo")
                .title("Meu livro")
                .build();
    }


}
