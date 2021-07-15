package builder;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MybatisBuilder {
    public static void main(String[] args) throws IOException, XMLParserException {
        List<String> warnings = new ArrayList<String>();

        File configFile = new File(
                "./src/test/java/builder/generatorConfig.xml");
        System.out.println(configFile.getAbsolutePath());
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(true);
//        /Users/aqiu/Desktop/Person/Java学习/SpringBoot/springboot-web/src/test/builder/generatorConfig.xml
        //   ... fill out the config object as appropriate...
///Users/aqiu/Desktop/Person/Java学习/SpringBoot/springboot-web/target/classes/generatorConfig.xml
        try {
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
            System.out.println(warnings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
