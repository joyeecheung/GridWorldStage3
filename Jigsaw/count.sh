#! /usr/bin/env/bash

echo "$1 $2 $3" >> result.txt

for i in {1..20}; do
    ant run
    result=`grep "No solution" ASearchDialog.txt`
    if [ ! -z "$result" ]; then
        echo "-1" >> result.txt
    fi
    result=`grep -o "Total number of searched nodes:[0-9]\+" ASearchDialog.txt | grep -o '[0-9]\+$'`
    echo $result >> result.txt
done
