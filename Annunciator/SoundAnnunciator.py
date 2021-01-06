import pygame
import time

delay = int(input("How long to play the sound (min)? Input: \n> ")) * 60

time.sleep(delay)

pygame.mixer.init()
pygame.mixer.music.load("pizza.mp3")
pygame.mixer.music.play()

time.sleep(500)