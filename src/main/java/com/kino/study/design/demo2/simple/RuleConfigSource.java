package com.kino.study.design.demo2.simple;

import com.kino.study.design.demo2.*;

/**
 * 简单工厂模式
 *
 * 根据配置文件的后缀（json、xml、yaml、properties），选择不同的解析器（JsonRuleConfigParser、XmlRuleConfigParser……），
 * 将存储在文件中的配置解析成内存对象 RuleConfig。
 * @author kino
 * @date 2024/6/28 12:08
 */
public class RuleConfigSource {

    public RuleConfig load(String ruleConfigFilePath) {
        IRuleConfigParser parser = RuleConfigParserFactory.createParser(getFileExtension(ruleConfigFilePath));
        if (parser == null) throw new RuntimeException("Rule config file format is not supported:" + ruleConfigFilePath);
        return parser.parse();
    }

    private static String getFileExtension(String ruleConfigFilePath) {
        // TODO: 根据不同的文件后缀返回对应的类型, 默认返回json
        return "json";
    }
}
