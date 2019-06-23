#!/bin/bash

DOCS=$(pwd)/docs
CONF=$DOCS/icbc.conf

if [ -f $CONF ]; then
  rm $DOCS/icbc.conf
  rm -rf  $DOCS/*.txt
fi
cat > $DOCS/icbc.conf <<EOT
-- basic config --
password=12345678

-- input file --
cert=$DOCS/icbcOpayTest.crt
key=$DOCS/icbcOpayTest.key
message=$DOCS/message.tmp

-- output file --
cert_base64=$DOCS/cert_base64.txt
key_base64=$DOCS/key_base64.txt
message_base64=$DOCS/message_base64.txt
signed_message_base64=$DOCS/signed_message_base64.txt
EOT

java -jar jar/icbc-sign-message.jar $CONF
