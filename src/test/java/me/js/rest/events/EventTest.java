package me.js.rest.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {
    @Test
    public void builder() {
        Event event = Event.builder()
                .name("REST API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        String name = "event";
        String description = "Spring";

        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setId(1);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    // @Parameters convention (method 이름이 parametersFor(테스트메소드이름) 이면 method="" 생략가능)
    @Test
    @Parameters
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        // When
        event.update();
        // then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private Object[] parametersForTestFree() {
        return new Object[] {
          new Object[] {0,0,true},
          new Object[] {100,0,false},
          new Object[] {0,100,false},
          new Object[] {100,200,false}
        };
    }

    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline) {
        // Given
        Event event = Event.builder()
                .location(location)
                .build();
        // When
        event.update();
        // then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private Object[] parametersForTestOffline() {
        return new Object[] {
                new Object[] {"강남", true},
                new Object[] {null,false},
                new Object[] {"   ", false}
        };
    }

}