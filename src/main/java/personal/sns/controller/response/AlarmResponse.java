package personal.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.sns.domain.Alarm;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AlarmResponse {
    private Integer id;
    private String text;
    private Timestamp created_at;
    private Timestamp modified_at;
    private Timestamp deleted_at;

    public static AlarmResponse fromAlarm(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmText(),
                alarm.getCreated_at(),
                alarm.getModified_at(),
                alarm.getDeleted_at()
        );
    }
}
