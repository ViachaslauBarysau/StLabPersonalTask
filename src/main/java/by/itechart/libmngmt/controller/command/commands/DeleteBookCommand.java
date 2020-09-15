package by.itechart.libmngmt.controller.command.commands;

import by.itechart.libmngmt.controller.command.LibraryCommand;
import by.itechart.libmngmt.service.BookService;
import by.itechart.libmngmt.service.impl.BookServiceImpl;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;

public class DeleteBookCommand extends LibraryCommand {
    private final BookService bookService = BookServiceImpl.getInstance();
    private static DeleteBookCommand instance;

    public static DeleteBookCommand getInstance() {
        if(instance == null){
            instance = new DeleteBookCommand();
        }
        return instance;
    }

    @Override
    public void process() throws ServletException, IOException {

        try {
            Object[] booksIdsForDeleting = Arrays.stream(request.getParameterValues("bookid")).mapToInt(Integer::parseInt).boxed().toArray();
            bookService.delete(booksIdsForDeleting);
        } catch (Exception e) {

        }

        int pageNumber = 1;

        try {
            pageNumber = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {

        }
        int countOfPages = bookService.getPageCount();
        pageNumber = Math.min(countOfPages, pageNumber);
//        List<BookEntity> pageOfBooks = bookService.getBookPage(pageNumber);

        response.sendRedirect(request.getContextPath() + "lib-app?command=GET_BOOK_LIST&page=" + pageNumber);
    }
}
