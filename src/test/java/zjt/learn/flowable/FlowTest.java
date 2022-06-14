package zjt.learn.flowable;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.FormType;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.task.api.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zjt.learn.flowable.dto.basicloandelay.BasicLoanDelayApproveFormDTO;
import zjt.learn.flowable.enums.ApproveTypeEnum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能：
 *
 * @Author: zhaojiatao
 * @Date: 2022/6/3 23:19
 * @ClassName: FlowTest
 */
@Slf4j
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
    public static final String PROCESS_INSTANCE_KEY= ApproveTypeEnum.BASIC_LOAN_CREDIT.name();


    /**
     * 启动流程实例
     */
    @Test
    public void start(){
        // 在启动流程实例的时候创建了流程变量
        Map<String,Object> variables = new HashMap<>();
        //1、设置业务参数
        variables.put("param","100001");
        variables.put("code","100001");
        //2、TODO 此处可以参考B2B通过流程类型查询数据库配置
        variables.put("step20Name","财务审批");//审批任务名称
        variables.put("step20","财务主管");//审批任务分配给谁
        //3、若有其他变量需要则可以设置
        //...

        //调用流程引擎启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_INSTANCE_KEY, variables);
        //TODO 返回结果可以进一步封装
        String processInstanceId = processInstance.getId();
        String processDefinitionId = processInstance.getProcessDefinitionId();
        String processDefinitionKey = processInstance.getProcessDefinitionKey();
        String businessKey = processInstance.getBusinessKey();
        String tenantId = processInstance.getTenantId();
        String description = processInstance.getDescription();
        boolean suspended = processInstance.isSuspended();
        boolean ended = processInstance.isEnded();
        String activityId = processInstance.getActivityId();
        Date startTime = processInstance.getStartTime();
        String startUserId = processInstance.getStartUserId();


    }

    /**
     * 查询我的任务
     * TODO 可改为使用ES查询
     */
    @Test
    public void queryTasks(){
        //此处查询任务id
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(PROCESS_INSTANCE_KEY)
                .taskAssignee("财务主管")
                .orderByTaskCreateTime().desc()
                .list().get(0);
        log.info("taskId={}",task.getId());

        //获取该审批节点的表单，可以返回前端
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        for (FormProperty formProperty : formProperties) {
            String id = formProperty.getId();
            String name = formProperty.getName();
            FormType type = formProperty.getType();
            System.out.println("id = " + id);
            System.out.println("name = " + name);
            System.out.println("type.getClass() = " + type.getClass());
        }

    }

    /**
     * 停止流程实例
     */
    @Test
    public void deleteInstance(){
        //此处查询任务id
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(PROCESS_INSTANCE_KEY)
                .taskAssignee("财务主管")
                .orderByTaskCreateTime().desc()
                .list().get(0);
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.deleteProcessInstance(processInstanceId,"手动停止");
    }




    /**
     * 完成任务，同时指定流程变量
     */
    @Test
    public void completeTask(){
        //此处查询任务id
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(PROCESS_INSTANCE_KEY)
                .taskAssignee("财务主管")
                .orderByTaskCreateTime().desc()
                .list().get(0);
        // 获取当前流程实例的所有的变量
        Map<String, Object> processVariables = task.getProcessVariables();
        processVariables.put("remark","dddaaaaccccdddddeeee2");
        processVariables.put("result","true");
        //复杂表单可以封装到json中
        BasicLoanDelayApproveFormDTO formDTO = BasicLoanDelayApproveFormDTO.builder()
                .amount(BigDecimal.valueOf(20000.11))
                .delayEndAt("2022-07-30")
                .build();
        processVariables.put("formJson", JSON.toJSONString(formDTO));
        //完成任务
        taskService.complete(task.getId(),processVariables);
    }




}
