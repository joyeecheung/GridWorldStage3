set terminal png size 1378,768
set output 'result.png'
set xrange [-1:30000]
set title '10 20 10'
set boxwidth 200 absolute
set style fill solid 1.0 noborder
bin_width = 1000;
bin_number(x) = floor(x/bin_width)
rounded(x) = bin_width * ( bin_number(x) + 0.5 )
plot 'test.dat' every ::1 using (rounded($1)):(1.0/20) smooth frequency with lines
