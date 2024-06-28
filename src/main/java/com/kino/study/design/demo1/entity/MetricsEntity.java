package com.kino.study.design.demo1.entity;

/**
 * 计数器的实体类
 * @author kino
 * @date 2024/6/24 22:57
 */
public class MetricsEntity {
    // 响应时间的最大值
    private Double max;
    // 最小值
    private Double min;
    // 平均值
    private Double avg;
    // 百分位值
    private Double percentile;
    // 接口调用次数
    private Integer count;
    // 频率
    private Double tps;
    // 可扩展...

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public Double getPercentile() {
        return percentile;
    }

    public void setPercentile(Double percentile) {
        this.percentile = percentile;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getTps() {
        return tps;
    }

    public void setTps(Double tps) {
        this.tps = tps;
    }

    @Override
    public String toString() {
        return "Metrics{" +
                "max=" + max +
                ", min=" + min +
                ", avg=" + avg +
                ", percentile=" + percentile +
                ", count=" + count +
                ", tps=" + tps +
                '}';
    }
}

