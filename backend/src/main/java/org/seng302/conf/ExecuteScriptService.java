//Reference:    https://mubasil-bokhari.medium.com/execute-sql-script-in-spring-boot-30636884a932

package org.seng302.conf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.main.MainApplicationRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import javax.sql.DataSource;
import java.util.concurrent.Future;

public class ExecuteScriptService {

    /**
     * injecting an instance of Datasource class
     */
    @Autowired
    private DataSource dataSource;

    private static final Logger logger = LogManager.getLogger(MainApplicationRunner.class.getName());

    public void executeScript(Resource script) {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(script);
        boolean success = true;
        String error = null;
        try {
            databasePopulator.execute(this.dataSource);
        } catch (ScriptException e) {
            success = false;
            error = "Error executing script "+ ", script: "+script.getFilename()+". Error: "+e.getMessage();
            logger.error("Error executing script , script: {}. Error: {}",
                    script.getFilename(), e.getMessage(), e);
            throw e;
        } finally {
        }
    }

    @Async
    public Future<Boolean> runScript(Resource script) {

        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(script);

        try {

            logger.info("Working with script: {}", script.getFilename());
            this.executeScript(script);

        } catch (ScriptException e) {
            return new AsyncResult<Boolean>(false);
        }
        logger.info("Done with script {}...", script.getFilename());
        return new AsyncResult<Boolean>(true);
    }
}
