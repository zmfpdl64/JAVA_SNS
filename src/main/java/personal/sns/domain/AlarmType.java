package personal.sns.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    NEW_COMMENT_ON_POST("NEW COMMENT"),
    NEW_LIKE_ON_POST("NEW LIKE");
    private final String description;
}
