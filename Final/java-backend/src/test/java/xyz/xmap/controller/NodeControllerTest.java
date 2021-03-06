package xyz.xmap.controller;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import xyz.xmap.TestDefault;

import static org.junit.Assert.*;


public class NodeControllerTest extends TestDefault {

    @Test
    public void nodePost() throws  Exception{
        // invalid author
        mvc.perform(MockMvcRequestBuilders.post("/node")
                .param("author", "NotExist")
                .param("buildingId", building.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"test\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(404));

        // invalid buildingId
        mvc.perform(MockMvcRequestBuilders.post("/node")
                .param("author", author.getId())
                .param("buildingId", "NotExist")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"test\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(404));


        // ok
        assertFalse(nodeRepository.findByBuildingAndNameLike(building.getId(), "testPost", 0, 5).iterator().hasNext());

        mvc.perform(MockMvcRequestBuilders.post("/node")
                .param("buildingId", building.getId())
                .param("author", author.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"testPost\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertTrue(nodeRepository.findByBuildingAndNameLike(building.getId(), "testPost", 0, 5).iterator().hasNext());

    }

    @Test
    public void nodeGetByBuilding() throws  Exception{
        // ok
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("buildingId", building.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nodes[0].id").value(node.getId()));

        //invalid buildingId
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("buildingId", "NotExist"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("nodes").isEmpty());

        // invalid param
        mvc.perform(MockMvcRequestBuilders.get("/nodes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(404));
    }

    @Test
    public void nodeGetByAuthor() throws Exception{
        // ok
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("authorId", author.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nodes[0].id").value(node.getId()));

        // invalid authorId
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("authorId", "NotExist"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nodes").isEmpty());
    }


    @Test
    public void nodeGetByAuthorAndBuilding() throws Exception{
        // ok
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("authorId", author.getId())
                .param("buildingId", building.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nodes[0].id").value(node.getId()));

        // invalid authorId
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("authorId", "NotExist")
                .param("buildingId", building.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nodes").isEmpty());
    }

    @Test
    public void nodeGetByBuildingAndName() throws Exception{
        // ok
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("buildingId", building.getId())
                .param("name", node.getName()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nodes[0].id").value(node.getId()));

        // invalid buildingId
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("buildingId", "NotExist")
                .param("name", node.getName()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("node").isEmpty());

        // invalid name
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("buildingId", building.getId())
                .param("name","NotExist"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("node").isEmpty());
    }

    @Test
    public void nodeGetByOrigin() throws Exception{
        // ok
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("originId", node.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nodes[0].id").value(node2.getId()));

        // invalid originId
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("originId", "NotExist"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nodes").isEmpty());
    }

    @Test
    public void nodeGetByDataSet() throws Exception{
        // ok
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("dataSetId", dataSetNode.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nodes[0].id").value(node.getId()));

        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("dataSetId", dataSetNode.getId())
                .param("name", node.getName()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nodes[0].id").value(node.getId()));

        // invalid dataSetId
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("dataSetId", "NotExist"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nodes").isEmpty());

        // invalid name
        mvc.perform(MockMvcRequestBuilders.get("/nodes")
                .param("dataSetId", dataSetNode.getId())
                .param("name", "NotExist"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nodes").isEmpty());
    }

    @Test
    public void nodeDelete() throws Exception{
        // invalid authorId
        assertTrue(nodeRepository.findById(node.getId()).isPresent());
        mvc.perform(MockMvcRequestBuilders.delete("/node")
                .param("authorId", "NotExist")
                .param("nodeId", node.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(404));
        // ok
        assertTrue(nodeRepository.findById(node.getId()).isPresent());
        mvc.perform(MockMvcRequestBuilders.delete("/node")
                .param("authorId", author.getId())
                .param("nodeId", "NotExist"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(404));

        // ok
        assertTrue(nodeRepository.findById(node.getId()).isPresent());
        mvc.perform(MockMvcRequestBuilders.delete("/node")
                .param("authorId", author.getId())
                .param("nodeId", node.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));

        assertFalse(nodeRepository.findById(node.getId()).isPresent());
    }


    @Test
    public void nodeGetByTwoNodes() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/nodes/twonodes/v2")
                .param("nId1", node.getId())
                .param("nId2", node2.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }
}