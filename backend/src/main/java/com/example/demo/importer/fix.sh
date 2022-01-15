#!/bin/sh
for i in `find . -name \*.java -print`
do
    cat $i | sed s/"com.fin.proto.ui.server.hibernate.domain"/"com.example.demo.domain"/g > /tmp/doom
    mv /tmp/doom $i
done
