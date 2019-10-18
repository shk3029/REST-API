package me.js.rest.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.js.rest.common.BaseControllerTest;
import me.js.rest.common.RestDocsConfiguration;
import me.js.rest.common.TestDescription;
import me.js.rest.events.dto.EventDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTests extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Sping")
                .description("REST TEST SPRING")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23 ,12, 10))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24 ,12, 10))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25 ,12, 10))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26 ,12, 10))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2")
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8) // 요청의 본문에 JSON을 담아 보냄
                .accept(MediaTypes.HAL_JSON) // HAL_JSON으로 응답을 받고 싶다
                .content(objectMapper.writeValueAsString(event))
        )  // perform 안에 요청을 적음
                .andDo(print()) // 응답 상태를 볼 수 있음
                .andExpect(status().isCreated()) // status().is(201) 원하는 것을 적어도 됨 (응답 기대값)
                .andExpect(jsonPath("id").exists()) // id가 존재하는지 테스트
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("free").value((false)))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                            links(
                                    linkWithRel("self").description("link to self"),
                                    linkWithRel("query-events").description("link to qeury-events"),
                                    linkWithRel("update-event").description("link to update-events")
                            ),
                            requestHeaders(
                                    headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                            ),
                            requestFields(
                                    fieldWithPath("name").description("name of new event"),
                                    fieldWithPath("description").description("description of new event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime"),
                                    fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime"),
                                    fieldWithPath("beginEventDateTime").description("beginEventDateTime"),
                                    fieldWithPath("endEventDateTime").description("endEventDatetime"),
                                    fieldWithPath("limitOfEnrollment").description("limitOfEnrollment"),
                                    fieldWithPath("location").description("location"),
                                    fieldWithPath("basePrice").description("basePrice"),
                                    fieldWithPath("maxPrice").description("maxPrice")
                            ),
                            responseHeaders(
                                    headerWithName(HttpHeaders.LOCATION).description("location"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content_type")
                            ),
                            responseFields(
                                    fieldWithPath("id").description("id of new event"),
                                    fieldWithPath("name").description("name of new event"),
                                    fieldWithPath("description").description("description of new event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime"),
                                    fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime"),
                                    fieldWithPath("beginEventDateTime").description("beginEventDateTime"),
                                    fieldWithPath("endEventDateTime").description("endEventDatetime"),
                                    fieldWithPath("limitOfEnrollment").description("limitOfEnrollment"),
                                    fieldWithPath("location").description("location"),
                                    fieldWithPath("basePrice").description("basePrice"),
                                    fieldWithPath("maxPrice").description("maxPrice"),
                                    fieldWithPath("free").description("free"),
                                    fieldWithPath("offline").description("offline"),
                                    fieldWithPath("eventStatus").description("eventStatus"),
                                    fieldWithPath("_links.self.href").description("_links.self.href"),
                                    fieldWithPath("_links.query-events.href").description("_links.query-events.href"),
                                    fieldWithPath("_links.update-event.href").description("_links.update-event.href")
                            )
                        ))
        ;
    }


    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우 에러가 발생하는 테스트")
    public void createEvent_bad_request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Sping")
                .description("REST TEST SPRING")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23 ,12, 10))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24 ,12, 10))
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 25 ,12, 10))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26 ,12, 10))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();
        //Mockito.when(eventRepository.save(event)).thenReturn(event);


        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8) // 요청의 본문에 JSON을 담아 보냄
                .accept(MediaTypes.HAL_JSON) // HAL_JSON으로 응답을 받고 싶다
                .content(objectMapper.writeValueAsString(event))
        )  // perform 안에 요청을 적음
                .andDo(print()) // 응답 상태를 볼 수 있음
                .andExpect(status().isBadRequest());
    }

/*    @Test
    @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void create_Event_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();
        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }*/

    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void create_Event_Bad_Request_Empty_Input_잘못된파라미터() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Sping22")
                .description("REST TEST SPRING")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 11, 23 ,12, 10))
                .closeEnrollmentDateTime(LocalDateTime.of(2017, 11, 24 ,12, 10))
                .beginEventDateTime(LocalDateTime.of(2019, 11, 25 ,12, 10))
                .endEventDateTime(LocalDateTime.of(2017, 11, 26 ,12, 10))
                .basePrice(1000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2")
                .build();
        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @TestDescription("30개 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvent() throws Exception {
        // Given
        IntStream.range(0,30).forEach(this::generateEvent);

        // When & then
        this.mockMvc.perform(get("/api/events")
                    .param("page", "1")
                    .param("size", "10")
                    .param("sort", "name,DESC"))
                .andDo(print())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    @TestDescription("기존의 이벤트 하나 조회하기")
    public void getEvent() throws Exception {
        // given
        Event event = this.generateEvent(100);
        // when & then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
        ;
    }

    @Test
    @TestDescription("없는 이벤트를 조회했을 때, 404 응답받기")
    public void getEvent_404() throws Exception {
        // when & then
        this.mockMvc.perform(get("/api/events/11883"))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @TestDescription("이벤트 수정하는 테스트")
    public void updateEvent_200() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        String eventNm  = "Updated Event";
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setName(eventNm);


        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(this.objectMapper.writeValueAsString(eventDto))
                    )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventNm))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"))
        ;
    }

    @Test
    @TestDescription("입력값이 비어있는 경우에 이벤트 수정 실패하는 테스트")
    public void updateEvent400_empty() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = new EventDto();


        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(this.objectMapper.writeValueAsString(eventDto))
                    )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 이벤트 수정 실패하는 테스트")
    public void updateEvent400_wrong() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(200000);
        eventDto.setMaxPrice(1000);


        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("존재하지않는 이벤트 수정 실패하는 테스트")
    public void updateEvent404() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        // When & Then
        this.mockMvc.perform(put("/api/events/123123")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    private Event generateEvent(int i) {
        Event event = Event.builder()
                .name("event" + i)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2015, 11, 23 ,12, 10))
                .closeEnrollmentDateTime(LocalDateTime.of(2017, 11, 24 ,12, 10))
                .beginEventDateTime(LocalDateTime.of(2015, 11, 25 ,12, 10))
                .endEventDateTime(LocalDateTime.of(2017, 11, 26 ,12, 10))
                .basePrice(1000)
                .maxPrice(20000)
                .limitOfEnrollment(100)
                .location("강남역 D2")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();
        return this.eventRepository.save(event);
    }
}











