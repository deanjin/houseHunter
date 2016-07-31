package net.dean.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dean on 16/6/24.
 */
public class Runner {

    private final static Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) throws Exception{

        DepartmentInfoRunner departmentInfoRunner = new DepartmentInfoRunner();
        departmentInfoRunner.run();

    }
}
