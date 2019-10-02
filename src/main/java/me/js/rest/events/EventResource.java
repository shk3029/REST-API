package me.js.rest.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

// BeanSerializer로 객체를 Json으로 변환
// @JsonUnwrapped를 안써도 자동으로 json 객체를 풀어줌
// 빈이 아니고 매번 컨버팅해서 써야하는 객체임
public class EventResource extends Resource<Event> {
    public EventResource(Event event, Link... links) {
        super(event, links);
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
       // add(new Link("http://localhost:8080/api/events/" + event.getId())); 위랑 같은거
    }
}

/*
public class EventResource extends ResourceSupport {
    @JsonUnwrapped // Json에서 Event로 묶여있는것을 풀어줌
    private  Event event;

    public EventResource(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
*/
