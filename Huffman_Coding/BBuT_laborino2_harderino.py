def sortorino(d):
	max = 0
	buff = {}
	n = '#'
	nd = len(d)
	for j in range(nd):
		for i in d:
			if d[i] > max:
				max = d[i]
				n = i
		buff[n] = max
		max = 0
		d.pop(n)
		n = '#'
	return buff

def FormatValues(Values):
	Answer = [[-1, ''] for i in range(len(Values))]
	for i in range(len(Values)):
		Answer[i][1] = [Values[i][0]]
		Answer[i][0] = Values[i][1]
	return Answer

def LenEvenly(d):
	Amount = len(d)
	power = 0
	while (2**power < Amount):
		power += 1
	return power

def LenPhono(d):
	LenP = 0
	for i in d:
		LenP += d[i][2] * len(d[i][3])
	return LenP

def LenHuffman(d):
	LenH = 0
	for i in d:
		LenH += d[i][2] * len(d[i][4])
	return LenH

def InstanceDictionary(S):
	'''
	d[...]: [
			0) Symbol, 
			1) IncludeAmount, 
			2) IncludeProbability, 
			3) Phono, 
			4) Huffman
			]
	'''
	d = {}
	for i in S:
		d[i] = 0
	for i in S:
		d[i] += 1
	d = sortorino(d)
	for i in d:
		d[i] = [i, d[i], d[i]/len(S), "", ""]
	return d
	
def Phono(Values, d, lenS):
	if (len(Values) <= 1):
		return
	half = lenS
	nhalf = 1
	for i in range(1, len(Values)-1):
		sumBefore = 0
		for j in range(i):
			sumBefore += Values[j][1]
		sumAfter = 0
		for j in range(i, len(Values)):
			sumAfter += Values[j][1]
		if(abs(sumAfter - sumBefore) < half):
			half = abs(sumAfter - sumBefore)
			nhalf = i
	for i in range(len(Values)):
		if (i < nhalf):
			d[Values[i][0]][3] += '0'
		else:
			d[Values[i][0]][3] += '1'
	Phono(Values[:nhalf], d, lenS)
	Phono(Values[nhalf:], d, lenS)
	return

def UnpackForHaffman(Values, Snum, d):
	if(len(Values) == 1):
		d[Values[0]][4] = Snum
		return
	UnpackForHaffman(Values[0][1], Snum + '0', d)
	UnpackForHaffman(Values[1][1], Snum + '1', d)
	return

def Huffman(Values, d):
	while(len(Values) > 2):
		penult = len(Values) - 2
		Values[penult] = [ (Values[penult][0] + Values[penult+1][0]), [ Values[penult], Values[penult+1] ] ]
		Values = Values[:penult+1]
		i = len(Values)-1
		while(Values[i- 1][0] < Values[i][0] and i > 0):
			Values[i-1], Values[i] = Values[i], Values[i-1]
			i -= 1
	UnpackForHaffman(Values[0][1], "0", d)
	UnpackForHaffman(Values[1][1], "1", d)
	return

def printorino(d):
	MaxLenH = 7
	MaxLenP = 14
	for i in d:
		buff = len(d[i][4] +  " ( " + str(len(d[i][4])) + " ) ")
		if (buff > MaxLenH):
			MaxLenH = buff
		buff = len(d[i][3] + " ( " + str(len(d[i][3])) + " ) ")
		if (buff > MaxLenP):
			MaxLenP = buff
	print("+------+-%s-+-%s-+-%s-+" % ("-"*7, "-"*MaxLenH, "-"*MaxLenP))
	print("|  %s | %7s | %*s | %*s |" % ("Sym", "%", MaxLenH, "Huffman", MaxLenP, "Shannon - Fano"))
	print("+------+-%s-+-%s-+-%s-+" % ("-"*7, "-"*MaxLenH, "-"*MaxLenP))
	for i in d:
		if i == '\n':
			c = '\'' + '\\n' + '\''
		else:
			c = '\'' + i + '\''
		print("| %4s | %7s | %*s | %*s |" % (c, str(round(d[i][2]*100, 4)), MaxLenH ,(d[i][4] +  " ( " + str(len(d[i][4])) + " ) "), MaxLenP , (d[i][3] + " ( " + str(len(d[i][3])) + " ) ")))
	print("+------+-%s-+-%s-+-%s-+" % ("-"*7, "-"*MaxLenH, "-"*MaxLenP))
	
	Len7 = max(len(str(round(LenHuffman(d), 5))), len(str(round(LenPhono(d), 5))), len(str(LenEvenly(d))), 7)
	print("\n+-%s-+-%s-+-%s-+" % ("-"*Len7, "-"*Len7, "-"*Len7))
	print("| %*s | %*s | %*s |" % (Len7, "Huffman", Len7, "Fano", Len7, "Evenly"))
	print("+-%s-+-%s-+-%s-+" % ("-"*Len7, "-"*Len7, "-"*Len7))
	print("| %*s | %*s | %*s |" % (Len7, str(round(LenHuffman(d), 5)), Len7, str(round(LenPhono(d), 5)), Len7, str(float(LenEvenly(d)))))
	print("+-%s-+-%s-+-%s-+" % ("-"*Len7, "-"*Len7, "-"*Len7))
	return

#==========================================
f = open('text.txt', 'r', encoding="utf-8")
S = f.read()
dic = InstanceDictionary(S)
Phono(list(dic.values()), dic, len(S))
Huffman(FormatValues(list(dic.values())), dic)
printorino(dic)


'''

Centuries ago there lived--"A king!" my little readers will say immediately.No, children, you are mistaken. Once upon a time there wasa piece of wood. It was not an expensive piece of wood. Farfrom it. Just a common block of firewood, one of thosethick, solid logs that are put on the fire in winter tomake cold rooms cozy and warm.I do not know how this really happened, yet the factremains that one fine day this piece of wood found itselfin the shop of an old carpenter. His real name was MastroAntonio, but everyone called him Mastro Cherry, for the tipof his nose was so round and red and shiny that it lookedlike a ripe cherry.As soon as he saw that piece of wood, Mastro Cherry wasfilled with joy. Rubbing his hands together happily, hemumbled half to himself:"This has come in the nick of time. I shall use it to makethe leg of a table."He grasped the hatchet quickly to peel off the bark andshape the wood. But as he was about to give it the firstblow, he stood still with arm uplifted, for he had heard awee, little voice say in a beseeching tone: "Please becareful! Do not hit me so hard!"What a look of surprise shone on Mastro Cherry'sface! His funny face became still funnier.He turned frightened eyes about the room to find out wherethat wee, little voice had come from and he saw no one! Helooked under the bench--no one! He peeped inside thecloset--no one! He searched among the shavings--no one! Heopened the door to look up and down the street--and stillno one!"Oh, I see!" he then said, laughing and scratching his Wig."It can easily be seen that I only thought I heard the tinyvoice say the words! Well, well--to work once more."He struck a most solemn blow upon the piece of wood."Oh, oh! You hurt!" cried the same far-away little voice. Mastro Cherry grew dumb, his eyes popped out of his head,his mouth opened wide, and his tongue hung down on hischin.As soon as he regained the use of his senses, he said,trembling and stuttering from fright:"Where did that voice come from, when there is no onearound? Might it be that this piece of wood has learned toweep and cry like a child? I can hardly believe it. Here itis--a piece of common firewood, good only to burn in thestove, the same as any other. Yet--might someone be hiddenin it? If so, the worse for him. I'll fix him!"With these words, he grabbed the log with both hands andstarted to knock it about unmercifully. He threw it to thefloor, against the walls of the room, and even up to theceiling.He listened for the tiny voice to moan and cry. He waitedtwo minutes--nothing; five minutes--nothing; ten minutes--nothing."Oh, I see," he said, trying bravely to laugh and rufflingup his wig with his hand. "It can easily be seen I onlyimagined I heard the tiny voice! Well, well--to work oncemore!"The poor fellow was scared half to death, so he tried tosing a gay song in order to gain courage.He set aside the hatchet and picked up the plane to makethe wood smooth and even, but as he drew it to and fro, heheard the same tiny voice. This time it giggled as itspoke:"Stop it! Oh, stop it! Ha, ha, ha! You tickle my stomach."This time poor Mastro Cherry fell as if shot. When heopened his eyes, he found himself sitting on the floor.His face had changed; fright had turned even the tip of hisnose from red to deepest purple. Mastro Cherry gives the piece of wood to his friendGeppetto,who takes it to make himself a Marionette that will dance,fence, and turn somersaultsIn that very instant, a loud knock sounded on the door."Come in," said the carpenter, not having an atom ofstrength left with which to stand up.At the words, the door opened and a dapper little old mancame in. His name was Geppetto, but to the boys of theneighborhood he was Polendina (or, cornmeal mush), onaccount of the wig he always wore which was just the colorof yellow corn.Geppetto had a very bad temper. Woe to the one who calledhim Polendina! He became as wild as a beast and no onecould soothe him."Good day, Mastro Antonio," said Geppetto. "What are youdoing on the floor?""I am teaching the ants their A B C's.""Good luck to you!""What brought you here, friend Geppetto?""My legs. And it may flatter you to know, Mastro Antonio,that I have come to you to beg for a favor.""Here I am, at your service," answered the carpenter,raising himself on to his knees."This morning a fine idea came to me.""Let's hear it.""I thought of making myself a beautiful wooden Marionette.It must be wonderful, one that will be able to dance,fence, and turn somersaults. With it I intend to go aroundthe world, to earn my crust of bread and cup of wine. Whatdo you think of it?""Bravo, Polendina!" cried the same tiny voice which camefrom no one knew where.On hearing himself called Polendina, Mastro Geppetto turnedthe color of a red pepper and, facing the carpenter, saidto him angrily:"Why do you insult me?" "Who is insulting you?""You called me Polendina.""I did not.""I suppose you think _I_ did! Yet I KNOW it was you.""No!""Yes!""No!""Yes!"And growing angrier each moment, they went from words toblows, and finally began to scratch and bite and slap eachother.When the fight was over, Mastro Antonio had Geppetto'syellow wig in his hands and Geppetto found the carpenter'scurly wig in his mouth."Give me back my wig!" shouted Mastro Antonio in a surlyvoice."You return mine and we'll be friends."The two little old men, each with his own wig back on hisown head, shook hands and swore to be good friends for therest of their lives."Well then, Mastro Geppetto," said the carpenter, to showhe bore him no ill will, "what is it you want?""I want a piece of wood to make a Marionette. Will you giveit to me?"Mastro Antonio, very glad indeed, went immediately to hisbench to get the piece of wood which had frightened him somuch. But as he was about to give it to his friend, with aviolent jerk it slipped out of his hands and hit againstpoor Geppetto's thin legs."Ah! Is this the gentle way, Mastro Antonio, in which youmake your gifts? You have made me almost lame!""I swear to you I did not do it!""It wasI, of course!""It's the fault of this piece of wood.""You're right; but remember you were the one to throw it atmy legs." Pinocchio...6 "I did not throw it!""Liar!""Geppetto, do not insult me or I shall call you Polendina.""Idiot.""Polendina!""Donkey!""Polendina!""Ugly monkey!""Polendina!"On hearing himself called Polendina for the third time,Geppetto lost his head with rage and threw himself upon thecarpenter. Then and there they gave each other a soundthrashing.After this fight, Mastro Antonio had two more scratches onhis nose, and Geppetto had two buttons missing from hiscoat. Thus having settled their accounts, they shook handsand swore to be good friends for the rest of their lives.Then Geppetto took the fine piece of wood, thanked MastroAntonio, and limped away toward home. As soon as he gets home, Geppetto fashions the Marionetteand calls it Pinocchio. The first pranks of the MarionetteLittle as Geppetto's house was, it was neat andcomfortable. It was a small room on the ground floor, witha tiny window under the stairway. The furniture could nothave been much simpler: a very old chair, a rickety oldbed, and a tumble-down table. A fireplace full of burninglogs was painted on the wall opposite the door. Over thefire, there was painted a pot full of something which keptboiling happily away and sending up clouds of what lookedlike real steam.As soon as he reached home, Geppetto took his tools andbegan to cut and shape the wood into a Marionette."What shall I call him?" he said to himself. "I think I'llcall him PINOCCHIO. This name will make his fortune. I knewa whole family of Pinocchi once--Pinocchio the father,Pinocchia the mother, and Pinocchi the children--and theywere all lucky. The richest of them begged forhis living."After choosing the name for his Marionette, Geppetto setseriously to work to make the hair, the forehead, the eyes.Fancy his surprise when he noticed that these eyes movedand then stared fixedly at him. Geppetto, seeing this, feltinsulted and said in a grieved tone:"Ugly wooden eyes, why do you stare so?"There was no answer.After the eyes, Geppetto made the nose, which began tostretch as soon as finished. It stretched and stretched andstretched till it became so long, it seemed endless.Poor Geppetto kept cutting it and cutting it, but the morehe cut, the longer grew that impertinent nose. In despairhe let it alone.Next he made the mouth.No sooner was it finished than it began to laugh and pokefun at him."Stop laughing!" said Geppetto angrily; but he might aswell have spoken to the wall."Stop laughing, I say!" he roared in a voice of thunder.The mouth stopped laughing, but it stuck out a long tongue. Not wishing to start an argument, Geppetto made believe hesaw nothing and went on with his work. After the mouth, hemade the chin, then the neck, the shoulders, the stomach,the arms, and the hands.As he was about to put the last touches on the finger tips,Geppetto felt his wig being pulled off. He glanced up andwhat did he see? His yellow wig was in the Marionette'shand. "Pinocchio, give me my wig!"But instead of giving it back, Pinocchio put it on his ownhead, which was half swallowed up in it.At that unexpected trick, Geppetto became very sad anddowncast, more so than he had ever been before."Pinocchio, you wicked boy!" he cried out. "You are not yetfinished, and you start out by being impudent to your poorold father. Very bad, my son, very bad!"And he wiped away a tear.The legs and feet still had to be made. As soon as theywere done, Geppetto felt a sharp kick on the tip of hisnose."I deserve it!" he said to himself. "I should have thoughtof this before I made him. Now it's too late!"He took hold of the Marionette under the arms and put himon the floor to teach him to walk.Pinocchio's legs were so stiff that he could not move them,and Geppetto held his hand and showed him how to put outone foot after the other.When his legs were limbered up, Pinocchio started walkingby himself and ran all around the room. He came to the opendoor, and with one leap he was out into the street. Away heflew!Poor Geppetto ran after him but was unable to catch him,for Pinocchio ran in leaps and bounds, his two wooden feet,as they beat on the stones of the street, making as muchnoise as twenty peasants in wooden shoes."Catch him! Catch him!" Geppetto kept shouting. But thepeople in the street, seeing a wooden Marionette runninglike the wind, stood still to stare and to laugh until theycried.At last, by sheer luck, a Carabineer (or, militarypoliceman) happened along, who, hearing all that noise,thought that it might be a runaway colt, and stood bravelyin the middle of the street, with legs wide apart, firmlyresolved to stop it and prevent any trouble. Pinocchio saw the Carabineer from afar and tried his bestto escape between the legs of the big fellow, but withoutsuccess.The Carabineer grabbed him by the nose (it was an extremelylong one and seemed made on purpose for that very thing)and returned him to Mastro Geppetto.The little old man wanted to pull Pinocchio's ears. Thinkhow he felt when, upon searching for them, he discoveredthat he had forgotten to make them!All he could do was to seize Pinocchio by the back of theneck and take him home. As he was doing so, he shook himtwo or three times and said to him angrily:"We're going home now. When we get home, then we'll settlethis matter!"Pinocchio, on hearing this, threw himself on the ground andrefused to take another step. One person after anothergathered around the two.Some said one thing, some another."Poor Marionette," called out a man. "I am not surprised hedoesn't want to go home. Geppetto, no doubt, will beat himunmercifully, he is so mean and cruel!""Geppetto looks like a good man," added another, "but withboys he's a real tyrant. If we leave that poor Marionettein his hands he may tear him to pieces!"They said so much that, finally, the Carabineer endedmatters by setting Pinocchio at liberty and draggingGeppetto to prison. The poor old fellow did not know how todefend himself, but wept and wailed like a child and saidbetween his sobs:"Ungrateful boy! To think I tried so hard to make you awell-behaved Marionette! I deserve it, however! I shouldhave given the matter more thought."What happened after this is an almost unbelievable story,but you may read it, dear children, in the chapters thatfollow. The story of Pinocchio and the Talking Cricket, in whichone sees that bad children do not like to be corrected bythose who know more than they doVery little time did it take to get poor old Geppetto toprison. In the meantime that rascal, Pinocchio, free nowfrom the clutches of the Carabineer, was running wildlyacross fields and meadows, taking one short cut afteranother toward home. In his wild flight, he leaped overbrambles and bushes, and across brooks and ponds, as if hewere a goat or a hare chased by hounds.On reaching home, he found the house door half open. Heslipped into the room, locked the door, and threw himselfon the floor, happy at his escape.But his happiness lasted only a short time, for just thenhe heard someone saying:"Cri-cri-cri!""Who is calling me?" asked Pinocchio, greatly frightened."I am!"Pinocchio turned and saw a large cricket crawling slowly upthe wall."Tell me, Cricket, who are you?""I am the Talking Cricket and I have been living in thisroom for more than one hundred years.""Today, however, this room is mine," said the Marionette,"and if you wish to do me a favor, get out now, and don'tturn around even once.""I refuse to leave this spot," answered the Cricket, "untilI have told you a great truth.""Tell it, then, and hurry.""Woe to boys who refuse to obey their parents and run awayfrom home! They will never be happy in this world, and whenthey are older they will be very sorry for it.""Sing on, Cricket mine, as you please. What I know is, thattomorrow, at dawn, I leave this place forever. If I stayhere the same thing will happen to me which happens to allother boys and girls. They are sent to school, and whetherthey want to or not, they must study. As for me, let metell you, I hate to study! It's much more fun, I think, to chase after butterflies, climb trees, and steal birds'nests.""Poor little silly! Don't you know that if you go on likethat, you will grow into a perfect donkey and that you'llbe the laughingstock of everyone?""Keep still, you ugly Cricket!" cried Pinocchio.But the Cricket, who was a wise old philosopher, instead ofbeing offended at Pinocchio's impudence, continued in thesame tone:"If you do not like going to school, why don't you at leastlearn a trade, so that you can earn an honest living?""Shall I tell you something?" asked Pinocchio, who wasbeginning to lose patience. "Of all the trades in theworld, there is only one that really suits me.""And what can that be?""That of eating, drinking, sleeping, playing, and wanderingaround from morning till night.""Let me tell you, for your own good, Pinocchio," said theTalking Cricket in his calm voice, "that those who followthat trade always end up in the hospital or in prison.""Careful, ugly Cricket! If you make me angry, you'll besorry!""Poor Pinocchio, I am sorry for you.""Why?""Because you are a Marionette and, what is much worse, youhave a wooden head."At these last words, Pinocchio jumped up in a fury, took ahammer from the bench, and threw it with all his strengthat the Talking Cricket.Perhaps he did not think he would strike it. But, sad torelate, my dear children, he did hit the Cricket, straighton its head.With a last weak "cri-cri-cri" the poor Cricket fell fromthe wall, dead! Pinocchio is hungry and looks for an egg to cook himself anomelet;but, to his surprise, the omelet flies out of the windowIf the Cricket's death scared Pinocchio at all, it was onlyfor a very few moments. For, as night came on, a queer,empty feeling at the pit of his stomach reminded theMarionette that he had eaten nothing as yet.A boy's appetite grows very fast, and in a few moments thequeer, empty feeling had become hunger, and the hunger grewbigger and bigger, until soon he was as ravenous as a bear.Poor Pinocchio ran to the fireplace where the pot wasboiling and stretched out his hand to take the cover off,but to his amazement the pot was only painted! Think how hefelt! His long nose became at least two inches longer.He ran about the room, dug in all the boxes and drawers,and even looked under the bed in search of a piece ofbread, hard though it might be, or a cookie, or perhaps abit of fish. A bone left by a dog would have tasted good tohim! But he found nothing.And meanwhile his hunger grew and grew. The only reliefpoor Pinocchio had was to yawn; and he certainly did yawn,such a big yawn that his mouth stretched out to the tips ofhis ears. Soon he became dizzy and faint. He wept andwailed to himself: "The Talking Cricket was right. It waswrong of me to disobey Father and to run away from home. Ifhe were here now, I wouldn't be so hungry! Oh, how horribleit is to be hungry!"Suddenly, he saw, among the sweepings in a corner,something round and white that looked very much like ahen's egg. In a jiffy he pounced upon it. It was an egg.The Marionette's joy knew no bounds. It is impossible todescribe it, you must picture it to yourself. Certain thathe was dreaming, he turned the egg over and over in hishands, fondled it, kissed it, and talked to it:"And now, how shall I cook you? Shall I make an omelet? No,it is better to fry you in a pan! Or shall I drink you? No,the best way is to fry you in the pan. You will tastebetter."No sooner said than done. He placed a little pan over afoot warmer full of hot coals. In the pan, instead of oilor butter, he poured a little water. As soon as the waterstarted to boil--tac!--he broke the eggshell. But in placeof the white and the yolk of the egg, a little yellowChick, fluffy and gay and smiling, escaped from it. Bowingpolitely to Pinocchio, he said to him: "Many, many thanks, indeed, Mr. Pinocchio, for having savedme the trouble of breaking my shell! Good-by and good luckto you and remember me to the family!"With these words he spread out his wings and, darting tothe open window, he flew away into space till he was out ofsight.The poor Marionette stood as if turned to stone, with wideeyes, open mouth, and the empty halves of the egg-shell inhis hands. When he came to himself, he began to cry andshriek at the top of his lungs, stamping his feet on theground and wailing all the while:"The Talking Cricket was right! If I had not run away fromhome and if Father were here now, I should not be dying ofhunger. Oh, how horrible it is to be hungry!"And as his stomach kept grumbling more than ever and he hadnothing to quiet it with, he thought of going out for awalk to the near-by village, in the hope of finding somecharitable person who might give him a bit of bread.

'''