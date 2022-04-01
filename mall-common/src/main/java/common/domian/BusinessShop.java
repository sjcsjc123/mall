package common.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BusinessShop implements Serializable {
    private String businessName;
    private String shopName;
    private Long shopId;
}
