package com.neo.springboot.seckill.test;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.neo.springboot.seckill.SeckillMain;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author: neo
 * @description 秒杀测试
 * @date: 2023/7/16 15:55
 */
@SpringBootTest(classes = {SeckillMain.class})
public class SeckillTest {

    @Resource
    private DataSource dataSource;
    @Test
    public void testDataSource(){
        System.out.println(dataSource.toString());
    }

    @Test
    public void genCode(){


        //全局配置
        String projectPath = System.getProperty("user.dir");
        GlobalConfig globalConfig = new GlobalConfig.Builder()
                .outputDir(projectPath + "/src/main/java")
                .author("neo")
                .enableSwagger()
                .dateType(DateType.TIME_PACK)
                //.commentDate("yyyy-MM-dd")
                .build();

        // 包配置
        PackageConfig packageConfig = new PackageConfig.Builder()
                .parent("com.neo.springboot")
                .moduleName("seckill")
                .entity("bean")
                .service("service")
                .serviceImpl("service.impl")
                .mapper("mapper")
                .xml("mapper")
                .controller("controller")
                .other("other")
                .build();

        //  策略配置
        StrategyConfig strategyConfig = new StrategyConfig.Builder()
                // bean配置
                .entityBuilder()
                .enableChainModel()
                .enableLombok()
                .enableRemoveIsPrefix()
                .enableTableFieldAnnotation()
                //.versionColumnName("version")
                //.versionPropertyName("version")
                //.logicDeleteColumnName("deleted")
                //.logicDeletePropertyName("deleteFlag")
                .naming(NamingStrategy.no_change)
                .columnNaming(NamingStrategy.underline_to_camel)
                //.addSuperEntityColumns("id", "created_by", "created_time", "updated_by", "updated_time")
                .idType(IdType.AUTO)
                // 控制层配置
                .controllerBuilder()
                .enableRestStyle()
                // 服务层配置
                .serviceBuilder()
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImpl")
                // 持久层配置
                .mapperBuilder()
                .enableMapperAnnotation()
                .enableBaseResultMap()
                .enableBaseColumnList()
                .formatMapperFileName("%sMapper")
                .formatXmlFileName("%sXml")
                .build();

        DataSourceConfig dataSourceConfig = new DataSourceConfig
                .Builder("jdbc:mysql://localhost:3306/neo_seckill?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true",
                "root",
                "root")
                .build();


        // 添加以上配置到AutoGenerator中
        AutoGenerator autoGenerator = new AutoGenerator(dataSourceConfig); // 数据源配置
        autoGenerator.global(globalConfig); // 全局策略配置
        autoGenerator.packageInfo(packageConfig);    // 包配置
        autoGenerator.strategy(strategyConfig);
        // 生成代码
        autoGenerator.execute();

    }
}
