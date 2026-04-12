package ulb.repository;

import java.util.List;

public interface AccountRepository {

	record PendingFriendRequest(int requesterId, String requesterUsername) {}

	public boolean register(String username, String password) throws LoadException;
	public String getPasswordHash(String username) throws LoadException;
	public int getUserId(String username) throws LoadException;
	List<String> getFriendsList(int userId) throws  LoadException;
	void addFriend(int userId, int friendId) throws LoadException;

	void sendFriendRequest(int requesterId, int addresseeId) throws LoadException;

	List<PendingFriendRequest> getPendingFriendRequestsForAddressee(int addresseeUserId) throws LoadException;

	void acceptFriendRequest(int addresseeId, int requesterId) throws LoadException;

	void declineFriendRequest(int addresseeId, int requesterId) throws LoadException;
}
