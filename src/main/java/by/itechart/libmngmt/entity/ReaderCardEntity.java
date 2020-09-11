package by.itechart.libmngmt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReaderCardEntity {

    private int id;
    private int bookId;
    private int readerId;
    private String readerName;
    private String readerEmail;
    private Date borrowDate;
    private String status;
    private Date dueDate;
    private Timestamp returnDate;
    private String comment;

}
