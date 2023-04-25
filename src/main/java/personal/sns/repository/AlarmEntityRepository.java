package personal.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.sns.domain.entity.AlarmEntity;

public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Integer> {

}
