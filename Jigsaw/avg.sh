tail -n 50 result.txt | awk '{ if ($1 != -1) { total += $1; count++ } } END { print total/count }'
