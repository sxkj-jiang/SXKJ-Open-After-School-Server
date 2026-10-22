package com.sxkj.edu.job;

import com.sxkj.edu.module.afterschool.support.StudentTermChangeApplySupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 学生学期内资料变更生效任务。
 */
@Component
@Slf4j
public class ApplyStudentTermChangeJob {

    @Resource
    private StudentTermChangeApplySupport studentTermChangeApplySupport;

    /**
     * 每分钟处理已经到达生效时间的学生资料变更。
     */
    @Scheduled(cron = "0 0/15 * * * ?")
    public void applyDueChanges() {
        Date now = new Date();
        List<Long> changeIds =
                studentTermChangeApplySupport.listDueChangeIds(now);
        if (changeIds.isEmpty()) {
            return;
        }
        for (Long changeId : changeIds) {
            try {
                studentTermChangeApplySupport.applyPendingChange(
                        changeId, new Date());
            } catch (Exception exception) {
                log.error("学生资料变更到期生效失败，changeId={}",
                        changeId, exception);
                studentTermChangeApplySupport.markApplyFailed(
                        changeId, resolveMessage(exception), new Date());
            }
        }
    }

    /**
     * 提取可供管理员排查的中文失败原因。
     */
    private String resolveMessage(Exception exception) {
        Throwable current = exception;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        String message = current.getMessage();
        return message == null || message.trim().isEmpty()
                ? "学生资料变更生效失败" : message;
    }
}
