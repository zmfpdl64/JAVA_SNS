package personal.sns.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmArgs {
    private Integer fromUserId; // 알람 발생시킨 유저Id
    private Integer targetId; // 해당 객체 작성자 유저Id
}
