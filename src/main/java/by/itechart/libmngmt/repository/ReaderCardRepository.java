package by.itechart.libmngmt.repository;

import by.itechart.libmngmt.entity.ReaderCardEntity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ReaderCardRepository {
    Date getNearestReturnDate(int bookId);

    List<ReaderCardEntity> getAllReaderCards(int bookId);

    List<ReaderCardEntity> getActiveReaderCards(int bookId);

    void add(ReaderCardEntity readerCard, Connection connection) throws SQLException;

    void update(ReaderCardEntity readerCard, Connection connection) throws SQLException;

    ReaderCardEntity getReaderCard(int readerCardId);

    List<ReaderCardEntity> getExpiringReaderCards(int days);
}
