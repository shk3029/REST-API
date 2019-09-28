package me.js.rest.events;

import me.js.rest.events.dto.EventDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
public class EventValidator {
    public void validate(EventDto eventDto, Errors errors) {
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
            // field error
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is Wrong");
            errors.rejectValue("maxPrice", "wrongValue", "maxPrice is Wrong");
            // global error
            errors.reject("wrongPrices", "Values fo prices is wrong");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) || endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())
            || endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) || endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
        }
        // TODO beginEventDateTimte
        // TODO CloseEnrollmentDateTime
    }

}
