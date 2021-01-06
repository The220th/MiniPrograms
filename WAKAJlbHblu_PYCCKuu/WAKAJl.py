def PYC2WAKAJl(charerino):
    c = charerino.lower()
    Rus_Alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
    WAKAJl_Alphabet = ["A", "6", "B", "r", "D", "E", "E", ")l(", "3", "u", "u", "K", "Jl", "M", "H", "O", "n", "P", "C", "T", "Y", "qp", "X", "c", "4", "W", "W", "b", "bl", "b", "E", "lo", "9"]
    if(c in Rus_Alphabet):
        return WAKAJl_Alphabet[Rus_Alphabet.find(c)]
    else:
        return charerino

fin = open("in.txt", 'r', encoding="utf-8")
fout = open("out.txt", 'w', encoding="utf-8")
S = fin.read()
fin.close()

res = ""
for i in S:
    res += PYC2WAKAJl(i)

fout.write(res)
fout.close()