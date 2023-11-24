package bg.project.petsittingapp.model.entity;

import bg.project.petsittingapp.model.enums.AnimalType;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "pets")
public class Pet extends BaseEntity {
    @Column(nullable = false)
    @Length(min = 1)
    private String name;
    @OneToOne(optional = false)
    private Image image;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnimalType type;
    @Column
    private String breed;
    @Column(nullable = false)
    @Length(min = 1)
    private String owner;

    public Pet() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public AnimalType getType() {
        return type;
    }

    public void setType(AnimalType type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
