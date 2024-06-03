package exercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.model.Task;
import exercise.repository.TaskRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class ApplicationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    private Task task;

    // BEGIN
    @BeforeEach
    public void setUp() {
        // Создаем объект Task с помощью Instancio и faker
        task = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
                .create();
    }
    // END

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    @Test
    public void testShow() throws Exception {
        // Сохраняем объект task в репозиторий
        taskRepository.save(task);

        // Формируем запрос на получение задачи по id
        var request = get("/tasks/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // Выполняем запрос и проверяем статус ответа
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        // Получаем тело ответа
        var body = result.getResponse().getContentAsString();

        // Проверяем, что тело ответа содержит правильные данные
        assertThat(body).contains("\"id\":" + task.getId());
        assertThat(body).contains("\"title\":\"" + task.getTitle() + "\"");
        assertThat(body).contains("\"description\":\"" + task.getDescription() + "\"");
    }

    @Test
    public void testCreate() throws Exception {
        // Созданному объекту присваивается порядковый номер (strategy = IDENTITY)
        task.setId(1L);

        // Формируем запрос на создание
        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(task));

        // Выполняем запрос и проверяем статус ответа
        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        // Проверяем, что тело ответа содержит правильные данные
        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("\"id\":" + task.getId());
        assertThat(body).contains("\"title\":\"" + task.getTitle() + "\"");
        assertThat(body).contains("\"description\":\"" + task.getDescription() + "\"");
    }

    @Test
    public void testUpdate() throws Exception {
        var updatedTask = taskRepository.save(task);

        var data = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.science().scientist())
                .supply(Select.field(Task::getDescription), () -> faker.science().unit())
                .create();

        // Формируем запрос на обновление задачи по id
        var request = put("/tasks/" + updatedTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(data));

        // Выполняем запрос и проверяем статус ответа
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        // Проверяем, что тело ответа содержит правильные данные
        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("\"title\":\"" + data.getTitle() + "\"");
        assertThat(body).contains("\"description\":\"" + data.getDescription() + "\"");

    }

    @Test
    public void testDelete() throws Exception {
        var deletedTask = taskRepository.save(task);

        // Формируем запрос на удаление задачи по id
        var request = delete("/tasks/" + deletedTask.getId());

        // Выполняем запрос и проверяем статус ответа
        var result = mockMvc.perform(request)
                .andExpect(status().isOk());

        // Проверяем, что задача удалена из репозитория
        assertThat(taskRepository.findById(deletedTask.getId())).isNotPresent();
    }
    // END
}
