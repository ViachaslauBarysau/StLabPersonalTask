package by.itechart.service.impl;

import by.itechart.entity.Book;
import by.itechart.repository.BookRepository;
import by.itechart.repository.impl.BookRepositoryImpl;
import by.itechart.service.BookService;

import java.util.List;

public class BookServiceImpl implements BookService {
    private BookRepository bookRepository = BookRepositoryImpl.getInstance();
    private static BookServiceImpl instance = new BookServiceImpl();

    public static BookServiceImpl getInstance() {
        return instance;
    }

    @Override
    public List<Book> getPageOfBooks(int pageNumber) {
        return bookRepository.getPageOfBooks(pageNumber);
    }

    @Override
    public int getCountOfPages() {
        return bookRepository.getCountOfPages();
    }
}