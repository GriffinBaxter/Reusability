#!/usr/bin/bash

# retrieve environment variables
source "staging-backend/env_variables.txt"

# Run the staging backend server

fuser -k 9499/tcp || true
java -jar staging-backend/libs/backend-0.0.1-SNAPSHOT.jar --server.port=9499
