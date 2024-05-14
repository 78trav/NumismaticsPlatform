#!/bin/bash

#TOKEN=""
TOKEN=$(./keycloak-tokens.sh)
echo "${TOKEN}"

#curl -H "Authorization: Bearer ${TOKEN}" \
#  -H "X-Request-ID: 1234" \
#  -H "x-client-request-id: 1235" \
#  http://localhost:8090/

curl -H "Authorization: Bearer ${TOKEN}" http://localhost:8090/
