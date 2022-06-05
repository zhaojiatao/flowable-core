package zjt.learn.flowable;

import com.alibaba.fastjson.JSON;
import org.flowable.engine.*;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.task.api.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zjt.learn.flowable.dto.basicloandelay.BasicLoanDelayApproveFormDTO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：
 *
 * @Author: zhaojiatao
 * @Date: 2022/6/3 23:19
 * @ClassName: FlowTest
 */
public class FlowTest extends FlowableCoreApplicationTests{

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FormService formService;
    @Autowired
    private FormRepositoryService formRepositoryService;

    /**
     * 流程实例key
     */
    public static final String PROCESS_INSTANCE_KEY="BASIC_LOAN_CREDIT";


    /**
     * 启动流程实例
     */
    @Test
    public void start(){
        // 在启动流程实例的时候创建了流程变量
        Map<String,Object> variables = new HashMap<>();
        variables.put("step20Name","财务审批");
        variables.put("step20","财务主管");
        variables.put("param","100001");
        variables.put("code","100001");
        runtimeService.startProcessInstanceByKey(PROCESS_INSTANCE_KEY,variables);

    }

    /**
     * 完成任务，同时指定流程变量
     */
    @Test
    public void completeTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(PROCESS_INSTANCE_KEY)
                .taskAssignee("财务主管")
                .orderByTaskCreateTime().desc()
                .list().get(0);
        // 获取当前流程实例的所有的变量
        Map<String, Object> processVariables = task.getProcessVariables();
        processVariables.put("remark","dddaaaaccccdddddeeee2");
        processVariables.put("result","true");
        BasicLoanDelayApproveFormDTO formDTO = BasicLoanDelayApproveFormDTO.builder()
                .amount(BigDecimal.valueOf(20000.11))
                .delayEndAt("2022-07-30")
                .build();
        processVariables.put("formJson", JSON.toJSONString(formDTO));

        taskService.complete(task.getId(),processVariables);
    }




}
