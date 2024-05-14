#!/bin/bash

KCHOST=http://localhost:8081
REALM=numismatic
CLIENT_ID=numismatic-service
UNAME=test-user
PASSWORD=2

# shellcheck disable=SC2006
ACCESS_TOKEN=`curl \
  -d "client_id=$CLIENT_ID" \
  -d "username=$UNAME" \
  -d "password=$PASSWORD" \
  -d "grant_type=password" \
  "$KCHOST/realms/$REALM/protocol/openid-connect/token" | jq -r '.access_token'`
echo "$ACCESS_TOKEN"
