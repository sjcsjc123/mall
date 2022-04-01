package common.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Business implements Serializable {
    private String businessName;
    private Long id;
    private String businessPassword;
    private String businessPhone;
    private int businessScore;
    private Date businessRegTime;
    private Date businessLastLogin;
    private int businessStatus;
    private int status;
}
