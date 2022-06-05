package zjt.learn.flowable.callback.basicloandelay;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.form.api.FormRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能：
 *
 * @Author: zhaojiatao
 * @Date: 2022/6/5 23:02
 * @ClassName: BasicLoanDelayService
 */
@Component
@Slf4j
public class BasicLoanDelayService {
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
     * 定义任务执行器
     * 当审批任务提交的表单比较复杂时，使用json来传递
     * 表达式：${basicLoanDelayService.approveSuccess(param,formJson)}
     * @param param
     * @param formJson
     */
    public void approveSuccess(String param, String formJson){
        log.info("审批通过");
        log.info("param={},amount={}",param,formJson);
    }

    /**
     * 定义任务执行器
     * 表达式：${basicLoanDelayService.approveFail(param,remark)}
     * @param param
     * @param remark
     */
    public void approveFail(String param, String remark){
        log.info("审批拒绝");
        log.info("param={},remark={}",param,remark);
    }


}
