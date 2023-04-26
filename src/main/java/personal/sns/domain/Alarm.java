package personal.sns.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import personal.sns.domain.entity.AlarmEntity;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Alarm {
    private Integer id;
    private Member member;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private Timestamp created_at;
    private Timestamp modified_at;
    private Timestamp deleted_at;

    public String getAlarmText(){
        if(this.alarmType == AlarmType.NEW_COMMENT_ON_POST){
            return String.format("%s님이 댓글을 달았습니다.", member.getName());
        }
        return String.format("%s님이 좋아요를 눌렀습니다.", member.getName());
    }
    public static Alarm fromEntity(AlarmEntity entity){
        return new Alarm(
                entity.getId(),
                Member.fromEntity(entity.getMember()),
                entity.getAlarmType(),
                entity.getAlarmArgs(),
                entity.getCreated_at(),
                entity.getModified_at(),
                entity.getDeleted_at()
        );
    }
}
