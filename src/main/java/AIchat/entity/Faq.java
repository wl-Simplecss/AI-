package AIchat.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "faq")
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer", nullable = false)
    private String answer;

    public String getAnswer() {
     return answer;
    }

    // getters and setters
    // ...
}
