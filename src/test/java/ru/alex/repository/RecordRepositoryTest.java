package ru.alex.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.alex.entity.Record;
import ru.alex.entity.RecordStatus;
import ru.alex.entity.User;
import ru.alex.entity.dto.UserRole;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RecordRepositoryTest {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("Test User", "test@example.com", "password", UserRole.USER);
        userRepository.save(testUser);
    }

    @Test
    void findAllByUser_ShouldReturnUserRecords() {
        // given
        Record record1 = new Record("Task 1", RecordStatus.ACTIVE, testUser);
        Record record2 = new Record("Task 2", RecordStatus.DONE, testUser);
        recordRepository.saveAll(List.of(record1, record2));

        // when
        List<Record> records = recordRepository.findAllByUser(testUser);

        // then
        assertThat(records).hasSize(2);
        assertThat(records).extracting(Record::getTitle)
                .containsExactlyInAnyOrder("Task 1", "Task 2");
    }

    @Test
    void findActiveByUser_ShouldReturnOnlyActiveRecords() {
        // given
        Record active = new Record("Active Task", RecordStatus.ACTIVE, testUser);
        Record done = new Record("Done Task", RecordStatus.DONE, testUser);
        recordRepository.saveAll(List.of(active, done));

        // when
        List<Record> activeRecords = recordRepository.findActiveByUser(testUser);

        // then
        assertThat(activeRecords).hasSize(1);
        assertThat(activeRecords.get(0).getTitle()).isEqualTo("Active Task");
        assertThat(activeRecords.get(0).getStatus()).isEqualTo(RecordStatus.ACTIVE);
    }

    @Test
    void findDoneByUser_ShouldReturnOnlyDoneRecords() {
        // given
        Record active = new Record("Active Task", RecordStatus.ACTIVE, testUser);
        Record done = new Record("Done Task", RecordStatus.DONE, testUser);
        recordRepository.saveAll(List.of(active, done));

        // when
        List<Record> doneRecords = recordRepository.findDoneByUser(testUser);

        // then
        assertThat(doneRecords).hasSize(1);
        assertThat(doneRecords.get(0).getTitle()).isEqualTo("Done Task");
        assertThat(doneRecords.get(0).getStatus()).isEqualTo(RecordStatus.DONE);
    }

    @Test
    void countByUserAndStatus_ShouldReturnCorrectCount() {
        // given
        Record active1 = new Record("Task 1", RecordStatus.ACTIVE, testUser);
        Record active2 = new Record("Task 2", RecordStatus.ACTIVE, testUser);
        Record done = new Record("Task 3", RecordStatus.DONE, testUser);
        recordRepository.saveAll(List.of(active1, active2, done));

        // when
        long activeCount = recordRepository.countByUserAndStatus(testUser, RecordStatus.ACTIVE);
        long doneCount = recordRepository.countByUserAndStatus(testUser, RecordStatus.DONE);

        // then
        assertThat(activeCount).isEqualTo(2);
        assertThat(doneCount).isEqualTo(1);
    }
}