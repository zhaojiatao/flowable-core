package zjt.learn.flowable.dto.basicloandelay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 功能：
 * 审批表单对象
 * @Author: zhaojiatao
 * @Date: 2022/6/5 23:18
 * @ClassName: BasicLoanDelayApproveFormDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasicLoanDelayApproveFormDTO implements Serializable {

    private static final long serialVersionUID = -1853689790343133579L;

    private BigDecimal amount;

    private String delayEndAt;

}
