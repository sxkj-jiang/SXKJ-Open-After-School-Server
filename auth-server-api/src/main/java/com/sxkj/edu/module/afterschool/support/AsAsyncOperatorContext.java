package com.sxkj.edu.module.afterschool.support;

/**
 * 课后服务异步任务操作人上下文。
 *
 * <p>异步线程无法读取 HTTP 请求上下文，因此由启动线程显式传递操作人编号，
 * 任务结束后必须清理，避免线程复用导致操作人串用。</p>
 */
public final class AsAsyncOperatorContext {

    private static final ThreadLocal<Long> OPERATOR_USER_ID = new ThreadLocal<>();

    private AsAsyncOperatorContext() {
    }

    /** 保存当前异步任务的操作人编号。 */
    public static void setOperatorUserId(Long operatorUserId) {
        OPERATOR_USER_ID.set(operatorUserId);
    }

    /** 获取当前异步任务的操作人编号。 */
    public static Long getOperatorUserId() {
        return OPERATOR_USER_ID.get();
    }

    /** 清理当前线程的操作人编号。 */
    public static void clear() {
        OPERATOR_USER_ID.remove();
    }
}
