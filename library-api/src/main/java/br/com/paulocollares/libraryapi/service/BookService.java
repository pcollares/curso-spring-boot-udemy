package br.com.paulocollares.libraryapi.service;

import br.com.paulocollares.libraryapi.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {
    public Book save(Book book);

    public Book update(Book book);

    Optional<Book> getById(Long id);

    void delete(Book book);

    Page<Book> find(Book filter, Pageable pageRequest );

}
