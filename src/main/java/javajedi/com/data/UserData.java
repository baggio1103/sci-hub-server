package javajedi.com.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javajedi.com.constant.TimeFormat.DATE_TIME;
import static javajedi.com.constant.TimeFormat.DATE_TIME_FORMAT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class UserData {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String profileImageUrl;
    @JsonFormat(pattern = DATE_TIME)
    private LocalDate joinDate;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime lastLoginDate;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime lastLoginDateDisplay;
    private InstituteData institute;
    private List<TopicData> topics;
    private List<PublicationData> publications;
}
