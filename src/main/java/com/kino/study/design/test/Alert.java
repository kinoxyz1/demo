package com.kino.study.design.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kino
 * @date 2024/6/13 23:52
 */
// 定义 NotificationEmergencyLevel 枚举
enum NotificationEmergencyLevel {
    SEVERE,
    URGENCY,
    NORMAL,
    TRIVIAL;
}

// 定义 Notification 类
class Notification {
    public void notify(NotificationEmergencyLevel level, String message) {
        // 根据紧急程度发送通知，例如通过邮件、短信、微信等渠道发送
        switch (level) {
            case SEVERE:
                // 发送严重级别的通知
                sendEmail(message);
                sendSMS(message);
                break;
            case URGENCY:
                // 发送紧急级别的通知
                sendEmail(message);
                break;
            case NORMAL:
                // 发送普通级别的通知
                sendWeChat(message);
                break;
            case TRIVIAL:
                // 发送无关紧要的通知
                break;
        }
    }

    private void sendEmail(String message) {
        // 发送邮件的具体实现
        System.out.println("Sending email: " + message);
    }

    private void sendSMS(String message) {
        // 发送短信的具体实现
        System.out.println("Sending SMS: " + message);
    }

    private void sendWeChat(String message) {
        // 发送微信消息的具体实现
        System.out.println("Sending WeChat message: " + message);
    }
}

// 定义 AlertRule 类和相关规则
class AlertRule {
    private Map<String, ApiRule> rules = new HashMap<>();

    public void addRule(String api, ApiRule rule) {
        rules.put(api, rule);
    }

    public ApiRule getMatchedRule(String api) {
        if (rules.containsKey(api)) {
            return rules.get(api);
        } else {
            throw new RuntimeException("No rule found for api: " + api);
        }
    }

    public static class ApiRule {
        private long maxTps;
        private long maxErrorCount;
        private long maxTimeoutCount;

        public ApiRule(long maxTps, long maxErrorCount, long maxTimeoutCount) {
            this.maxTps = maxTps;
            this.maxErrorCount = maxErrorCount;
            this.maxTimeoutCount = maxTimeoutCount;
        }

        public long getMaxTps() {
            return maxTps;
        }

        public long getMaxErrorCount() {
            return maxErrorCount;
        }

        public long getMaxTimeoutCount() {
            return maxTimeoutCount;
        }
    }
}

// 定义 ApiStatInfo 类
class ApiStatInfo {
    private String api;
    private long requestCount;
    private long errorCount;
    private long timeoutCount;
    private long durationOfSeconds;

    public ApiStatInfo(String api, long requestCount, long errorCount, long timeoutCount, long durationOfSeconds) {
        this.api = api;
        this.requestCount = requestCount;
        this.errorCount = errorCount;
        this.timeoutCount = timeoutCount;
        this.durationOfSeconds = durationOfSeconds;
    }

    public String getApi() {
        return api;
    }

    public long getRequestCount() {
        return requestCount;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public long getTimeoutCount() {
        return timeoutCount;
    }

    public long getDurationOfSeconds() {
        return durationOfSeconds;
    }
}

abstract class AlertHandler {

    protected AlertRule rule;
    protected Notification notification;

    public AlertHandler(AlertRule rule, Notification notification) {
        this.rule = rule;
        this.notification = notification;
    }

    public abstract void check(ApiStatInfo apiStatInfo);
}

class TpsAlertHandler extends AlertHandler {

    public TpsAlertHandler(AlertRule rule, Notification notification) {
        super(rule, notification);
    }

    @Override
    public void check(ApiStatInfo apiStatInfo) {
        long tps = apiStatInfo.getRequestCount() / apiStatInfo.getDurationOfSeconds();
        if (tps > rule.getMatchedRule(apiStatInfo.getApi()).getMaxTps()) {
            notification.notify(NotificationEmergencyLevel.URGENCY, "High TPS alert for API: " + apiStatInfo.getApi());
        }
    }
}

class ErrorAlertHandler extends AlertHandler {

    public ErrorAlertHandler(AlertRule rule, Notification notification) {
        super(rule, notification);
    }

    @Override
    public void check(ApiStatInfo apiStatInfo) {
        if (apiStatInfo.getErrorCount() > rule.getMatchedRule(apiStatInfo.getApi()).getMaxErrorCount()) {
            notification.notify(NotificationEmergencyLevel.SEVERE, "High error count alert for API: " + apiStatInfo.getApi());
        }
    }
}

class TimeoutAlertHandler extends AlertHandler {
    public TimeoutAlertHandler(AlertRule rule, Notification notification) {
        super(rule, notification);
    }

    @Override
    public void check(ApiStatInfo apiStatInfo) {
        if (apiStatInfo.getTimeoutCount() > rule.getMatchedRule(apiStatInfo.getApi()).getMaxTimeoutCount()) {
            notification.notify(NotificationEmergencyLevel.URGENCY, "High timeout count alert for API: " + apiStatInfo.getApi());
        }
    }

}

// 定义 Alert 类
public class Alert {
    private List<AlertHandler> alertHandlers = new ArrayList<>();

    public void addAlertHandler(AlertHandler handler) {
        alertHandlers.add(handler);
    }

    public void check(ApiStatInfo apiStatInfo) {
        for (AlertHandler handler : alertHandlers) {
            handler.check(apiStatInfo);
        }
    }
}

class ApplicationContext {
    private AlertRule alertRule;
    private Notification notification;
    private Alert alert;

    public void initializeBeans() {
        // 初始化 AlertRule
        alertRule = new AlertRule();
        alertRule.addRule("getUser", new AlertRule.ApiRule(100, 10, 5));
        alertRule.addRule("createOrder", new AlertRule.ApiRule(50, 5, 2));

        // 初始化 Notification
        notification = new Notification();

        // 初始化 Alert 并添加处理器
        alert = new Alert();
        alert.addAlertHandler(new TpsAlertHandler(alertRule, notification));
        alert.addAlertHandler(new ErrorAlertHandler(alertRule, notification));
        alert.addAlertHandler(new TimeoutAlertHandler(alertRule, notification)); // 添加超时告警处理器
    }

    public Alert getAlert() { return alert; }

    // 饿汉式单例
    private static final ApplicationContext instance = new ApplicationContext();
    private ApplicationContext() {
        initializeBeans();
    }

    public static ApplicationContext getInstance() {
        return instance;
    }
}

class Demo {
    public static void main(String[] args) {
        // 获取 ApplicationContext 单例
        ApplicationContext applicationContext = ApplicationContext.getInstance();

        // 获取 Alert 实例
        Alert alert = applicationContext.getAlert();

        // 创建 ApiStatInfo 对象并进行测试
        ApiStatInfo apiStatInfo1 = new ApiStatInfo("getUser", 120, 5, 6, 10);
        ApiStatInfo apiStatInfo2 = new ApiStatInfo("createOrder", 30, 6, 1, 10);
        ApiStatInfo apiStatInfo3 = new ApiStatInfo("createOrder", 30, 3, 3, 10);

        alert.check(apiStatInfo1); // 应该触发高TPS告警和超时告警
        alert.check(apiStatInfo2); // 应该触发高错误数告警
        alert.check(apiStatInfo3); // 应该触发超时告警
    }

}
