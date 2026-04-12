package ulb.repository;

import java.util.List;

public interface AccountRepository {
	public boolean register(String username, String password) throws LoadException;
	public String getPasswordHash(String username) throws LoadException;
	public int getUserId(String username) throws LoadException;
	List<String> getFriendsList(int userId) throws LoadException;
	void addFriend(int userId, int friendId) throws LoadException;
	void sendFriendRequest(int senderId, int receiverId) throws LoadException;
	List<String> getPendingRequests(int receiverId) throws LoadException;
	void acceptFriendRequest(int senderId, int receiverId) throws LoadException;
	void declineFriendRequest(int senderId, int receiverId) throws LoadException;
}
