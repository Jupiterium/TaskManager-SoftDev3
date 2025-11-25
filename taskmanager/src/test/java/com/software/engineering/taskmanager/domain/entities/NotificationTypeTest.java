package com.software.engineering.taskmanager.domain.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTypeTest {

    @Test
    void testNotificationTypeValues() {
        NotificationType[] types = NotificationType.values();
        
        assertEquals(5, types.length);
        assertTrue(containsType(types, NotificationType.TASK_DUE_SOON));
        assertTrue(containsType(types, NotificationType.TASK_OVERDUE));
        assertTrue(containsType(types, NotificationType.TASK_ASSIGNED));
        assertTrue(containsType(types, NotificationType.TASK_COMPLETED));
        assertTrue(containsType(types, NotificationType.CUSTOM_REMINDER));
    }

    @Test
    void testNotificationTypeValueOf() {
        assertEquals(NotificationType.TASK_DUE_SOON, NotificationType.valueOf("TASK_DUE_SOON"));
        assertEquals(NotificationType.TASK_OVERDUE, NotificationType.valueOf("TASK_OVERDUE"));
        assertEquals(NotificationType.TASK_ASSIGNED, NotificationType.valueOf("TASK_ASSIGNED"));
        assertEquals(NotificationType.TASK_COMPLETED, NotificationType.valueOf("TASK_COMPLETED"));
        assertEquals(NotificationType.CUSTOM_REMINDER, NotificationType.valueOf("CUSTOM_REMINDER"));
    }

    private boolean containsType(NotificationType[] types, NotificationType target) {
        for (NotificationType type : types) {
            if (type == target) {
                return true;
            }
        }
        return false;
    }
}