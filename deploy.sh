#!/usr/bin/env bash
if [ $# == 1 ]; then
	sh newVersion.sh $1
fi
mvn -N clean deploy -Dmaven.test.skip=true
mvn clean deploy -Dmaven.test.skip=true
