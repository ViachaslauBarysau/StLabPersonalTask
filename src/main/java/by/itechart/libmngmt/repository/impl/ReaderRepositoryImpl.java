package by.itechart.libmngmt.repository.impl;


import by.itechart.libmngmt.dto.ReaderDto;
import by.itechart.libmngmt.entity.ReaderEntity;

import by.itechart.libmngmt.repository.ReaderRepository;
import by.itechart.libmngmt.util.ConnectionHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ReaderRepositoryImpl implements ReaderRepository {

    public static ReaderRepositoryImpl getInstance() {
        return instance;
    }

    private static ReaderRepositoryImpl instance = new ReaderRepositoryImpl();

    private static final String SQL_GET_READERS_BY_BOOKID = "SELECT * FROM Readers LEFT JOIN BOOKS_READERS ON Readers.ID=Books_Readers.Reader_Id WHERE Books_Readers.Book_id = ?;";
    private static final String SQL_ADD_READER = "INSERT INTO Readers (Name, Email, Date_of_registration, Phone_number) VALUES (?,?,?,?);";
    private static final String SQL_GET_READERS_EMAILS = "SELECT Email FROM Readers WHERE Email LIKE ?;";
    private static final String SQL_GET_READER_NAME_BY_EMAIL = "SELECT NAME FROM Readers WHERE Email = ?;";

    @Override
    public Map<Integer, ReaderEntity> get(int bookId) {
        Map<Integer, ReaderEntity> map = new HashMap<>();
        try (Connection connection = ConnectionHelper.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_READERS_BY_BOOKID)) {
                preparedStatement.setInt(1, bookId);
                final ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    map.put(resultSet.getInt("ID"), ReaderEntity.builder()
                                                .id(resultSet.getInt("ID"))
                                                .name(resultSet.getString("Name"))
                                                .email(resultSet.getString("Email"))
                                                .dateOfRegistration(resultSet.getDate("Date_of_registration"))
                                                .phoneNumber(resultSet.getLong("Phone_number"))
                                                .build());
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public String getName(String email) {
        String name = "";
        try (Connection connection = ConnectionHelper.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_READER_NAME_BY_EMAIL)) {
                preparedStatement.setString(1, email);
                final ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    name = resultSet.getString("Name");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    public List<String> getEmails(String searchParameter) {
        List<String> list = new ArrayList<>();
        try (Connection connection = ConnectionHelper.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_READERS_EMAILS)) {
                preparedStatement.setString(1, searchParameter);
                final ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                        list.add(resultSet.getString("Email"));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    @Override
    public void add(ReaderDto readerDto) {
        try (Connection connection = ConnectionHelper.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_READER)) {
                preparedStatement.setString(1, readerDto.getName());
                preparedStatement.setString(2, readerDto.getEmail());
                preparedStatement.setDate(3, (java.sql.Date) readerDto.getDateOfRegistration());
                preparedStatement.setLong(4, readerDto.getPhoneNumber());
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
