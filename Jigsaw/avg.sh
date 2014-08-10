tail -n 20 result.txt | awk '{ total += $1; count++ } END { print total/count }'
