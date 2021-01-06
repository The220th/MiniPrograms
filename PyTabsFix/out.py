ans = 1;

a, b = map(int, input("Input on one line \'a\' and \'b\' (a^b) to \"Exponentiation by squaring\": \n> ").split())

while b > 0:
    if (b % 2 == 1):
        ans = ans * a
        b = b - 1
    a = a * a
    b = b // 2

print(ans)