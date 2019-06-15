for i in 0000 1000 2000 2250 2500 2750 3000 4000 4250 4500 4750 ; do echo -n "Speculate-$i: " ; diff --brief Speculate-$i.sol Speculate-$i.out && echo "OK" ; done
