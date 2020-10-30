package se.supernovait.edusoft.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import se.supernovait.edusoft.entities.Course;

/**
 * @author Ahmed Shakir
 * @version 1.0
 * @since 2020-10-18
 */
@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
}
