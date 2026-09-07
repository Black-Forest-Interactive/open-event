#!/bin/sh
set -eu

TEMPLATE="/usr/share/nginx/html/app/config.json.template"

if [ -f "$TEMPLATE" ]; then
  envsubst '${MAPS_API_KEY}' < "$TEMPLATE" > /usr/share/nginx/html/app/config.json
fi
