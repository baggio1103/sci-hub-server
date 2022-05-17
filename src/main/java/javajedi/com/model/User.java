package javajedi.com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Getter
@Setter
@Entity
@Table(name = "users")
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = WRITE_ONLY)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String profileImageUrl;

    private String firstName;

    private String lastName;

    private String role;

    private String[] authorities;

    private boolean isActive;

    private boolean isNotLocked;

    private LocalDate dob;

    private LocalDate joinDate;

    private LocalDateTime lastLoginDate;

    private LocalDateTime lastLoginDateDisplay;

    @ManyToOne(fetch = FetchType.LAZY)
    private Institute institute;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "user_topics",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private List<Topic> topics;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private List<Publication> publications;

    public User() {
        topics = new ArrayList<>();
        publications = new ArrayList<>();
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
    }

    public void removeTopic(Topic topic) {
        topics.remove(topic);
    }

    public void addPublication(Publication publication) {
        publications.add(publication);
        publication.setAuthor(this);
    }

}
