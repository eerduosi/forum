package com.forum.controller;

import com.forum.entity.Message;
import com.forum.entity.Page;
import com.forum.entity.User;
import com.forum.service.MessageService;
import com.forum.service.UserService;
import com.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
