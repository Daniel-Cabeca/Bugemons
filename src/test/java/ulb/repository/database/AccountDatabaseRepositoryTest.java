package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ulb.exceptions.LoadException;
import ulb.model.ability.Ability;
import ulb.model.type.Type;
import ulb.repository.database.sql.Database;
import ulb.repository.database.sql.DatabaseInMemory;
import ulb.repository.database.sql.DatabaseInitializer;import ulb.repository.database.sql.DatabaseMock;
import ulb.utils.DuplicateElementException;

import java.util.List;
import java.util.stream.StreamSupport;

public class AccountDatabaseRepositoryTest {

    private AccountDatabaseRepository newRepository() {
            Database database = new DatabaseInMemory();
            new DatabaseInitializer(database).createTables();
            return new AccountDatabaseRepository(database);
        }

        @Test
        void registerReturnsTrueForNewUser() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            assertTrue(repo.register("alice", "hash1"));
        }

        @Test
        void registerReturnsFalseWhenUsernameAlreadyTaken() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            assertTrue(repo.register("alice", "hash1"));
            assertFalse(repo.register("alice", "hash2"));
        }

        @Test
        void getPasswordHashReturnsStoredHash() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            repo.register("alice", "secret_hash");
            assertEquals("secret_hash", repo.getPasswordHash("alice"));
        }

        @Test
        void getPasswordHashReturnsNullWhenUserMissing() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            assertNull(repo.getPasswordHash("nobody"));
        }

        @Test
        void getUserIdReturnsAssignedIdAndMinusOneWhenMissing() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            repo.register("first", "a");
            repo.register("second", "b");
            assertEquals(1, repo.getUserId("first"));
            assertEquals(2, repo.getUserId("second"));
            assertEquals(-1, repo.getUserId("ghost"));
        }

        @Test
        void addFriendInsertsSymmetricRows() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            repo.register("a", "1");
            repo.register("b", "2");
            repo.addFriend(1, 2);

            List<String> forA = repo.getFriendsList(1);
            List<String> forB = repo.getFriendsList(2);
            assertTrue(forA.contains("b"));
            assertTrue(forB.contains("a"));
        }

        @Test
        void sendFriendRequestAndGetPendingFriendRequests() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            repo.register("sender", "1");
            repo.register("receiver", "2");
            repo.sendFriendRequest(1, 2);

            assertEquals(List.of("sender"), repo.getPendingFriendRequests(2));
        }

        @Test
        void declineFriendRequestRemovesRequestWithoutFriendship() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            repo.register("s", "1");
            repo.register("r", "2");
            repo.sendFriendRequest(1, 2);

            repo.declineFriendRequest(1, 2);

            assertTrue(repo.getPendingFriendRequests(2).isEmpty());
            assertTrue(repo.getFriendsList(1).isEmpty());
            assertTrue(repo.getFriendsList(2).isEmpty());
        }

        @Test
        void acceptFriendRequestRemovesRequestAndAddsFriends() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            repo.register("s", "1");
            repo.register("r", "2");
            repo.sendFriendRequest(1, 2);

            repo.acceptFriendRequest(1, 2);

            assertTrue(repo.getPendingFriendRequests(2).isEmpty());
            assertTrue(repo.getFriendsList(2).contains("s"));
            assertTrue(repo.getFriendsList(1).contains("r"));
        }

        @Test
        void sendBattleRequestAndHasPendingBetweenBothDirections() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            repo.register("p1", "1");
            repo.register("p2", "2");

            repo.sendBattleRequest(1, 2);

            assertTrue(repo.hasPendingBattleRequestBetween(1, 2));
            assertTrue(repo.hasPendingBattleRequestBetween(2, 1));
        }

        @Test
        void hasPendingBattleRequestBetweenFalseWhenNone() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            repo.register("p1", "1");
            repo.register("p2", "2");

            assertFalse(repo.hasPendingBattleRequestBetween(1, 2));
        }

        @Test
        void getPendingBattleRequestsListsSenderUsernames() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            repo.register("attacker", "1");
            repo.register("defender", "2");
            repo.sendBattleRequest(1, 2);

            assertEquals(List.of("attacker"), repo.getPendingBattleRequests(2));
        }

        @Test
        void acceptBattleRequestRemovesPendingBattle() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            repo.register("a", "1");
            repo.register("b", "2");
            repo.sendBattleRequest(1, 2);

            repo.acceptBattleRequest(1, 2);

            assertFalse(repo.hasPendingBattleRequestBetween(1, 2));
            assertTrue(repo.getPendingBattleRequests(2).isEmpty());
        }

        @Test
        void declineBattleRequestRemovesPendingBattle() throws LoadException {
            AccountDatabaseRepository repo = newRepository();
            repo.register("a", "1");
            repo.register("b", "2");
            repo.sendBattleRequest(1, 2);

            repo.declineBattleRequest(1, 2);

            assertFalse(repo.hasPendingBattleRequestBetween(1, 2));
            assertTrue(repo.getPendingBattleRequests(2).isEmpty());
        }


    }


