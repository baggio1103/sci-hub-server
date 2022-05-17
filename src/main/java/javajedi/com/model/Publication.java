package javajedi.com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javajedi.com.enumeration.PublicationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Getter
@Setter
@Entity
@Table(name = "publication")
@AllArgsConstructor
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = WRITE_ONLY)
    private Long id;

    @Column(name = "publicationId", unique = true, nullable = false)
    private String publicationId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "storage_path")
    private String storagePath;

    @Column(name = "is_public", columnDefinition = "boolean default true")
    private Boolean isPublic;

    @Enumerated(EnumType.STRING)
    private PublicationType publicationType;

    @CreationTimestamp
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @CreationTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "publication_topics",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id"))
    private List<Topic> topics;

    public Publication() {
        topics = new ArrayList<>();
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
    }

    public void removeTopic(Topic publicationTopic) {
        topics.remove(publicationTopic);
    }

}
