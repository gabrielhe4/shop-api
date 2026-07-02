package io.github.gabrielhe4.shop_api.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")        
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Getter
    @NotBlank
    @Size(max = 20)
    private String username;


    @Getter
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @Getter
    @NotBlank
    @Size(max = 100)
    private String password;

    public User(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 40) @Email String email,
            @NotBlank @Size(max = 100) String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    
    @Setter
    @Getter
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch =  FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Getter
    @Setter
    @OneToMany(mappedBy = "user", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

}
