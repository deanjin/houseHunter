package net.dean.executor;

import net.dean.watcher.parser.DepartmentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dean on 16/7/10.
 */
public class DepartmentInfoRunner {

    private final static Logger log = LoggerFactory.getLogger(Runner.class);

    public void run() throws Exception{
        DepartmentParser departmentParser = new DepartmentParser();
        departmentParser.run();
    }
}
