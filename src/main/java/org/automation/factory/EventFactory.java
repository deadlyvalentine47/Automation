package org.automation.factory;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.automation.model.EventType;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
@Slf4j
public class EventFactory {

  private static final Map<EventType, Event> EVENT_FACTORY = new HashMap<>();

  public static void registerEvent(final EventType eventType, final Event event) {
    EVENT_FACTORY.put(eventType, event);
  }

  public Event getEvent(final EventType eventType) {
    return EVENT_FACTORY.get(eventType);
  }
}