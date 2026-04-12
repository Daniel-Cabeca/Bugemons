package ulb.repository;

import java.util.List;

public interface AccountRepository {
	public boolean register(String username, String password) throws LoadException;
	public String getPasswordHash(String username) throws LoadException;
	public int getUserId(String username) throws LoadException;
	List<String> getFriendsList(int userId) throws  LoadException;
	void addFriend(int userId, int friendId) throws LoadException;
}
