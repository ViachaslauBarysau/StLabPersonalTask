<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
</head>
<body>
<!-- Form starts -->
<form method="post">
    <!-- Table starts -->
    <table class="table table-striped">
        <thead class="thead-dark">
        <tr>
            <th></th>
            <th>Title</th>
            <th>Authors</th>
            <th>Publish date</th>
            <th>Amount available</th>
        </tr>
        </thead>
        <c:forEach var="book" items="${books}">
            <tr>
                <td><input type="checkbox" name="bookid" value="${book.id}"></td>
                <td>
                    <a class="stretched-link" href="lib-app?command=BOOK_PAGE&id=${book.id}">
                        ${book.title}
                    </a>
                </td>
                <td><c:forEach var="author" items="${book.authors}">
                    ${author}<br>
                    </c:forEach>
                </td>
                <td>${book.publishDate}</td>
                <td>${book.availableAmount}</td>
            </tr>
        </c:forEach>
    </table>
    <!-- Table ends -->
    <input hidden name="page" value="${pageNumber}">
    <button type="submit" class="btn btn-primary" formaction="lib-app?command=ADD_BOOK_PAGE" />Add Book</button>
    <button type="submit" class="btn btn-primary" formaction="lib-app?command=DELETE_BOOK" />Delete Book(-s)</button>

    <c:if test ="${not empty pageNumber}">
        <c:if test ="${pageNumber > 1}">
            <button type="submit" class="btn btn-primary"
                    formaction="lib-app?command=GET_BOOK_LIST&page=${pageNumber-1}" />
            &lt;
            </button>
            ${pageNumber} of ${pageCount}
            <button type="submit" class="btn btn-primary"
                    formaction="lib-app?command=GET_BOOK_LIST&page=${pageNumber+1}" disabled/>
            &gt;
            </button>
        </c:if>
        <c:if test ="${pageNumber < pageCount}">
            <button type="submit" class="btn btn-primary"
                    formaction="lib-app?command=GET_BOOK_LIST&page=${pageNumber-1}" disabled/>
            &lt;
            </button>
            ${pageNumber} of ${pageCount}
            <button type="submit" class="btn btn-primary"
                    formaction="lib-app?command=GET_BOOK_LIST&page=${pageNumber+1}" />
            &gt;
            </button>
        </c:if>
    </c:if>
    <c:if test ="${not empty hideUnavailable}">
    <span style="float:right"><input type="checkbox" id="hide-unavailable" name="hideunavailable"  align="right"
                                             onchange="hideUnavailable(this)" checked />   Hide unavailable</span>
    </c:if>
    <c:if test ="${empty hideUnavailable}">
    <span style="float:right"><input type="checkbox" id="hide-unavailable" name="hideunavailable"  align="right"
                                             onchange="hideUnavailable(this)" />   Hide unavailable</span>
    </c:if>
</form>
<!-- Form ends -->
</body>
</html>
<script type="text/javascript">
    <%@include file="/WEB-INF/js/booklistpage.js"%>
</script>
