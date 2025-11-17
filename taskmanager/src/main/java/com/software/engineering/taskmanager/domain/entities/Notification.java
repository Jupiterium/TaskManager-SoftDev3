package com.software.engineering.taskmanager.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "type", nullable = false)
    private NotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    public Notification() { }

    public Notification(UUID id, String title, String message, NotificationType type, Task task, boolean isRead, LocalDateTime created) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.type = type;
        this.task = task;
        this.isRead = isRead;
        this.created = created;
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public NotificationType getType() { return type; }

    public void setType(NotificationType type) { this.type = type; }

    public Task getTask() { return task; }

    public void setTask(Task task) { this.task = task; }

    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }

    public LocalDateTime getCreated() { return created; }

    public void setCreated(LocalDateTime created) { this.created = created; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return isRead == that.isRead && Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(message, that.message) && type == that.type && Objects.equals(task, that.task) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, message, type, task, isRead, created);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", type=" + type +
                ", task=" + task +
                ", isRead=" + isRead +
                ", created=" + created +
                '}';
    }
}