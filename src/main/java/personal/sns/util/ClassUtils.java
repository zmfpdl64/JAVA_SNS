package personal.sns.util;

import java.util.Optional;

public class ClassUtils {
    public static <T> Optional<T> getSafeCastInstance(Object o, Class<T> clazz){
        // class 타입이 없지 않고 And clazz가 o객체의 인스턴스인가??
        // 참이면 clazz객체 반환 거짓이면 empty() 반환
        return clazz != null && clazz.isInstance(o) ? Optional.of(clazz.cast(o)) : Optional.empty();
    }
}
