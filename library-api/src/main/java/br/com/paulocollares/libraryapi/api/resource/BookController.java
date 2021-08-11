package br.com.paulocollares.libraryapi.api.resource;

import br.com.paulocollares.libraryapi.api.dto.BookDTO;
import br.com.paulocollares.libraryapi.api.exception.ApiErrors;
import br.com.paulocollares.libraryapi.exception.BusinessException;
import br.com.paulocollares.libraryapi.model.entity.Book;
import br.com.paulocollares.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final ModelMapper modelMapper;

    public BookController(BookService bookService, ModelMapper modelMapper) {
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("{id}")
    public BookDTO get(@PathVariable Long id) {
        return bookService
                .getById(id)
                .map(book -> modelMapper.map(book, BookDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Book book = bookService
                .getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        bookService.delete(book);
    }

    @GetMapping
    public Page<BookDTO> find(BookDTO dto, Pageable pageRequest ){
        Book filter = modelMapper.map(dto, Book.class);
        Page<Book> result = bookService.find(filter, pageRequest);
        List<BookDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, BookDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<BookDTO>( list, pageRequest, result.getTotalElements() );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) {

        Book book = modelMapper.map(dto, Book.class);

        book = bookService.save(book);

        return modelMapper.map(book, BookDTO.class);
    }

    @PutMapping("{id}")
    public BookDTO update(@PathVariable Long id, @RequestBody BookDTO dto) {
        Book book = bookService
                .getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        book.setAuthor(dto.getAuthor());
        book.setTitle(dto.getTitle());

        Book updateBook = bookService.update(book);

        return modelMapper.map(updateBook, BookDTO.class);
    }

    //MethodArgumentNotValidException é lançada toda vez que um objeto com o @Valid é validado com erro
    //A anotação @ExceptionHandler intercepta toda exceção lançada do tipo informado nesta classe
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBuisnessExcepiton(BusinessException ex) {
        return new ApiErrors(ex);
    }

}
