package by.itechart.libmngmt.controller.command.commands;

import by.itechart.libmngmt.controller.command.LibraryCommand;
import by.itechart.libmngmt.entity.BookEntity;
import by.itechart.libmngmt.service.BookService;
import by.itechart.libmngmt.service.impl.BookServiceImpl;
import lombok.Data;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@Data
public class GetBookListCommand extends LibraryCommand {
    private final BookService bookService = BookServiceImpl.getInstance();
    private static GetBookListCommand instance;

    public static GetBookListCommand getInstance() {
        if(instance == null){
            instance = new GetBookListCommand();
        }
        return instance;
    }

    @Override
    public void process() throws ServletException, IOException {

        int pageNumber = 1;

        try {
            pageNumber = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {

        }

        int pageCount = bookService.getPageCount();
        if (pageNumber > pageCount) {
            pageNumber = pageCount;
        } else if (pageNumber < 1) {
            pageNumber = 1;
        }

        List<BookEntity> books = bookService.getBookPage(pageNumber);

        request.setAttribute("books", books);
        request.setAttribute("pageCount", pageCount);
        request.setAttribute("pageNumber", pageNumber);

        forward("mainpage");
    }
}
