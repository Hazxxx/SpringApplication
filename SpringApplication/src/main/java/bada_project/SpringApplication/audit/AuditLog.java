package bada_project.SpringApplication.audit;

import java.time.LocalDateTime;

public class AuditLog {
    private LocalDateTime timestamp;
    private String username;
    private String action;
    private String details;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public String getDetails() {
        return details;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}