package bg.project.petsittingapp.model.entity;

import bg.project.petsittingapp.model.enums.RoleEnum;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "roles")
public class UserRole extends BaseEntity {
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public UserRole() {
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return this.role.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole userRole)) return false;
        return getRole() == userRole.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRole());
    }
}
