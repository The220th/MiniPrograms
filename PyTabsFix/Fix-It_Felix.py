numberOfSpaces = 4

fin = open("in.py", 'r', encoding="utf-8")
fout = open("out.py", 'w', encoding="utf-8")
S = fin.read()
fin.close()

res = ""
quotes1 = False
quotes2 = False

for i in S:
    if(quotes1 == True):
        res += i
        if(i == '\''):
            quotes1 = False
    elif(quotes2 == True):
        res += i
        if(i == '\"'):
            quotes2 = False
    else:
        if(i == '\t'):
            res += " "*numberOfSpaces
        elif(i == '\''):
            res += i
            quotes1 = True
        elif(i == '\"'):
            res += i
            quotes2 = True
        else:
            res += i

fout.write(res)
fout.close()