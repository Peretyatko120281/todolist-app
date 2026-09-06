package ru.alex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.alex.entity.Record;
import ru.alex.entity.RecordStatus;
import ru.alex.entity.User;

import java.util.List;

@Repository

public interface RecordRepository extends JpaRepository<Record,Integer> {
    @Modifying
    @Query("UPDATE Record SET status = :status WHERE id= :id")
    void update(int id, @Param("status") RecordStatus newStatus);


    List<Record> findAllByUser(User testUser);

    List<Record> findActiveByUser(User testUser);

    List<Record> findDoneByUser(User testUser);
    long countByUserAndStatus(User user, RecordStatus status);
}
