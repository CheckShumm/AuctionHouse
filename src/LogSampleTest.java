import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogSampleTest {

    private static Logger log = LogManager.getLogger("auctionhouse");

    public static void main(String[] args) {
        System.out.println("printing logs");
        log.debug("debug message here");
        log.error("debug message here");
        log.warn("debug message here");
        log.fatal("debug message here");
        log.warn("debug message here");
        log.fatal("debug message here"); log.warn("debug message here");
        log.fatal("debug message here");

    }
}
