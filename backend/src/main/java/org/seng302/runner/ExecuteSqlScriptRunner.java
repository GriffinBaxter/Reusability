////Reference:    https://mubasil-bokhari.medium.com/execute-sql-script-in-spring-boot-30636884a932
//
//package org.seng302.runner;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.seng302.conf.ExecuteScriptService;
//import org.seng302.main.MainApplicationRunner;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.Future;
//import java.util.stream.Collectors;
//
//public class ExecuteSqlScriptRunner {
//
//    private static final Logger logger = LogManager.getLogger(MainApplicationRunner.class.getName());
//    private final ExecuteScriptService service = new ExecuteScriptService();
//    private String dirName = "scriptsToRun";
//    private static final String baseScriptFolder = "../runner/resources";
//
//    //    logger.info("The application is about to run for {}", (Object) dirName);
//    List<Future<Boolean>> tasks;
//
//    try {
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        Resource[] resources = new Resource[0];
//        try {
//            resources = resolver.getResources(ExecuteSqlScriptRunner.baseScriptFolder + dirName + "/*.sql");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        tasks = Arrays.stream(resources).map(e -> this.service.runScript(e)).collect(Collectors.toList());
//    } catch ( IOException ex) {
//        logger.error("Could not open files from directory '{} '", dirName);
//        return;
//    }
//
//}
