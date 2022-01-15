#!/bin/sh
rm *.log
mvn spring-boot:run -Dspring-boot.run.profiles=laptop
