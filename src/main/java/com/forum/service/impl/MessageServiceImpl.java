package com.forum.service.impl;

import com.forum.entity.Message;
import com.forum.mapper.MessageMapper;
import com.forum.service.MessageService;
import com.forum.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service(value = "messageServiceImpl")
public class MessageServiceImpl implements MessageService {

    private MessageMapper messageMapper;

    private SensitiveFilter sensitiveFilter;

    @Autowired
    public void setMessageMapper(MessageMapper messageMapper){
        this.messageMapper = messageMapper;
    }

    @Autowired
    public void setSensitiveFilter(SensitiveFilter sensitiveFilter){
        this.sensitiveFilter = sensitiveFilter;
    }

    @Override
    public List<Message> findConversations(Integer userId, Integer offset, Integer limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    @Override
    public Integer findConversationCount(Integer userId) {
        return messageMapper.selectConversationCount(userId);
    }

    @Override
    public List<Message> findLetters(String conversationId, Integer offset, Integer limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    @Override
    public Integer findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    @Override
    public Integer findLetterUnreadCount(Integer userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    @Override
    public Integer addMessage(Message message) {

        message.setContent(HtmlUtils.htmlEscape(message.getContent()));

        message.setContent(sensitiveFilter.filter(message.getContent()));

        return messageMapper.insertMessage(message);
    }

    @Override
    public Integer readMessage(List<Integer> ids) {
        return messageMapper.updateStatus(ids, 1);
    }
}
