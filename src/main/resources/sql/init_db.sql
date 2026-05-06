CREATE TABLE IF NOT EXISTS users (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    username TEXT NOT NULL UNIQUE,
                                    password_hash TEXT NOT NULL,
                                    points INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS tower_saves (
									user_id INTEGER PRIMARY KEY,
									current_floor_id INTEGER NOT NULL,
									completed_rooms_id TEXT,
									current_team_id INTEGER,

									FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
									FOREIGN KEY (current_team_id) REFERENCES teams(id) ON DELETE CASCADE

);

CREATE TABLE IF NOT EXISTS inventory (
                                        user_id INTEGER,
                                        item_id TEXT,
                                        quantity INTEGER DEFAULT 1,

                                        PRIMARY KEY (user_id, item_id),

                                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                        FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS abilities (
                                         id TEXT PRIMARY KEY,
                                         name TEXT NOT NULL,
                                         type TEXT NOT NULL,
                                         description TEXT,
                                         power INTEGER
);


CREATE TABLE IF NOT EXISTS items (
                                     id TEXT PRIMARY KEY,
                                     name TEXT NOT NULL,
                                     description TEXT,
                                     category TEXT,
                                     sprite TEXT
);

CREATE TABLE IF NOT EXISTS effects (
                                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                                       type TEXT NOT NULL,
                                       ability_id TEXT,
                                       item_id TEXT,

                                       target TEXT NOT NULL,
                                       value INTEGER,



                                       FOREIGN KEY (ability_id) REFERENCES abilities(id) ON DELETE CASCADE,
                                       FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bugemon_species (
                                               id TEXT PRIMARY KEY,
                                               name TEXT NOT NULL,
                                               type TEXT NOT NULL,
                                               sprite TEXT,
                                               starter INTEGER,


                                               hp INTEGER NOT NULL,
                                               attack INTEGER NOT NULL,
                                               defense INTEGER NOT NULL,
                                               initiative INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS species_abilities (
                                                 species_id TEXT,
                                                 ability_id TEXT,
                                                 PRIMARY KEY (species_id, ability_id),
                                                 FOREIGN KEY (species_id) REFERENCES bugemon_species(id) ON DELETE CASCADE,
                                                 FOREIGN KEY (ability_id) REFERENCES abilities(id) ON DELETE CASCADE
);



CREATE TABLE IF NOT EXISTS bugemons (
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        species_id TEXT NOT NULL,

                                        user_id INTEGER NOT NULL,


                                        level INTEGER DEFAULT 1,
                                        xp INTEGER DEFAULT 0,
                                        remaining_rewards INTEGER DEFAULT 0,


                                        hp INTEGER NOT NULL,
                                        attack INTEGER NOT NULL,
                                        defense INTEGER NOT NULL,
                                        initiative INTEGER NOT NULL,

                                        FOREIGN KEY (species_id) REFERENCES bugemon_species(id) ON DELETE CASCADE,
                                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS effect_stats_modifier (
                                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                     effect_id INTEGER NOT NULL, -- LIEN VERS L'EFFET
                                                     hp INTEGER DEFAULT 0,
                                                     attack INTEGER DEFAULT 0,
                                                     defense INTEGER DEFAULT 0,
                                                     initiative INTEGER DEFAULT 0,
                                                     duration TEXT,
                                                     FOREIGN KEY (effect_id) REFERENCES effects(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS chat_messages (
                                        id                INTEGER PRIMARY KEY AUTOINCREMENT,
                                        sender_username   TEXT NOT NULL,
                                        receiver_username TEXT NOT NULL,
                                        content           TEXT NOT NULL,
                                        sent_at           TEXT NOT NULL,
                                        FOREIGN KEY (sender_username)   REFERENCES users(username),
                                        FOREIGN KEY (receiver_username) REFERENCES users(username));
                                        
CREATE TABLE IF NOT EXISTS friends (
                                       user_id INTEGER NOT NULL,
                                       friend_id INTEGER NOT NULL,

                                       PRIMARY KEY (user_id, friend_id),
                                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                       FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE,
                                       CHECK (user_id <> friend_id)
);

CREATE TABLE IF NOT EXISTS friend_requests (
                                       sender_id   INTEGER NOT NULL,
                                       receiver_id INTEGER NOT NULL,

                                       PRIMARY KEY (sender_id, receiver_id),
                                       FOREIGN KEY (sender_id)   REFERENCES users(id) ON DELETE CASCADE,
                                       FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS teams (
                                team_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                user_id INTEGER NOT NULL,
                                team_name TEXT NOT NULL,
								tower_team BOOLEAN DEFAULT 0,
                                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS team_members (
                                     team_id INTEGER NOT NULL,
                                     bugemon_id INTEGER NOT NULL,
                                     PRIMARY KEY (team_id, bugemon_id),
                                     FOREIGN KEY (team_id) REFERENCES teams(team_id) ON DELETE CASCADE,
                                     FOREIGN KEY (bugemon_id) REFERENCES bugemons(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS battle_requests (
                                       sender_id   INTEGER NOT NULL,
                                       receiver_id INTEGER NOT NULL,

                                       PRIMARY KEY (sender_id, receiver_id),
                                       FOREIGN KEY (sender_id)   REFERENCES users(id) ON DELETE CASCADE,
                                       FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);
