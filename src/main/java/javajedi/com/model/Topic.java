package javajedi.com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Getter
@Setter
@Entity
@Table(name = "topic")
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = WRITE_ONLY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "topics")
    private List<User> users;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "topics")
    private List<Publication> publications;

    public Topic(String name) {
        this.name = this.description = name;
    }

}
