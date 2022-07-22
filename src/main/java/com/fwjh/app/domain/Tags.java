package com.fwjh.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tags.
 */
@Entity
@Table(name = "tags")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tags implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "tags" }, allowSetters = true)
    private Set<Topics> entries = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tags" }, allowSetters = true)
    private Set<Projects> names = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tags" }, allowSetters = true)
    private Set<Sources> sources = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tags id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Tags name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Topics> getEntries() {
        return this.entries;
    }

    public void setEntries(Set<Topics> topics) {
        if (this.entries != null) {
            this.entries.forEach(i -> i.removeTags(this));
        }
        if (topics != null) {
            topics.forEach(i -> i.addTags(this));
        }
        this.entries = topics;
    }

    public Tags entries(Set<Topics> topics) {
        this.setEntries(topics);
        return this;
    }

    public Tags addEntry(Topics topics) {
        this.entries.add(topics);
        topics.getTags().add(this);
        return this;
    }

    public Tags removeEntry(Topics topics) {
        this.entries.remove(topics);
        topics.getTags().remove(this);
        return this;
    }

    public Set<Projects> getNames() {
        return this.names;
    }

    public void setNames(Set<Projects> projects) {
        if (this.names != null) {
            this.names.forEach(i -> i.removeTags(this));
        }
        if (projects != null) {
            projects.forEach(i -> i.addTags(this));
        }
        this.names = projects;
    }

    public Tags names(Set<Projects> projects) {
        this.setNames(projects);
        return this;
    }

    public Tags addName(Projects projects) {
        this.names.add(projects);
        projects.getTags().add(this);
        return this;
    }

    public Tags removeName(Projects projects) {
        this.names.remove(projects);
        projects.getTags().remove(this);
        return this;
    }

    public Set<Sources> getSources() {
        return this.sources;
    }

    public void setSources(Set<Sources> sources) {
        if (this.sources != null) {
            this.sources.forEach(i -> i.removeTags(this));
        }
        if (sources != null) {
            sources.forEach(i -> i.addTags(this));
        }
        this.sources = sources;
    }

    public Tags sources(Set<Sources> sources) {
        this.setSources(sources);
        return this;
    }

    public Tags addSources(Sources sources) {
        this.sources.add(sources);
        sources.getTags().add(this);
        return this;
    }

    public Tags removeSources(Sources sources) {
        this.sources.remove(sources);
        sources.getTags().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tags)) {
            return false;
        }
        return id != null && id.equals(((Tags) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tags{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
