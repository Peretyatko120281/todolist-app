package ru.alex.service;

import org.springframework.stereotype.Service;
import ru.alex.entity.User;
import ru.alex.repository.RecordRepository;
import ru.alex.entity.Record;
import ru.alex.entity.RecordStatus;
import ru.alex.entity.dto.RecordsConteinerDto;

import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class RecordService {
    private final UserService userService;
    private final RecordRepository recordRepository;

    public RecordService(UserService userService, RecordRepository recordRepository) {
        this.userService = userService;
        this.recordRepository = recordRepository;
    }

    public RecordsConteinerDto findAllService(String filterMode) {
        User user = userService.getCurrentUser();
       List <Record> records = user.getRecords().stream()
               .sorted(Comparator.comparingInt(Record::getId))
               .collect(Collectors.toList());
        int numberOfDoneRecords = (int)records.stream().filter(record->record.getStatus()== RecordStatus.DONE).count();
        int numberOfActiveRecords = (int)records.stream().filter(record->record.getStatus()== RecordStatus.ACTIVE).count();
        if(filterMode == null || filterMode.isBlank()) {
            return new RecordsConteinerDto(user.getName(), records,numberOfActiveRecords,numberOfDoneRecords);
        }

        String filterModeInUpperCase = filterMode.toUpperCase();
        List<String> allowedFilterModes = Arrays.stream(RecordStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        if(allowedFilterModes.contains(filterModeInUpperCase)){
            List<Record> filterRecords = records.stream()
                    .filter(record -> record.getStatus()==RecordStatus.valueOf(filterModeInUpperCase))
                    .collect(Collectors.toList());

            return new RecordsConteinerDto(user.getName(), filterRecords,numberOfActiveRecords,numberOfDoneRecords);

        }else {
            return new RecordsConteinerDto(user.getName(), records,numberOfActiveRecords,numberOfDoneRecords);
        }
    }
    public void saveRecord(String title) {
        if (title != null && !title.isBlank()) {
            User user = userService.getCurrentUser();
            recordRepository.save(new Record(title,user));
        }
    }
    public void updateRecordStatus(int id,RecordStatus newStatus){
        recordRepository.update(id,newStatus);
    }
    public void deleteRecord(int id){
            recordRepository.deleteById(id);
}
}
