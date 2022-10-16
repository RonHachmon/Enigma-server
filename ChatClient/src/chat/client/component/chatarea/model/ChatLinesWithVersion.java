package chat.client.component.chatarea.model;

import java.util.List;

public class ChatLinesWithVersion {

    private int version;
    private List<SingleChatLine> entries;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<SingleChatLine> getEntries() {
        return entries;
    }

    public void setEntries(List<SingleChatLine> entries) {
        this.entries = entries;
    }
}
