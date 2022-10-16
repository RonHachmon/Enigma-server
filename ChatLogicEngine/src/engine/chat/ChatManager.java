package engine.chat;

import java.util.ArrayList;
import java.util.List;

/*
This class is thread safe in the manner of adding\fetching new chat lines, but not in the manner of getting the size of the list
if the use of getVersion is to be incorporated with other methods here - it should be synchronized from the user code
 */
public class ChatManager {

    private final List<SingleChatEntry> chatDataList;

    public ChatManager() {
        chatDataList = new ArrayList<>();
    }

    public synchronized void addChatString(String chatString, String username) {
        chatDataList.add(new SingleChatEntry(chatString, username));
    }

    public synchronized List<SingleChatEntry> getChatEntries(int fromIndex){
        if (fromIndex < 0 || fromIndex > chatDataList.size()) {
            fromIndex = 0;
        }
        return chatDataList.subList(fromIndex, chatDataList.size());
    }

    public int getVersion() {
        return chatDataList.size();
    }


}