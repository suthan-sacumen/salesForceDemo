package org.sacumen.demo.dto;

import java.util.List;

public class EventLogDTO {

    private int totalSize;

    private boolean done;

    private List<RecordDTO> records;

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public List<RecordDTO> getRecords() {
        return records;
    }

    public void setRecords(List<RecordDTO> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "EventLogDTO{" +
                "totalSize=" + totalSize +
                ", done=" + done +
                ", records=" + records +
                '}';
    }
}
