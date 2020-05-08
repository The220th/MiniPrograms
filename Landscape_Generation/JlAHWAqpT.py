import random
from termcolor import colored
import sys
from termcolor import colored, cprint
import colorama

def divide(x0, y0, size0):
	if (size0 == 1):
		return
	Map[x0 + int(size0/2)][y0 + int(size0/2)].SetH( (Map[x0][y0].GetH() + Map[x0 + size0][y0].GetH() + Map[x0][y0 + size0].GetH() + Map[x0 + size0][y0 + size0].GetH())/4 + random.randint(-int(size0*GlobalVar.G), int(size0*GlobalVar.G)))
	#Map[x0 + int(size0/2)][y0 + int(size0/2)].SetH( (Map[x0][y0].GetH() + Map[x0 + size0][y0].GetH() + Map[x0][y0 + size0].GetH() + Map[x0 + size0][y0 + size0].GetH())/4)
	
	Map[x0 + int(size0/2)][y0 + size0].SetH(Romb(x0 + int(size0/2), y0 + size0, int(size0/2)))
	Map[x0 + size0][y0 + int(size0/2)].SetH(Romb(x0 + size0, y0 + int(size0/2), int(size0/2)))
	Map[x0 + int(size0/2)][y0].SetH(Romb(x0 + int(size0/2), y0, int(size0/2)))
	Map[x0][y0 + int(size0/2)].SetH(Romb(x0, y0 + int(size0/2), int(size0/2)))
	
	divide(x0 + int(size0/2), y0, int(size0/2))
	divide(x0 + int(size0/2), y0 + int(size0/2), int(size0/2))
	divide(x0, y0 + int(size0/2), int(size0/2))
	divide(x0, y0, int(size0/2))
	return
	
def Romb(x, y, size0):
	Buff = [[x, y + size0], [x + size0, y], [x, y - size0], [x - size0, y]]
	Returned = [0.0, 0.0, 0.0, 0.0]
	for i in range(4):
		if(Buff[i][0] >= 0 and Buff[i][0] <= GlobalVar.Gsize and Buff[i][1] >= 0 and Buff[i][1] <= GlobalVar.Gsize):
			Returned[i] = Map[Buff[i][0]][Buff[i][1]].GetH()
	return (Returned[0] + Returned[1] + Returned[2] + Returned[3])/4 + random.randint(-int(size0*GlobalVar.G), int(size0*GlobalVar.G))
	#return (Returned[0] + Returned[1] + Returned[2] + Returned[3])/4
def PrintColor(Sint):
	if(Sint == 0):
		cprint(' ', 'green', 'on_blue', end = "")
		return
	if(Sint == 1):
		cprint(' ', 'green', 'on_cyan', end = "")
		return
	if(Sint == 2 or Sint == 3):
		cprint(' ', 'green', 'on_yellow', end = "")
		return
	if(Sint == 4 or Sint == 5 or Sint == 6):
		cprint(' ', 'green', 'on_green', end = "")
		return
	if(Sint == 7 or Sint == 7 or Sint == 8):
		cprint(' ', 'green', 'on_grey', end = "")
		return
	if(Sint == 9):
		cprint(' ', 'green', 'on_white', end = "")
		return
		

class Block:
	h = 5
	def __init__(self, x, y):
		self.x = x
		self.y = y
	def SetH(self, h):
		if(h < 0):
			h = 0
		if(h > GlobalVar.MaxH):
			h = GlobalVar.MaxH
		self.h = h
	def GetH(self):
		return self.h
class GlobalVar:
	Gsize = 128
	G = 0.5
	MaxH = 9

colorama.init()
size = GlobalVar.Gsize + 1
Map = []
for i in range(size):
	buff = []
	for j in range(size):
		x = Block(i, j)
		buff.append(x)
	Map.append(buff)
Map[0][0].SetH(random.randint(0, GlobalVar.MaxH))
Map[GlobalVar.Gsize][0].SetH(random.randint(0, GlobalVar.MaxH))
Map[0][GlobalVar.Gsize].SetH(random.randint(0, GlobalVar.MaxH))
Map[GlobalVar.Gsize][GlobalVar.Gsize].SetH(random.randint(0, GlobalVar.MaxH))
divide(0, 0, GlobalVar.Gsize)
for j in range(size):
	for i in range(size):
		PrintColor(int(Map[i][j].GetH()))
	print("\n", end='')
