package xyz.xmap.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.xmap.domain.Message;
import xyz.xmap.result.MessageResult;
import xyz.xmap.result.Result;
import xyz.xmap.service.AuthorService;
import xyz.xmap.service.BuildingAdminService;
import xyz.xmap.service.BuildingService;
import xyz.xmap.service.MessageService;

@Api(value = "message-controller")
@RestController
@RequestMapping("/")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private BuildingAdminService buildingAdminService;

    @ApiOperation(value = "发布信息", notes = "建筑管理员发布信息")
    @PostMapping("/message")
    public MessageResult postMessage(@RequestBody Message message, @RequestParam String buildingId, @RequestParam String authorId) {

        MessageResult result = new MessageResult();
        if (buildingService.getBuildingById(buildingId) == null) {
            result.setMessage("建筑不存在");
            result.setCode(404);
        } else if (authorService.getAuthorById(authorId) == null) {
            result.setMessage("用户不存在");
            result.setCode(404);
        } else if (!buildingAdminService.existValidBuildingAdmin(buildingId, authorId)) {
            result.setMessage("用户无权限发布信息");
            result.setCode(403);
        } else {
            message.setBuilding(buildingService.findById(buildingId));
            message.setAuthor(authorService.findById(authorId));
            message = messageService.addMessage(message);
            result.setCode(200);
            result.setMessage("发布信息成功");
            result.setSingleMessage(message);
        }
        return result;
    }

    @ApiOperation(value = "删除信息", notes = "建筑管理员删除信息")
    @DeleteMapping("/message")
    public Result deleteMessage(@RequestParam String authorId, @RequestParam String messageId) {
        Result result = new Result();
        if (authorService.getAuthorById(authorId) == null) {
            result.setMessage("用户不存在");
            result.setCode(405);
        } else if (messageService.getMessageById(messageId) == null) {
            result.setMessage("信息不存在");
            result.setCode(404);
        } else if (!messageService.existMessageAndAuthor(authorId, messageId)) {
            result.setMessage("该用户无权限删除此信息");
            result.setCode(403);
        } else {
            messageService.deleteMessage(authorId, messageId);
            result.setMessage("删除信息成功");
            result.setCode(200);
        }
        return result;
    }

    @GetMapping("/message")
    public MessageResult getMessage(@RequestParam(required = false) String buildingId,  //此处与dataset-controller一样都没做buildingId和authorId的检验
                                    @RequestParam(required = false) String authorId,
                                    @RequestParam(required = false, defaultValue = "") String title,
                                    @RequestParam(required = false, defaultValue = "0") Integer skip,
                                    @RequestParam(required = false, defaultValue = "5") Integer limit) {
        MessageResult result = new MessageResult();
        result.setCode(404);
        if (buildingId != null && authorId != null) {
            result.setMessages(messageService.findMessageByAuthorAndBuilding(buildingId, authorId, title, skip, limit));
            result.setMessage("查询成功");      //区别singleMessage方法
            result.setCode(200);
        } else if (buildingId != null) {
            result.setMessages(messageService.findMessageByBuildingAndTitle(buildingId,title, skip, limit));
            result.setMessage("查询成功");      //区别singleMessage方法
            result.setCode(200);
        } else if (authorId != null) {
            result.setMessages(messageService.findMessageByAuthorAndTitle(authorId,title,skip, limit));
            result.setMessage("查询成功");      //区别singleMessage方法
            result.setCode(200);
        }
        return result;
    }
}
