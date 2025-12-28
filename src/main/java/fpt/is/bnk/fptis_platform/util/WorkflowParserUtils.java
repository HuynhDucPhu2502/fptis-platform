package fpt.is.bnk.fptis_platform.util;

import fpt.is.bnk.fptis_platform.entity.proccess.ProcessTask;
import fpt.is.bnk.fptis_platform.entity.proccess.ProcessVariable;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.bpm.model.dmn.instance.Input;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin 12/28/2025
 *
 **/
public class WorkflowParserUtils {

    public static List<ProcessTask> extractBpmnTasks(InputStream inputStream) {
        List<ProcessTask> tasks = new ArrayList<>();
        BpmnModelInstance modelInstance = Bpmn.readModelFromStream(inputStream);

        modelInstance.getModelElementsByType(UserTask.class).forEach(userTask -> {
            ProcessTask task = new ProcessTask();
            task.setTaskCode(userTask.getId());
            task.setTaskName(userTask.getName() != null ? userTask.getName() : userTask.getId());
            task.setCamundaActivityId(userTask.getId());
            tasks.add(task);
        });
        return tasks;
    }

    public static List<ProcessVariable> extractDmnVariables(InputStream inputStream) {
        List<ProcessVariable> variables = new ArrayList<>();
        DmnModelInstance modelInstance = Dmn.readModelFromStream(inputStream);

        modelInstance.getModelElementsByType(Input.class).forEach(input -> {
            ProcessVariable var = new ProcessVariable();
            var.setVariableName(input.getInputExpression().getTextContent());
            var.setDisplayName(input.getLabel() != null ? input.getLabel() : input.getInputExpression().getTextContent());
            var.setDataType(input.getInputExpression().getTypeRef());
            variables.add(var);
        });
        return variables;
    }

}
