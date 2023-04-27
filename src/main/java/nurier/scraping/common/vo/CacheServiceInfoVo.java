package nurier.scraping.common.vo;

public class CacheServiceInfoVo {

    private String name;
    private String nodeId;
    private String statistics;
    private String type;
    private String running;
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the nodeId
     */
    public String getNodeId() {
        return nodeId;
    }
    /**
     * @param nodeId the nodeId to set
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    /**
     * @return the statistics
     */
    public String getStatistics() {
        return statistics;
    }
    /**
     * @param statistics the statistics to set
     */
    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the running
     */
    public String getRunning() {
        return running;
    }
    /**
     * @param running the running to set
     */
    public void setRunning(String running) {
        this.running = running;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CacheServiceInfoVo [name=" + name + ", nodeId=" + nodeId
                + ", statistics=" + statistics + ", type=" + type
                + ", running=" + running + "]";
    }
    
    
    
    
}
