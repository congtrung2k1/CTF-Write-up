#fix and find the 10,497th prime number

import math
def main():
    primes = []
    count = 2
    index = 0
    while True:
        isprime = False
        for x in range(2, int(math.sqrt(count) + 1)):
            if count % x == 0: 
                isprime = True
                continue
        if isprime:
            primes.append(count)
            print(index, primes[index])
            index += 1
        count += 1
if __name__ == "__main__":
    main()

