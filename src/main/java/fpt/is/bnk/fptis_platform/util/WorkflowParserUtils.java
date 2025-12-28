package fpt.is.bnk.fptis_platform.util;

import fpt.is.bnk.fptis_platform.entity.proccess.ProcessTask;
import fpt.is.bnk.fptis_platform.entity.proccess.ProcessVariable;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.bpm.model.dmn.instance.Input;
import org.camunda.bpm.model.dmn.instance.InputExpression;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        // 1. Đọc Model DMN
        DmnModelInstance modelInstance = Dmn.readModelFromStream(inputStream);
        String camundaNs = "http://camunda.org/schema/1.0/dmn";

        // 2. Sử dụng Stream để biến đổi Input thành ProcessVariable
        return modelInstance.getModelElementsByType(Input.class).stream()
                .map(input -> mapToProcessVariable(input, camundaNs))
                .filter(Objects::nonNull) // Loại bỏ các biến không hợp lệ
                .collect(Collectors.toList());
    }

    private static ProcessVariable mapToProcessVariable(Input input, String camundaNs) {
        // Lấy tên biến (Ưu tiên inputVariable của Camunda, sau đó là nội dung thẻ text)
        String variableName = getVariableName(input, camundaNs);

        if (variableName == null || variableName.isBlank()) {
            return null; // Không có tên biến thì không bóc tách
        }

        ProcessVariable var = new ProcessVariable();
        var.setVariableName(variableName);

        // Thiết lập nhãn hiển thị: Ưu tiên Label, sau đó là Variable Name
        String label = input.getLabel();
        var.setDisplayName(StringUtils.hasText(label) ? label : variableName);

        // Thiết lập kiểu dữ liệu
        var.setDataType(extractDataType(input));

        return var;
    }

    private static String getVariableName(Input input, String ns) {
        // 1. Thử lấy từ camunda:inputVariable
        String camundaVar = input.getAttributeValueNs(ns, "inputVariable");
        if (StringUtils.hasText(camundaVar)) return camundaVar;

        // 2. Nếu không có, lấy từ InputExpression text
        return Optional.ofNullable(input.getInputExpression())
                .map(InputExpression::getTextContent)
                .filter(StringUtils::hasText)
                .orElse(null);
    }

    private static String extractDataType(Input input) {
        // Mặc định là string nếu không định nghĩa typeRef
        return Optional.ofNullable(input.getInputExpression())
                .map(InputExpression::getTypeRef)
                .orElse("string");
    }

}
