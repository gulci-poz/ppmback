package pl.gulci.ppmback.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Project implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Project name is required")
    private String projectName;

    @NotBlank(message = "Project identifier is required")
    @Size(min = 4, max = 5, message = "Please use 4 to 5 characters")
    @Column(updatable = false, unique = true)
    private String projectIdentifier;

    @NotBlank(message = "Project description is required")
    private String description;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
