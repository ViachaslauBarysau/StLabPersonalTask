package by.itechart.libmngmt.repository.impl;


import by.itechart.libmngmt.entity.BookEntity;
import by.itechart.libmngmt.repository.BookRepository;
import by.itechart.libmngmt.util.ConnectionHelper;


import java.sql.*;
import java.util.*;

public class BookRepositoryImpl implements BookRepository {

    private static BookRepositoryImpl instance = new BookRepositoryImpl();
    public static BookRepositoryImpl getInstance() {
        return instance;
    }


    private static final int BOOK_PAGE_COUNT = 10;
    private static final String SQL_DELETE_BOOKS_BY_IDS = "DELETE FROM Books WHERE id = ANY (?);";
    private static final String SQL_GET_PAGE_COUNT = "SELECT COUNT(ID) FROM Books;";
    private static final String SQL_GET_BOOK_PAGE = "SELECT Books.*, Authors.Name FROM Books" +
            " LEFT JOIN Books_Authors ON Books_Authors.Book_Id=Books.Id" +
            " LEFT JOIN Authors ON Authors.Id=Books_Authors.Author_Id" +
            " WHERE Book_ID IN (SELECT ID FROM BOOKS ORDER BY Title ASC LIMIT ? OFFSET ?) ORDER BY Title ASC;";
    private static final String SQL_SEARCH_BOOKS = "SELECT Books.*, Authors.Name FROM Books" +
            " LEFT JOIN Books_Authors ON Books_Authors.Book_Id=Books.Id" +
            " LEFT JOIN Authors ON Authors.Id=Books_Authors.Author_Id" +
            " WHERE Books.ID IN (SELECT Books.ID FROM Books" +
            " LEFT JOIN Books_Authors ON Books_Authors.Book_Id=Books.Id" +
            " LEFT JOIN Authors ON Authors.Id=Books_Authors.Author_Id" +
            " LEFT JOIN Books_Genres ON Books_Genres.Book_Id = Books.Id" +
            " LEFT JOIN Genres on Genres.Id=Books_Genres.Genre_Id" +
            " WHERE Books.title LIKE ? AND name LIKE ? AND Genres.Title LIKE ? AND description LIKE ?" +
            "  ORDER BY Books.Title ASC) ORDER BY Title ASC LIMIT ? OFFSET ?;";

    private static final String SQL_GET_SEARCH_PAGE_COUNT = "SELECT COUNT(ID) FROM (SELECT DISTINCT Books.ID" +
            " FROM Books LEFT JOIN Books_Authors ON Books_Authors.Book_Id=Books.Id" +
            " LEFT JOIN Authors ON Authors.Id=Books_Authors.Author_Id" +
            " LEFT JOIN Books_Genres ON Books_Genres.Book_Id = Books.Id" +
            " LEFT JOIN Genres on Genres.Id=Books_Genres.Genre_Id" +
            " WHERE Books.title LIKE ? AND name LIKE ? AND Genres.Title LIKE ? AND description LIKE ?);";


    private static final String SQL_GET_BOOK = "SELECT Books.*, Authors.Name, Genres.Title AS Genre," +
            " Covers.Title AS Cover FROM Books" +
            " LEFT JOIN Books_Authors ON Books_Authors.Book_Id=Books.Id" +
            " LEFT JOIN Authors ON Authors.Id=Books_Authors.Author_Id" +
            " LEFT JOIN Books_Genres ON Books.Id=Books_Genres.Book_Id" +
            " LEFT JOIN Genres ON Books_Genres.Genre_Id=Genres.Id" +
            " LEFT JOIN Covers ON Covers.Book_Id=Books.Id" +
            " WHERE Books.ID = ?;";
    private static final String SQL_ADD_BOOK = "INSERT INTO Books(Title, Publisher, Publish_date, Page_count, " +
            "Isbn, Description, Total_amount, Available_amount) VALUES (?,?,?,?,?,?,?,?);";
    private static final String SQL_UPDATE_BOOK = "UPDATE Books SET Title=?, Publisher=?, Publish_date=?," +
            " Page_count=?, ISBN=?, Description=?, Total_amount=?, Available_amount=? WHERE Id=?;";


    @Override
    public void update(BookEntity book) {
        try (Connection connection = ConnectionHelper.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_BOOK)) {
                int index = 1;
                preparedStatement.setString(index++, book.getTitle());
                preparedStatement.setString(index++, book.getPublisher());
                preparedStatement.setInt(index++, book.getPublishDate());
                preparedStatement.setInt(index++, book.getPageCount());
                preparedStatement.setString(index++, book.getISBN());
                preparedStatement.setString(index++, book.getDescription());
                preparedStatement.setInt(index++, book.getTotalAmount());
                preparedStatement.setInt(index++, book.getAvailableAmount());
                preparedStatement.setInt(index++, book.getId());
                preparedStatement.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSearchPageCount(List<String> searchParams) {
        int searchPageCount = 1;
        try (Connection connection = ConnectionHelper.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_SEARCH_PAGE_COUNT)) {
                int index = 1;
                preparedStatement.setString(index++, searchParams.get(0));
                preparedStatement.setString(index++, searchParams.get(1));
                preparedStatement.setString(index++, searchParams.get(2));
                preparedStatement.setString(index++, searchParams.get(3));
                final ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    int booksCount = resultSet.getInt(1);
                    if (booksCount% BOOK_PAGE_COUNT ==0) {
                        searchPageCount = booksCount/(BOOK_PAGE_COUNT);
                    } else {
                        searchPageCount = booksCount/(BOOK_PAGE_COUNT) + 1;
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchPageCount;
    }

    @Override
    public void update(BookEntity book, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_BOOK)) {
            int index = 1;
            preparedStatement.setString(index++, book.getTitle());
            preparedStatement.setString(index++, book.getPublisher());
            preparedStatement.setInt(index++, book.getPublishDate());
            preparedStatement.setInt(index++, book.getPageCount());
            preparedStatement.setString(index++, book.getISBN());
            preparedStatement.setString(index++, book.getDescription());
            preparedStatement.setInt(index++, book.getTotalAmount());
            preparedStatement.setInt(index++, book.getAvailableAmount());
            preparedStatement.setInt(index++, book.getId());
            preparedStatement.execute();
        }
    }

    @Override
    public int add(BookEntity book, Connection connection) throws SQLException {
        int id = 0;
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_BOOK, Statement.RETURN_GENERATED_KEYS)) {
                int index = 1;
                preparedStatement.setString(index++, book.getTitle());
                preparedStatement.setString(index++, book.getPublisher());
                preparedStatement.setInt(index++, book.getPublishDate());
                preparedStatement.setInt(index++, book.getPageCount());
                preparedStatement.setString(index++, book.getISBN());
                preparedStatement.setString(index++, book.getDescription());
                preparedStatement.setInt(index++, book.getTotalAmount());
                preparedStatement.setInt(index++, book.getAvailableAmount());
                preparedStatement.execute();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                while (resultSet.next()) {
                    id = resultSet.getInt(1);
                }

            }

        return id;
    }

    @Override
    public int add(BookEntity book) {
        int id = 0;
        try (Connection connection = ConnectionHelper.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_BOOK, Statement.RETURN_GENERATED_KEYS)) {
                int index = 1;
                preparedStatement.setString(index++, book.getTitle());
                preparedStatement.setString(index++, book.getPublisher());
                preparedStatement.setInt(index++, book.getPublishDate());
                preparedStatement.setInt(index++, book.getPageCount());
                preparedStatement.setString(index++, book.getISBN());
                preparedStatement.setString(index++, book.getDescription());
                preparedStatement.setInt(index++, book.getTotalAmount());
                preparedStatement.setInt(index++, book.getAvailableAmount());
                preparedStatement.execute();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                while (resultSet.next()) {
                    id = resultSet.getInt(1);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public List<BookEntity> findAll(int offset) {
        List<BookEntity> resultList = new ArrayList<>();
        try (Connection connection = ConnectionHelper.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_BOOK_PAGE)) {
                int index = 1;
                preparedStatement.setInt(index++, BOOK_PAGE_COUNT);
                preparedStatement.setInt(index++, (offset-1)* BOOK_PAGE_COUNT);
                final ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    if (resultList.isEmpty() || resultList.get(resultList.size()-1).getId()!=resultSet.getInt("ID")) {
                        resultList.add(BookEntity.builder()
                                .id(resultSet.getInt("ID"))
                                .title(resultSet.getString("Title"))
                                .authors(new ArrayList<>(Arrays.asList(resultSet.getString("Name"))))
                                .publishDate(resultSet.getInt("Publish_date"))
                                .availableAmount(resultSet.getInt("Available_amount"))
                                .build());
                    } else {
                        resultList.get(resultList.size()-1).getAuthors().add(resultSet.getString("Name"));
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    @Override
    public BookEntity find(int bookId) {
        BookEntity book = null;
        try (Connection connection = ConnectionHelper.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_BOOK, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                preparedStatement.setInt(1, bookId);
                final ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        book = BookEntity.builder()
                                .id(resultSet.getInt("ID"))
                                .title(resultSet.getString("Title"))
                                .publishDate(resultSet.getInt("Publish_date"))
                                .publisher(resultSet.getString("Publisher"))
                                .pageCount(resultSet.getInt("Page_count"))
                                .ISBN(resultSet.getString("Isbn"))
                                .description(resultSet.getString("Description"))
                                .totalAmount(resultSet.getInt("Total_amount"))
                                .availableAmount(resultSet.getInt("Available_amount"))
                                .authors(new ArrayList<>())
                                .covers(new ArrayList<>())
                                .genres(new ArrayList<>())
                                .build();
                        break;
                    }
                Set<String> authors = new HashSet<>();
                Set<String> genres = new HashSet<>();
                Set<String> covers = new HashSet<>();

                if (book != null) {
                    resultSet.beforeFirst();
                    while (resultSet.next()) {
                        if (resultSet.getString("Name") != null) {
                            authors.add(resultSet.getString("Name"));
                        }
                        if (resultSet.getString("Genre")!= null) {
                            genres.add(resultSet.getString("Genre"));
                        }
                        if (resultSet.getString("Cover") != null) {
                            covers.add(resultSet.getString("Cover"));
                        }
                    }
                }

                book.getAuthors().addAll(authors);
                book.getGenres().addAll(genres);
                book.getCovers().addAll(covers);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public int getPageCount() {
        int PageCount = 1;
        try (Connection connection = ConnectionHelper.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_PAGE_COUNT)) {

                final ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    int booksCount = resultSet.getInt(1);
                    if (booksCount% BOOK_PAGE_COUNT ==0) {
                        PageCount = booksCount/(BOOK_PAGE_COUNT);
                    } else {
                        PageCount = booksCount/(BOOK_PAGE_COUNT) + 1;
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return PageCount;
    }


    @Override
    public void delete(Object[] bookList) {
        try (Connection connection = ConnectionHelper.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BOOKS_BY_IDS)) {

                Array booksArray = connection.createArrayOf("INTEGER", bookList);
                preparedStatement.setArray(1, booksArray);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<BookEntity> search(List<String> searchParams, int offset) {
        List<BookEntity> resultList = new ArrayList<>();
        try (Connection connection = ConnectionHelper.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SEARCH_BOOKS)) {
                int index = 1;
                preparedStatement.setString(index++, searchParams.get(0));
                preparedStatement.setString(index++, searchParams.get(1));
                preparedStatement.setString(index++, searchParams.get(2));
                preparedStatement.setString(index++, searchParams.get(3));
                preparedStatement.setInt(index++, BOOK_PAGE_COUNT);
                preparedStatement.setInt(index++, (offset-1)* BOOK_PAGE_COUNT);
                final ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    if (resultList.isEmpty() || resultList.get(resultList.size()-1).getId()!=resultSet.getInt("ID")) {
                        resultList.add(BookEntity.builder()
                                .id(resultSet.getInt("ID"))
                                .title(resultSet.getString("Title"))
                                .authors(new ArrayList<>(Arrays.asList(resultSet.getString("Name"))))
                                .publishDate(resultSet.getInt("Publish_date"))
                                .availableAmount(resultSet.getInt("Available_amount"))
                                .build());
                    } else {
                        resultList.get(resultList.size()-1).getAuthors().add(resultSet.getString("Name"));
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
