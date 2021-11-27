g++ -Wall -Werror -std=c++17 -g *.cpp -o currentLab
for i in in[1-9][0-9].txt
do
	out=${i/in/out}
	printf "Comparing $i with $out\n   "
	start_time=`date +%s`
	./currentLab $i > check.txt
	end_time=`date +%s`
	diff -q check.txt $out
	echo  "  " execution time was `expr $end_time - $start_time` s.
	printf "\n\n"
done
