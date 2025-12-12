package fpt.is.bnk.fptis_platform.entity.authorization;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "permissions",
        uniqueConstraints = @UniqueConstraint(name = "uk_permission_name", columnNames = "name"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @ManyToMany(mappedBy = "permissions")
    Set<Role> roles;
}
