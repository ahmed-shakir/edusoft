package se.supernovait.edusoft.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * <description>
 *
 * @author Ahmed Shakir
 * @version 1.0
 * @since 2020-10-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    private String id;
    @NotBlank
    @Size(min = 3, max = 15)
    private String name;
    @NotBlank
    @Size(min = 10)
    private String description;
    @NotBlank
    @Size(min = 3, max = 15)
    private String designation;
    @PastOrPresent
    private LocalDate created;
    @FutureOrPresent
    private LocalDate starts;
    @Future
    private LocalDate ends;
}
