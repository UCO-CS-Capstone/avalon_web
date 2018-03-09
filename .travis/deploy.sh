#!/use/bin/env bash

# Copy ROOT.war to Tomcat
rsync -q --inplace "$TRAVIS_BUILD_DIR/build/libs/ROOT.war" "git@$server:$destination_file"

# Update database from schema
cat "$TRAVIS_BUILD_DIR/src/sql/avalon_db.sql" | ssh "git@$server" "mysql -uroot -p$db_pass"
