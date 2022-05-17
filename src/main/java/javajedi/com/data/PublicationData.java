package javajedi.com.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javajedi.com.constant.TimeFormat.DATE_TIME_FORMAT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class PublicationData {
    private String publicationId;
    private String title;
    private String storagePath;
    private String description;
    private Boolean isPublic;
    private String publicationType;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime publishedAt;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime updatedAt;
    private UserData author;
    private List<TopicData> topics;
}
