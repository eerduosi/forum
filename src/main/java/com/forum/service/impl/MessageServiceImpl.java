package com.forum.service.impl;

import com.forum.entity.Message;
import com.forum.mapper.MessageMapper;
import com.forum.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "messageServiceImpl")
public class MessageServiceImpl implements MessageService {

    private MessageMapper messageMapper;

    @Autowired
    public void setMessageMapper(MessageMapper messageMapper){
        this.messageMapper = messageMapper;
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
}
