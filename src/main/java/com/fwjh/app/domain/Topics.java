package com.fwjh.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Topics.
 */
@Entity
@Table(name = "topics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Topics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(min = 5)
    @Column(name = "science")
    private String science;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "information")
    private String information;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(name = "rel_topics__tags", joinColumns = @JoinColumn(name = "topics_id"), inverseJoinColumns = @JoinColumn(name = "tags_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "entries", "names", "sources" }, allowSetters = true)
    private Set<Tags> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Topics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Topics name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScience() {
        return this.science;
    }

    public Topics science(String science) {
        this.setScience(science);
        return this;
    }

    public void setScience(String science) {
        this.science = science;
    }

    public String getInformation() {
        return this.information;
    }

    public Topics information(String information) {
        this.setInformation(information);
        return this;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Topics date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Topics user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Tags> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tags> tags) {
        this.tags = tags;
    }

    public Topics tags(Set<Tags> tags) {
        this.setTags(tags);
        return this;
    }

    public Topics addTags(Tags tags) {
        this.tags.add(tags);
        tags.getEntries().add(this);
        return this;
    }

    public Topics removeTags(Tags tags) {
        this.tags.remove(tags);
        tags.getEntries().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Topics)) {
            return false;
        }
        return id != null && id.equals(((Topics) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Topics{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", science='" + getScience() + "'" +
            ", information='" + getInformation() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
