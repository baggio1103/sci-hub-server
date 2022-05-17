package javajedi.com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Getter
@Setter
@Entity
@Table(name = "institute")
@AllArgsConstructor
public class Institute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = WRITE_ONLY)
    private Long id;

    @Column(name = "institute_id", unique = true)
    private String instituteId;

    private String country;

    private String name;

    public Institute() {
        users = new ArrayList<>();
    }

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "institute")
    private List<User> users;

    public void addUser(User user) {
        users.add(user);
        user.setInstitute(this);
    }

}
