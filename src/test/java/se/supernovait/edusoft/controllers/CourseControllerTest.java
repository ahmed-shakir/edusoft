package se.supernovait.edusoft.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;
import se.supernovait.edusoft.entities.Course;
import se.supernovait.edusoft.repositories.CourseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureRestDocs(uriPort = 7000)
class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CourseRepository courseRepository;

    /*@BeforeEach
    void setUp() {
    }*/

    @Test
    void findAllCourses() throws Exception {
        given(courseRepository.findAll()).willReturn(List.of(Course.builder().build()));

        mockMvc.perform(get("/api/v1/courses").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("v1/courses-get-all"/*,
                        responseFields(
                                fieldWithPath("id").description("Course ID"),
                                fieldWithPath("name").description("Course name"),
                                fieldWithPath("description").description("Course description"),
                                fieldWithPath("designation").description("Course designation"),
                                fieldWithPath("created").description("Date created"),
                                fieldWithPath("starts").description("Date course starts"),
                                fieldWithPath("ends").description("Date course ends")
                        )*/));
    }

    @Test
    void findCourseById() throws Exception {
        given(courseRepository.findById(any())).willReturn(Optional.of(Course.builder().build()));

        mockMvc.perform(get("/api/v1/courses/{id}", UUID.randomUUID().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("v1/courses-get-one",
                        pathParameters(
                                parameterWithName("id").description("UUID string of desired course to get.")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Course ID"),
                                fieldWithPath("name").description("Course name"),
                                fieldWithPath("description").description("Course description"),
                                fieldWithPath("designation").description("Course designation"),
                                fieldWithPath("created").description("Date created").type(LocalDate.class),
                                fieldWithPath("starts").description("Date course starts").type(LocalDate.class),
                                fieldWithPath("ends").description("Date course ends").type(LocalDate.class)
                        )));
    }

    @Test
    void saveCourse() throws Exception {
        Course course = getValidCourse();
        String courseJson = objectMapper.writeValueAsString(course);
        
        given(courseRepository.save(any())).willReturn(Course.builder().build());

        ConstrainedFields fields = new ConstrainedFields(Course.class);
        
        mockMvc.perform(post("/api/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courseJson))
                .andExpect(status().isCreated())
                .andDo(document("v1/courses-new",
                        requestFields(
                                fields.withPath("id").ignored(),
                                fields.withPath("name").description("Course name"),
                                fields.withPath("description").description("Course description"),
                                fields.withPath("designation").description("Course designation"),
                                fields.withPath("created").description("Date created"),
                                fields.withPath("starts").description("Date course starts"),
                                fields.withPath("ends").description("Date course ends")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Course ID"),
                                fieldWithPath("name").description("Course name"),
                                fieldWithPath("description").description("Course description"),
                                fieldWithPath("designation").description("Course designation"),
                                fieldWithPath("created").description("Date created").type(LocalDate.class),
                                fieldWithPath("starts").description("Date course starts").type(LocalDate.class),
                                fieldWithPath("ends").description("Date course ends").type(LocalDate.class)
                        )));
    }

    @Test
    void updateCourse() throws Exception {
        Course course = getValidCourse();
        String courseJson = objectMapper.writeValueAsString(course);

        ConstrainedFields fields = new ConstrainedFields(Course.class);

        mockMvc.perform(put("/api/v1/courses/{id}", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(courseJson))
                .andExpect(status().isNoContent())
                .andDo(document("v1/courses-update",
                        pathParameters(
                                parameterWithName("id").description("UUID string of desired course to update.")
                        ),
                        requestFields(
                                fields.withPath("id").description("Course ID"),
                                fields.withPath("name").description("Course name"),
                                fields.withPath("description").description("Course description"),
                                fields.withPath("designation").description("Course designation"),
                                fields.withPath("created").description("Date created"),
                                fields.withPath("starts").description("Date course starts"),
                                fields.withPath("ends").description("Date course ends")
                        )));
    }

    @Test
    void deleteCourse() throws Exception {
        mockMvc.perform(delete("/api/v1/courses/{id}", UUID.randomUUID().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document("v1/courses-delete",
                        pathParameters(
                                parameterWithName("id").description("UUID string of desired course to delete.")
                        )));
    }

    Course getValidCourse() {
        return Course.builder()
                .name("Java 101")
                .description("Java for beginners")
                .designation("JAVA101")
                .created(LocalDate.now())
                .starts(LocalDate.now().plusMonths(2))
                .ends(LocalDate.now().plusYears(1).minusMonths(2))
                .build();
    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions.descriptionsForProperty(path), ". ")));
        }
    }
}