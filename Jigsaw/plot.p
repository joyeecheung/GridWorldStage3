set terminal png size 1378,768
set output '0-5-2.png'
set yrange [0.0:0.5]
set xrange [-5000:30000]
set title '0 5 2'
set boxwidth 200 absolute
set style fill solid 1.0 noborder
bin_width = 1000;
bin_number(x) = floor(x/bin_width)
rounded(x) = bin_width * ( bin_number(x) + 0.5 )
plot '0-5-2.dat' using (rounded($1)):(1.0/50) smooth frequency with boxes
