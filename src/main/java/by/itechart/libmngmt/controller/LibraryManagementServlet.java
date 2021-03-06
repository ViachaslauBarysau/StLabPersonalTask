package by.itechart.libmngmt.controller;

import by.itechart.libmngmt.controller.command.LibraryCommand;
import by.itechart.libmngmt.controller.command.LibraryCommandFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Used for processing requests with URL patter "/lib-app".
 */
@MultipartConfig
@WebServlet(urlPatterns = {"/lib-app"})
public class LibraryManagementServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        LibraryCommand command = LibraryCommandFactory.getCommand(req);
        command.init(getServletContext(), req, resp);
        command.process();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        LibraryCommand command = LibraryCommandFactory.getCommand(req);
        command.init(getServletContext(), req, resp);
        command.process();
    }
}
