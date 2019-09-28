package me.js.rest.events;

import me.js.rest.events.dto.EventDto;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {
    /*@Autowired
     EventRepository eventRepository;*/
    // 생성자 DI or @Autowired
    private final EventRepository eventRepository;
    private final ModelMapper modelmapper;
    private final EventValidator eventValidator;

    // 스프링 생성자가 1개만 있고, 생성자로 사용하는 파라미터가 이미 Bean으로 등록이 되어있다면, @Autowired 생략가능 (스프링 4.3 이상)
    // @Autowired
    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelmapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        eventValidator.validate(eventDto, errors);
        // Errors는 java bean 스펙을 준수하는 객체가 아니다
        // 따라서 json 변환을 할 수 없음
        // -> ErrorsSerializer를 이용해서 JSON 변환
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        // java bean 스펙을 준수하는 객체
        Event event = modelmapper.map(eventDto, Event.class);
        Event newEvent = this.eventRepository.save(event);
        URI createdURI = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdURI).body(event);
    }
}
