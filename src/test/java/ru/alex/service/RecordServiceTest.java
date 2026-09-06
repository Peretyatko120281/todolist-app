package ru.alex.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alex.entity.Record;
import ru.alex.entity.RecordStatus;
import ru.alex.entity.User;
import ru.alex.entity.dto.RecordsConteinerDto;
import ru.alex.entity.dto.UserRole;
import ru.alex.repository.RecordRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RecordService recordService;

    private User testUser;
    private List<Record> testRecords;

    @BeforeEach
    void setUp() {
        testUser = new User("Test User", "test@example.com", "password", UserRole.USER);
        testUser.setId(1);

        Record record1 = new Record("Task 1", RecordStatus.ACTIVE, testUser);
        record1.setId(1);
        Record record2 = new Record("Task 2", RecordStatus.DONE, testUser);
        record2.setId(2);
        Record record3 = new Record("Task 3", RecordStatus.ACTIVE, testUser);
        record3.setId(3);

        testRecords = Arrays.asList(record1, record2, record3);
    }

    @Test
    void findAllService_ShouldReturnAllRecords_WhenNoFilter() {
        // given
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(recordRepository.findAllByUser(testUser)).thenReturn(testRecords);

        // when
        RecordsConteinerDto result = recordService.findAllService(null);

        // then
        assertThat(result.getRecords()).hasSize(3);
        assertThat(result.getNumberOfActiveRecords()).isEqualTo(2);
        assertThat(result.getNumberOfDoneRecords()).isEqualTo(1);
        assertThat(result.getUserName()).isEqualTo("Test User");
    }

    @Test
    void findAllService_ShouldReturnActiveRecords_WhenFilterActive() {
        // given
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(recordRepository.findActiveByUser(testUser))
                .thenReturn(testRecords.stream()
                        .filter(r -> r.getStatus() == RecordStatus.ACTIVE)
                        .toList());

        // when
        RecordsConteinerDto result = recordService.findAllService("active");

        // then
        assertThat(result.getRecords()).hasSize(2);
        assertThat(result.getRecords()).allMatch(r -> r.getStatus() == RecordStatus.ACTIVE);
    }

    @Test
    void findAllService_ShouldReturnDoneRecords_WhenFilterDone() {
        // given
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(recordRepository.findDoneByUser(testUser))
                .thenReturn(testRecords.stream()
                        .filter(r -> r.getStatus() == RecordStatus.DONE)
                        .toList());

        // when
        RecordsConteinerDto result = recordService.findAllService("done");

        // then
        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getRecords()).allMatch(r -> r.getStatus() == RecordStatus.DONE);
    }

    @Test
    void saveRecord_ShouldCreateNewRecord() {
        // given
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(recordRepository.save(any(Record.class))).thenAnswer(invocation -> {
            Record saved = invocation.getArgument(0);
            saved.setId(4);
            return saved;
        });

        // when
        recordService.saveRecord("New Task");

        // then
        verify(recordRepository).save(any(Record.class));
    }

    @Test
    void updateRecordStatus_ShouldUpdateRecordStatus() {
        // given
        Record record = testRecords.get(0);
        when(recordRepository.findById(1)).thenReturn(java.util.Optional.of(record));

        // when
        recordService.updateRecordStatus(1, RecordStatus.DONE);

        // then
        assertThat(record.getStatus()).isEqualTo(RecordStatus.DONE);
        verify(recordRepository).save(record);
    }

    @Test
    void deleteRecord_ShouldDeleteRecord() {
        // when
        recordService.deleteRecord(1);

        // then
        verify(recordRepository).deleteById(1);
    }
}
