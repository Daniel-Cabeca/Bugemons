CREATE TABLE IF NOT EXISTS users (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    username TEXT NOT NULL UNIQUE,
                                    password_hash TEXT NOT NULL
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


-- 4. Table des Espèces de Bugemons
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

                                        FOREIGN KEY (species_id) REFERENCES bugemon_species(id) ON DELETE CASCADE
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

CREATE TABLE IF NOT EXISTS friends (
                                       user_id INTEGER NOT NULL,
                                       friend_id INTEGER NOT NULL,

                                       PRIMARY KEY (user_id, friend_id),
                                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                       FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE,
                                       CHECK (user_id <> friend_id) -- Empêche d'être ami avec soi-même
);