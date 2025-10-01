package domainModel;

import java.util.Objects;

public class Category {

    private String description;

    public Category(String description) {

        this.description = description;
    }

    public Category(Category category) {
        this.description = category.getDescription();
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(description, category.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
