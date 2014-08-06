#! /usr/bin/env bash

for i in {1..20}; do
    ant run
    result=`grep "No solution" ASearchDialog.txt`
    if [ ! -z "$result" ]; then
        echo "-1" >> result.txt
    else 
        result=`grep -o "Total number of searched nodes:[0-9]\+" ASearchDialog.txt | grep -o '[0-9]\+$'`
        echo $result >> result.txt
    fi
done
