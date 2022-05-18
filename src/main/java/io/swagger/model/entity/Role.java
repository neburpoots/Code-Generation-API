package io.swagger.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Role implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer role_id;
    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.MERGE,
            },
            mappedBy = "roles")
    @JsonIgnore
    private Set<User> users = new HashSet<>();
}
