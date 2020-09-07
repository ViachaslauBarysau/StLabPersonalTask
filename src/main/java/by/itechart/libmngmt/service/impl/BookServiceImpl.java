package by.itechart.libmngmt.service.impl;

import by.itechart.libmngmt.dto.BookDto;
import by.itechart.libmngmt.entity.BookEntity;
import by.itechart.libmngmt.repository.BookRepository;
import by.itechart.libmngmt.repository.impl.BookRepositoryImpl;
import by.itechart.libmngmt.service.AuthorService;
import by.itechart.libmngmt.service.BookService;
import by.itechart.libmngmt.service.CoverService;
import by.itechart.libmngmt.service.GenreService;
import by.itechart.libmngmt.util.ConnectionHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BookServiceImpl implements BookService {

    private BookRepository bookRepository = BookRepositoryImpl.getInstance();
    private AuthorService authorService = AuthorServiceImpl.getInstance();
    private GenreService genreService = GenreServiceImpl.getInstance();
    private CoverService coverService = CoverServiceImpl.getInstance();
    private static BookServiceImpl instance = new BookServiceImpl();

    public static BookServiceImpl getInstance() {
        return instance;
    }

    @Override
    public List<BookEntity> getBookPage(int pageNumber) {
        return bookRepository.get(pageNumber);
    }

    @Override
    public int getPageCount() {
        return bookRepository.getPageCount();
    }

    @Override
    public void delete(Object[] booksList) {
        bookRepository.delete(booksList);
    }

    @Override
    public int getSearchPageCount(List<String> searchParams) {

        for (int index=0; index<searchParams.size(); index++){
            searchParams.set(index, "%" + searchParams.get(index) + "%");
        }

        return bookRepository.getSearchPageCount(searchParams);
    }

    @Override
    public void updateBook(BookDto bookDto) {
        bookRepository.update(bookDto);
    }

    @Override
    public BookDto find(int bookId) {
        BookEntity bookEntity = bookRepository.find(bookId);
        BookDto bookDto = BookDto.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .publisher(bookEntity.getPublisher())
                .publishDate(bookEntity.getPublishDate())
                .ISBN(bookEntity.getISBN())
                .description(bookEntity.getDescription())
                .totalAmount(bookEntity.getTotalAmount())
                .pageCount(bookEntity.getPageCount())
                .availableAmount(bookEntity.getAvailableAmount())
                .authors(bookEntity.getAuthors())
                .genres(bookEntity.getGenres())
                .covers(bookEntity.getCovers())
                .build();
        return bookDto;
    }

    @Override
    public List<BookEntity> search(List<String> searchParams, int pageNumber) {

        for (int index=0; index<searchParams.size(); index++){
            searchParams.set(index, "%" + searchParams.get(index) + "%");
        }
        return bookRepository.search(searchParams, pageNumber);

    }

    @Override
    public void updateBook(BookDto bookDto, Connection connection) throws SQLException {
        bookRepository.update(bookDto, connection);
    }

    @Override
    public void addEditBook(BookDto book) {
        try (Connection connection = ConnectionHelper.getConnection()) {
            connection.setAutoCommit(false);
            int bookId = 0;
            if (book.getId()==0) {
                bookId = addBookGetId(book, connection);
            } else {
                updateBook(book, connection);
                bookId = book.getId();
            }
            BookDto bookDto = new BookDto();
            bookDto = BookDto.builder().id(bookId)
                    .authors(book.getAuthors())
                    .covers(book.getCovers())
                    .genres(book.getGenres())
                    .build();

            authorService.add(bookDto, connection);
            genreService.add(bookDto, connection);
            coverService.add(bookDto, connection);
            connection.commit();

        }
             catch (SQLException e) {
            e.printStackTrace();
        }


    }


    @Override
    public int addBookGetId(BookDto book) {
        return bookRepository.add(book);
    }

    @Override
    public int addBookGetId(BookDto book, Connection connection) throws SQLException {
        return bookRepository.add(book, connection);
    }


}
