package ulb.repository;

public interface AccountRepository {
	public boolean register(String username, String password) throws LoadException;
	public String getPasswordHash(String username) throws LoadException;
	public int getUserId(String username) throws LoadException;
	public boolean isFirstLogin(String username) throws LoadException;
	public String[] getPlayerProfile(String username) throws LoadException;
	public void savePlayerProfile(String username, String playerName, String gender) throws LoadException;
}
