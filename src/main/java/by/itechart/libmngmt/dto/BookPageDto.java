package by.itechart.libmngmt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookPageDto {

    BookDto bookDto;
    List<ReaderCardDto> readerCards;
    Date nearestAvailableDate;
    Date nextNearestAvailableDate;
    int nearestAvailableDateID;

}
