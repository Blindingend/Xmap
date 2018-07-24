package xyz.veiasai.neo4j.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.veiasai.neo4j.domain.CountSum;
import xyz.veiasai.neo4j.result.BuildingAdminResult;
import xyz.veiasai.neo4j.result.BuildingResult;
import xyz.veiasai.neo4j.result.CountSumResult;
import xyz.veiasai.neo4j.result.Result;
import xyz.veiasai.neo4j.service.*;

import java.util.Collection;

@Api(value = "buildingAdmin-controller")
@RestController
@RequestMapping("/")
public class BuildingAdminController {
    @Autowired
    private AuthorService authorService;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private BuildingAdminService buildingAdminService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private PathService pathService;

    @PostMapping("/building/admin/login")
    public Result loginBuildingAdmin(@RequestParam String authorId) {
        Result result = new Result();
        if (buildingAdminService.isBuildingAdmin(authorId)) {
            result.setCode(200);
            result.setMessage("登录成功");
        } else {
            result.setCode(403);
            result.setMessage("不是建筑管理员");       //authorId是否存在不应该判断。存在可能泄密
        }
        return result;
    }

    @GetMapping("/building/admin/admin")       //to be continued
    public BuildingAdminResult getAdminByBuildingId(@RequestParam String buildingId) {
        BuildingAdminResult result = new BuildingAdminResult();
        if (buildingService.getBuildingById(buildingId) == null) {
            result.setMessage("建筑不存在");
            result.setCode(404);
        } else {
            result.setBuildingAdmins(buildingAdminService.findAdminByBuildingId(buildingId));
            result.setCode(200);
            result.setMessage("查询成功");      //进一步判断查询结果是否为空 to be continued
        }
        return result;
    }

    @DeleteMapping("building/admin/node")
    public Result deleteNodeByAdmin(@RequestParam String buildingId, @RequestParam String adminId, @RequestParam String nodeId) {
        Result result = new Result();
        if (buildingService.getBuildingById(buildingId) == null) {
            result.setCode(404);
            result.setMessage("建筑不存在");
        } else if (authorService.getAuthorById(adminId) == null) {
            result.setCode(404);
            result.setMessage("用户不存在");
        } else if (nodeService.findById(nodeId) == null) {
            result.setCode(404);
            result.setMessage("点位不存在");
        } else if (!buildingAdminService.existValidBuildingAdmin(buildingId, adminId)) {
            result.setCode(403);
            result.setMessage("该用户不是该建筑管理员");
        } else {
            result.setCode(200);
            result.setMessage("删除成功");
            nodeService.deleteNodeByAdmin(buildingId, nodeId);
        }
        return result;
    }

    @DeleteMapping("building/admin/path")
    public Result deletePathByAdmin(@RequestParam String buildingId, @RequestParam String adminId, @RequestParam String pathId) {
        Result result = new Result();
        if (buildingService.getBuildingById(buildingId) == null) {
            result.setCode(404);
            result.setMessage("建筑不存在");
        } else if (authorService.getAuthorById(adminId) == null) {
            result.setCode(404);
            result.setMessage("用户不存在");
        } else if (pathService.findById(pathId) == null) {
            result.setCode(404);
            result.setMessage("路线不存在");
        } else if (!buildingAdminService.existValidBuildingAdmin(buildingId, adminId)) {
            result.setCode(403);
            result.setMessage("该用户不是该建筑管理员");
        } else {
            result.setCode(200);
            result.setMessage("删除成功");
            pathService.deletePathByAdmin(buildingId, pathId);
        }
        return result;
    }

    @GetMapping("/building/admin/building")    //to be continued
    public BuildingResult getBuildingByAdminId(@RequestParam String adminId) {
        BuildingResult result = new BuildingResult();
        if (authorService.getAuthorById(adminId) == null) {
            result.setMessage("用户不存在");
            result.setCode(405);
        } else {
            result.setBuildings(buildingAdminService.findBuildingByAdmin(adminId));
            result.setCode(200);
            result.setMessage("查询成功");
        }
        return result;
    }

    @GetMapping("building/admin/buildingandcount")
    public CountSumResult getBuildingAndCountByAdminId(@RequestParam String adminId) {
        CountSumResult result = new CountSumResult();
        if (authorService.getAuthorById(adminId) == null) {
            result.setMessage("用户不存在");
            result.setCode(405);
        } else {
            result.setCountSums(buildingAdminService.findBuildingAndCountByAdmin(adminId));
            result.setCode(200);
            result.setMessage("查询成功");
        }
        return result;
    }
}
