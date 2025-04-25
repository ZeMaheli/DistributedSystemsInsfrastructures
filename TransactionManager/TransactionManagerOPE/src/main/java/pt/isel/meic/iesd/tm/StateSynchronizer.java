package pt.isel.meic.iesd.tm;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class StateSynchronizer implements Watcher {

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case Event.EventType.NodeDeleted:
            case Event.EventType.NodeCreated:
                break;
        }
    }
}
