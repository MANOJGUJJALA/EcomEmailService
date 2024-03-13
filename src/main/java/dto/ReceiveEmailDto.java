package dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Setter
@Getter
public class ReceiveEmailDto {
    private String From;
    private String To;
    private String Subject;
    private String Body;
}
