package xyz.xmap.domain;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.id.UuidStrategy;
import xyz.xmap.converter.ContentConverter;
import xyz.xmap.pojo.Content;

import javax.validation.constraints.NotNull;
import java.util.List;

@NodeEntity
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Path {
    @Id
    @GeneratedValue(strategy = UuidStrategy.class)
    private String id;

    @NotNull
    private String name;

    private String img;

    private Integer state;

    @ApiModelProperty(hidden = true)
    private Integer steps;

    @ApiModelProperty(hidden = true)
    private Integer curves;

    @NotNull
    @Convert(ContentConverter.class)
    private List<Content> contents;

    @JsonIgnore
    @Relationship(type = "AUTHOR", direction = Relationship.INCOMING)
    private Author author;

    @JsonIgnore
    @Relationship(type = "BUILDING", direction = Relationship.INCOMING)
    private Building building;


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Path() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Integer getCurves() {
        return curves;
    }

    public void setCurves(Integer curves) {
        this.curves = curves;
    }
}
