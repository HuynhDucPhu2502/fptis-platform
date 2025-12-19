package fpt.is.bnk.fptis_platform.service.daily_log.datasource;

import fpt.is.bnk.fptis_platform.dto.report.daily_log.DailyLogReportObject;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.util.Iterator;

public class DailyLogDataSource implements JRDataSource {
    private final Iterator<DailyLogReportObject> iterator;
    private DailyLogReportObject currentValue;

    public DailyLogDataSource(Iterator<DailyLogReportObject> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean next() throws JRException {
        if (iterator != null && iterator.hasNext()) {
            currentValue = iterator.next();
            return true;
        }
        return false;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        if (currentValue == null) return null;

        return switch (jrField.getName()) {
            case "id" -> currentValue.getId();
            case "mainTask" -> currentValue.getMainTask();
            case "result" -> currentValue.getResult();
            case "workDate" -> currentValue.getWorkDate();
            default -> null;
        };
    }
}