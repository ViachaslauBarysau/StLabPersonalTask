package by.itechart.libmngmt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a ReaderDto.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReaderDto {
    private int id;
    private String name;
    private String email;
}
