
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
                                       stat_name TEXT,
                                       multiplier REAL,

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


                                        level INTEGER DEFAULT 1,
                                        xp INTEGER DEFAULT 0,
                                        remaining_rewards INTEGER DEFAULT 0,


                                        hp INTEGER NOT NULL,
                                        attack INTEGER NOT NULL,
                                        defense INTEGER NOT NULL,
                                        initiative INTEGER NOT NULL,

                                        FOREIGN KEY (species_id) REFERENCES bugemon_species(id) ON DELETE CASCADE
);