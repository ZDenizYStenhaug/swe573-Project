package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Message;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.MessageStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;

    @Transactional (readOnly = true)
    public int checkForUnreadMessage(Member member) {
        return messageRepo.checkIfUnreadExists(member);
    }

    @Transactional (readOnly = true)
    public List<Message> getAllReadMessages(Member member) {
        return messageRepo.getAllRead(member);
    }

    @Transactional(readOnly = true)
    public List<Message> getAllUnreadMessages(Member member) {
        return messageRepo.getAllUnread(member);
    }

    @Transactional
    public void markAsRead(Long messageId) {
        messageRepo.markAsRead(messageId, MessageStatus.READ);
    }

    @Transactional
    public void markAsUnread(Long messageId) {
        messageRepo.markAsUnread(messageId, MessageStatus.UNREAD);
    }

    @Transactional
    public void delete(Long messageId) {
        messageRepo.delete(messageId, MessageStatus.DELETED);
    }
}
