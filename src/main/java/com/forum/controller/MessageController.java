package com.forum.controller;

import com.forum.entity.Message;
import com.forum.entity.Page;
import com.forum.entity.User;
import com.forum.service.MessageService;
import com.forum.service.UserService;
import com.forum.util.ForumUtil;
import com.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    /**
     * 私信列表
     * @param model
     * @param page
     * @return
     */
    @GetMapping(value = "/letter/list")
    public String getLetterList(Model model, Page page){

        User user = hostHolder.getUser();

        /**
         * 分页信息
         */
        page.setLimit(5);

        page.setPath("/letter/list");

        page.setRows(messageService.findConversationCount(user.getId()));

        /**
         * 会话列表
         */
        List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());

        List<Map<String, Object>> conversations = new ArrayList<>();

        if(conversationList != null){
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();

                map.put("conversation", message);

                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));

                map.put("unreadCount", messageService.findLetterUnreadCount(user.getId(), message.getConversationId()));

                Integer targetId = user.getId().equals(message.getFromId()) ? message.getToId() : message.getFromId();

                map.put("target", userService.findUserByUserId(targetId));

                conversations.add(map);
            }
        }

        model.addAttribute("conversations", conversations);

        /**
         * 查询未读消息数量
         */
        Integer letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);

        model.addAttribute("letterUnreadCount", letterUnreadCount);

        return "site/letter";

    }

    @GetMapping(value = "/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable(value = "conversationId")String conversationId, Page page, Model model){

        page.setLimit(5);

        page.setPath("/letter/detail/"+conversationId);

        page.setRows(messageService.findLetterCount(conversationId));

        /**
         * 私信列表
         */
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());

        List<Map<String, Object>> letters = new ArrayList<>();

        if(letterList != null){
            for (Message message : letterList) {

                Map<String, Object> map = new HashMap<>();

                map.put("letter", message);

                map.put("fromUser", userService.findUserByUserId(message.getFromId()));

                letters.add(map);

            }

        }

        model.addAttribute("letters", letters);

        /**
         * 私信目标
         */
        model.addAttribute("target", getLetterTarget(conversationId));

        /**
         * 设置未读消息为已读
         */
        List<Integer> ids = getLetterIds(letterList);

        if(!ids.isEmpty()){
            messageService.readMessage(ids);
        }

        return "site/letter-detail";

    }

    private User getLetterTarget(String conversationId){

        String[] ids = conversationId.split("_");

        Integer id_0 =  Integer.parseInt(ids[0]);

        Integer id_1 =  Integer.parseInt(ids[1]);

        if(hostHolder.getUser().getId().equals(id_0)){

            return userService.findUserByUserId(id_1);

        }else{

            return userService.findUserByUserId(id_0);

        }

    }

    /**
     * 读取未读消息的id
     * @param letterList
     * @return
     */
    private List<Integer> getLetterIds(List<Message> letterList){
        List<Integer> ids = new ArrayList<>();

        if(letterList != null){

            for (Message message : letterList) {

                if ((hostHolder.getUser().getId().equals(message.getToId())) && message.getStatus() == 0){

                    ids.add(message.getId());

                }

            }

        }

        return ids;
    }

    @PostMapping(value = "/letter/send")
    @ResponseBody
    public String sendLetter(String toName, String content){

        User target = userService.findUserByName(toName);

        if(target == null){

            return ForumUtil.getJSONString(1, "目标用户不存在");

        }

        Message message = new Message();

        message.setFromId(hostHolder.getUser().getId());

        message.setToId(target.getId());

        if(message.getFromId() < message.getToId()){

            message.setConversationId(message.getFromId() + "_" + message.getToId());

        }else {

            message.setConversationId(message.getToId() + "_" + message.getFromId());

        }

        message.setContent(content);

        message.setCreateTime(new Date());

        messageService.addMessage(message);

        return ForumUtil.getJSONString(0);

    }

}
