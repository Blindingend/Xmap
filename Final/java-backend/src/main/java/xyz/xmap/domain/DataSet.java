package xyz.xmap.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.id.UuidStrategy;

@NodeEntity
@ApiModel(value = "数据组")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSet {
    @Id
    @GeneratedValue(strategy = UuidStrategy.class)
    @ApiModelProperty(hidden = true)
    private String id;

    private String name;

    private String type;

    @ApiModelProperty(hidden = true)
    private Integer state;
    @JsonIgnore
    @Relationship(type="AUTHOR")
    private Author author;

    @JsonIgnore
    @Relationship(type="BUILDING")
    private Building building;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
