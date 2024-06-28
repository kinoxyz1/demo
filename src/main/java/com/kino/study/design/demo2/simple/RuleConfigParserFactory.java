package com.kino.study.design.demo2.simple;

import com.kino.study.design.demo2.IRuleConfigParser;
import com.kino.study.design.demo2.JsonRuleConfigParser;
import com.kino.study.design.demo2.XmlRuleConfigParser;
import com.kino.study.design.demo2.YamlRuleConfigParser;

/**
 * @author kino
 * @date 2024/6/28 12:23
 */
public class RuleConfigParserFactory {
    public static IRuleConfigParser createParser(String fileExtension) {
        IRuleConfigParser parser = null;
        // 如果要将 if 分支逻辑去掉, 比较经典处理方法就是利用多态。
        // 按照多态的实现思路, 对上面的代码进行重构, 看工厂方法模式
        if (fileExtension.equals("json")) {
            parser = new JsonRuleConfigParser();
        } else if (fileExtension.equals("xml")) {
            parser = new XmlRuleConfigParser();
        } else if (fileExtension.equals("yaml")) { // 扩展yaml类型的文件加一个if即可
            parser = new YamlRuleConfigParser();
        } else {
            throw new RuntimeException("Rule config file format is not supported:" + fileExtension);
        }
        return parser;
    }
}
