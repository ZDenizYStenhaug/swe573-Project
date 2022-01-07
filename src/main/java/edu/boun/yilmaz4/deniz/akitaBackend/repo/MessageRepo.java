package edu.boun.yilmaz4.deniz.akitaBackend.repo;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Message;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message,Long> {

    @Query("SELECT m FROM Message m WHERE m.receiver = :member")
    List<Message> getAllForMember(@Param("member") Member member);

    // check if any unread message exists
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver = :member AND m.status = 'UNREAD'")
    int checkIfUnreadExists(@Param("member") Member member);

    // get the read messages for a member
    @Query("SELECT m FROM Message m WHERE m.receiver = :member AND m.status = 'READ' ORDER BY m.date DESC")
    List<Message> getAllRead(@Param("member") Member member);

    // get the unread messages for a member
    @Query("SELECT m FROM Message m WHERE m.receiver = :member AND m.status = 'UNREAD' ORDER BY m.date DESC ")
    List<Message> getAllUnread(@Param("member") Member member);

    // mark selected message as read
    @Modifying
    @Query("UPDATE Message m SET m.status = :targetStatus WHERE m.id = :messageId")
    void markAsRead(@Param("messageId") Long id, @Param("targetStatus") MessageStatus targetStatus);

    // mark selected message as unread
    @Modifying
    @Query("UPDATE Message m SET m.status = :targetStatus WHERE m.id = :messageId")
    void markAsUnread(@Param("messageId") Long id, @Param("targetStatus") MessageStatus targetStatus);

    // delete selected message
    @Modifying
    @Query("UPDATE Message m SET m.status = :targetStatus WHERE m.id = :messageId")
    void delete(@Param("messageId") Long id, @Param("targetStatus") MessageStatus targetStatus);
}
